/**
 * @(#)AsyncDataAccessCallback.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcOperations;

/**
 * <p>
 * <a href="AsyncDataAccessCallback.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: AsyncDataAccessCallback.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface AsyncDataAccessCallback<T> {

	public T doInTemplate(SimpleJdbcOperations template);
}
