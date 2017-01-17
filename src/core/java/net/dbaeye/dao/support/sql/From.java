/**
 * @(#)From.java Apr 05, 2012
 * 
 * Copyright 2012  Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="From.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: From.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class From {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Table table;
	
	//~ Constructors ===========================================================

	public From() {}
	
	public From(Table table) {
		this.table = table;
	}
	
	//~ Methods ================================================================

	public String toSQL() {
		if (table.getAlias() == null) {
			return table.getName();
		}
		return table.getName() + " " + table.getAlias();
	}
	
	//~ Accessors ==============================================================

}
