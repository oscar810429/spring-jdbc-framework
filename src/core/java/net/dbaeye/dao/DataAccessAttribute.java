/**
 * @(#)DataAccessAttribute.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;


/**
 * <p>
 * <a href="DataAccessAttribute.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessAttribute.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DataAccessAttribute {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================
	
	private int cacheKey;
	private int shardingClue;
	private boolean cacheResult;
	private int[] flushArgs;
	private boolean flushResult;
	private boolean onceWithSameArgs;
	
	// lazy loading support
	private String managerName;
	private String methodName;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	/**
	 * @return the cacheKey
	 */
	public int getCacheKey() {
		return cacheKey;
	}
	
	/**
	 * @param cacheKey the cacheKey to set
	 */
	public void setCacheKey(int cacheKey) {
		this.cacheKey = cacheKey;
	}
	
	/**
	 * @return the shardingClue
	 */
	public int getShardingClue() {
		return shardingClue;
	}
	
	/**
	 * @param shardingClue the shardingClue to set
	 */
	public void setShardingClue(int shardingClue) {
		this.shardingClue = shardingClue;
	}
	
	/**
	 * @return the cacheResult
	 */
	public boolean isCacheResult() {
		return cacheResult;
	}
	
	/**
	 * @param cacheResult the cacheResult to set
	 */
	public void setCacheResult(boolean cacheResult) {
		this.cacheResult = cacheResult;
	}
	
	/**
	 * @return the cacheArgs
	 */
	public int[] getFlushArgs() {
		return flushArgs;
	}
	
	/**
	 * @param cacheArgs the cacheArgs to set
	 */
	public void setFlushArgs(int[] flushArgs) {
		this.flushArgs = flushArgs;
	}
	
	/**
	 * @param flushResult the flushResult to set
	 */
	public void setFlushResult(boolean flushResult) {
		this.flushResult = flushResult;
	}
	
	/**
	 * @return the flushResult
	 */
	public boolean isFlushResult() {
		return flushResult;
	}

	/**
	 * @return the onceWithSameArgs
	 */
	public boolean isOnceWithSameArgs() {
		return onceWithSameArgs;
	}

	/**
	 * @param onceWithSameArgs the onceWithSameArgs to set
	 */
	public void setOnceWithSameArgs(boolean onceWithSameArgs) {
		this.onceWithSameArgs = onceWithSameArgs;
	}
	

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}
	
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
