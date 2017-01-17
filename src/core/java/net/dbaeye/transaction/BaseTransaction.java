/**
 * @(#)JdbcTransaction.java Aug 2, 2008
 * 
 * Copyright 2005 Yupoo. All rights reserved.
 */
package net.dbaeye.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.dao.ConnectionChangedEvent;
import net.dbaeye.dao.DataAccessEvent;
import net.dbaeye.dao.DataAccessListener;

/**
 * <p>
 * <a href="JdbcTransaction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: BaseTransaction.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class BaseTransaction implements Transaction, DataAccessListener {
	//~ Static fields/initializers =============================================

	private static final Logger logger = LoggerFactory.getLogger(BaseTransaction.class);
	
	//~ Instance fields ========================================================

	protected String name;
	protected Connection connection;
	protected Connection nestedConnection;
	protected Set<Connection> origAutoCommits = new HashSet<Connection>(2);
	
	protected Lock lock;
	
	protected boolean started;
	protected boolean completed;
	
	//~ Constructors ===========================================================
	
	//~ Methods ================================================================
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#isCompleted()
	 */
	public boolean isCompleted() {
		return completed;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#isStarted()
	 */
	public boolean isStarted() {
		return started;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.transaction.Transaction#getLock()
	 */
	public Lock getLock() {
		return lock;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.transaction.Transaction#setLock(net.dbaeye.transaction.Lock)
	 */
	public void setLock(Lock lock) {
		this.lock = lock;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#begin()
	 */
	public void begin() throws TransactionException {
		if (started) {
			throw new TransactionException("This transaction is already started");
		}
		if (connection != null) {			
			prepareConnection(connection);
		}
		if (nestedConnection != null) {
			prepareConnection(nestedConnection);
		}
		started = true;
	}
	
	protected void prepareConnection(Connection conn) {
		if (conn != null) {
			try {
				// Switch to manual commit if necessary. This is very expensive in some JDBC drivers,
				// so we don't want to do it unnecessarily (for example if we've explicitly
				// configured the connection pool to set it already).
				if (conn.getAutoCommit()) {
					origAutoCommits.add(conn);
					if (logger.isDebugEnabled()) {
						logger.debug("Switching JDBC Connection [" + conn + "] to manual commit");
					}
					conn.setAutoCommit(false);
				}
			} catch (SQLException e) {
				logger.error("SQLException occurred: {}", e.getMessage());
				throw new TransactionException(e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#commit()
	 */
	public void commit() throws TransactionException {
		if (completed) {
			throw new TransactionException("This transaction is already completed");
		}
		
		commitConnection(connection);
		commitConnection(nestedConnection);
		completed = true;
	}
	
	protected void commitConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.commit();
				if (origAutoCommits.contains(conn)) {
					conn.setAutoCommit(true);
					origAutoCommits.remove(conn);
				}
			} catch (SQLException e) {
				logger.error("SQLException occurred: {}", e.getMessage());
				throw new TransactionException(e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.core.transaction.Transaction#rollback()
	 */
	public void rollback() throws TransactionException {
		if (completed) {
			throw new TransactionException("This transaction is already completed");
		}
		rollbackConnection(connection);
		rollbackConnection(nestedConnection);
		completed = true;
	}
	
	protected void rollbackConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
				if (origAutoCommits.contains(conn)) {
					conn.setAutoCommit(true);
					origAutoCommits.remove(conn);
				}
			} catch (SQLException e) {
				logger.error("SQLException occurred: {}", e.getMessage());
				throw new TransactionException(e.getMessage());
			}
		}
	}
	
	//~ Accessors ==============================================================

	/* (non-Javadoc)
	 * @see net.dbaeye.dao.DataAccessListener#onDataAccessEvent(net.dbaeye.dao.DataAccessEvent)
	 */
	public void onDataAccessEvent(DataAccessEvent event) {
		if (completed) {
			return;
		}
		if (event instanceof ConnectionChangedEvent) {
			ConnectionChangedEvent e = (ConnectionChangedEvent) event;
			Connection conn = e.getNewConnection();
			
			if (conn == null || conn == connection || conn == nestedConnection) {
				return;
			}
			if (connection == null) {
				connection = conn;
				if (started) {
					prepareConnection(connection);
				}
			} else if (nestedConnection == null) {
				nestedConnection = conn;
				if (started) {
					prepareConnection(nestedConnection);
				}
			} else {
				throw new TransactionException("More then 2 connections resolved in this transaction: " + this.getName());
			}
		}
	}
}
