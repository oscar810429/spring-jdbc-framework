/**
 * @(#)UUID.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p>
 * <a href="UUID.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: UUID.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class UUID {
	
	public static String randomUUID() {
		java.util.UUID uuid = java.util.UUID.randomUUID();
		long mostSigBits = uuid.getMostSignificantBits();
		long leastSigBits = uuid.getLeastSignificantBits();
		return (digits(mostSigBits >> 32, 8) + 
				digits(mostSigBits >> 16, 4) + 
				digits(mostSigBits, 4) + 
				digits(leastSigBits >> 48, 4) + 
				digits(leastSigBits, 12));
	}
	/** Returns val represented by the specified number of hex digits. */
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return Long.toHexString(hi | (val & (hi - 1))).substring(1);
	}
	
	public static void main(String[] args){
		
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID().length());
		
	}
	
}
