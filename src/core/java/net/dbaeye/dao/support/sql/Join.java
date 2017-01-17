/**
 * @(#)Join.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="Join.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Join.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Join extends From {
	//~ Static fields/initializers =============================================

	enum Type {
		INNER,
		OUTER;
	}
	
	//~ Instance fields ========================================================

	private Column from;
	private Column to;
	private Type type;
	
	//~ Constructors ===========================================================

	public Join(Column from, Column to, Type type) {
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	//~ Methods ================================================================

	public String toSQL() {
		Table fromTable = from.getTable();
		Table toTable = to.getTable();
		StringBuilder sb = new StringBuilder();
		sb.append(fromTable.getName()).append(" ").append(fromTable.getAlias());
		sb.append(" ").append(type.toString()).append(" JOIN ");
		sb.append(toTable.getName()).append(" ").append(toTable.getAlias());
		sb.append(" ON ");
		sb.append(from.toSQL()).append(" = ").append(to.toSQL());
		return sb.toString();
	}
	
	//~ Accessors ==============================================================

}
