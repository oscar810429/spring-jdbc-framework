/**
 * @(#)SkipMethodInvocationException.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

/**
 * <p>
 * <a href="SkipMethodInvocationException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: SkipMethodInvocationException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class SkipMethodInvocationException extends RuntimeException {
	//~ Static fields/initializers =============================================

	private static final long serialVersionUID = -2030399244507131912L;

	//~ Instance fields ========================================================

	private Object result;
	
	//~ Constructors ===========================================================

	public SkipMethodInvocationException(Object result) {
		this.result = result;
	}

	//~ Methods ================================================================

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
}
