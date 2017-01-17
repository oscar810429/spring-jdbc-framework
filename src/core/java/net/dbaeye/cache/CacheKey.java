/**
 * @(#)CacheKey.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

/**
 * <p>
 * <a href="CacheKey.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: CacheKey.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface CacheKey {
	
	String getRegionName();
	
	String getKey();
}
