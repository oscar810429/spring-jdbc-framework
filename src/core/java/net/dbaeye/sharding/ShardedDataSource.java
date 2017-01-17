/**
 * @(#)ShardedDataSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <p>
 * <a href="ShardedDataSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardedDataSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class ShardedDataSource implements DataSource {
	//~ Static fields/initializers =============================================
	
	//~ Instance fields ========================================================
	
	private Shard shard;
	private DataSource dataSource;

	//~ Constructors ===========================================================
	
	public ShardedDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		//this.shard = shard;
	}

	public ShardedDataSource(DataSource dataSource, Shard shard) {
		this.dataSource = dataSource;
		this.shard = shard;
	}
	
	//~ Methods ================================================================
	
	/**
	 * @return the shard
	 */
	public Shard getShard() {
		return shard;
	}
	
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return dataSource.getConnection(username, password);
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	public <T> T unwrap(Class<T> class1) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> class1) throws SQLException {
		return false;
	}

}
