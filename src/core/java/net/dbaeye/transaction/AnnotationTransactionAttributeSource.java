/**
 * @(#)AnnotationTransactionAttributeSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>
 * <a href="AnnotationTransactionAttributeSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AnnotationTransactionAttributeSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class AnnotationTransactionAttributeSource extends AbstractTransactionAttributeSource {

	private final boolean publicMethodsOnly;


	/**
	 * Create a default AnnotationTransactionAttributeSource, supporting
	 * public methods that carry the <code>Transactional</code> annotation.
	 */
	public AnnotationTransactionAttributeSource() {
		this.publicMethodsOnly = false;
	}

	/**
	 * Create a custom AnnotationTransactionAttributeSource.
	 * @param publicMethodsOnly whether to support public methods that carry
	 * the <code>Transactional</code> annotation only (typically for use
	 * with proxy-based AOP), or protected/private methods as well
	 * (typically used with AspectJ class weaving)
	 */
	public AnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
		this.publicMethodsOnly = publicMethodsOnly;
	}

	protected TransactionAttribute findTransactionAttribute(Method method) {
		Annotation[] annotations = AnnotationUtils.getAnnotations(method);
		if (annotations == null) {
			return null;
		}

		// See if there is a transaction annotation.
		for (int i = 0; i < annotations.length; i++) {
			Annotation a = annotations[i];
			
			if (a instanceof Transactional) {
				Transactional tx = (Transactional) a;
				
				TransactionAttribute ta = new TransactionAttribute();
				ta.setLocked(tx.lock());
				ta.setLockName(tx.lockName());
				ta.setLockTimeout(tx.lockTimeout());
				
				return ta;
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
		return (this == other || other instanceof AnnotationTransactionAttributeSource);
	}

	public int hashCode() {
		return AnnotationTransactionAttributeSource.class.hashCode();
	}
	

}
