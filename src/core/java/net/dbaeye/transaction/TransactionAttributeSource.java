/**
 * @(#)TransactionAttributeSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.reflect.Method;

/**
 * <p>
 * <a href="TransactionAttributeSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionAttributeSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 * @see org.springframework.transaction.interceptor.TransactionAttributeSource
 */
public interface TransactionAttributeSource {

	/**
	 * Return the transaction attribute for this method.
	 * Return null if the method is non-transactional.
	 * @param method method
	 * @param targetClass target class. May be <code>null</code>, in which
	 * case the declaring class of the method must be used.
	 * @return TransactionAttribute the matching transaction attribute,
	 * or <code>null</code> if none found
	 */
	TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass);

}
