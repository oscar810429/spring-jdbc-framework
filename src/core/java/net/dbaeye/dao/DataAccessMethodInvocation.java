/**
 * @(#)DataAccessMethodInvocation.java Apr 05, 2012
 * 
 * Copyright 2012 Net365. All rights reserved.
 */
package net.dbaeye.dao;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.dbaeye.sharding.ShardedDataSource;

/**
 * <p>
 * <a href="DataAccessMethodInvocation.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: DataAccessMethodInvocation.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class DataAccessMethodInvocation {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	private Method method;
	private Object[] args;
	
	private ShardedDataSource dataSource;
	
	//~ Constructors ===========================================================

	public DataAccessMethodInvocation(Method method, Object[] args) {
		this.method = method;
		this.args = args;
	}
	
	//~ Methods ================================================================

	//~ Accessors ==============================================================

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}
	
	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}
	
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(ShardedDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * @return the dataSource
	 */
	public ShardedDataSource getDataSource() {
		return dataSource;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int code = method.hashCode();
		if (dataSource != null) {
			code += dataSource.getShard().hashCode();
		}
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				code += args[i].hashCode();
			}
		}
		return code;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataAccessMethodInvocation)) {
			return false;
		}
		DataAccessMethodInvocation rhs = (DataAccessMethodInvocation) obj;
		if (dataSource == null || rhs.dataSource == null || 
				dataSource.getShard().getId() != rhs.getDataSource().getShard().getId()) {
			return false;
		}
		if (!method.equals(rhs.method)) {
			return false;
		}
		return Arrays.equals(args, rhs.args);
	}
}
