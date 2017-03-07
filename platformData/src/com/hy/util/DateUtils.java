package com.hy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式操作类
 * @author yong
 */
public class DateUtils
{

	/**
     * 得到当前时间
     * 格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
	public static String getNowTime()
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = dateFormat.format(now);
		return nowTime;
	}
	/**
     * 得到当前时间
     * @param dateformat 设置格式化日期
     * @return
     */
	public static String getNowTime(String dateformat)
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		String nowTime = dateFormat.format(now);
		return nowTime;
	}
	/**
     * 得到当前时间
     * @param dateformat 设置格式化日期
     * @return util.Date
     */
	public static java.util.Date getUtilDateNowTime(String dateformat)
	{
		return getUtilDateByString(getNowTime(dateformat), dateformat);
	}
	/**
     * 得到当前时间
     * @param dateformat 设置格式化日期
     * @return sql.date
     */
	public static java.sql.Date getSqlDateNowTime(String dateformat)
	{
		return getSqlDateByString(getNowTime(dateformat), dateformat);
	}
    
    /**
	 * 得到对应周几
	 * @param date
	 * @return
	 */
	public static String getWeekNameCN(Date date)
	{
		String ret = "星期";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int w = cal.get(Calendar.DAY_OF_WEEK);
			String ws = "日一二三四五六";
			ret += ws.charAt(w - 1);
		}
		catch (Exception e) {
			ret = "";
		}
		return ret;
	}
	/**
	 * 根据日期得到星期
	 * @param date
	 * @return
	 */
	public static int getWeekByDateNum(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK);
		return w-1;
	}
	/**
	 * 取得与当前时间是第几周
	 * @param date
	 * @return int
	 */
	protected static int getWeekByDate(Date date)
	{   
        Calendar cal = Calendar.getInstance();   
        cal.setTime(date);    
        int weekno=cal.get(Calendar.WEEK_OF_YEAR); 
        return weekno;   
    }
	
	/**
	 * 格式化得到的日期
	 * @param date 传入要格式化的日期
	 * @param dateformat 
	 * @return
	 */
    public static String getDateTime(Date date, String dateformat)
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
    	String nowTime = "";
    	if(date!=null){
    		 nowTime = dateFormat.format(date);
    	}
		return nowTime;
    }
	/**
	 * 从String转化为java.sql.Date
	 * @param strDate 
	 * @return
	 */
	public static java.sql.Date getSqlDateByString(String strDate, String format)
	{
		if(strDate == null)
			return null;
		java.sql.Date sqlDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
		try {
			sqlDate = new java.sql.Date(sdf.parse(strDate).getTime());
        }
        catch (ParseException e) {
	        e.printStackTrace();
        }
		return sqlDate;
	}
	/**
	 * 从String转化为java.util.Date
	 * @param strDate
	 * @return
	 */
	public static java.util.Date getUtilDateByString(String strDate, String format)
	{
		if(strDate == null)
			return null;
		else if(strDate.equals("null"))
			return null;
		else if(strDate.equals(""))
			return null;
		java.util.Date sqlDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
		try {
			sqlDate = sdf.parse(strDate);
        }
        catch (ParseException e) {
	        e.printStackTrace();
        }
		return sqlDate;
	}
	
	/**
	 * 计算两个日期之间相差天数、去除周六周日.
	 * @param date1	时间字符串格式为2000-01-01
	 * @param date2	时间字符串格式为2000-01-01
	 * @return
	 */
	public static int getWorkingDay(String date1, String date2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();
		try
		{
			d1.setTime(sdf.parse(date1));
			d2.setTime(sdf.parse(date2));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		int result = -1;	// 记录相差天数、去除周六周日.
		//int betweendays = getDaysBetween(d1, d2);	// 记录相差天数
		int charge_start_date = 0;	// 开始日期的日期偏移量   
		int charge_end_date = 0;	// 结束日期的日期偏移量   
		// 日期不在同一个日期内
		int stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		int etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6)// 开始日期为星期六和星期日时偏移量为0   
			charge_start_date = stmp - 1;
		if (etmp != 0 && etmp != 6)// 结束日期为星期六和星期日时偏移量为0   
			charge_end_date = etmp - 1;

		result = (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7)
				* 5 + charge_start_date - charge_end_date;

		//System.out.println("between day is-->" + betweendays);
		return result;

	}
	private static Calendar getNextMonday(Calendar date)
	{
		Calendar result = null;
		result = date;
		do
		{
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		}
		while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}
	private static int getDaysBetween(Calendar d1, Calendar d2)
	{
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2)
		{
			d1 = (java.util.Calendar) d1.clone();
			do
			{
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			}
			while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;
	}
	
	/**
	 * 给定日期和月数计算前后日期，返回dateFormat格式化后的日期
	 * @param date 给定的日期
	 * @param month  给定的月数
	 * @param dateFormat
	 * @return String类型日期
	 */
	public static String dayMonth(String date, int month, String dateFormat)
	{
		Calendar cal = Calendar.getInstance();	// 使用默认时区和语言环境获得一个日历。
		cal.setTime(DateUtils.getUtilDateByString(date, dateFormat));
		cal.add(Calendar.MONTH, month);	// 取当前日期的前后日期
		//通过格式化输出日期
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);    
		return format.format(cal.getTime());
	}
	
	/**
	 * 给定日期和天数计算前后日期，返回dateFormat格式化后的日期
	 * @param date 给定的日期
	 * @param day  给定的天数
	 * @param dateFormat
	 * @return String类型日期
	 */
	public static String dayAdd(String date, int day, String dateFormat)
	{
		Calendar cal = Calendar.getInstance();	// 使用默认时区和语言环境获得一个日历。
		cal.setTime(DateUtils.getUtilDateByString(date, dateFormat));
		cal.add(Calendar.DAY_OF_MONTH, day);	// 取当前日期的前后日期
		//通过格式化输出日期
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);    
		return format.format(cal.getTime());
	}
	
	/**
	 * 给定日期和小时计算时间时相加，返回dateFormat格式化后的日期
	 * @param date 给定的日期
	 * @param hour  给定的时间
	 * @param dateFormat
	 * @return String类型日期
	 */
	public static String hourAdd(String date, int hour, String dateFormat)
	{
		Calendar cal = Calendar.getInstance();	// 使用默认时区和语言环境获得一个日历。
		cal.setTime(DateUtils.getUtilDateByString(date, dateFormat));
		cal.add(Calendar.HOUR_OF_DAY, hour);	// 时相加减
		//通过格式化输出日期    
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);    
		return format.format(cal.getTime());
	}
	
	/**
	 * 给定日期和分钟计算时间时相加，返回dateFormat格式化后的日期
	 * @param date 给定的日期
	 * @param minute  分钟
	 * @param dateFormat
	 * @return String类型日期
	 */
	public static String minuteAdd(String date, int minute, String dateFormat)
	{
		Calendar cal = Calendar.getInstance();	// 使用默认时区和语言环境获得一个日历。
		cal.setTime(DateUtils.getUtilDateByString(date, dateFormat));
		cal.add(Calendar.MINUTE, minute);		// 分钟相加减
		//通过格式化输出日期    
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);    
		return format.format(cal.getTime());
	}
	
	/**
	 * 计算两个时间相差
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @param str 返回标识，d返回相差天、h返回相差小时、m返回相差分钟、s返回相差秒
	 * @return
	 */
	public static Long dateDiff(String startTime, String endTime,  String format, String str)
	{ 
        // 按照传入的格式生成一个simpledateformate对象  
        SimpleDateFormat sd = new SimpleDateFormat(format);  
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数  
        long nh = 1000 * 60 * 60;// 一小时的毫秒数  
        long nm = 1000 * 60;// 一分钟的毫秒数  
        long ns = 1000;// 一秒钟的毫秒数  
        long diff;  
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        // 获得两个时间的毫秒时间差异  
        try
        {  
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();  
            day = diff / nd;// 计算差多少天  
            hour = diff % nd / nh + day * 24;// 计算差多少小时  
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟  
            sec = diff % nd % nh % nm / ns;// 计算差多少秒  
            // 输出结果  
            System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时" 
                    + (min - day * 24 * 60) + "分钟" + sec + "秒。");  
            System.out.println("hour=" + hour + ",min=" + min);  
        }
        catch (ParseException e)
        {
            e.printStackTrace();  
        }
        if (str.equalsIgnoreCase("d"))
            return day;
        else if (str.equalsIgnoreCase("h"))
            return hour;
        else if (str.equalsIgnoreCase("m"))
            return min;
        else if (str.equalsIgnoreCase("s"))
        	return sec;
        else
        	return 0L;
	}
	
	/**
	 * 计算两个时间相差，返回时间戳 long
	 * @param startTime 开始时间
	 * @param endTime	结束时间
	 * @param format	时间的格式
	 * @return
	 */
	public static Long dateDiff(String startTime, String endTime,  String format)
	{
		Date start_date = getUtilDateByString(startTime, format);
		Date end_date = getUtilDateByString(endTime, format);
		long start = start_date.getTime();
		long end = end_date.getTime();
		return (end - start);
	} 
	
	/**
	 * 给定时间戳，返回给定格式的字符串时间
	 * @param timestamp
	 * @param format
	 * @return 字符串时间
	 */
	public static String getUtilDate(Long timestamp, String format)
	{
		SimpleDateFormat fm = new SimpleDateFormat(format);
		String date = fm.format(timestamp);
		return date;
	}
	
	public static void main(String[] args) 
	{
		String s = "2016-10-11 12:11:11";
		long d = DateUtils.getUtilDateByString(s, "yyyyMMddHHmmss").getTime();
		String dateStartFormt = DateUtils.getUtilDate(d, "yyyyMMddHHmmss");
		System.out.println(d);
		System.out.println(dateStartFormt);
	}
	
	/**
	 * 
	 * @param birthDate
	 * @return
	 */
	public static int getAge(Date birthDate) {

		if (birthDate == null)
		throw new
		RuntimeException("出生日期不能为null");

		int age = 0;

		Date now = new Date();

		SimpleDateFormat format_y = new
		SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new
		SimpleDateFormat("MM");

		String birth_year =
		format_y.format(birthDate);
		String this_year =
		format_y.format(now);

		String birth_month =
		format_M.format(birthDate);
		String this_month =
		format_M.format(now);

		// 初步，估算
		age =
		Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if
		(this_month.compareTo(birth_month) < 0)
		age -=
		1;
		if (age <
		0)
		age =
		0;
		return age;
		}
}

