/**
 * @(#)AnnotationDataAccessAttributeSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

import net.dbaeye.transaction.Transactional;

/**
 * <p>
 * <a href="AnnotationDataAccessAttributeSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AnnotationDataAccessAttributeSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class AnnotationDataAccessAttributeSource extends AbstractDataAccessAttributeSource {

	private final boolean publicMethodsOnly;


	public AnnotationDataAccessAttributeSource() {
		this.publicMethodsOnly = false;
	}

	public AnnotationDataAccessAttributeSource(boolean publicMethodsOnly) {
		this.publicMethodsOnly = publicMethodsOnly;
	}




	/* (non-Javadoc)
	 * @see net.dbaeye.dao.AbstractDataAccessAttributeSource#findDataAccessAttribute(java.lang.reflect.Method)
	 */
	@Override
	protected DataAccessAttribute findDataAccessAttribute(Method method) {
		Annotation[] annotations = AnnotationUtils.getAnnotations(method);
		
		if (annotations == null) {
			return null;
		}
		
		for (int i = 0; i < annotations.length; i++) {
			Annotation annotation = annotations[i];
			DataAccessAttribute attr = null;
			
			if (annotation instanceof DataAccessMethod) {
				DataAccessMethod dam = (DataAccessMethod) annotation;
				
				if (attr == null) {
					attr = new DataAccessAttribute();
				}
				attr.setShardingClue(dam.shardingClue());
				attr.setCacheKey(dam.cacheKey());
				attr.setFlushArgs(dam.flushArgs());
				attr.setFlushResult(dam.flushResult());
				attr.setCacheResult(dam.cacheResult());
				attr.setOnceWithSameArgs(dam.onceWithSameArgs());

			} else if (annotation instanceof LazyLoading) {
				LazyLoading lazy = (LazyLoading) annotation;
				
				if (attr == null) {
					attr = new DataAccessAttribute();
				}
				attr.setManagerName(lazy.manager());
				attr.setMethodName(lazy.method());
			}
			
			if (attr != null) {
				return attr;
			}
		}
		return null;
	}
	
	/**
	 * By default, only public methods can be made transactional using
	 * {@link Transactional}.
	 */
	protected boolean allowPublicMethodsOnly() {
		return this.publicMethodsOnly;
	}


	public boolean equals(Object other) {
		return (this == other || other instanceof AnnotationDataAccessAttributeSource);
	}

	public int hashCode() {
		return AnnotationDataAccessAttributeSource.class.hashCode();
	}
}
