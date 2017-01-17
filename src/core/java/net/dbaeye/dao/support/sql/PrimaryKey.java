/**
 * @(#)PrimaryKey.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="PrimaryKey.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id $
 */
public class PrimaryKey {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Column[] columns;
	
	//~ Constructors ===========================================================

	public PrimaryKey(String column) {
		this(new String[] { column });
	}
	
	public PrimaryKey(String[] columns) {
		this.columns = new Column[columns.length];
		for (int i = 0; i < columns.length; i++) {
			this.columns[i] = new Column(columns[i]);
			this.columns[i].setUpdate(false);
		}
	}
	
	public PrimaryKey(Column[] columns) {
		this.columns = columns;
		for (int i = 0; i < columns.length; i++) {
			columns[i].setUpdate(false);
		}
	}
	
	//~ Methods ================================================================

	/**
	 * @return the columns
	 */
	public Column[] getColumns() {
		return columns;
	}
	
	public String[] getColumnNames() {
		String[] columnNames = new String[columns.length];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columns[i].getName();
		}
		return columnNames;
	}
}
