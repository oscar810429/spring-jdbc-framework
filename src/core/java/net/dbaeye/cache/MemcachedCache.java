/**
 * @(#)MemcachedModelCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <a href="MemcachedModelCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MemcachedCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MemcachedCache implements Cache {
	//~ Static fields/initializers =============================================
	
	private static final Logger log = LoggerFactory.getLogger(MemcachedCache.class);
	
	//~ Instance fields ========================================================
	
	protected final String regionName;
	protected final int timeToLiveSeconds;
	
	protected MemcachedClient memcached;
	
	//~ Constructors ===========================================================

	public MemcachedCache(MemcachedClient client, String regionName, int timeToLiveSeconds) {
		this.regionName = regionName;
		this.timeToLiveSeconds = timeToLiveSeconds;
		
		this.memcached = client;
	}
	
	//~ Methods ================================================================
	
	protected String getKey(CacheKey key) {
		if (key.getRegionName() == null) {
			return key.getKey();
		}
		return key.getRegionName() + "#" + key.getKey();
	}
	
	/**
	 * @param memcached the memcached to set
	 */
	public void setMemcached(MemcachedClient client) {
		this.memcached = client;
	}
	
	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#del(net.dbaeye.cache.CacheKey)
	 */
	public void del(CacheKey key) {
		String strKey = getKey(key);
		
		if (log.isDebugEnabled()) {
			log.debug("** memcached[{}].delete({})", regionName, strKey);
		}
		
		memcached.delete(strKey);
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#flush()
	 */
	public void flush() {
		if (log.isDebugEnabled()) {
			log.debug("** memcached[{}].flush()", regionName);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#get(net.dbaeye.cache.CacheKey)
	 */
	public Cachable get(CacheKey key) {
		String strKey = getKey(key);
		
		if (log.isDebugEnabled()) {
			log.debug("** memcached[{}].get({})", regionName, strKey);
		}
		
		try {
			Cachable cached = (Cachable) memcached.get(strKey);
			if (log.isDebugEnabled()) {
				if (cached != null) {
					log.debug("** memcached: object {} found in cache for key {}", cached, strKey);
				} else {
					log.debug("** memcached[{}] missed: key {}", regionName, strKey);
				}
			}
			return cached;
		} catch (Throwable t) {
			log.info(t.getMessage());
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#put(net.dbaeye.cache.Cachable)
	 */
	public void put(Cachable obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Can not cache null");
		}
		int cacheExpirationTime = obj.getCacheExpirationTime();
		if (cacheExpirationTime == 0) {
			cacheExpirationTime = timeToLiveSeconds;
		}
		String key = getKey(obj.getCacheKey());
		if (log.isDebugEnabled()) {
			log.debug("** memcached[{}].set({}, {})", new Object[] {regionName, key, cacheExpirationTime});
		}
		memcached.set(key, cacheExpirationTime, obj);
	}

}
