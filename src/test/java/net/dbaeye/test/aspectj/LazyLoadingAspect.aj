/**
 * @(#)LazyLoadingAspect.aj Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.test.aspectj;

import net.dbaeye.dao.AnnotationDataAccessAttributeSource;
import net.dbaeye.dao.AbstractLazyLoadingAspect;
import net.dbaeye.dao.LazyLoading;

/**
 * <p>
 * <a href="LazyLoadingAspect.aj.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: LazyLoadingAspect.aj 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public aspect LazyLoadingAspect extends AbstractLazyLoadingAspect {
	public LazyLoadingAspect() {
		super(new AnnotationDataAccessAttributeSource());
	}

	protected pointcut lazyLoadingMethodExecution(Object model) : 
				execution(* net.dbaeye.core.model..*.*(..)) && 
				@annotation(LazyLoading) &&
				this(model);
}
