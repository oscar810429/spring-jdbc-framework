/**
 * @(#)ShardingDaoSupport.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import net.dbaeye.core.dao.Dao;
import net.dbaeye.core.search.Result;
import net.dbaeye.dao.AsyncDataAccessCallback;
import net.dbaeye.dao.ConcurrentDataCollector;
import net.dbaeye.dao.support.sql.Select;

/**
 * <p>
 * <a href="ShardingDaoSupport.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Zhang Songfu
 * @version $Id: BaseDaoSupport.java 105 2012-04-26 09:14:24Z jiangjinrong $
 */
public class BaseDaoSupport extends DaoSupport implements Dao {
	//~ Static fields/initializers =============================================

	private static final Logger logger = LoggerFactory.getLogger(BaseDaoSupport.class);
	
	//private static final Map<Shard, SimpleJdbcOperations> jdbcTemplates = new HashMap<Shard, SimpleJdbcOperations>();

	//~ Instance fields ========================================================

	private JdbcTemplate classicJdbcTemplate;
	private SimpleJdbcOperations jdbcTemplate;
	
	protected TaskExecutor taskExecutor;

	//~ Constructors ===========================================================

	//~ Methods ================================================================

	/**
	 * @return the jdbcTemplate
	 */
	public SimpleJdbcOperations getJdbcTemplate() {
		if (jdbcTemplate == null) {
			jdbcTemplate = new SimpleJdbcTemplate(classicJdbcTemplate);
		}
		return jdbcTemplate;
	}

	
	/*public SimpleJdbcOperations getJdbcTemplate(Shard shard) {
		synchronized (ShardingDaoSupport.class) {
			SimpleJdbcOperations template = jdbcTemplates.get(shard);
			if (template != null) {
				return template;
			}
			ShardingJdbcTemplate classic = new ShardingJdbcTemplate();
			classic.setShard(shard);
			classic.setExceptionTranslator(classicJdbcTemplate.getExceptionTranslator());
			template = new SimpleJdbcTemplate(classic);
			jdbcTemplates.put(shard, template);
			return template;
		}
	}*/
	
	/**
	 * @param classicJdbcTemplate the classicJdbcTemplate to set
	 */
	public void setClassicJdbcTemplate(JdbcTemplate classicJdbcTemplate) {
		this.classicJdbcTemplate = classicJdbcTemplate;
	}
	
	/**
	 * @param taskExecutor the taskExecutor to set
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.dao.support.DaoSupport#checkDaoConfig()
	 */
	@Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		if (this.classicJdbcTemplate == null) {
			throw new IllegalArgumentException("'classicJdbcTemplate' is required");
		}
	}

	protected void assertUpdated(int rowcount) {
		if (rowcount != 1) {
			throw new IncorrectResultSetColumnCountException(1, rowcount);
		}
	}


	protected <T> List<T> findForList(String sql,
			ParameterizedRowMapper<T> rowMapper, Object arg) {
		return findForList(sql, rowMapper, new Object[] { arg });
	}

	protected <T> List<T> findForList(String sql,
			ParameterizedRowMapper<T> rowMapper, Object... args) {
		return findForResult(null, sql, rowMapper, -1, -1, args).getData();
	}
	

	protected <T> Result<T> findForResult(String sqlCount, String sql,
			ParameterizedRowMapper<T> rowMapper, int offset, int limit,
			Object... args) {
		Result<T> result = new Result<T>(offset, limit);

		int total = -1;

		if (sqlCount != null && offset != -1 && limit > 0) {
			total = getJdbcTemplate().queryForInt(sqlCount, args);
		}

		List<T> list = getJdbcTemplate().query(sql, rowMapper,
				getArguments(args, offset, limit));

		if (total == -1) {
			total = list.size();
		}

		result.setTotal(total);
		result.setData(list);

		return result;
	}

	private Object[] getArguments(Object[] varArgs, int offset, int limit) {
		if (limit > 0) {
			if (offset == -1) {
				offset = 0;
			}
			Object[] args = new Object[varArgs.length + 2];
			System.arraycopy(varArgs, 0, args, 0, varArgs.length);
			args[varArgs.length] = offset;
			args[varArgs.length + 1] = limit;
			return args;
		}
		return varArgs;
	}

	protected <T> List<T> findForList(Select select,
			ParameterizedRowMapper<T> rowMapper, Object... args) {
		return findForResult(select, rowMapper, args).getData();
	}

	protected <T> Result<T> findForResult(Select select,
			ParameterizedRowMapper<T> rowMapper, Object... args) {
		if (select.getLimit() == null) {
			return findForResult(null, select.toSQL(), rowMapper, -1, -1, args);
		}
		return findForResult(select.toCountSQL(), select.toSQL(), rowMapper,
				select.getLimit().getOffset(), select.getLimit().getLimit(),
				args);
	}
	
	
	/*protected <T> void doInShards(Collection<Shard> shards, AsyncDataAccessCallback<T> callback, long timeout) {
		CountDownLatch latch = new CountDownLatch(shards.size());
		
		for (Shard shard : shards) {
			SimpleJdbcOperations template = getJdbcTemplate(shard);
			doInExecutor(template, callback, latch);
		}
		
		try {
			latch.await(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("InterruptedException: " + e.getMessage(), e);
		}
	}

	protected <T> T findInShards(AsyncDataAccessCallback<T> callback,
			ConcurrentDataCollector<T> collector) {
		DataAccessContext context = DataAccessContext.getContext();

		List<Shard> shards = context.getShards();

		collector.init(shards.size());

		for (Shard shard : shards) {
			SimpleJdbcOperations template = getJdbcTemplate(shard);
			doInExecutor(template, callback, collector);
		}
		
		return collector.get();
	}*/

	protected <T> void doInExecutor(final SimpleJdbcOperations template,
			final AsyncDataAccessCallback<T> callback) {

		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					callback.doInTemplate(template);
				}
			});
		} catch (TaskRejectedException e) {
			logger.error("Can not execute task: {}", e.getMessage());
		}
	}

	protected <T> void doInExecutor(final SimpleJdbcOperations template,
			final AsyncDataAccessCallback<T> callback,
			final ConcurrentDataCollector<T> collector) {

		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					T result = null;
					try {
						result = callback.doInTemplate(template);
					} finally {
						if (collector != null) {
							collector.collect(result);
						}
					}
				}
			});
		} catch (TaskRejectedException e) {
			logger.error("Can not execute task: {}", e.getMessage());
		}
	}

	protected <T> Future<T> doInExecutor(final SimpleJdbcOperations template,
			final AsyncDataAccessCallback<T> callback,
			final CountDownLatch latch) {
		final CountDownLatch l = new CountDownLatch(1);
		final DataAccessFuture<T> future = new DataAccessFuture<T>(latch, 1000);

		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					T result = null;
					try {
						result = callback.doInTemplate(template);
					} finally {
						future.set(result);
						l.countDown();
						if (latch != null) {
							latch.countDown();
						}
					}
				}
			});
		} catch (TaskRejectedException e) {
			logger.error("Can not execute task: {}", e.getMessage());
		}

		return future;
	}

	
	static class DataAccessFuture<T> implements Future<T> {
		private final CountDownLatch latch;
		private final AtomicReference<T> objRef;
		private final long globalOperationTimeout;

		public DataAccessFuture(CountDownLatch l, long globalOperationTimeout) {
			this(l, new AtomicReference<T>(null), globalOperationTimeout);
		}

		public DataAccessFuture(CountDownLatch l, AtomicReference<T> oref,
				long timeout) {
			super();
			latch = l;
			objRef = oref;
			globalOperationTimeout = timeout;
		}

		public boolean cancel(boolean ign) {
			return false;
		}

		public T get() throws InterruptedException, ExecutionException {
			try {
				return get(globalOperationTimeout, TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				throw new RuntimeException("Timed out waiting for operation", e);
			}
		}

		public T get(long duration, TimeUnit units)
				throws InterruptedException, TimeoutException,
				ExecutionException {
			if (!latch.await(duration, units)) {
				throw new TimeoutException("Timed out waiting for operation");
			}
			if (isCancelled()) {
				throw new ExecutionException(new RuntimeException("Cancelled"));
			}

			return objRef.get();
		}

		void set(T o) {
			objRef.set(o);
		}

		public boolean isCancelled() {
			return false;
		}

		public boolean isDone() {
			return latch.getCount() == 0;
		}

	}
	
	
	public static <T> void collectResult(List<T> result,
			List<List<T>> data, Comparator<T> comparator, 
			int offset, int limit, boolean last) {
		
		List<T> merged = new ArrayList<T>();
		merged.addAll(result);
		result.clear();
		
		for (List<T> list : data) {
			merged.addAll(list);
		}
		
		Collections.sort(merged, comparator);
		
		int size = merged.size();
		
		if (!last) {
			if (size <= offset + limit) {
				result.addAll(merged);
			} else {
				result.addAll(merged.subList(0, offset + limit));
			}
		} else if (offset < size) {
			int toIndex = offset + limit;
			if (toIndex > size) {
				toIndex = size;
			}
			result.addAll(merged.subList(offset, toIndex));
		}
	}

}
