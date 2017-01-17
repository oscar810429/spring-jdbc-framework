/**
 * @(#)AbstractTransactionAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;

import net.dbaeye.transaction.TransactionAspectSupport;
import net.dbaeye.transaction.TransactionAttributeSource;

/**
 * <p>
 * <a href="AbstractTransactionAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AbstractTransactionAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract aspect AbstractTransactionAspect extends TransactionAspectSupport {

	public AbstractTransactionAspect(TransactionAttributeSource tas) {
		setTransactionAttributeSource(tas);
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	before(Object txObject) : transactionalMethodExecution(txObject) {
		MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Object[] args = thisJoinPoint.getArgs();
		Transaction trans = createTransactionIfNecessary(method, txObject.getClass(), args);
		if (trans != null && logger.isDebugEnabled()) {
			logger.debug("Transaction created: {}", trans.getName());
		}
	}
	

	@SuppressAjWarnings("adviceDidNotMatch")
	after(Object txObject) throwing(Throwable t) : transactionalMethodExecution(txObject) {
		try {
			completeTransactionAfterThrowing(null, t);
		} catch (Throwable t2) {
			logger.error("Failed to close transaction after throwing in a transactional method", t2);
		}
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	after(Object txObject) returning() : transactionalMethodExecution(txObject) {
		commitTransactionAfterReturning(null);
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	after(Object txObject) : transactionalMethodExecution(txObject) {
		cleanupTransaction(null);
	}
	
	/**
	 * Concrete subaspects must implement this pointcut, to identify
	 * transactional methods. For each selected joinpoint, TransactionMetadata
	 * will be retrieved using Spring's TransactionAttributeSource interface.
	 */
	protected abstract pointcut transactionalMethodExecution(Object txObject);
}
