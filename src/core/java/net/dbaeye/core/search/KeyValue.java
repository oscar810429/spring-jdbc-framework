/**
 * @(#)KeyValue.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.core.search;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * <a href="KeyValue.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: KeyValue.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class KeyValue<K,V> implements Serializable {
	//~ Static fields/initializers =============================================

	private static final long serialVersionUID = 412146523441483895L;

	//~ Instance fields ========================================================

	private K key;
	
	private V value;
	
	//~ Constructors ===========================================================

	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	//~ Methods ================================================================

	//~ Accessors ==============================================================

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}
	
	/**
	 * @return the value
	 */
	public V getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("key", key).append("value", value).toString();
	}
}
