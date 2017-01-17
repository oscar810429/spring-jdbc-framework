/**
 * @(#)LocalModelCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * <a href="LocalModelCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: LocalCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class LocalCache implements Cache {
	//~ Static fields/initializers =============================================

	private static final Logger logger = LoggerFactory.getLogger(LocalCache.class);
	
	//~ Instance fields ========================================================
	
	private Map<String, Cachable> cache = null;

	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	private String getKey(CacheKey key) {
		if (key.getRegionName() == null) {
			return key.getKey();
		}
		return key.getRegionName() + "#" + key.getKey();
	}
	
	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#del(net.dbaeye.cache.CacheKey)
	 */
	public void del(CacheKey key) {
		String strKey = getKey(key);
		
		logger.debug("Deleting cachable: {}", strKey);
		
		if (cache != null) {
			cache.remove(strKey);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#flush()
	 */
	public void flush() {
		logger.debug("Flushing session cache");
		
		if (cache != null) {
			cache.clear();
			cache = null;
		}
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#get(net.dbaeye.cache.CacheKey)
	 */
	public Cachable get(CacheKey key) {
		if (cache != null) {
			String strKey = getKey(key);
			Cachable obj = cache.get(strKey);
			if (logger.isDebugEnabled()) {
				if (obj != null) {
					logger.debug("Object with cache key[{}] found in session cache", strKey);
				}
			}
			return obj;
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#put(net.dbaeye.cache.Cachable)
	 */
	public void put(Cachable cachable) {
		logger.debug("Putting cachable: {} into session cache", cachable);
		
		if (cache == null) {
			cache = new HashMap<String, Cachable>();
		}
		String strKey = getKey(cachable.getCacheKey());
		cache.put(strKey, cachable);
	}

}
