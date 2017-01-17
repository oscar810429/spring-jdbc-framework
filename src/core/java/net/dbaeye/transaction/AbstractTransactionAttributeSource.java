/**
 * @(#)AbstractTransactionAttributeSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.JdkVersion;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * <a href="AbstractTransactionAttributeSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AbstractTransactionAttributeSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class AbstractTransactionAttributeSource implements TransactionAttributeSource {
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractTransactionAttributeSource.class);

	/**
	 * Canonical value held in cache to indicate no transaction attribute was
	 * found for this method, and we don't need to look again.
	 */
	private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new TransactionAttribute();
	

	/**
	 * Cache of TransactionAttributes, keyed by DefaultCacheKey (Method + target Class).
	 * <p>As this base class is not marked Serializable, the cache will be recreated
	 * after serialization - provided that the concrete subclass is Serializable.
	 */
//	private final Map<Object, TransactionAttribute> cache = new HashMap<Object, TransactionAttribute>();
	final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap<Object, TransactionAttribute>();

	/**
	 * Return the transaction attribute for this method invocation.
	 * <p>Defaults to the class's transaction attribute if no method attribute is found.
	 * @param method method for the current invocation. Can't be <code>null</code>
	 * @param targetClass target class for this invocation. May be <code>null</code>.
	 * @return TransactionAttribute for this method, or <code>null</code> if the method is non-transactional
	 */
	public final TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
		// First, see if we have a cached value.
		Object cacheKey = getCacheKey(method, targetClass);
		TransactionAttribute cached = this.attributeCache.get(cacheKey);
		if (cached != null) {
			// Value will either be canonical value indicating there is no transaction attribute,
			// or an actual transaction attribute.
			if (cached == NULL_TRANSACTION_ATTRIBUTE) {
				return null;
			}
			return cached;
		}
		// We need to work it out.
		TransactionAttribute txAtt = computeTransactionAttribute(method, targetClass);
		// Put it in the cache.
		if (txAtt == null) {
			this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Adding transactional method [" + method.getName() + "] with attribute [" + txAtt + "]");
			}
			this.attributeCache.put(cacheKey, txAtt);
		}
		return txAtt;
	}

	/**
	 * Determine a cache key for the given method and target class.
	 * <p>Must not produce same key for overloaded methods.
	 * Must produce same key for different instances of the same method.
	 * @param method the method (never <code>null</code>)
	 * @param targetClass the target class (may be <code>null</code>)
	 * @return the cache key (never <code>null</code>)
	 */
	protected Object getCacheKey(Method method, Class<?> targetClass) {
		return new DefaultCacheKey(method, targetClass);
	}

	/**
	 * Same signature as <code>getTransactionAttribute</code>, but doesn't cache the result.
	 * <code>getTransactionAttribute</code> is effectively a caching decorator for this method.
	 * @see #getTransactionAttribute
	 */
	private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) {
		// Don't allow no-public methods as required.
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return null;
		}

		// The method may be on an interface, but we need attributes from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		// If we are dealing with method with generic parameters, find the original method.
		if (JdkVersion.isAtLeastJava15()) {
			specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
		}

		// First try is the method in the target class.
		TransactionAttribute txAtt = findTransactionAttribute(specificMethod);
		if (txAtt != null) {
			return txAtt;
		}

		// Second try is the transaction attribute on the target class.
//		txAtt = findDataAccessAttribute(specificMethod.getDeclaringClass());
//		if (txAtt != null) {
//			return txAtt;
//		}

		if (specificMethod != method) {
			// Fallback is to look at the original method.
			txAtt = findTransactionAttribute(method);
			if (txAtt != null) {
				return txAtt;
			}
			// Last fallback is the class of the original method.
//			return findTransactionAttribute(method.getDeclaringClass());
		}
		return null;
	}

	protected abstract TransactionAttribute findTransactionAttribute(Method method);
	
	/**
	 * Should only public methods be allowed to have transactional semantics?
	 * Default implementation returns <code>false</code>.
	 */
	protected boolean allowPublicMethodsOnly() {
		return false;
	}


	/**
	 * Default cache key for the TransactionAttribute cache.
	 */
	private static class DefaultCacheKey {

		private final Method method;

		private final Class<?> targetClass;

		public DefaultCacheKey(Method method, Class<?> targetClass) {
			this.method = method;
			this.targetClass = targetClass;
		}

		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof DefaultCacheKey)) {
				return false;
			}
			DefaultCacheKey otherKey = (DefaultCacheKey) other;
			return (this.method.equals(otherKey.method) &&
					ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass));
		}

		public int hashCode() {
			return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
		}
	}
}
