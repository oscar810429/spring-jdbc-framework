/*
 * @(#)MemCacheProvider.java Apr 06, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import java.io.IOException;
import java.util.Properties;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.util.ClassLoaderUtils;

/**
 * <p>
 * <a href="MemCacheProvider.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MemCacheProvider.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MemCacheProvider implements CacheProvider {
	
	private static final Logger log = LoggerFactory.getLogger(MemCacheProvider.class);
	
	private static final String DEFAULT_SETTINGS = "memcached.properties";
	
	
	private Properties settings = new Properties();
	
	private MemcachedClient client;
	
	/* (non-Javadoc)
	 * @see net.dbaeye.cache.CacheProvider#start(java.util.Properties)
	 */
	public void start(Properties properties) throws CacheException {
		try {
			settings.load(ClassLoaderUtils.getResourceAsStream(DEFAULT_SETTINGS, MemCacheProvider.class));
		} catch (IOException e) {
			log.error("IOException occurred while loading file " + DEFAULT_SETTINGS, e);
			
			throw new CacheException("Error loading MemcachedClient settings: " + e.getMessage(), e);
		}
		
		initClient();
	}
	
	private void initClient() {
		try {
			client = new MemcachedClient(AddrUtil.getAddresses(settings.getProperty("servers", " ")));
		} catch (IOException e) {
			throw new CacheException("Error creating MemcachedClient: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.CacheProvider#stop()
	 */
	public void stop() {
		client.shutdown();
	}

	/* (non-Javadoc)
	 * @see net.dbaeye.cache.CacheProvider#buildCache(java.lang.String, java.util.Properties)
	 */
	public Cache buildCache(String regionName, Properties properties) throws CacheException {
		if (properties == null) {
			properties = settings;
		}
		int timeToLiveSeconds = Integer.parseInt(properties.getProperty("cache." + regionName + ".timeToLiveSeconds", "0"));
		return new MemcachedCache(client, regionName, timeToLiveSeconds);
	}
	
	public GenericCache buildGenericCache(String regionName, Properties properties) throws CacheException {
		if (properties == null) {
			properties = settings;
		}
		int timeToLiveSeconds = Integer.parseInt(properties.getProperty("cache." + regionName + ".timeToLiveSeconds", "0"));
		return new MemcachedGenericCache(client, regionName, timeToLiveSeconds);
	}
}
