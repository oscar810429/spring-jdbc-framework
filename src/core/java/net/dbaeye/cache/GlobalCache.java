/**
 * @(#)GlobalCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

/**
 * <p>
 * <a href="GlobalCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: GlobalCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class GlobalCache implements Cache {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	//~ Constructors ===========================================================

	//~ Methods ================================================================

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#del(net.dbaeye.cache.CacheKey)
	 */
	public void del(CacheKey key) {
		CacheManager.getCache(key.getRegionName()).del(key);
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#flush()
	 */
	public void flush() {
		// never flush global cache
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#get(net.dbaeye.cache.CacheKey)
	 */
	public Cachable get(CacheKey key) {
		return CacheManager.getCache(key.getRegionName()).get(key);
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#put(net.dbaeye.cache.Cachable)
	 */
	public void put(Cachable cachable) {
		CacheManager.getCache(cachable.getCacheKey().getRegionName()).put(cachable);
	}
}
