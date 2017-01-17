/**
 * @(#)TransactionException.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import org.springframework.core.NestedRuntimeException;

/**
 * <p>
 * <a href="TransactionException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class TransactionException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7309437506569142849L;

	/**
	 * Constructor for TransactionException.
	 * @param msg the detail message
	 */
	public TransactionException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for TransactionException.
	 * @param msg the detail message
	 * @param cause the root cause from the transaction API in use
	 */
	public TransactionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
