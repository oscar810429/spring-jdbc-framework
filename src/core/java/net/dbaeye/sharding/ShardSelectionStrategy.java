/**
 * @(#)ShardSelectionStrategy.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import java.util.List;

/**
 * <p>
 * <a href="ShardSelectionStrategy.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardSelectionStrategy.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface ShardSelectionStrategy {
	
	public void setShards(List<Shard> shards);
	
	public Shard selectShard(ShardingClue<?> clue);
}
