/**
 * @(#)Cachable.java Apr 20, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

/**
 * <p>
 * <a href="Cachable.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Cachable.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface Cachable {
	
	public CacheKey getCacheKey();
	
	public int getCacheExpirationTime();
}
