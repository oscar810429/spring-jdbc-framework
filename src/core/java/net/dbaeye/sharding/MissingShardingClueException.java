/**
 * @(#)MissingShardingClueException.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;


/**
 * <p>
 * <a href="MissingShardingClueException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MissingShardingClueException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MissingShardingClueException extends ShardingException {
	//~ Static fields/initializers =============================================

	private static final long serialVersionUID = 794478573510437209L;

	//~ Instance fields ========================================================
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================

	//~ Accessors ==============================================================

	/**
	 * 
	 */
	public MissingShardingClueException() {
		super("Object missing sharding clue");
	}

}
