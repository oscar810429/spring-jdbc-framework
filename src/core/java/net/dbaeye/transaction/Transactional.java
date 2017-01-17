/**
 * @(#)Transactional.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.transaction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <a href="Transactional.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Transactional.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
	
	/**
	 * <code>true</code> if the transaction is locked.
	 * <p>Defaults to <code>false</code>.
	 */
	boolean lock() default false;
	
	String lockName() default "";
	
	long lockTimeout() default 0;
}
