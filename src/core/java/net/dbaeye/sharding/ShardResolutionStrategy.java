/**
 * @(#)ShardResolveStrategy.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import java.util.List;

/**
 * <p>
 * <a href="ShardResolveStrategy.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardResolutionStrategy.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface ShardResolutionStrategy {
	
	public void setGlobalShard(Shard shard);
	
	public void setShards(List<Shard> shards);
	
	public Shard resolveShard(ShardingClue<?> clue);
	
	public void bind(ShardingClue<?> clue, Shard shard);
	
	public void unbind(ShardingClue<?> clue);
	
	public void updateBinding(ShardingClue<?> oldClue, ShardingClue<?> newClue, Shard shard);
}
