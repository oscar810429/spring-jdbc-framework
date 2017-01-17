/*
 * @(#)MissingParameterException.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.core.exception;

/**
 * <p>
 * <a href="MissingParameterException.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: MissingParameterException.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class MissingParameterException extends RuntimeException {
    private String paramName;
    
    public MissingParameterException(String paramName) {
        super("parameter '" + paramName + "' is required.");
        this.paramName = paramName;
    }
    
    public String getParamName() {
        return paramName;
    }
}
