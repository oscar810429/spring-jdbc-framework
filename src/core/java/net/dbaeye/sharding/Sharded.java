/**
 * @(#)ShardedId.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;


/**
 * <p>
 * <a href="ShardedId.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Sharded.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface Sharded<T> {
	
	public ShardingClue<T> getShardingClue();
	
	public void setShardingClue(ShardingClue<T> clue);
}
