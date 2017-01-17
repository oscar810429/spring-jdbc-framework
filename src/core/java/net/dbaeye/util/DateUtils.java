package net.dbaeye.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtils.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 *   to correct time pattern. Minutes should be mm not MM
 * 	(MM is month). 
 * @version $Revision: 1.7 $ $Date: 2005/05/04 04:57:41 $
 */
public class DateUtils {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(DateUtils.class);
    
    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";
    public static final String BUNDLE_KEY = "ApplicationResources";

    // http://www.w3.org/Protocols/rfc822/Overview.html#z28
    // Using Locale.US to fix ROL-725 and ROL-628
    private static final SimpleDateFormat mFormatRfc822 = 
        new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US); 
    
    //~ Methods ================================================================

    /**
     * Return default datePattern (yyyy-MM-dd)
     * @return a string representing the date pattern on the UI
     */
    public static synchronized String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(BUNDLE_KEY, locale)
                .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "yyyy-MM-dd";
        }
        
        return defaultDatePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to yyyy-MM-dd.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                      + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * yyyy-MM-dd HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: yyyy-MM-dd
     * 
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format yyyy-MM-dd)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
      throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                      + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }

        return aDate;
    }
    
    /**
     * 将int类型的时间（精确到秒的）转化为date类型 
     * @author chenshihui (Apr 13, 2012)
     * @param intDate
     * @return date
     */
    public static Date intToDate(Integer intDate){
    	Date date =null;
    	if(intDate>0){
    		long l = Long.valueOf(intDate+"");
    		l = l*1000;
    		date = new Date(l);
    	}
    	return date;
    }
    
    /**
     * 将date类型转化为 int类型的时间（精确到秒的）
     * @author chenshihui (Apr 13, 2012)
     * @param date
     * @return
     */
    public static int dateToint(Date date){
    	int intDate = 0;
    	if(date!=null){
    		Long longDate = date.getTime()/1000;
    		intDate = Integer.valueOf(longDate.toString()) ;
    	}
    	return intDate;
    }
    
    /**
	 * 返回两个时间相差的秒钟数
	 * @param a
	 * @param b
	 * @return
	 */
	public static final int subSecond(Date a, Date b) {
		long result = (a.getTime() / (1000) - b.getTime() / (1000));
		return Integer.parseInt(result+"");
	}

    /**
     * Get day names of a week.
     * 
     * @param locale
     * @return
     */
    public static String[] getWeekDayNames(Locale locale) {
    	String[] weekDayNames = new String[7];
		Calendar cal = Calendar.getInstance(locale);
		SimpleDateFormat formatter = new SimpleDateFormat("E", locale);
		cal.set(Calendar.DAY_OF_WEEK, 0);
		for (int i = 0; i < 7; i++) {
			cal.add(Calendar.DATE, 1);
			weekDayNames[i] = formatter.format(cal.getTime());
		}
		return weekDayNames;
    }
    
    public static String formatRfc822(Date date) {
        return mFormatRfc822.format(date);
    }    
    
	/**
	 * 返回指定日期多少天后的日期
	 * @param date 起始时间
	 * @param days 增加天数 可以为负数表示过去的某天
	 * @return
	 * @author shuchao Dec 23, 2008 9:50:45 AM
	 * @modifier shuchao Dec 23, 2008 9:50:45 AM
	 */
	@SuppressWarnings("static-access")
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * 一天的开始时间
	 * @param date
	 * @return
	 */
	public static final Date dateBegin(Date date) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateBegin(calendar);
		return calendar.getTime();
	}
	
	/**
	 * 一天的开始时间
	 * @param calendar
	 * @return
	 */
	public static final Calendar dateBegin(Calendar calendar) {
		if (calendar == null)
			return null;
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	

	/**
	 * 一天的结束时间
	 * @param calendar
	 * @return
	 */
	public static final Calendar dateEnd(Calendar calendar) {
		if (calendar == null)
			return null;
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	

	/**
	 * 一天的结束时间
	 * @param date
	 * @return
	 */
	public static final Date dateEnd(Date date) {
		if (date == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateEnd(calendar);
		return calendar.getTime();
	}
	

	/**
	 * 在一个已知时间的基础上增加指定的时间
	 * @param oldDate
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static final Date addDate(Date oldDate, int year, int month, int date,int hour,int minute,int sec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE,minute);
		calendar.add(Calendar.SECOND,sec);
		return calendar.getTime();
	}
	
	
}
