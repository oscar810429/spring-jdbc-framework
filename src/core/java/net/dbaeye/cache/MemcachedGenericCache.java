/**
 * @(#)MemcachedGenericCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import java.io.Serializable;

import net.spy.memcached.MemcachedClient;

/**
 * <p>
 * <a href="MemcachedGenericCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MemcachedGenericCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MemcachedGenericCache extends MemcachedCache implements GenericCache {

	/**
	 * @param client
	 * @param regionName
	 * @param timeToLiveSeconds
	 * @param addPrefix
	 */
	public MemcachedGenericCache(MemcachedClient client, String regionName, int timeToLiveSeconds) {
		super(client, regionName, timeToLiveSeconds);
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.GenericCache#del(java.lang.String)
	 */
	public void del(String key) {
		del(new DefaultCacheKey(regionName, key));
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.GenericCache#get(java.lang.String)
	 */
	public Object get(String key) {
		Cachable cachable = get(new DefaultCacheKey(regionName, key));
		if (cachable == null || !(cachable instanceof DefaultCachable)) {
			return null;
		}
		return ((DefaultCachable) cachable).obj;
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.GenericCache#put(java.lang.String, java.lang.Object)
	 */
	public void put(String key, Object value) {
		put(new DefaultCachable(new DefaultCacheKey(regionName, key), value));
	}

	static class DefaultCacheKey implements CacheKey, Serializable {
		private static final long serialVersionUID = 1L;
		
		String regionName;
		String key;
		
		DefaultCacheKey(String regionName, String key) {
			this.regionName = regionName;
			this.key = key;
		}
		
		/* (non-Javadoc)
		 * @see net.dbaeye.cache.CacheKey#getRegionName()
		 */
		public String getRegionName() {
			return regionName;
		}
		/* (non-Javadoc)
		 * @see net.dbaeye.cache.CacheKey#getKey()
		 */
		public String getKey() {
			return key;
		}
	}
	
	static class DefaultCachable implements Cachable, Serializable {
		private static final long serialVersionUID = 1L;
		
		CacheKey key;
		Object obj;
		
		DefaultCachable(CacheKey key, Object obj) {
			this.key = key;
			this.obj = obj;
		}
		
		/* (non-Javadoc)
		 * @see net.dbaeye.cache.Cachable#getCacheExpirationTime()
		 */
		public int getCacheExpirationTime() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see net.dbaeye.cache.Cachable#getCacheKey()
		 */
		public CacheKey getCacheKey() {
			return key;
		}	
	}
}
