/**
 * @(#)OrderBy.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="OrderBy.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: OrderBy.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class OrderBy {
	//~ Static fields/initializers =============================================

	enum Type {
		ASC,
		DESC;
	}

	//~ Instance fields ========================================================
	
	private Column column;
	private Type type;
	
	//~ Constructors ===========================================================

	public OrderBy(Column column, Type type) {
		this.column = column;
		this.type = type;
	}
	
	public static OrderBy asc(Column column) {
		return new OrderBy(column, Type.ASC);
	}
	
	public static OrderBy desc(Column column) {
		return new OrderBy(column, Type.DESC);
	}
	
	//~ Methods ================================================================

	public String toSQL() {
		return column.toSQL() + " " + type.toString();
	}
	
	//~ Accessors ==============================================================

}
