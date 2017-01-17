/**
 * @(#)CachableString.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache.support;

import java.io.Serializable;

import net.dbaeye.cache.Cachable;
import net.dbaeye.cache.CacheKey;

/**
 * <p>
 * <a href="CachableString.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: SimpleCachable.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class SimpleCachable<V> implements Serializable, Cachable, CacheKey {
	//~ Static fields/initializers =============================================

	private static final long serialVersionUID = -3489517890718653977L;

	//~ Instance fields ========================================================

	private String key;
	private String regionName;
	private V value;
	
	//~ Constructors ===========================================================

	public SimpleCachable(String key, String regionName, V value) {
		this.key = key;
		this.regionName = regionName;
		this.value = value;
	}
	
	//~ Methods ================================================================

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cachable#getCacheExpirationTime()
	 */
	public int getCacheExpirationTime() {
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cachable#getCacheKey()
	 */
	public CacheKey getCacheKey() {
		return this;
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.CacheKey#getKey()
	 */
	public String getKey() {
		return key;
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.CacheKey#getRegionName()
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @return the value
	 */
	public V getValue() {
		return value;
	}
}
