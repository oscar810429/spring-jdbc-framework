/**
 * @(#)Bytes.java Dec 13, 2009
 * 
 * Copyright 2009 Net365. All rights reserved.
 */
package net.dbaeye.util;

/**
 * <p>
 * <a href="Bytes.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author Zhang Songfu
 * @version $Id: Bytes.java 29 2012-04-06 10:18:35Z zhangsongfu $
 */
public class Bytes {
	//~ Static fields/initializers =============================================

	//~ Instance fields ========================================================

	//~ Constructors ===========================================================

	private Bytes() {}
	
	//~ Methods ================================================================

	/**
	 * Convert 4 bytes to int: bigEndian
	 */
	public static int toInt(byte[] bytes) {
		return  ((bytes[0] & 0xff) << 24) |
		 		((bytes[1] & 0xff) << 16) |
		 		((bytes[2] & 0xff) << 8)  |
		 		((bytes[3] & 0xff) << 0);
	}
	
	/**
	 * Encode int value to bigEndian seq bytes
	 * 
	 * @param i
	 * @return bytes
	 */
	public static byte[] toBytes(int i) {
		byte[] bytes = new byte[4];
		
		bytes[0] = (byte) (i >> 24);
		bytes[1] = (byte) (i >> 16);
		bytes[2] = (byte) (i >> 8);
		bytes[3] = (byte) (i >> 0);
		
		return bytes;
	}
	
	//~ Accessors ==============================================================

}
