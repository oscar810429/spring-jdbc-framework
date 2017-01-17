/**
 * @(#)ConnectionEvent.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.util.EventObject;

/**
 * <p>
 * <a href="ConnectionEvent.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessEvent.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DataAccessEvent extends EventObject {
	
	private static final long serialVersionUID = 7539203026857944510L;
	
	/** System time when the event happened */
	private final long timestamp;

	/**
	 * Create a new ApplicationEvent.
	 * @param source the component that published the event (never <code>null</code>)
	 */
	public DataAccessEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Return the system time in milliseconds when the event happened.
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}
}
