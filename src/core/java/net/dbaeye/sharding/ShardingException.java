/**
 * @(#)ShardingException.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

/**
 * <p>
 * <a href="ShardingException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ShardingException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class ShardingException extends RuntimeException {

	private static final long serialVersionUID = -2769678526039364680L;

	/**
	 * 
	 */
	public ShardingException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ShardingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ShardingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ShardingException(Throwable cause) {
		super(cause);
	}

	
}
