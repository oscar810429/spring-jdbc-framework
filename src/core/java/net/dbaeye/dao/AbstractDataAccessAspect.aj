/**
 * @(#)AbstractDataAccessAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;

import net.dbaeye.dao.DataAccessAttributeSource;

/**
 * <p>
 * <a href="AbstractDataAccessAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AbstractDataAccessAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract aspect AbstractDataAccessAspect extends DataAccessAspectSupport {

	public AbstractDataAccessAspect(DataAccessAttributeSource daas) {
		setDataAccessAttributeSource(daas);
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(Object daoObj) : dataAccessMethodExecution(daoObj) {
		MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Object[] args = thisJoinPoint.getArgs();
		
		Object result = null;
		try {
			result = beforeExecution(method, daoObj.getClass(), args);
		} catch (SkipMethodInvocationException e) {
			return e.getResult();
		}
		try {
			result = proceed(daoObj);
		} finally {
			result = afterReturning(method, daoObj.getClass(), args, result);
		}
		return result;
	}
	
	
	protected abstract pointcut dataAccessMethodExecution(Object daoObj);
}
