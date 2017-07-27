/**
 * @(#)DataAccessAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.test.aspectj;

import net.dbaeye.dao.DataAccessMethod;
import net.dbaeye.dao.AbstractDataAccessAspect;
import net.dbaeye.dao.AnnotationDataAccessAttributeSource;

/**
 * <p>
 * <a href="DataAccessAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public aspect DataAccessAspect extends AbstractDataAccessAspect {
	public DataAccessAspect() {
		super(new AnnotationDataAccessAttributeSource());
	}

	protected pointcut dataAccessMethodExecution(Object daoObj) : 
				execution(* net.dbaeye.core.dao.jdbc..*.*(..)) && 
				@annotation(DataAccessMethod) &&
				this(daoObj);
	
}
