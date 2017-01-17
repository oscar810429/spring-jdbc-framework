/**
 * @(#)SQLSelect.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="SQLSelect.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Select.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Select {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Column[] columns;
	private From from;
	private Condition condition;
	private OrderBy orderBy;
	private Limit limit;
	
	//~ Constructors ===========================================================

	public Select(Column[] columns) {
		this.columns = columns;
	}
	
	//~ Methods ================================================================

	public Select from(From from) {
		this.from = from;
		return this;
	}
	
	public Select where(String condition) {
		this.condition = new Condition(condition);
		return this;
	}
	
	public Select orderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	public Select limit(int offset, int limit) {
		if (offset != -1 && limit != -1) {
			this.limit = new Limit(offset, limit);
		}
		return this;
	}
	
	public String toSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			sb.append(column.toSQL());
			if (column.getAlias() != null) {
				sb.append(" AS ").append(column.getAlias());
			}
			if (i < columns.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(" FROM ").append(from.toSQL());
		sb.append(" WHERE ").append(condition.toSQL());
		if (orderBy != null) {
			sb.append(" ORDER BY ").append(orderBy.toSQL());
		}
		if (limit != null) {
			sb.append(" LIMIT ?, ?");
		}
		return sb.toString();
	}
	
	public String toCountSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*) FROM ");
		sb.append(from.toSQL());
		sb.append(" WHERE ").append(condition.toSQL());
		return sb.toString();
	}
	
	//~ Accessors ==============================================================

	/**
	 * @return the limit
	 */
	public Limit getLimit() {
		return limit;
	}
}
