package cn.com.infohold.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	/**
	 * 默认时间格式
	 */
	public static final String DEFAULT_TIME_FORMAT_DB_EN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认日期格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
	/**
	 * 默认时间格式
	 */
	public static final String DEFAULT_TIME_FORMAT = "HHmmss";

	/**
	 * 数据库存储的默认时间格式
	 */
	public static final String DEFAULT_TIME_FORMAT_DB = "yyyyMMddHHmmss";
	/**
	 * 默认时间格式(EN)
	 */
	public static final String DEFAULT_TIME_FORMAT_EN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认时间格式(中文)
	 */
	public static final String DEFAULT_TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";
	/**
	 * 默认日期格式(EN)
	 */
	public static final String DEFAULT_DATE_FORMAT_EN = "yyyy-MM-dd";
	/**
	 * 默认日期格式(中文)
	 */
	public static final String DEFAULT_DATE_FORMAT_CN = "yyyy年MM月dd日";
	/**
	 * 年月日时分秒时间格式
	 */
	public static String DEFAULT_DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	/**
	 * 段内位置表示
	 */
	protected static final char[] CHINESE_MONEY_CONVERT_GROUP_IN = { '拾', '佰', '仟' };
	/**
	 * 段名表示
	 */
	protected static final char[] CHINESE_MONEY_CONVERT_GROUP_OUT = { '万', '亿', '兆' };
	/**
	 * 数字表示
	 */
	protected static final char[] CHINESE_MONEY_CONVERT_DIGIT = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
	/**
	 * 单位表示
	 */
	protected static final String[] CHINESE_MONEY_UNIT = { "整", "分", "角", "圆" };

	/**
	 * 禁用构造函数
	 */
	private DateUtil() {
	}

	/**
	 * 根据参数格式显示格式化日期
	 * 
	 * @param date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String formatDate(String date, String format) {
		Date dt = null;
		SimpleDateFormat inFmt = null, outFmt = null;
		ParsePosition pos = new ParsePosition(0);
		if ((date == null) || ("".equals(date.trim())))
			return "";
		try {
			if (Long.parseLong(date) == 0)
				return "";
		} catch (Exception nume) {
			return date;
		}
		try {
			switch (date.trim().length()) {
			case 14:
				inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
				break;
			case 12:
				inFmt = new SimpleDateFormat("yyyyMMddHHmm");
				break;
			case 10:
				inFmt = new SimpleDateFormat("yyyyMMddHH");
				break;
			case 8:
				inFmt = new SimpleDateFormat("yyyyMMdd");
				break;
			case 6:
				inFmt = new SimpleDateFormat("yyyyMM");
				break;
			default:
				return date;
			}
			if ((dt = inFmt.parse(date, pos)) == null)
				return date;
			if ((format == null) || ("".equals(format.trim()))) {
				outFmt = new SimpleDateFormat("yyyy年MM月dd日");
			} else {
				outFmt = new SimpleDateFormat(format);
			}
			return outFmt.format(dt);
		} catch (Exception ex) {
			return date;
		}
	}

	/**
	 * 重新格式化时间
	 * 
	 * @param datetime
	 *            原有时间字符串，如20050101
	 * @param oldFormat
	 *            原有时间字符串的格式，如20050101160145为"yyyyMMddHHmmss"
	 * @param newFormat
	 *            新的时间字符串的格式，如2005-01-01 16:01:45为"yyyy-MM-dd HH:mm:ss"
	 * @return String
	 */
	public static String convertTimeFormat(String datetime, String oldFormat, String newFormat) {
		SimpleDateFormat oldFmt = null, newFmt = null;
		oldFmt = new SimpleDateFormat(oldFormat);
		newFmt = new SimpleDateFormat(newFormat);
		Date date = null;
		try {
			date = oldFmt.parse(datetime);
			return newFmt.format(date);
		} catch (ParseException ex) {

		}
		return datetime;
	}

	/**
	 * 取得指定时间间隔后的系统时间<br>
	 * 示例：<br>
	 * getDifferentTime( 1, 2, 3,"yyyyMMddHHmmss") <br>
	 * 使用yyyyMMddHHmmss格式输出1小时2分3秒后的系统时间<br>
	 * getDifferentTime( -24, 0, 0,"yyyyMMdd") <br>
	 * 使用yyyyMMdd格式输出1天前的系统日期<br>
	 * 
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @param timeFormat
	 *            格式化
	 * @return String
	 */
	public static String getDifferentTime(int hour, int minute, int second, String timeFormat) {
		String format = (timeFormat == null) ? DateUtil.DEFAULT_TIME_FORMAT_DB : timeFormat;
		GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 如果是空值，则以参数指定字符串取代
	 * 
	 * @param dataValue
	 * @param returnValue
	 * @return String
	 */
	public static String nvl(Object dataValue, String returnValue) {
		if (null == dataValue || dataValue.toString().length() == 0)
			return returnValue;
		else
			return dataValue.toString();
	}

	/**
	 * 取得格式化的服务器时间
	 * 
	 * @param timeFormat
	 *            时间：yyyyMMddHHmmss；日期：yyyyMMdd
	 * @return String
	 */
	public static String getServerTime(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 取得本月第一天
	 * 
	 * @return String
	 */
	public static String getThisMonthFirstDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 取得上个月第一天
	 * 
	 * @return String
	 */
	public static String getFirstDayOfLastMonth(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}
	
	/**
	 * 取得上个月最后一天
	 * 
	 * @return String
	 */
	public static String getLastDayOfLastMonth(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;

		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date strDateTo = calendar.getTime();
		String lastday = new SimpleDateFormat(format).format(strDateTo);

		return lastday;
	}

	/**
	 * 取得本月最后一天
	 * 
	 * @return String
	 */
	public static String getThisMonthLastDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		String lastday = new SimpleDateFormat(format).format(l.getTime());
		return lastday;
	}

	/**
	 * 获得本季度 第一天
	 * 
	 * @return String
	 */

	public static String getThisSeasonFirstDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		int month = Integer.parseInt(getServerTime("MM"));
		return getSeasonFirstDay(month, format);
	}

	/**
	 * 获得本季度 最后一天
	 * 
	 * @return String
	 */

	public static String getThisSeasonLastDay(String timeFormat) {
		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		int month = Integer.parseInt(getServerTime("MM"));
		return getSeasonLastDay(month, format);
	}

	/**
	 * 获得季度 第一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */

	public static String getSeasonFirstDay(int month, String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;

		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];

		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, start_month - 1);
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得季度 最后一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */

	public static String getSeasonLastDay(int month, String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;

		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int end_month = array[season - 1][2];

		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();

		int end_days = getLastDayOfMonth(cal.get(Calendar.YEAR), end_month);

		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, end_month - 1);
		f.set(Calendar.DATE, end_days);

		f.add(Calendar.DATE, 1);
		f.add(Calendar.SECOND, -1);

		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得本年的第一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */

	public static String getThisYearFirstDay(String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		String firstday = new SimpleDateFormat(format).format(f.getTime());
		return firstday;
	}

	/**
	 * 获得本年最后一天
	 * 
	 * @param month
	 *            MM
	 * @return String
	 */

	public static String getThisYearLastDay(String timeFormat) {

		String format = (timeFormat == null) ? DEFAULT_TIME_FORMAT_DB : timeFormat;
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		f.add(Calendar.SECOND, -1);
		String lastday = new SimpleDateFormat(format).format(f.getTime());
		return lastday;
	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * 取得beginDayTime
	 * 
	 * @return String
	 */
	public static String getDayBegin(String inputStringDate) {
		String outStringDate = inputStringDate.replaceAll("-", "");

		if (outStringDate.length() == 8) {

			outStringDate = outStringDate + "000000";
			return outStringDate;
		} else {
			return inputStringDate;
		}
	}

	/**
	 * 取得endDayTime
	 * 
	 * @return String
	 */
	public static String getDayEnd(String inputStringDate) {
		String outStringDate = inputStringDate.replaceAll("-", "");
		if (outStringDate.length() == 8) {
			outStringDate = outStringDate + "235959";
			return outStringDate;
		} else {
			return inputStringDate;
		}
	}

	/**
	 * 计算某个日期n天后的日期
	 * 
	 * @param beginDateStr
	 *            开始时间
	 * @param count
	 *            天数
	 * @return
	 */
	public static String addDate(String beginDateStr, int count) {

		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		endDate = new Date(beginDate.getTime() + (24 * 60 * 60 * 1000 * count));

		return sdfDay.format(endDate);
	}

	/**
     * 拿指定日期跟其它日期比较，大于就返回true，小于就返回false
     * @param compare
     * @param compared
     * @return
     */
    public static boolean compareDate(String compare, String compared){
    	
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date compareDate = null;
        java.util.Date comparedDate = null;
        boolean flag = false;
        
        try {
        	compareDate = format.parse(compare);
        	comparedDate = format.parse(compared);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        if(compareDate.getTime() > comparedDate.getTime()){
        	flag = true;
        }else {
        	flag = false;
		}
        
    	return flag;
    }
    
    /**
     * 日期转String
     * @param date
     * @return
     */
    public static String getDateString(Date date, String format){
    	
    	java.text.SimpleDateFormat fm = new java.text.SimpleDateFormat(format);
    	
    	return fm.format(date);
    }
    
    /**
     * 获取当前指定格式的系统时间
     * @param format
     * @return
     */
    public static String getSystemDate(String format){
    	Date now = new Date(); 
    	SimpleDateFormat dateFormat = new SimpleDateFormat(format);//可以方便地修改日期格式
    	String date = dateFormat.format( now ); 
    	
    	return date;
    }
    
    /**
     * 获取当前的系统时间， 格式默认yyyy-MM-dd HH:mm:ss
     * @param format
     * @return
     */
    public static String getSystemDate(){
    	
    	Date now = new Date(); 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String date = dateFormat.format( now ); 
    	
    	return date;
    }
    
}
