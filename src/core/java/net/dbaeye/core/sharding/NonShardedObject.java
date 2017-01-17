/**
 * @(#)NonSharded.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.core.sharding;

import java.io.Serializable;

import net.dbaeye.cache.Cachable;
import net.dbaeye.cache.CacheKey;
import net.dbaeye.sharding.NonShardedClue;

/**
 * <p>
 * <a href="NonSharded.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: NonShardedObject.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class NonShardedObject implements NonShardedClue<String>, Cachable, CacheKey, Serializable {
	//~ Static fields/initializers =============================================
	
	private static final long serialVersionUID = -5989435463596514749L;
	
	public static final NonShardedClue<String> GLOBAL_SHARDING_CLUE = new NonShardedClue<String>() {
		public String getClue() { return null; }
		public String getClueCacheKey() { return null; }
	};

	//~ Instance fields ========================================================
	
	protected String id;
	
	protected Class<?> type;
	
	//~ Constructors ===========================================================
	
	public NonShardedObject() {}
	
	public NonShardedObject(Class<?> type, String id) {
		this.type = type;
		this.id = id;
	}
	
	//~ Methods ================================================================

	/* (non-Javadoc)
	 * @see net.dbaeye.sharding.ShardingClue#getClue()
	 */
	public String getClue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.sharding.ShardingClue#getClueCacheKey()
	 */
	public String getClueCacheKey() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.cache.Cachable#getCacheKey()
	 */
	public CacheKey getCacheKey() {
		return this;
	}
	
	public int getCacheExpirationTime() {
		return 0; // 0 make memcached will never expire this item 
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.cache.CacheKey#getRegionName()
	 */
	public String getRegionName() {
		return type.getSimpleName();
		//return null;
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.cache.CacheKey#getKey(java.lang.Class)
	 */
	public String getKey() {
		return id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
