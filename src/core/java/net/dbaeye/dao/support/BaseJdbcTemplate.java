/**
 * @(#)ShardingJdbcTemplate.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import net.dbaeye.dao.DataAccessContext;

/**
 * <p>
 * <a href="ShardingJdbcTemplate.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: BaseJdbcTemplate.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class BaseJdbcTemplate extends JdbcTemplate {
	//~ Static fields/initializers =============================================

	protected static final Logger logger = LoggerFactory.getLogger(BaseJdbcTemplate.class);
	
	//~ Instance fields ========================================================

	//private Shard shard;
	private DataSource dataSource;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	protected Connection getConnection() {
		Connection connection = DataAccessContext.getContext().getConnection(dataSource);
	    if (connection == null) {
			throw new DataAccessResourceFailureException("Can not get connection");
		}
		return connection;
	}
	
	protected void releaseConnection(Connection conn) {
		if (conn == null) {
			return;
		}
		DataAccessContext.getContext().releaseConnection(conn);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.support.JdbcAccessor#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.support.JdbcAccessor#getDataSource()
	 */
	/*@Override
	@Deprecated
	public DataSource getDataSource() {
		throw new RuntimeException("JdbcTemplate is no more DataSource aware");
	}*/
	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.support.JdbcAccessor#setDataSource(javax.sql.DataSource)
	 */
	/*@Override
	@Deprecated
	public void setDataSource(DataSource dataSource) {
		throw new RuntimeException("JdbcTemplate is no more DataSource aware");
	}*/
	
	
	
	/**
	 * Determine SQL from potential provider object.
	 * @param sqlProvider object that's potentially a SqlProvider
	 * @return the SQL string, or <code>null</code>
	 * @see SqlProvider
	 */
	private static String getSql(Object sqlProvider) {
		if (sqlProvider instanceof SqlProvider) {
			return ((SqlProvider) sqlProvider).getSql();
		}
		return null;
	}
	
	//-------------------------------------------------------------------------
	// Methods dealing with callable statements
	//-------------------------------------------------------------------------

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	public Object execute(CallableStatementCreator csc, CallableStatementCallback action)
			throws DataAccessException {

		Assert.notNull(csc, "CallableStatementCreator must not be null");
		Assert.notNull(action, "Callback object must not be null");
		if (logger.isDebugEnabled()) {
			String sql = getSql(csc);
			logger.debug("Calling stored procedure" + (sql != null ? " [" + sql  + "]" : ""));
		}

		Connection con = getConnection();
		CallableStatement cs = null;
		try {
			Connection conToUse = con;
			if (this.getNativeJdbcExtractor() != null) {
				conToUse = this.getNativeJdbcExtractor().getNativeConnection(con);
			}
			cs = csc.createCallableStatement(conToUse);
			applyStatementSettings(cs);
			CallableStatement csToUse = cs;
			if (this.getNativeJdbcExtractor() != null) {
				csToUse = this.getNativeJdbcExtractor().getNativeCallableStatement(cs);
			}
			Object result = action.doInCallableStatement(csToUse);
			handleWarnings(cs.getWarnings());
			return result;
		}
		catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			if (csc instanceof ParameterDisposer) {
				((ParameterDisposer) csc).cleanupParameters();
			}
			String sql = getSql(csc);
			csc = null;
			JdbcUtils.closeStatement(cs);
			cs = null;
			releaseConnection(con);
			con = null;
			throw getExceptionTranslator().translate("CallableStatementCallback", sql, ex);
		}
		finally {
			if (csc instanceof ParameterDisposer) {
				((ParameterDisposer) csc).cleanupParameters();
			}
			JdbcUtils.closeStatement(cs);
			releaseConnection(con);
		}
	}
	

	//-------------------------------------------------------------------------
	// Methods dealing with a plain java.sql.Connection
	//-------------------------------------------------------------------------

	public Object execute(ConnectionCallback action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");

		Connection con = getConnection();
		try {
			Connection conToUse = con;
			if (this.getNativeJdbcExtractor() != null) {
				// Extract native JDBC Connection, castable to OracleConnection or the like.
				conToUse = this.getNativeJdbcExtractor().getNativeConnection(con);
			}
			else {
				// Create close-suppressing Connection proxy, also preparing returned Statements.
				conToUse = createConnectionProxy(con);
			}
			return action.doInConnection(conToUse);
		}
		catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			releaseConnection(con);
			con = null;
			throw getExceptionTranslator().translate("ConnectionCallback", getSql(action), ex);
		}
		finally {
			releaseConnection(con);
		}
	}
	
	
	//-------------------------------------------------------------------------
	// Methods dealing with prepared statements
	//-------------------------------------------------------------------------

	public Object execute(PreparedStatementCreator psc, PreparedStatementCallback action)
			throws DataAccessException {

		Assert.notNull(psc, "PreparedStatementCreator must not be null");
		Assert.notNull(action, "Callback object must not be null");
		if (logger.isDebugEnabled()) {
			String sql = getSql(psc);
			logger.debug("Executing prepared SQL statement" + (sql != null ? " [" + sql + "]" : ""));
		}

		Connection con = getConnection();
		PreparedStatement ps = null;
		try {
			Connection conToUse = con;
			if (this.getNativeJdbcExtractor() != null &&
					this.getNativeJdbcExtractor().isNativeConnectionNecessaryForNativePreparedStatements()) {
				conToUse = this.getNativeJdbcExtractor().getNativeConnection(con);
			}
			ps = psc.createPreparedStatement(conToUse);
			applyStatementSettings(ps);
			PreparedStatement psToUse = ps;
			if (this.getNativeJdbcExtractor() != null) {
				psToUse = this.getNativeJdbcExtractor().getNativePreparedStatement(ps);
			}
			Object result = action.doInPreparedStatement(psToUse);
			handleWarnings(ps.getWarnings());
			return result;
		}
		catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			if (psc instanceof ParameterDisposer) {
				((ParameterDisposer) psc).cleanupParameters();
			}
			String sql = getSql(psc);
			psc = null;
			JdbcUtils.closeStatement(ps);
			ps = null;
			releaseConnection(con);
			con = null;
			throw getExceptionTranslator().translate("PreparedStatementCallback", sql, ex);
		}
		finally {
			if (psc instanceof ParameterDisposer) {
				((ParameterDisposer) psc).cleanupParameters();
			}
			JdbcUtils.closeStatement(ps);
			releaseConnection(con);
		}
	}
	
	//-------------------------------------------------------------------------
	// Methods dealing with static SQL (java.sql.Statement)
	//-------------------------------------------------------------------------

	public Object execute(StatementCallback action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");

		Connection con = getConnection();
		Statement stmt = null;
		try {
			Connection conToUse = con;
			if (this.getNativeJdbcExtractor() != null &&
					this.getNativeJdbcExtractor().isNativeConnectionNecessaryForNativeStatements()) {
				conToUse = this.getNativeJdbcExtractor().getNativeConnection(con);
			}
			stmt = conToUse.createStatement();
			applyStatementSettings(stmt);
			Statement stmtToUse = stmt;
			if (this.getNativeJdbcExtractor() != null) {
				stmtToUse = this.getNativeJdbcExtractor().getNativeStatement(stmt);
			}
			Object result = action.doInStatement(stmtToUse);
			handleWarnings(stmt.getWarnings());
			return result;
		}
		catch (SQLException ex) {
			// Release Connection early, to avoid potential connection pool deadlock
			// in the case when the exception translator hasn't been initialized yet.
			JdbcUtils.closeStatement(stmt);
			stmt = null;
			releaseConnection(con);
			con = null;
			throw getExceptionTranslator().translate("StatementCallback", getSql(action), ex);
		}
		finally {
			JdbcUtils.closeStatement(stmt);
			releaseConnection(con);
		}
	}
	
	/**
	 * Prepare the given JDBC Statement (or PreparedStatement or CallableStatement),
	 * applying statement settings such as fetch size, max rows, and query timeout.
	 * @param stmt the JDBC Statement to prepare
	 * @throws SQLException if thrown by JDBC API
	 * @see #setFetchSize
	 * @see #setMaxRows
	 * @see #setQueryTimeout
	 */
	protected void applyStatementSettings(Statement stmt) throws SQLException {
		int fetchSize = getFetchSize();
		if (fetchSize > 0) {
			stmt.setFetchSize(fetchSize);
		}
		int maxRows = getMaxRows();
		if (maxRows > 0) {
			stmt.setMaxRows(maxRows);
		}
//		DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
		stmt.setQueryTimeout(getQueryTimeout());
	}
}
