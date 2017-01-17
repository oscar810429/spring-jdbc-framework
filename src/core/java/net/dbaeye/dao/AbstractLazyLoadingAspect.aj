/**
 * @(#)AbstractLazyLoadingAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * <p>
 * <a href="AbstractLazyLoadingAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AbstractLazyLoadingAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract aspect AbstractLazyLoadingAspect extends LazyLoadingAspectSupport {

	public AbstractLazyLoadingAspect(DataAccessAttributeSource daas) {
		setDataAccessAttributeSource(daas);
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(Object model) : lazyLoadingMethodExecution(model) {
		Object result = proceed(model);
		
		if (result != null) {
			return result;
		}
		
		MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
		Method method = methodSignature.getMethod();
		
		return loadObject(model, method);
	}
	
	
	protected abstract pointcut lazyLoadingMethodExecution(Object model);
}