/**
 * @(#)Lockable.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;


/**
 * <p>
 * <a href="Lockable.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Lockable.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface Lockable<T> {
	
	public String getLockName();
	
}
