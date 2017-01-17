/**
 * @(#)DataSourceFactoryBean.java Jul 18, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.dao.support;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <p>
 * <a href="DataSourceFactoryBean.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataSourceFactoryBean.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DataSourceFactoryBean implements FactoryBean, InitializingBean {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================
	
	private DataSourceFactory dataSourceFactory;
	private Properties properties;
	private DataSource dataSource;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSourceFactory);
		Assert.notNull(properties);
		dataSource = dataSourceFactory.createDataSource(properties);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return dataSource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return DataSource.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}	
	
	//~ Accessors ==============================================================

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * @param dataSourceFactory the dataSourceFactory to set
	 */
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
}
