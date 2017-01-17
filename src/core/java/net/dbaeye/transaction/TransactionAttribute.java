/**
 * @(#)TransactionAttribute.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

/**
 * <p>
 * <a href="TransactionAttribute.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionAttribute.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class TransactionAttribute {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================
	
	private boolean locked;
	private long lockTimeout;
	private String lockName;
	private boolean complexLockName;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * @return the lockName
	 */
	public String getLockName() {
		return lockName;
	}
	
	/**
	 * @param lockName the lockName to set
	 */
	public void setLockName(String lockName) {
		if (lockName == null || "".equals(lockName)) {
			this.lockName = null;
			return;
		}
		this.lockName = lockName;
		if (lockName.indexOf("%s") != -1) {
			this.complexLockName = true;
		}
	}

	/**
	 * @return the lockTimeout
	 */
	public long getLockTimeout() {
		return lockTimeout;
	}
	
	/**
	 * @param lockTimeout the lockTimeout to set
	 */
	public void setLockTimeout(long lockTimeout) {
		this.lockTimeout = lockTimeout;
	}
	
	/**
	 * @return the complexLockName
	 */
	public boolean isComplexLockName() {
		return complexLockName;
	}
	
	//~ Accessors ==============================================================

}
