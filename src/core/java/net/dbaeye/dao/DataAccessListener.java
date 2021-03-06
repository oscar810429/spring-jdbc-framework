/**
 * @(#)ConnectionListener.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.util.EventListener;

/**
 * <p>
 * <a href="ConnectionListener.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessListener.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface DataAccessListener extends EventListener {
	
	void onDataAccessEvent(DataAccessEvent event);
}
