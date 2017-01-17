/**
 * @(#)Database.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.sharding;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * <a href="Database.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu 
 * @version $Id: Database.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Database {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private int id;
	private int shardId;
	
	private String dirver;
	private String username;
	private String password;
	private String url;
	
	private ShardedDataSource dataSource;
	
	//~ Constructors ===========================================================

	//~ Methods ================================================================

	/**
	 * @return the dataSource
	 */
	public ShardedDataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(ShardedDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	//~ Accessors ==============================================================

	/**
	 * @return the dirver
	 */
	public String getDirver() {
		return dirver;
	}
	/**
	 * @param dirver the dirver to set
	 */
	public void setDirver(String dirver) {
		this.dirver = dirver;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the shardId
	 */
	public int getShardId() {
		return shardId;
	}
	/**
	 * @param shardId the shardId to set
	 */
	public void setShardId(int shardId) {
		this.shardId = shardId;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("shardId", this.shardId)
				.append("dirver", this.dirver)
				.append("username", this.username)
				.append("password", this.password)
				.append("url", this.url)
				.toString();
	}
	
}
