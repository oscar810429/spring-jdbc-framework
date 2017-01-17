/**
 * @(#)Cache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;


/**
 * <p>
 * <a href="Cache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Cache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface Cache {

	public Cachable get(CacheKey key);
	
	public void put(Cachable cachable);
	
	public void del(CacheKey key);
	
	public void flush();
}
