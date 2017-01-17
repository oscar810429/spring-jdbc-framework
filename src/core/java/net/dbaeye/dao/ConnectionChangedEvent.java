/**
 * @(#)ConnectionChangedEvent.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.sql.Connection;

/**
 * <p>
 * <a href="ConnectionChangedEvent.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: ConnectionChangedEvent.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class ConnectionChangedEvent extends DataAccessEvent {
	//~ Static fields/initializers =============================================

	private static final long serialVersionUID = 1698845354361099529L;

	//~ Instance fields ========================================================

	private Connection oldConnection;
	private Connection newConnection;
	
	//~ Constructors ===========================================================

	/**
	 * @param source
	 */
	public ConnectionChangedEvent(Object source, Connection oldConnection, Connection newConnection) {
		super(source);
		this.oldConnection = oldConnection;
		this.newConnection = newConnection;
	}
	//~ Methods ================================================================

	//~ Accessors ==============================================================

	/**
	 * @return the oldConnection
	 */
	public Connection getOldConnection() {
		return oldConnection;
	}
	
	/**
	 * @return the newConnection
	 */
	public Connection getNewConnection() {
		return newConnection;
	}
	
}
