/**
 * @(#)SQL.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="SQL.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: SQL.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public abstract class SQL {
	
	public static Select select(Column[] columns) {
		return new Select(columns);
	}
	
	public static Select select(Table table) {
		Column[] columns = table.getAllColumns();
		if (table.getAlias() != null) {
			for (int i = 0; i < columns.length; i++) {
				columns[i].setAlias(columns[i].getName());
			}
		}
		return new Select(columns);
	}
}
