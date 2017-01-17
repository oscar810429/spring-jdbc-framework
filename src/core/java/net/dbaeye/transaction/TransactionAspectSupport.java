/**
 * @(#)TransactionAspectSupport.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import net.dbaeye.dao.DataAccessContext;

/**
 * <p>
 * <a href="TransactionAspectSupport.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionAspectSupport.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class TransactionAspectSupport {
	//~ Static fields/initializers =============================================

	protected static final Logger logger = LoggerFactory.getLogger(TransactionAspectSupport.class);
	
	//~ Instance fields ========================================================
	
	private TransactionManager transactionManager;
	private TransactionAttributeSource transactionAttributeSource;

	//~ Constructors ===========================================================
	
	//~ Methods ================================================================
	
	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}
	
	/**
	 * @param transactionManager the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	/**
	 * @return the transactionAttributeSource
	 */
	public TransactionAttributeSource getTransactionAttributeSource() {
		return transactionAttributeSource;
	}
	
	/**
	 * @param transactionAttributeSource the transactionAttributeSource to set
	 */
	public void setTransactionAttributeSource(
			TransactionAttributeSource transactionAttributeSource) {
		this.transactionAttributeSource = transactionAttributeSource;
	}
	
	protected Transaction createTransactionIfNecessary(
			TransactionAttribute txAttr, final String joinpointIdentification, Object[] args) {
		
		Transaction trans = null;
		if (txAttr != null) {
			TransactionManager tm = getTransactionManager();
			if (tm != null) {
				if (txAttr.isLocked() && txAttr.isComplexLockName()) {
					TransactionAttribute attr = new TransactionAttribute();
					attr.setLocked(true);
					attr.setLockTimeout(txAttr.getLockTimeout());
					if (args.length > 0 && args[0] instanceof Lockable) {						
						attr.setLockName(String.format(txAttr.getLockName(), ((Lockable<?>) args[0]).getLockName()));
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Skipping transaction locking: lockable argument is not found");
						}
						attr.setLocked(false);
					}
					
					txAttr = attr;
				}
				
				trans = tm.getTransaction(txAttr);
				trans.setName(joinpointIdentification);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Skipping transactional joinpoint [" + joinpointIdentification +
							"] because no transaction manager has been configured");
				}
			}
		}
		return trans;
	}
	
	/**
	 * Create a transaction if necessary, based on the given method and class.
	 * <p>Performs a default TransactionAttribute lookup for the given method.
	 * @param method method about to execute
	 * @param targetClass class the method is on
	 * @return a TransactionInfo object, whether or not a transaction was created.
	 * The hasTransaction() method on TransactionInfo can be used to tell if there
	 * was a transaction created.
	 * @see #getTransactionAttributeSource()
	 */
	protected Transaction createTransactionIfNecessary(Method method, Class<?> targetClass, Object[] args) {
		// If the transaction attribute is null, the method is non-transactional.
		TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
		return createTransactionIfNecessary(txAttr, methodIdentification(method), args);
	}

	/**
	 * Convenience method to return a String representation of this Method
	 * for use in logging. Can be overridden in subclasses to provide a
	 * different identifier for the given method.
	 * @param method the method we're interested in
	 * @return log message identifying this method
	 * @see org.springframework.util.ClassUtils#getQualifiedMethodName
	 */
	protected String methodIdentification(Method method) {
		return ClassUtils.getQualifiedMethodName(method);
	}
	
	/**
	 * Execute after successful completion of call, but not after an exception was handled.
	 * Do nothing if we didn't create a transaction.
	 * @param txInfo information about the current transaction
	 */
	protected void commitTransactionAfterReturning(Transaction trans) {
		if (trans == null) {
			trans = DataAccessContext.getContext().getTransaction();
		}
		if (trans != null && !trans.isCompleted()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Completing transaction for [" + trans.getName() + "]");
			}
			getTransactionManager().commit(trans);
		}
	}

	/**
	 * Handle a throwable, completing the transaction.
	 * We may commit or roll back, depending on the configuration.
	 * @param txInfo information about the current transaction
	 * @param ex throwable encountered
	 */
	protected void completeTransactionAfterThrowing(Transaction trans, Throwable ex) {
		if (trans == null) {
			trans = DataAccessContext.getContext().getTransaction();
		}
		
		if (trans != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Completing transaction for [" + trans.getName() + "] after exception: " + ex);
			}

			try {
				getTransactionManager().rollback(trans);
			}
			catch (TransactionException ex2) {
				logger.error("Application exception overridden by rollback exception", ex);
				throw ex2;
			}
			catch (RuntimeException ex2) {
				logger.error("Application exception overridden by rollback exception", ex);
				throw ex2;
			}
			catch (Error err) {
				logger.error("Application exception overridden by rollback error", ex);
				throw err;
			}

		}
	}

	/**
	 * Reset the TransactionInfo ThreadLocal.
	 * <p>Call this in all cases: exception or normal return!
	 * @param txInfo information about the current transaction (may be <code>null</code>)
	 */
	protected void cleanupTransaction(Transaction trans) {
		DataAccessContext.getContext().finishTransaction();
	}

}
