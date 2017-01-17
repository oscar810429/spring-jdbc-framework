/**
 * @(#)DataAccessAttributeSource.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.Method;

/**
 * <p>
 * <a href="DataAccessAttributeSource.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessAttributeSource.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface DataAccessAttributeSource {

	DataAccessAttribute getDataAccessAttribute(Method method, Class<?> targetClass);

}
