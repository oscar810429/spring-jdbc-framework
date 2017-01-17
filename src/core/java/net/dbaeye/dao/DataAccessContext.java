/**
 * @(#)DataAccessContext.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import net.dbaeye.cache.Cache;
//import net.dbaeye.sharding.Cluster;
//import net.dbaeye.sharding.Database;
//import net.dbaeye.sharding.Shard;
//import net.dbaeye.sharding.ShardedDataSource;
//import net.dbaeye.sharding.ShardingClue;
import net.dbaeye.sharding.ShardedDataSource;
import net.dbaeye.sharding.ShardingClue;
import net.dbaeye.transaction.Transaction;

/**
 * <p>
 * <a href="DataAccessContext.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessContext.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DataAccessContext {
	//~ Static fields/initializers =============================================

	private static final Logger logger = LoggerFactory.getLogger(DataAccessContext.class);

	private static final ThreadLocal<DataAccessContext> contexts = new ThreadLocal<DataAccessContext>();
	
	//~ Instance fields ========================================================

	//private Cluster cluster;
	private int preferredDatabase = -1;
	
	private Cache cache;
    private Map<Object, ShardedDataSource> dataSources = new HashMap<Object, ShardedDataSource>();
	private Map<DataSource, Connection> dataSourceConnections = new HashMap<DataSource, Connection>();
	
	
	private Connection connection;
	private Transaction transaction;
	// connections in transaction
	private Set<Connection> connections = new HashSet<Connection>();
	// invocations in transaction
	private Set<DataAccessMethodInvocation> invocations = new HashSet<DataAccessMethodInvocation>();
	
//	private Method method; // DataAccessMethod
	private DataAccessMethodInvocation invocation;
	
	private Set<DataAccessListener> listeners = new HashSet<DataAccessListener>(2);
	
	//~ Constructors ===========================================================
	
	private DataAccessContext() {}
	
	public static DataAccessContext getContext() {
		if (contexts.get() == null) {
			contexts.set(new DataAccessContext());
		}
		return contexts.get();
	}
	
	//~ Methods ================================================================
	
	//public static boolean initialized() {
    //		return getContext().cluster != null;
	//}
	
	public void addListener(DataAccessListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(DataAccessListener listener) {
		listeners.remove(listener);
	}
	
	//public Cluster getCluster() {
	//	if (cluster == null) {
	//		throw new DataAccessResourceFailureException("Cluster has not set yet");
	//	}
	//	return cluster;
	//}
	
	/*public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public List<Shard> getShards() {
		return getCluster().getShards();
	}
	
	public Shard getShard(ShardingClue<?> clue) {
		return getCluster().getShard(clue);
	}
	
	private ShardedDataSource getDataSource(Shard shard) {
		List<Database> databases = shard.getDatabases();
		if (preferredDatabase == -1) {
			preferredDatabase = RandomUtils.nextInt(databases.size());
		}
		int dbIdx = preferredDatabase;
		if (dbIdx >= databases.size()) {
			dbIdx = RandomUtils.nextInt(databases.size());
		}
		return databases.get(dbIdx).getDataSource();
	}*/
	
	private ShardedDataSource getDataSource(ShardingClue<?> clue) {
		logger.debug("Lookup DataSource for sharding clue: {}", clue);

		ShardedDataSource dataSource = null;
		if (clue.getClue() != null) {
			dataSource = dataSources.get(clue.getClue());
		}
		/*if (dataSource == null) {
			Shard shard = getCluster().getShard(clue);
			dataSource = getDataSource(shard);
			if (clue.getClue() != null) {
				dataSources.put(clue.getClue(), dataSource);
			}
		}*/
		
		if (invocation != null) {
			invocation.setDataSource(dataSource);
		}
		
		return dataSource;
	}
	
	/*public Connection getConnection(Shard shard) {
		DataSource dataSource = getDataSource(shard);
		return getConnection(dataSource);
	}*/
	
	public Connection getConnection(ShardingClue<?> clue) {
		ShardedDataSource dataSource = getDataSource(clue);
		return getConnection(dataSource);
	}
	
	public Connection getConnection(DataSource dataSource) {
		Connection conn = dataSourceConnections.get(dataSource);
		
		if (conn != null) {
			try {
				if (conn.isClosed()) {
					conn = null;
				}
			} catch (SQLException e) {
				conn = null;
			}
			if (conn == null) {
				dataSourceConnections.remove(dataSource);
			}
		}
		
		if (conn == null) {
			try {
				conn = dataSource.getConnection();
				if (logger.isDebugEnabled()) {
					logger.debug("** Getting JDBC Connection[{}] from DataSource", conn);
				}
			} catch (SQLException ex) {
				throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
			}
			dataSourceConnections.put(dataSource, conn);
		}
		
		setConnection(conn);
		return conn;
	}
	
	public void releaseConnection() {
		releaseConnection(null);
	}
	
	public void releaseConnection(Connection conn) {
		if (conn == null) {
			conn = getConnection();
		}
		if (conn == null) {
			return;
		}
		if (doReleaseConnection(conn)) {
			connections.remove(conn);
		}
	}
	
	public void releaseConnections() {
		for (Iterator<Connection> i = connections.iterator(); i.hasNext();) {
			Connection conn = i.next();
			if (doReleaseConnection(conn)) {
				i.remove();
			}
		}
	}
	
	public boolean inTransaction() {
		return transaction != null && !transaction.isCompleted();
	}
	
	private boolean doReleaseConnection(Connection conn) {
		if (inTransaction() || invocation != null) {
			// Do not close connection while we are in a transaction. 
			// I will close it after the transaction is complete
			return false;
		}
		if (conn == connection) {
			setConnection(null);
		}
		try {
			// Leave the Connection open only if the DataSource is our
			// special SmartDataSoruce and it wants the Connection left open.
			if (!conn.isClosed()) {
				logger.debug("** Returning JDBC Connection[{}] to DataSource", conn);
				conn.close();
			}
		}
		catch (SQLException ex) {
			logger.error("Could not close JDBC Connection", ex);
		}
		catch (Throwable ex) {
			logger.error("Unexpected exception on closing JDBC Connection", ex);
		}
		return true;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	private void setConnection(Connection conn) {
		if (conn != connection) {
			if (conn != null && !connections.contains(conn)) {
				connections.add(conn);
			}
			Connection old = connection;
			connection = conn;
			publishEvent(new ConnectionChangedEvent(this, old, connection));
		}
	}
	
	protected void publishEvent(DataAccessEvent event) {
		for (DataAccessListener listener : listeners) {
			listener.onDataAccessEvent(event);
		}
	}
	
	public void beginTransaction(Transaction transaction) {
		if (!inTransaction()) {
			this.transaction = transaction;
		}
	}
	
	public void finishTransaction() {
		this.transaction = null;
		this.invocations.clear();
		this.releaseConnections();
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	
	public void beginInvocation(DataAccessMethodInvocation invocation) {
		this.invocation = invocation;
	}
	
	public void finishInvocation() {
		if (invocation != null) {
			if (inTransaction()) {
				invocations.add(invocation);
			}
			invocation = null;
		}
		releaseConnections();
	}
	
	public DataAccessMethodInvocation getInvocation() {
		return invocation;
	}
	
	public boolean hasInvoked(DataAccessMethodInvocation invocation) {
		return invocations.contains(invocation);
	}
	
	
	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}
	
	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	/**
	 * @return the preferredDatabase
	 */
	public int getPreferredDatabase() {
		return preferredDatabase;
	}
	
	/**
	 * @param preferredDatabase the preferredDatabase to set
	 */
	public void setPreferredDatabase(int preferredDatabase) {
		this.preferredDatabase = preferredDatabase;
	}
	
	public void clear() {
		logger.debug("Clear current DataAccessContext of thread: {}", Thread.currentThread().getName());
		finishInvocation();
		finishTransaction();
		//cluster = null;
		preferredDatabase = -1;
		cache = null;
		dataSources.clear();
		dataSourceConnections.clear();
		connection = null;
		listeners.clear();
	}
	
}
