/**
 * @(#)Column.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="Column.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Column.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Column {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private String name;
	private String alias;
	
	private boolean insert = true;
	private boolean update = true;
	
	private Table table;
	
	//~ Constructors ===========================================================

	public Column(String name) {
		this(name, null);
	}
	
	public Column(String name, String alias) {
		this(name, alias, true, true);
	}
	
	public Column(String name, boolean insert, boolean update) {
		this(name, null, insert, update);
	}
	
	public Column(String name, String alias, boolean insert, boolean update) {
		this.name = name;
		this.alias = alias;
		this.insert = insert;
		this.update = update;
	}
	
	//~ Methods ================================================================
	
	public Column as(String alias) {
		this.alias = alias;
		return this;
	}
	
	public Join innerJoin(Column to) {
		return new Join(this, to, Join.Type.INNER);
	}
	
	public Join outerJoin(Column to) {
		return new Join(this, to, Join.Type.OUTER);
	}
	
	public String toSQL() {
		StringBuilder sb = new StringBuilder(30);
		if (table != null && table.getAlias() != null) {
			sb.append(table.getAlias()).append('.');
		}
		sb.append(name);
		return sb.toString();
	}
	
	//~ Accessors ==============================================================

	/**
	 * @param insert the insert to set
	 */
	public void setInsert(boolean insert) {
		this.insert = insert;
	}
	
	/**
	 * @return the insert
	 */
	public boolean isInsert() {
		return insert;
	}
	
	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param sQLTable the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}
}
