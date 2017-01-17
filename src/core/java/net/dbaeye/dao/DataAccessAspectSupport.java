/**
 * @(#)DataAccessAspectSupport.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dbaeye.cache.Cachable;
import net.dbaeye.cache.Cache;
import net.dbaeye.cache.CacheKey;
//import net.dbaeye.core.model.User;
import net.dbaeye.sharding.NonShardedClue;
import net.dbaeye.sharding.Sharded;
import net.dbaeye.sharding.ShardingClue;

/**
 * <p>
 * <a href="DataAccessAspectSupport.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessAspectSupport.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class DataAccessAspectSupport {
	//~ Static fields/initializers =============================================

	protected static final Logger logger = LoggerFactory.getLogger(DataAccessAspectSupport.class);
	
	private static final NonShardedClue<String> NON_SHARDED_CLUE = new NonShardedClue<String>() {
		public String getClue() {
			return null;
		}
		public String getClueCacheKey() {
			return null;
		}
	};
	
	//~ Instance fields ========================================================

	private DataAccessAttributeSource dataAccessAttributeSource;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	/**
	 * @param dataAccessAttributeSource the dataAccessAttributeSource to set
	 */
	public void setDataAccessAttributeSource(
			DataAccessAttributeSource dataAccessAttributeSource) {
		this.dataAccessAttributeSource = dataAccessAttributeSource;
	}
	
	/**
	 * @return the dataAccessAttributeSource
	 */
	public DataAccessAttributeSource getDataAccessAttributeSource() {
		return dataAccessAttributeSource;
	}
	
	protected Object beforeExecution(DataAccessAttribute attr, DataAccessMethodInvocation invocation) {
		DataAccessContext ctx = DataAccessContext.getContext();
		
		Method method = invocation.getMethod();
		Object[] args = invocation.getArgs();
		
		if (attr.isCacheResult() && method.getReturnType() != null && Cachable.class.isAssignableFrom(method.getReturnType())) {
			Cache cache = ctx.getCache();
			
			if (cache != null) {
				CacheKey key = findCacheKey(attr, args);
				
				if (key != null) {
					Object result = cache.get(key);
					if (result != null) {
						logger.debug("Returning cached result[{}] for DataAccessMethod: {}", result, method);
						throw new SkipMethodInvocationException(result);
					}
				}
			}
		}
		
		// find out the sharding clue, and locate the correct database connection
		/*ShardingClue<?> clue = findShardingClue(attr, args);
		
		Connection conn = null;
		
		if (clue != null) {
		    conn = ctx.getConnection(clue);
		}
		
		if (attr.isOnceWithSameArgs()) {
			if (ctx.hasInvoked(invocation)) {
				logger.debug("Same shard located for this invocation in transaction, skip invocation");
				throw new SkipMethodInvocationException(null);
			}
		}
		
		if (conn == null) {
			logger.warn("I can not automically locating Database connection, you should do it yourself.");
		}*/
		return null;
	}
	
	protected static CacheKey findCacheKey(DataAccessAttribute attr, Object[] args) {
		if (args == null || args.length == 0) {
			return null;
		}
		
		CacheKey key = null;
		
		if (attr.getCacheKey() < 0 || attr.getCacheKey() >= args.length) {
			logger.warn("cacheKey index is out of parameter arrry range");
		} else {
			try {
				key = (CacheKey) args[attr.getCacheKey()];
			} catch (ClassCastException e) {
				logger.error("Specified cache key is not a CacheKey instance.", e);
			}
		}
		if (key == null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof CacheKey) {
					return (CacheKey) args[i];
				}
			}
		}
		return key;
	}
	
	protected static ShardingClue<?> findShardingClue(DataAccessAttribute attr, Object[] args) {
		ShardingClue<?> clue = null;
		
		if (attr.getShardingClue() == -1) {
			return NON_SHARDED_CLUE;
		}
		
		if (args != null && args.length > 0) {
			if (attr.getShardingClue() < 0 || attr.getShardingClue() >= args.length) {
				logger.warn("shardingClue index is out of parameters array range");
			} else {
				Object obj = args[attr.getShardingClue()];
				clue = findShardingClue(obj);
			}
			if (clue == null) {
				for (int i = 0; i < args.length; i++) {
					clue = findShardingClue(args[i]);
					if (clue != null) {
						break;
					}
				}
			}
		}
		return clue;
	}
	
	private static ShardingClue<?> findShardingClue(Object obj) {
		if (obj instanceof ShardingClue) {
			return (ShardingClue<?>) obj;
		} else if (obj instanceof Sharded<?>) {
			return ((Sharded<?>) obj).getShardingClue();
		} /*else if (obj instanceof String) {
			return new User.Id((String) obj);
		}*/
		if (obj instanceof Iterable) {
			Iterator<?> i = ((Iterable<?>) obj).iterator();
			while (i.hasNext()) {
				return findShardingClue(i.next());
			}
		} else if (obj instanceof Array) {
			int len = Array.getLength(obj);
			for (int j = 0; j < len; j++) {
				return findShardingClue(Array.get(obj, j));
			}
		}
		return null;
	}

	protected Object beforeExecution(Method method, Class<?> targetClass, Object[] args) {
		DataAccessMethodInvocation invocation = new DataAccessMethodInvocation(method, args);
		DataAccessContext.getContext().beginInvocation(invocation);
		DataAccessAttribute attr = getDataAccessAttributeSource().getDataAccessAttribute(method, targetClass);
		return beforeExecution(attr, invocation);
	}
	
	@SuppressWarnings("unchecked")
	protected Object afterReturning(DataAccessAttribute attr, Method method, Object[] args, Object result) {
		if (result != null) {
			ShardingClue clue = findShardingClue(attr, args);
			
			if (clue != null) {
				if (result instanceof Sharded) {
					Sharded resultClue = (Sharded) result;
					if (resultClue.getShardingClue() == null) {
						resultClue.setShardingClue(clue);
					}
				} else if (result instanceof Iterable) {
					Iterator i = ((Iterable) result).iterator();
					while (i.hasNext()) {
						Object item = i.next();
						if (item instanceof Sharded) {
							Sharded resultClue = (Sharded) item;
							if (resultClue.getShardingClue() == null) {
								resultClue.setShardingClue(clue);
							}
						}
					}
				}
			}
		}
		
		// caching stuff
		Cache cache = DataAccessContext.getContext().getCache();
		if (cache == null) {
			return result;
		}
		
		// dealing with args caching
		if (args != null && args.length > 0) {
			int[] flushArgs = attr.getFlushArgs();
			for (int i = 0; i < flushArgs.length; i++) {
				if (flushArgs[i] < args.length) {
					flush(cache, args[flushArgs[i]]);
				}
			}
		}
		
		// dealing with result caching
		if (result != null) {
			if (attr.isCacheResult()) {
				cache(cache, result);
			} else if (attr.isFlushResult()) {
				flush(cache, result);
			}
		}
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private static void flush(Cache cache, Object arg) {
		if (arg instanceof CacheKey) {
			cache.del((CacheKey) arg);
		} else if (arg instanceof Cachable) {
			cache.del(((Cachable) arg).getCacheKey());
		} else if (arg instanceof Iterable) {
			Iterator iter = ((Iterable) arg).iterator();
			while (iter.hasNext()) {
				flush(cache, iter.next());
			}
		} else if (arg instanceof Array) {
			int len = Array.getLength(arg);
			for (int j = 0; j < len; j++) {
				Object object = Array.get(arg, j);
				flush(cache, object);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void cache(Cache cache, Object result) {
		if (result == null) {
			return;
		}
		if (result instanceof Cachable) {
			cache.put((Cachable) result);
		} else if (result instanceof Iterable) {
			Iterator iter = ((Iterable) result).iterator();
			while (iter.hasNext()) {
				cache(cache, iter.next());
			}
		} else if (result instanceof Array) {
			int len = Array.getLength(result);
			for (int j = 0; j < len; j++) {
				cache(cache, Array.get(result, j));
			}
		}
	}
	
	
	protected Object afterReturning(Method method, Class<?> targetClass, Object[] args, Object result) {
		DataAccessContext.getContext().finishInvocation();
		
		DataAccessAttribute attr = getDataAccessAttributeSource().getDataAccessAttribute(method, targetClass);
		return afterReturning(attr, method, args, result);
	}
	
	//~ Accessors ==============================================================

}
