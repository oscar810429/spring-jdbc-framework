/**
 * @(#)TransactionManager.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;


/**
 * <p>
 * <a href="TransactionManager.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionManager.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public interface TransactionManager {
	
	Transaction getTransaction(TransactionAttribute attribute);
	
	void commit(Transaction transaction);
	
	void rollback(Transaction transaction);
}
