/*
 * @(#)CacheProvider.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import java.util.Properties;

/**
 * <p>
 * <a href="CacheProvider.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: CacheProvider.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface CacheProvider {
	
	/**
	 * Configure the cache
	 *
	 * @param regionName the name of the cache region
	 * @param properties configuration settings
	 * @throws CacheException
	 */
	public Cache buildCache(String regionName, Properties properties) throws CacheException;
	
	public GenericCache buildGenericCache(String regionName, Properties properties) throws CacheException;

	/**
	 * Callback to perform any necessary initialization of the underlying cache implementation
	 * during SessionFactory construction.
	 *
	 * @param properties current configuration settings.
	 */
	public void start(Properties properties) throws CacheException;

	/**
	 * Callback to perform any necessary cleanup of the underlying cache implementation
	 * during SessionFactory.close().
	 */
	public void stop();
}
