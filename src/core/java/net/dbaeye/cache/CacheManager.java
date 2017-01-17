/*
 * @(#)CacheManager.java  2012-04-06
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.util.ClassLoaderUtils;

/**
 * <p>
 * <a href="CacheManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: CacheManager.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class CacheManager {
	private static final Logger log = LoggerFactory.getLogger(CacheManager.class);
	
	private static final String DEFAULT_PROVIDER = "net.dbaeye.cache.MemCacheProvider";
	
	private static CacheProvider cacheProvider;
	private static Map<String, Cache> caches = new HashMap<String, Cache>();
	private static Map<String, GenericCache> genericCaches = new HashMap<String, GenericCache>();
	
	static {
		
		String className = DEFAULT_PROVIDER;
		
		Class baseClazz = CacheProvider.class;

		Class clazz = null;

		try {
			clazz = ClassLoaderUtils.loadClass(className, CacheManager.class);
		} catch (ClassNotFoundException e) {
			log.warn("specified CacheProvider[" + className + "] is not found.");
		}

		// make sure it extends CacheProvider
		if (clazz == null || !baseClazz.isAssignableFrom(clazz)) {
			if (clazz != null) {
				log.warn("specified CacheProvider[" + className + "] is not assignable from com.painiu.cache.CacheProvider.");				
			}
			try {
				clazz = ClassLoaderUtils.loadClass(DEFAULT_PROVIDER, CacheManager.class);
			} catch (ClassNotFoundException ex) {
				// should not happen.
			}
		}

		try {
			cacheProvider = (CacheProvider) clazz.newInstance();
			cacheProvider.start(null);
		} catch (InstantiationException e) {
			log.error("InstantiationException occurred while initialze CacheProvider");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException occurred while initialze CacheProvider");			
			e.printStackTrace();
		}

	}
	
	
	private CacheManager() {}
//	
//	public static Cache getSessionCache(String regionName) {
//		Cache cache = (Cache) caches.get(regionName);
//		if (cache == null) {
//			Properties props = new Properties();
//			props.put("poolName", "session");
//			cache = buildCache(regionName, props);
//			caches.put(regionName, cache);
//		}
//		
//		return cache;
//	}
//	
	public static Cache getCache(String regionName) {
		Cache cache = caches.get(regionName);
		
		if (cache == null) {
			cache = buildCache(regionName, null);
			caches.put(regionName, cache);
		}
		
		return cache;
	}
	
	public static GenericCache getGenericCache(String regionName) {
		GenericCache cache = genericCaches.get(regionName);
		
		if (cache == null) {
			cache = buildGenericCache(regionName, null);
			caches.put(regionName, cache);
		}
		
		return cache;
	}
	
	private static Cache buildCache(String regionName, Properties properties) {
		return cacheProvider.buildCache(regionName, properties);
	}
	
	private static GenericCache buildGenericCache(String regionName, Properties properties) {
		return cacheProvider.buildGenericCache(regionName, properties);
	}
	
	public static void shutdown() {
		cacheProvider.stop();
	}
	
	//~ Accessors ==============================================================
}
