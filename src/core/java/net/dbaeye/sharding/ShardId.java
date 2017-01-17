/**
 * @(#)ShardId.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

/**
 * <p>
 * <a href="ShardId.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardId.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class ShardId implements ShardingClue<Integer> {
	//~ Static fields/initializers =============================================

	private static final char SHARD_ID_SEP = '@';
	
	//~ Instance fields ========================================================

	private int id;
	
	//~ Constructors ===========================================================

	public ShardId(int id) {
		this.id = id;
	}
	
	//~ Methods ================================================================

	public static ShardId extractFromString(String objId) {
		int idx = objId.indexOf(SHARD_ID_SEP);
		if (idx != -1) {
			try {
				return new ShardId(Integer.parseInt(objId.substring(idx + 1)));
			} catch (NumberFormatException e) {}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yupoo.sharding.ShardingClue#getClue()
	 */
	public Integer getClue() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see com.yupoo.sharding.ShardingClue#setClue(java.lang.Object)
	 */
	public void setClue(Integer clue) {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShardId[" + id + "]";
	}
	

	/* (non-Javadoc)
	 * @see com.yupoo.sharding.ShardingClue#getClueCacheKey()
	 */
	public String getClueCacheKey() {
		return null;
	}
	
	//~ Accessors ==============================================================

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
