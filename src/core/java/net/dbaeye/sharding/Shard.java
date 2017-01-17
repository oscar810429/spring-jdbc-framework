/**
 * @(#)Shard.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <a href="Shard.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Shard.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Shard {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private int id;
	private List<Database> databases = new ArrayList<Database>(2);
//	private Map<Integer, Database> databases = new HashMap<Integer, Database>(2);
	private boolean globalShard;
	
	//~ Constructors ===========================================================

	public Shard() {}
	
	public Shard(int id) {
		this.id = id;
	}
	
	//~ Methods ================================================================
	
	public void addDatabase(Database database) {
		databases.add(database);
	}
	
	public List<Database> getDatabases() {
		return databases;
	}
	
	//~ Accessors ==============================================================

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @param globalShard the globalShard to set
	 */
	public void setGlobalShard(boolean globalShard) {
		this.globalShard = globalShard;
	}
	
	/**
	 * @return the globalShard
	 */
	public boolean isGlobalShard() {
		return globalShard;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id;
	}
}
