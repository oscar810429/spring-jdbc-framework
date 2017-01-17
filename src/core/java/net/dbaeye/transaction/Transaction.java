/**
 * @(#)Transaction.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;


/**
 * <p>
 * <a href="Transaction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Transaction.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface Transaction {
	
	public String getName();
	public void setName(String name);
	
//	public Connection getConnection();
//	public void setConnection(Connection connection);
//	
//	public Connection getNestedConnection();
//	public void setNestedConnection(Connection connection);
	
	public boolean isStarted();
	public boolean isCompleted();	
	
	public void begin() throws TransactionException;
	public void commit() throws TransactionException;
	public void rollback() throws TransactionException;
	
	
	public Lock getLock();
	public void setLock(Lock lock);
}
