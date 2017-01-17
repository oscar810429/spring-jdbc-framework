/**
 * @(#)Lock.java Apr 9, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.sql.Connection;

/**
 * <p>
 * <a href="Lock.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Lock.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Lock {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Connection connection;
	private String name;
	
	//~ Constructors ===========================================================

	public Lock(String name, Connection connection) {
		this.name = name;
		this.connection = connection;
	}
	
	//~ Methods ================================================================

	//~ Accessors ==============================================================

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
