/**
 * @(#)LazyLoadingAspectSupport.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.data.Content;
import net.dbaeye.Context;

/**
 * <p>
 * <a href="LazyLoadingAspectSupport.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author  Zhang Songfu
 * @version $Id: LazyLoadingAspectSupport.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class LazyLoadingAspectSupport {
	//~ Static fields/initializers =============================================

	protected static final Logger logger = LoggerFactory.getLogger(LazyLoadingAspectSupport.class);
	
	//~ Instance fields ========================================================

	private DataAccessAttributeSource dataAccessAttributeSource;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	/**
	 * @param dataAccessAttributeSource the dataAccessAttributeSource to set
	 */
	public void setDataAccessAttributeSource(
			DataAccessAttributeSource dataAccessAttributeSource) {
		this.dataAccessAttributeSource = dataAccessAttributeSource;
	}
	
	/**
	 * @return the dataAccessAttributeSource
	 */
	public DataAccessAttributeSource getDataAccessAttributeSource() {
		return dataAccessAttributeSource;
	}
	
	protected Object loadObject(Object model, Method method) {
		DataAccessAttribute attr = getDataAccessAttributeSource().getDataAccessAttribute(method, model.getClass());
		
		String managerName = attr.getManagerName();
		String methodName = attr.getMethodName();
		
		if (methodName.length() == 0) {
			methodName = method.getName();
		}
		
		Object manager = Context.getBean(managerName);
		
		if (manager == null) {
			logger.error("LazyLoading Error: Bean '{}' not found", managerName);
			throw new RuntimeException("LazyLoading Error: Bean '" + managerName + "' not found");
		}
		
		Object result = null;
		
		try {
			Method mgrMethod = manager.getClass().getMethod(methodName, model.getClass());
			
			Class<?> resultType = method.getReturnType();
			if (mgrMethod.getReturnType() == null || mgrMethod.getReturnType() != resultType) {
				throw new RuntimeException("LazyLoading Error: return type of the manager method not match.");
			}
			result = mgrMethod.invoke(manager, model);
			
		} catch (SecurityException e) {
			logger.error("LazyLoadding Error: SecurityException occurred.", e);
		} catch (NoSuchMethodException e) {
			logger.error("LazyLoadding Error: Loading method not found.", e);
		} catch (IllegalArgumentException e) {
			logger.error("LazyLoadding Error: IllegalArgumentException.", e);
		} catch (IllegalAccessException e) {
			logger.error("LazyLoadding Error: IllegalAccessException.", e);
		} catch (InvocationTargetException e) {
			logger.error("LazyLoadding Error: Exception occurred while invoking loading method.", e);
		}
		
		if (result != null) {
			logger.debug("LazyLoading Succeed, object loaded: {}", result);
			setProperty(model, method, result);
		}
		
		return result;
	}
	
	private Method getSetter(Object obj, Method getter) {
		String fieldName = null;
		String methodName = getter.getName();
		if (methodName.startsWith("get")) {
			fieldName = methodName.substring(3);
		} else if (methodName.startsWith("is")) {
			fieldName = methodName.substring(2);
		}
		if (fieldName.length() == 0) {
			return null;
		}
		
		String setterName = "set" + fieldName;
		
		Method method = null;
		
		try {
			method = obj.getClass().getMethod(setterName, getter.getReturnType());
		} catch (SecurityException e) {
			logger.error("SecurityException occurred while getting method '{}' of Type '{}'", setterName, obj.getClass());
		} catch (NoSuchMethodException e) {
			logger.error("Setter method '{}' of Type '{}' not found", setterName, obj.getClass());
		}
		
		return method;
	}
	
	private void setProperty(Object obj, Method getter, Object property) {
		Method method = getSetter(obj, getter);
		
		try {
			method.invoke(obj, property);
		} catch (IllegalArgumentException e) {
			logger.error("LazyLoading Warning: IllegalArgumentException", e);
		} catch (IllegalAccessException e) {
			logger.error("LazyLoading Warning: IllegalAccessException", e);
		} catch (InvocationTargetException e) {
			logger.error("LazyLoading Warning: Exception occurred while setting lazy loaded property", e);
		}
	}
	
	
}
