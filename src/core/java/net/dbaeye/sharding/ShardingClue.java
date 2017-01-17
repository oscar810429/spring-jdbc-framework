/**
 * @(#)ShardingClue.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;


/**
 * <p>
 * <a href="ShardingClue.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardingClue.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface ShardingClue<T> {
	
	public T getClue();
	
//	public void setClue(T clue);
	
	public String getClueCacheKey();
}
