/**
 * @(#)AbstractAsyncDataCollector.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <a href="AbstractAsyncDataCollector.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AbstractConcurrentDataCollector.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class AbstractConcurrentDataCollector<T> implements ConcurrentDataCollector<T> {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private CountDownLatch latch;
	protected ConcurrentLinkedQueue<T> queue;
	protected T result;
	
	//~ Constructors ===========================================================

	public AbstractConcurrentDataCollector() {
		this(null, null);
	}
	
	public AbstractConcurrentDataCollector(CountDownLatch latch) {
		this(latch, null);
	}
	
	public AbstractConcurrentDataCollector(CountDownLatch latch, T result) {
		this.latch = latch;
		this.result = result;
		queue = new ConcurrentLinkedQueue<T>();
	}
	
	//~ Methods ================================================================

	/* (non-Javadoc)
	 * @see net.dbaeye.dao.ConcurrentDataCollector#init(int)
	 */
	public void init(int count) {
		if (latch != null) {
			throw new RuntimeException("Collector is already initialized");
		}
		latch = new CountDownLatch(count);
	}
	
	/* (non-Javadoc)
	 * @see net.dbaeye.dao.AsyncDataCollector#collect(java.lang.Object)
	 */
	public void collect(T data) {
		if (data != null) {
			queue.offer(data);
		}
		latch.countDown();
	}
	
	private void collect() {
		if (queue.peek() == null) {
			return;
		}
		synchronized (this) {
			List<T> list = new ArrayList<T>();
			while (queue.peek() != null) {
				list.add(queue.poll());
			}
			doCollect(list, latch.getCount() == 0);
		}
	}
	
	protected abstract void doCollect(List<T> data, boolean last);
	
	/* (non-Javadoc)
	 * @see net.dbaeye.dao.AsyncDataCollector#get()
	 */
	public T get() {
		try {
			while (!latch.await(50, TimeUnit.MILLISECONDS)) {
				collect();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		collect();
		
		return result;
	}

}
