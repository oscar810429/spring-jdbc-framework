/**
 * @(#)DataAccess.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * <a href="DataAccess.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author ZhangSongfu
 * @version $Id: DataAccessMethod.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataAccessMethod {
	
	int shardingClue() default 0;
	
	int cacheKey() default -1;
	
	int[] flushArgs() default {};
	
	boolean flushResult() default false;
	
	boolean cacheResult() default false;
	
	/**
	 * Just execute once with the same args in a Transaction
	 * @return
	 */
	boolean onceWithSameArgs() default false;
}
