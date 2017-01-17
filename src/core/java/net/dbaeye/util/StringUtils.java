package net.dbaeye.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * String Utility Class This is used to encode passwords programmatically
 *
 * <p>
 * <a h
 * ref="StringUtils.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StringUtils {
    //~ Static fields/initializers =============================================

    private final static Log log = LogFactory.getLog(StringUtils.class);
    
    private static final String DOMAIN_FILTER_LIST = "^(www[0-9]*|photo[0-9]*|mail|blog|console|admin|manage|send|beta|sender|hdyx|miss|search|group|forum|help|web2|print|star|travel|music|baby|show|rank|worldcup|list|2005|2006|2007|2008|2009|2010|client|city|lomo|blogger|wiki|free|school|news|life|tech|podcast|football|game|tour|auto|sport|mobile|post|chat|digi|test|demo|login|proxy)$";

    
    private final static Pattern p = Pattern.compile(DOMAIN_FILTER_LIST);

    //~ Methods ================================================================

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     *
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     */
    public static String encodeString(String str)  {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encodeBuffer(str.getBytes()).trim();
    }

    /**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     */
    public static String decodeString(String str) {
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        try {
            return new String(dec.decodeBuffer(str));
        } catch (IOException io) {
        	throw new RuntimeException(io.getMessage(), io.getCause());
        }
    }
    
    public static boolean isDomainFilter(String str) {
    	
	    Matcher m = p.matcher(str);
	    
	    return m.matches();
    }
    public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		} 
		return toStringArray(tokens);
	}
	public static String[] toStringArray(Collection collection) {
		if (collection == null) { 
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}
    public static void main(String[] args) {
    	
		 System.out.println(encodePassword("zhangsf", "SHA").length());
		 System.out.println(encodePassword("zhangsf", "MD5").length());
    	     System.out.println(encodeString("zhangsf").length());
	}
}
