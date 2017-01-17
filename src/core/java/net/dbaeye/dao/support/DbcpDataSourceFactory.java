/**
 * @(#)DBCPDataSourceFactory.java Jul 18, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.dao.support;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import net.dbaeye.dao.support.DataSourceFactory;

/**
 * <p>
 * <a href="DBCPDataSourceFactory.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DbcpDataSourceFactory.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DbcpDataSourceFactory implements DataSourceFactory {

    private final static String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
    private final static String PROP_DEFAULTREADONLY = "defaultReadOnly";
    private final static String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
    private final static String PROP_DEFAULTCATALOG = "defaultCatalog";
    private final static String PROP_DRIVERCLASSNAME = "driverClassName";
    private final static String PROP_MAXACTIVE = "maxActive";
    private final static String PROP_MAXIDLE = "maxIdle";
    private final static String PROP_MINIDLE = "minIdle";
    private final static String PROP_INITIALSIZE = "initialSize";
    private final static String PROP_MAXWAIT = "maxWait";
    private final static String PROP_TESTONBORROW = "testOnBorrow";
    private final static String PROP_TESTONRETURN = "testOnReturn";
    private final static String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    private final static String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
    private final static String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
    private final static String PROP_TESTWHILEIDLE = "testWhileIdle";
    private final static String PROP_PASSWORD = "password";
    private final static String PROP_URL = "url";
    private final static String PROP_USERNAME = "username";
    private final static String PROP_VALIDATIONQUERY = "validationQuery";
    private final static String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
//    private final static String PROP_REMOVEABANDONED = "removeAbandoned";
//    private final static String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
//    private final static String PROP_LOGABANDONED = "logAbandoned";
    private final static String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
    private final static String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
    private final static String PROP_CONNECTIONPROPERTIES = "connectionProperties";

    // -------------------------------------------------- ObjectFactory Methods

    /**
     * Creates and configures a {@link BasicDataSource} instance based on the
     * given properties.
     * 
     * @param properties the datasource configuration properties
     * @throws Exception if an error occurs creating the data source
     */
    public DataSource createDataSource(Properties properties) throws Exception {
        BasicDataSource dataSource = new BasicDataSource();
        String value = null;

        value = properties.getProperty(PROP_DEFAULTAUTOCOMMIT);
        if (value != null) {
            dataSource.setDefaultAutoCommit(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_DEFAULTREADONLY);
        if (value != null) {
            dataSource.setDefaultReadOnly(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_DEFAULTTRANSACTIONISOLATION);
        if (value != null) {
            int level = -1;
            if ("NONE".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_NONE;
            }
            else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_READ_COMMITTED;
            }
            else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_READ_UNCOMMITTED;
            }
            else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_REPEATABLE_READ;
            }
            else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
                level = Connection.TRANSACTION_SERIALIZABLE;
            }
            else {
                try {
                    level = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    System.err.println("Could not parse defaultTransactionIsolation: " + value);
                    System.err.println("WARNING: defaultTransactionIsolation not set");
                    System.err.println("using default value of database driver");
                    level = -1;
                }
            }
            dataSource.setDefaultTransactionIsolation(level);
        }

        value = properties.getProperty(PROP_DEFAULTCATALOG);
        if (value != null) {
            dataSource.setDefaultCatalog(value);
        }

        value = properties.getProperty(PROP_DRIVERCLASSNAME);
        if (value != null) {
            dataSource.setDriverClassName(value);
        }

        value = properties.getProperty(PROP_MAXACTIVE);
        if (value != null) {
            dataSource.setMaxActive(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MAXIDLE);
        if (value != null) {
            dataSource.setMaxIdle(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MINIDLE);
        if (value != null) {
            dataSource.setMinIdle(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_INITIALSIZE);
        if (value != null) {
            dataSource.setInitialSize(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MAXWAIT);
        if (value != null) {
            dataSource.setMaxWait(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_TESTONBORROW);
        if (value != null) {
            dataSource.setTestOnBorrow(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_TESTONRETURN);
        if (value != null) {
            dataSource.setTestOnReturn(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_TIMEBETWEENEVICTIONRUNSMILLIS);
        if (value != null) {
            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_NUMTESTSPEREVICTIONRUN);
        if (value != null) {
            dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_MINEVICTABLEIDLETIMEMILLIS);
        if (value != null) {
            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
        }

        value = properties.getProperty(PROP_TESTWHILEIDLE);
        if (value != null) {
            dataSource.setTestWhileIdle(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_PASSWORD);
        if (value != null) {
            dataSource.setPassword(value);
        }

        value = properties.getProperty(PROP_URL);
        if (value != null) {
            dataSource.setUrl(value);
        }

        value = properties.getProperty(PROP_USERNAME);
        if (value != null) {
            dataSource.setUsername(value);
        }

        value = properties.getProperty(PROP_VALIDATIONQUERY);
        if (value != null) {
            dataSource.setValidationQuery(value);
        }

        value = properties.getProperty(PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED);
        if (value != null) {
            dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value).booleanValue());
        }
        
        /*
        value = properties.getProperty(PROP_REMOVEABANDONED);
        if (value != null) {
            dataSource.setRemoveAbandoned(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_REMOVEABANDONEDTIMEOUT);
        if (value != null) {     
            dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_LOGABANDONED);
        if (value != null) {
            dataSource.setLogAbandoned(Boolean.valueOf(value).booleanValue());
        }
        */
        value = properties.getProperty(PROP_POOLPREPAREDSTATEMENTS);
        if (value != null) {
            dataSource.setPoolPreparedStatements(Boolean.valueOf(value).booleanValue());
        }

        value = properties.getProperty(PROP_MAXOPENPREPAREDSTATEMENTS);
        if (value != null) {
            dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
        }

        value = properties.getProperty(PROP_CONNECTIONPROPERTIES);
        if (value != null) {
        	Properties p = getProperties(value);
        	Enumeration<?> e = p.propertyNames();
        	while (e.hasMoreElements()) {
        		String propertyName = (String) e.nextElement();
        		dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
        	}
        }

        // Return the configured DataSource instance
        return dataSource;
    }

    /**
     * <p>Parse properties from the string. Format of the string must be [propertyName=property;]*<p>
     * @param propText
     * @return Properties
     * @throws Exception
     */
    static private Properties getProperties(String propText) throws Exception {
    	Properties p = new Properties();
    	if (propText != null) {
    		p.load(new ByteArrayInputStream(propText.replace(';', '\n').getBytes()));
    	}
    	return p;
    }
}
