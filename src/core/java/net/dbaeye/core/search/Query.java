/*
 * @(#)Query.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.core.search;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * <a href="Query.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Query.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Query implements Serializable {
    private int start;
    private int limit;
    private boolean count = false; 
    
    private Map params = new HashMap();
    
    public Query(int start, int limit) {
    	this(start, limit, false);
    }
    
    public Query(int start, int limit, boolean count) {
        this.start = start;
        this.limit = limit;
        this.count = count;
    } 
    
    public static Query CountQuery(int start, int limit) {
    	return new Query(start, limit, true);
    }
    
    /**
	 * @return Returns the count.
	 */
	public boolean isCount() {
		return count;
	}
	
	/**
	 * @param count The count to set.
	 */
	public void setCount(boolean count) {
		this.count = count;
	}
    

    /*
     * @see net.dbaeye.core.search.Query#getStart()
     */
    public int getStart() {
        return start;
    }

    /*
     * @see net.dbaeye.core.search.Query#setStart(int)
     */
    public Query setStart(int start) {
        this.start = start;
        return this;
    }

    /*
     * @see net.dbaeye.core.search.Query#getLimit()
     */
    public int getLimit() {
        return limit;
    }

    /*
     * @see net.dbaeye.core.search.Query#setLimit(int)
     */
    public Query setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /*
     * @see net.dbaeye.core.search.Query#add(java.lang.String, java.lang.Object)
     */
    public Query add(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    /*
     * @see net.dbaeye.core.search.Query#remove(java.lang.String)
     */
    public Query remove(String name) {
        this.params.remove(name);
        return this;
    }

    /*
     * @see net.dbaeye.core.search.Query#addAll(java.util.Map)
     */
    public Query addAll(Map conditions) {
        this.params.putAll(conditions);
        return this;
    }

    /*
     * @see net.dbaeye.core.search.Query#getConditionMap()
     */
    public Map getParams() {
        return params;
    }

    /*
     * @see net.dbaeye.core.search.Query#get(java.lang.String)
     */
    public Object get(String name) {
        return params.get(name);
    }

    /*
     * @see net.dbaeye.core.search.Query#getArray(java.lang.String)
     */
    public Object[] getArray(String name) {
        Object o = get(name);
        
        if (o == null) {
            return null;
        }
        
        if (!(o.getClass().isArray())) { 
            return new Object[] {o};
        }
        return (Object[]) o;
    }

    /*
     * @see net.dbaeye.core.search.Query#getString(java.lang.String)
     */
    public String getString(String name) {
        Object o = get(name);
        
        if (o == null) {
            return null;
        }
        
        if (o.getClass().isArray()) {
            if (Array.getLength(o) > 0) {
                return Array.get(o, 0).toString();
            }
            return null;
        }
        return o.toString();
    }

    /*
     * @see com.edu88.edupass.search.Query#getString(java.lang.String, java.lang.String)
     */
    public String getString(String name, String defaultValue) {
        String value = getString(name);
        
        return value == null ? defaultValue : value;
    }

    /*
     * @see net.dbaeye.core.search.Query#getStringArray(java.lang.String)
     */
    public String[] getStringArray(String name) {
        Object o = get(name);
        
        if (o == null) {
            return null;
        }
        
        if (!(o.getClass().isArray())) {
            return new String[] {o.toString()};
        }
        if (!(o instanceof String[])) {
            Object[] oa = (Object[]) o;
            String[] result = new String[oa.length];
            for (int i = 0; i < oa.length; i++) {
                result[i] = oa[i].toString();
            }
            return result;
        }
        
        return (String[]) o;
    }

    /*
     * @see net.dbaeye.core.search.Query#getInt(java.lang.String)
     */
    public int getInt(String name) {
        return getInt(name, 0);
    }
    
    /*
     * @see com.edu88.edupass.search.Query#getInt(java.lang.String, int)
     */
    public int getInt(String name, int defaultValue) {
        Object o = get(name);
        
        if (o == null) {
            return defaultValue;
        }
        
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /*
     * @see net.dbaeye.core.search.Query#getLong(java.lang.String)
     */
    public long getLong(String name) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getFloat(java.lang.String)
     */
    public float getFloat(String name) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getDouble(java.lang.String)
     */
    public double getDouble(String name) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getDouble(java.lang.String, double)
     */
    public double getDouble(String name, double defaultValue) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getFloat(java.lang.String, float)
     */
    public float getFloat(String name, float defaultValue) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getLong(java.lang.String, long)
     */
    public long getLong(String name, long defaultValue) {
        // todo Auto-generated method stub
        return 0;
    }

    /*
     * @see net.dbaeye.core.search.Query#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }
    
    /*
     * @see net.dbaeye.core.search.Query#getBoolean(java.lang.String, boolean)
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        Object o = get(name);
        
        if (o == null) {
            return defaultValue;
        }
        
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        
        String text = null;
        
        if (o.getClass().isArray()) {
            if (Array.getLength(o) > 0) {
                text = Array.get(o, 0).toString();
            } else {
                return false;
            }
        } else {
            text = o.toString();
        }
        
        if ("true".equalsIgnoreCase(text) ||
            "yes".equalsIgnoreCase(text) ||
            "1".equalsIgnoreCase(text)) {
            return true;
        }
        return false;
    }

    /*
     * @see net.dbaeye.core.search.Query#getDate(java.lang.String)
     */
    public Date getDate(String name) {
        Object o = get(name);
        
        if (o != null && (o instanceof Date)) {
            return (Date) o;
        }
        return null;
    }

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Query)) {
			return false;
		}
		Query rhs = (Query) object;
		return new EqualsBuilder()
				.append(this.limit, rhs.limit)
				.append(this.start, rhs.start)
				.append(this.params, rhs.params).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-152126031, -1146405679)
				.append(this.limit)
				.append(this.start)
				.append(this.params).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
				.append("start", this.start)
				.append("limit", this.limit)
				.append("params", this.params)
				.toString();
	}

}
