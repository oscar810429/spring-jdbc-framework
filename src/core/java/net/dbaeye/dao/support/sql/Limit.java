/**
 * @(#)Limit.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="Limit.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Limit.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Limit {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private int offset;
	private int limit;
	
	//~ Constructors ===========================================================

	public Limit(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}
	
	//~ Methods ================================================================

	public String toSQL() {
		return String.format("limit %d, %d", offset, limit);
	}
	
	//~ Accessors ==============================================================

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
}
