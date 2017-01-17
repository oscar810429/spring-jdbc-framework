/**
 * @(#)AccessDeniedException.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.core.exception;

/**
 * <p>
 * <a href="AccessDeniedException.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Zhang Songfu
 * @version $Id: AccessDeniedException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1028456046471990289L;

	/**
	 * Constructs an <code>AccessDeniedException</code> with the specified
	 * message.
	 * 
	 * @param msg
	 *            the detail message
	 */
	public AccessDeniedException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an <code>AccessDeniedException</code> with the specified
	 * message and root cause.
	 * 
	 * @param msg
	 *            the detail message
	 * @param t
	 *            root cause
	 */
	public AccessDeniedException(String msg, Throwable t) {
		super(msg, t);
	}
}
