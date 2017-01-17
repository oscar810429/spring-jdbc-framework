/**
 * @(#)LinkedModelCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;


/**
 * <p>
 * <a href="LinkedModelCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: LinkedCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class LinkedCache implements Cache {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Cache[] caches;
	
	//~ Constructors ===========================================================

	public LinkedCache(Cache[] caches) {
		this.caches = caches;
	}
	
	//~ Methods ================================================================

    /**
     * (non-Javadoc)
     * @see net.dbaeye.cache.Cache#del(net.dbaeye.cache.CacheKey)
     */
	public void del(CacheKey key) {
		for (int i = 0; i < caches.length; i++) {
			caches[i].del(key);
		}
	}


	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#flush()
	 */
	public void flush() {
		for (int i = 0; i < caches.length; i++) {
			caches[i].flush();
		}
	}


	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#get(net.dbaeye.cache.CacheKey)
	 */
	public Cachable get(CacheKey key) {
		for (int i = 0; i < caches.length; i++) {
			Cachable obj = caches[i].get(key);
			if (obj != null) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see net.dbaeye.cache.Cache#put(net.dbaeye.cache.Cachable)
	 */
	public void put(Cachable cachable) {
		for (int i = 0; i < caches.length; i++) {
			caches[i].put(cachable);
		}
	}

}
