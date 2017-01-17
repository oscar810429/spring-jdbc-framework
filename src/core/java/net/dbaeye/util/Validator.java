package net.dbaeye.util;



import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="Validator.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @author  Alysa Carver
 * @version $Revision: 1.1.1.1 $
 *
 */
public class Validator {

    public static boolean isAddress(String address) {
        if (isNull(address)) {
            return false;
        }

        char[] c = address.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if ((!isChar(c[i])) &&
                (!isDigit(c[i])) &&
                (!Character.isWhitespace(c[i]))) {

                return false;
            }
        }

        return true;
    }

    public static boolean isChar(char c) {
        return Character.isLetter(c);
    }

    public static boolean isChar(String s) {
        if (isNull(s)) {
            return false;
        }

        char[] c = s.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (!isChar(c[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDigit(char c) {
        int x = (int) c;

        if ((x >= 48) && (x <= 57)) {
            return true;
        }

        return false;
    }

    public static boolean isDigit(String s) {
        if (isNull(s)) {
            return false;
        }

        char[] c = s.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (!isDigit(c[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isHex(String s) {
        if (isNull(s)) {
            return false;
        }

        return true;
    }

    public static boolean isHTML(String s) {
        if (isNull(s)) {
            return false;
        }

        if (((s.indexOf("<html>") != -1) || (s.indexOf("<HTML>") != -1)) &&
            ((s.indexOf("</html>") != -1) || (s.indexOf("</HTML>") != -1))) {

            return true;
        }

        return false;
    }

    public static boolean isName(String name) {
        if (isNull(name)) {
            return false;
        }

        char[] c = name.trim().toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (((!isChar(c[i])) &&
                 (!Character.isWhitespace(c[i]))) ||
                (c[i] == ',')) {

                return false;
            }
        }

        return true;
    }

    public static boolean isNumber(String number) {
        if (isNull(number)) {
            return false;
        }

        char[] c = number.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (!isDigit(c[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str String
     * @return boolean
     */
   public static boolean isNumeric(String str) {
       int begin = 0;
       boolean once = true;
       if (str == null || str.trim().equals("")) {
           return false;
       }
       str = str.trim();
       if (str.startsWith("+") || str.startsWith("-")) {
           if (str.length() == 1) {
               // "+" "-"
               return false;
           }
           begin = 1;
       }
       for (int i = begin; i < str.length(); i++) {
           if (!Character.isDigit(str.charAt(i))) {
               if (str.charAt(i) == '.' && once) {
                   // '.' can only once
                   once = false;
               }
               else {
                   return false;
               }
           }
       }
       if (str.length() == (begin + 1) && !once) {
           // "." "+." "-."
           return false;
       }
       return true;
   }


   public static boolean isNull(Object obj) {
       if (obj == null) {
           return true;
       }

       obj = obj.toString().trim();

       if ((obj.equals("null")) || (obj.equals(""))) {
           return true;
       }

       return false;
   }

    public static boolean isNotNull(Object obj) {
        return!isNull(obj);
    }

    /**
     * 是否Long型ID：[<code>id!=null && id != 0</code>]
     * @author HuangFeng(Feb 1, 2010)
     * @param id Long型ID
     */
    public static boolean isLongId(Long id)
    {
       if(isNull(id))
       {
    	   return false;
       }
       else if( 0l >= id)
       {
    	   return false;
       } else {
    	   return true;
       }
    }

    /**
     * 是否Long型ID：[<code>id!=null && id != 0</code>]
     * @author HuangFeng(Feb 1, 2010)
     * @param id Long型ID
     */
    public static boolean isLongId(String id)
    {
       if(isNull(id))
       {
    	   return false;
       }
       else if( 0l >= Long.valueOf(id))
       {
    	   return false;
       } else {
    	   return true;
       }
    }

    public static boolean isNotLongId(Long id)
    {
       return !isLongId(id);
    }

    public static boolean isIntegerId(Integer id)
    {
    	return isLongId(Long.valueOf(id));
    }

    public static boolean isNotIntegerId(Integer id)
    {
    	return isNotLongId(Long.valueOf(id));
    }

    public static boolean isPassword(String password) {
        if (isNull(password)) {
            return false;
        }

        if (password.length() < 4) {
            return false;
        }

        char[] c = password.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if ((!isChar(c[i])) &&
                (!isDigit(c[i]))) {

                return false;
            }
        }

        return true;
    }
    /**
     * 校验两个数是否相等.<br>
     * <b>成真条件：</b>
     * <li>1.第一个参数不为空</li>
     * <li>2.第二个参数不为空</li>
     * <li>3.两个参数用equals判断为true</li>
     * @param str1 需要判断的第一个字符串
     * @param str2 需要判断的第二个字符串
     * @return boolean 两个数是否相等
     * @author jiangjinrong 2009-01-09
     */
    public static boolean isEquals(String str1,String str2){
    return (Validator.isNotNull(str1)&&Validator.isNotNull(str2)&&str1.equals(str2));
    }

    /**
     * 校验两个数是否不相等.<br>
     * <b>成真条件：</b>
     * <li>1.第一个参数为空</li>
     * <li>2.或者第二个参数为空</li>
     * <li>3.或者两个参数用equals判断为false</li>
     * @param str1 需要判断的第一个字符串
     * @param str2 需要判断的第二个字符串
     * @return boolean 两个数是否不相等
     * @author jiangjinrong 2009-01-09
     */
    public static boolean isNotEquals(String str1,String str2){
    return (Validator.isNull(str1)||Validator.isNull(str2)||!str1.equals(str2));
    }

    /**
     * 判断Map集合是否为Null或isEmpty
     * @author HuangFeng(2009-10-21)
     * @param map Map
     * @return <code>true</code>->isEmpty, <code>false</code>->isNotEmpty
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
    	if(null == map || map.isEmpty())
    	{
    		return true;
    	}
    	return false;
    }

    /**
     * 判断Map集合不为Null或isEmpty
     * @author HuangFeng(2009-10-21)
     * @param map Map
     * @return <code>true</code>->isNotEmpty, <code>false</code>->isEmpty
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
    	return !isEmpty(map);
    }

    /**
     * 判断Collection集合类包括子类List,Set是否为Null或isEmpty
     * @author HuangFeng(2009-10-21)
     * @param coll Collection及子类：<code>List</code>, <code>Set</code>
     * @return <code>true</code>->isEmpty, <code>false</code>->isNotEmpty
     */
    public static boolean isEmpty(Collection<?> coll)
    {
    	if(null == coll || coll.isEmpty() || coll.size() == 0)
    	{
    		return true;
    	}
    	return false;
    }

    /**
     * 判断Collection集合类包括子类List,Set为为Null或isEmpty
     * @author HuangFeng(2009-10-21)
     * @param coll Collection及子类：<code>List</code>, <code>Set</code>
     * @return <code>true</code>->isNotEmpty, <code>false</code>->isEmpty
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
    	return !isEmpty(coll);
    }

    /** URL地址正则表达式 */
    public static final String RPX = "([\\w-]+\\.)+[\\w-]+.([^a-z])(/[\\w- ./?%&=]*)?|[a-zA-Z0-9\\-\\.][\\w-]+.([^a-z])(/[\\w- ./?%&=]*)?";

    /** 域名后缀正则表达式 */
    public static final String DOMAIN_PIX = "(\\.[a-z]{2,5})";

    /** Email REG */
    public static final String EMAIL_REG = "^([-_a-z0-9.])+@([-_a-z0-9]+\\.)+[a-z0-9]{2,3}$";

    /**
     * 文章标题内，不能带有URL，发布时，做判断；有url的话，不允许提交，要求服务器端验证，对抗发帖机器；
     * @author HuangFeng(Feb 5, 2010)
     * @param title 文章标题
     * @return 是否带有URL
     */
	public static boolean isTitle(String title)
	{
		if (isNull(title))
		{
			return false;
		}
		final Pattern TITLE_PATTERN = Pattern.compile(DOMAIN_PIX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = TITLE_PATTERN.matcher(title);
		if(matcher.find())
		{
			return false;
		} else {
			return true;
		}
	}

	/**
	 * EMAIL Validate
	 * @author HuangFeng(Mar 10, 2010)
	 * @param email email
	 * @return Boolean
	 */
	public static final boolean isEmail(final String email)
	{
		return email.matches(EMAIL_REG);
	}

	/**
	 * 判断是否空数组
	 */
	public static final boolean isNullArray(Object[] obj) {
		if (obj == null || obj.length == 0)
			return true;
		return false;
	}

	/**
	 * 判断非否空数组
	 */
	public static final boolean isNotNullArray(Object[] obj) {
		return !isNullArray(obj);
	}

	/**
	 * 判断不为空并不为0.0的Double数据
	 * @author HuangFeng(2011-3-15)
	 * @param doub Double
	 */
	public static final boolean isDouble(Double doub)
	{
		if(isNull(doub))
		{
			return false;
		}
		
		if(0.0 == doub)
		{
			return false;
		}
		
		return true;
	}
	
	public static final boolean isNotDouble(Double doub)
	{
		return !isDouble(doub);
	}
	
	public static boolean equals(Long long0, Long long1)
	{
		if(Validator.isNull(long0) || Validator.isNull(long1))
		{
			return false;
		}
		
		return (long0.longValue() == long1.longValue());
	}
	
	public static boolean equals(Integer integer0, Integer integer1)
	{
		if(Validator.isNull(integer0) || Validator.isNull(integer1))
		{
			return false;
		}
		
		return (integer0.intValue() == integer1.intValue());
	}
	
	public static boolean equals(Double double0, Double double1)
	{
		if(Validator.isNull(double0) || Validator.isNull(double1))
		{
			return false;
		}
		
		return (double0.doubleValue() == double1.doubleValue());
	}
	
	public static boolean equals(String string0, String string1)
	{
		if(Validator.isNull(string0) || Validator.isNull(string1))
		{
			return false;
		}
		
		return (string0.toString().equals(string1.toString()));
	}
	
	public static void main(String[] args) {

		//String rpx = "[.com]?";
		//final Pattern TITLE_PATTERN = Pattern.compile("([\\w-]+\\.)");
		//Matcher matcher = TITLE_PATTERN.matcher("dsdhttp://");
		//System.out.println(Validator.isTitle("妈妈的.1212sdsdsds"));
		/*
		 *					 while (stBody.hasMoreTokens()) {
								String codeline = stBody.nextToken();
								if (codeline.indexOf("href") != -1) {
									Pattern pattern = Pattern.compile("([^<a]href=[\"\'])(.+?)([\"\'][^a>])");
									Matcher matcher = pattern.matcher(codeline);
									while (matcher.find()) {
									     String matchString  = " href=\""+B2CShop.getAppConfig().getMediaRoot()+"/trace.html?id="+emailSendLog.getId()+"&url="+matcher.group(2)+"\" ";
									     codeline = StringUtils.replace(codeline, matcher.group(), matchString);
									}

								}
		 */

		System.out.println("1:" + isEmail("oursky@qq.com"));
		System.out.println("2:" + isEmail("java@1.qq.com"));
		System.out.println("3:" + isEmail("java@1_qq.com"));
		System.out.println("4:" + isEmail("java@1-qq.com"));
		System.out.println("5:" + isEmail("java-12@1-qq.com"));
		System.out.println("6:" + isEmail("java@12@1-qq.com"));
		System.out.println("7:" + isEmail("java@12@1-qq-cc.com"));
		System.out.println("8:" + isEmail("www.353@365.com"));
		System.out.println("9:" + isEmail("www.3-53@365.com"));
	}
}
