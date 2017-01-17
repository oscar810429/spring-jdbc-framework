/*
 * @(#)CacheException.java  2012-04-06
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.cache;

/**
 * <p>
 * <a href="CacheException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: CacheException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class CacheException extends RuntimeException {
	
	private static final long serialVersionUID = 5695283789079660065L;

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Exception e) {
		super(s, e);
	}
	
	public CacheException(Exception e) {
		super(e);
	}
	
}
