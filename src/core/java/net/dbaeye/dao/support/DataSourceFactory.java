/**
 * @(#)DataSourceFactory.java Jul 18, 2009
 * 
 * Copyright 2009  Net365. All rights reserved.
 */
package net.dbaeye.dao.support;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * <p>
 * <a href="DataSourceFactory.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Zhang Songfu
 * @version $Id: DataSourceFactory.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface DataSourceFactory {
	
	public DataSource createDataSource(Properties props) throws Exception;
}
