/**
 * @(#)Condition.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao.support.sql;

/**
 * <p>
 * <a href="Condition.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Condition.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Condition {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private String whereClause;
	
	//~ Constructors ===========================================================

	public Condition(String whereClause) {
		this.whereClause = whereClause;
	}
	
	//~ Methods ================================================================

	public String toSQL() {
		return whereClause;
	}
	
	//~ Accessors ==============================================================

}
