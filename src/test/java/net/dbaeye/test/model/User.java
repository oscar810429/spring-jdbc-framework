package net.dbaeye.test.model;

import net.dbaeye.core.model.BaseObject;

public class User extends BaseObject{
	
	private static final long serialVersionUID = -8579893081775640129L;
	private Integer id;
	private String username;
	private String password;
	
	public User(){
		
	}
	
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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


	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	

	
	
}
