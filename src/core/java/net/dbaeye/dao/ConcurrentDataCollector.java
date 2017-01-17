/**
 * @(#)AsyncDataCollector.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;


/**
 * <p>
 * <a href="AsyncDataCollector.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ConcurrentDataCollector.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface ConcurrentDataCollector<T> {
	
	public void init(int count);
	
	public void collect(T data);
	
	public T get();
}
