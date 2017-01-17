/**
 * @(#)ShardingTransactionManager.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.core.sharding.NonShardedObject;
import net.dbaeye.dao.DataAccessContext;
import net.dbaeye.dao.DataAccessListener;

/**
 * <p>
 * <a href="ShardingTransactionManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: BaseTransactionManager.java 37 2012-04-09 09:08:31Z zhangsongfu $
 */
public class BaseTransactionManager implements TransactionManager {
	//~ Static fields/initializers =============================================

	private static final Logger logger = LoggerFactory.getLogger(BaseTransactionManager.class);
	
	//~ Instance fields ========================================================

	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	/*
	 * (non-Javadoc)
	 * @see net.dbaeye.transaction.TransactionManager#getTransaction(net.dbaeye.transaction.TransactionAttribute)
	 */
	public Transaction getTransaction(TransactionAttribute attribute) {
		DataAccessContext ctx = DataAccessContext.getContext();
		
		Lock lock = null;
		if (attribute.isLocked()) {
			lock = getLock(attribute.getLockName(), attribute.getLockTimeout());
		}
		
		Transaction trans = ctx.getTransaction();
		if (trans == null) {
			trans = new BaseTransaction();
			ctx.addListener((DataAccessListener) trans);			
			ctx.beginTransaction(trans);
			trans.begin();			
		}
		
		if (lock != null) {
			if (trans.getLock() != null) {
				releaseLock(trans.getLock());
			}
			trans.setLock(lock);
		}
		
		return trans;
	}
	

	/*
	 * (non-Javadoc)
	 * @see net.dbaeye.transaction.TransactionManager#commit(net.dbaeye.transaction.Transaction)
	 */
	public void commit(Transaction transaction) {
		DataAccessContext ctx = DataAccessContext.getContext();
		try {
			transaction.commit();
			
			if (transaction.getLock() != null) {
				releaseLock(transaction.getLock());
			}
			
			ctx.finishTransaction();
			if (transaction instanceof DataAccessListener) {
				ctx.removeListener((DataAccessListener) transaction);
			}
			ctx.releaseConnections();
		} catch (TransactionException e) {
			logger.info("Initiating transaction rollback after commit exception", e);
			rollback(transaction);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see net.dbaeye.transaction.TransactionManager#rollback(net.dbaeye.transaction.Transaction)
	 */
	public void rollback(Transaction transaction) {
		DataAccessContext ctx = DataAccessContext.getContext();
		try {
			transaction.rollback();
			
			if (transaction.getLock() != null) {
				releaseLock(transaction.getLock());
			}
			
			ctx.finishTransaction();
			if (transaction instanceof DataAccessListener) {
				ctx.removeListener((DataAccessListener) transaction);
			}
			ctx.releaseConnections();
		} catch (TransactionException e) {
			logger.error("Exception occurred while rolling back transaction", e);
			throw e;
		}
	}
	

	private Lock getLock(String lockName, long timeout) {
		logger.debug("Getting lock: {}", lockName);
		
		DataAccessContext ctx = DataAccessContext.getContext();
		//Connection conn = ctx.getConnection();
		Connection conn = ctx.getConnection();
		
		Lock lock = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("SELECT GET_LOCK(?, ?)");
			pstmt.setString(1, lockName);
			pstmt.setInt(2, (int) (timeout / 1000));
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next() && rs.getInt(1) == 1) {
				lock = new Lock(lockName, conn);
			}
			rs.close();
			
			if (lock == null) {
				throw new LockRetrievalTimeoutException();
			}
			
		} catch (SQLException e) {
			logger.error("SQLException occurred while retrivaling lock of name: " + lockName, e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					logger.debug("Could not close PreparedStatement", e);
				}
			}
			if (lock == null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					logger.debug("Could not close JDBC Connection", ex);
				}
			}
		}
		
		return lock;
	}
	
	private void releaseLock(Lock lock) {
		logger.debug("Releasing lock: {}", lock.getName());
		Connection conn = lock.getConnection();
		PreparedStatement pstmt = null;
		try {
			if (conn.isClosed()) {
				return ;
			}
			pstmt = conn.prepareStatement("SELECT RELEASE_LOCK(?)");
			pstmt.setString(1, lock.getName());
			ResultSet rs = pstmt.executeQuery();
			rs.close();
		} catch (SQLException e) {
			logger.error("SQLException occurred while releasing lock of name: " + lock.getName(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					logger.debug("Could not close PreparedStatement", e);
				}
			}
		}
	}
}
