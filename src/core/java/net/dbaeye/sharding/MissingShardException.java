/**
 * @(#)MissingShardException.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

/**
 * <p>
 * <a href="MissingShardException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MissingShardException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MissingShardException extends ShardingException {

	private static final long serialVersionUID = -140357394125817650L;

	private int shardId;
	
	public MissingShardException(int shardId) {
		super("Missing shard with ID: " + shardId);
		this.shardId = shardId;
	}
	
	/**
	 * @return the shardId
	 */
	public int getShardId() {
		return shardId;
	}
	
	/**
	 * @param shardId the shardId to set
	 */
	public void setShardId(int shardId) {
		this.shardId = shardId;
	}
}
