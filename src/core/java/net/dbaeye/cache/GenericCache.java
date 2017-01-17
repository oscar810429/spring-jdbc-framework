/**
 * @(#)GenericCache.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

/**
 * <p>
 * <a href="GenericCache.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: GenericCache.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface GenericCache extends Cache {
	
	public Object get(String key);
	
	public void put(String key, Object value);
	
	public void del(String key);
	
}
