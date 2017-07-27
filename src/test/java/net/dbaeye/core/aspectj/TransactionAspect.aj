/**
 * @(#)TransactionAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.core.aspectj;

import net.dbaeye.transaction.AbstractTransactionAspect;
import net.dbaeye.transaction.AnnotationTransactionAttributeSource;
import net.dbaeye.transaction.Transactional;

/**
 * <p>
 * <a href="TransactionAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: TransactionAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public aspect TransactionAspect extends AbstractTransactionAspect {

	public TransactionAspect() {
		super(new AnnotationTransactionAttributeSource(false));
	}

	private pointcut executionOfAnyTransactionalMethod() :
		execution(* net.dbaeye.core.service.impl..*.*(..)) && @annotation(Transactional);
	
	
	protected pointcut transactionalMethodExecution(Object txObject) :
		executionOfAnyTransactionalMethod() && this(txObject);
	
}
