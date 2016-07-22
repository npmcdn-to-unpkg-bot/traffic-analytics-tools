/**
 * Project:AutoBidding Package:com.adeaz.ab.helper File:DateUtils.java Date:2014年12月15日 下午5:57:19
 */
package com.traffic.analytics.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.traffic.analytics.commons.constants.SystemMessageCode;
import com.traffic.analytics.commons.exception.ServiceException;

/**
 * 日期操作的工具类
 *
 * @author ZhangYongjia
 */
public final class DateUtils {

	private static final Log log = LogFactory.getLog(DateUtils.class);

	/**
	 * 太平洋时区
	 */
	public static final TimeZone PACIFIC = TimeZone.getTimeZone("America/Los_Angeles");

	/**
	 * 北京时区
	 */
	public static final TimeZone BEIJING = TimeZone.getTimeZone("Asia/Hong_Kong");

	/**
	 * 德国时区
	 */
	public static final TimeZone BERLIN = TimeZone.getTimeZone("Europe/Berlin");

	/**
	 * 新加坡时区
	 */
	public static final TimeZone SINGAPORE = TimeZone.getTimeZone("Asia/Singapore");

	/**
	 * UTC时区
	 */
	public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	/**
	 * 每天的毫秒数
	 */
	public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

	/**
	 * 私有的构造函数
	 */
	private DateUtils() {
	}

	/**
	 * 获取当前日期的字符串，返回格式yyyyMMdd
	 * 
	 * @param timezone 时区
	 * @return yyyyMMdd的日期字符串
	 */
	public static String getCurrentDate(TimeZone timezone) {
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		yyyyMMdd.setTimeZone(timezone);
		String r = yyyyMMdd.format(new Date());
		log.info("Current Date:" + r);
		return r;
	}

    /**
     * 获取当前系统时间, 返回格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getYYYYMMDDHHMMSS() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }
    
    public static String convert(String oldFormat, String newFormat, String date) throws ParseException{
    	SimpleDateFormat o = new SimpleDateFormat(oldFormat);
    	Date oldDate = o.parse(date);
    	
    	SimpleDateFormat n = new SimpleDateFormat(newFormat);
    	return n.format(oldDate);
    }

	/**
	 * 获取昨天的日期
	 * 
	 * @param date 日期：yyyyMMdd
	 * @param timezone 时区
	 */
	public static String getYesterday(TimeZone timezone) {
		String date = getCurrentDate(timezone);
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		yyyyMMdd.setTimeZone(timezone);
		Date d = null;
		try {
			d = yyyyMMdd.parse(date);
		} catch (ParseException e) {
			throw new ServiceException(SystemMessageCode.DATE_FORMAT_ERROR);
		}
		d = new Date(d.getTime() - DAY_IN_MS);
		return yyyyMMdd.format(d);
	}
    
	/**
	 * 获取N天的日期字符串集合
	 * 
	 * @param n 数值，n>=0
	 * @param date 日期：yyyyMMdd
	 * @param timezone 时区
	 * @return N天的日期字符串集合
	 * @throws ParseException 日期解析的异常
	 */
	public static Set<String> getLastNdays(int n, String date, TimeZone timezone) {
		Set<String> days = new HashSet<String>();
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		yyyyMMdd.setTimeZone(timezone);
		Date d = null;
		try {
			d = yyyyMMdd.parse(date);
		} catch (ParseException e) {
			throw new ServiceException(SystemMessageCode.DATE_FORMAT_ERROR);
		}

		for (int i = 0; i < n; i++) {
			if (i == 0) {
				days.add(date);
				continue;
			}
			Date temp = new Date(d.getTime() - (i * DAY_IN_MS));
			String dateStr = yyyyMMdd.format(temp);
			days.add(dateStr);
		}
		return days;
	}
	
	public static String getLastNDayAgo(int n, String date, TimeZone timezone){
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		yyyyMMdd.setTimeZone(timezone);
		Date d = null;
		try {
			d = yyyyMMdd.parse(date);
		} catch (ParseException e) {
			throw new ServiceException(SystemMessageCode.DATE_FORMAT_ERROR);
		}

		d = new Date(d.getTime() - (n * DAY_IN_MS));
		return yyyyMMdd.format(d);
	}
	

	public static Set<String> getDateStringByRange(String startDate, String endDate, TimeZone timezone) {
		Set<String> days = new HashSet<String>();
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
		yyyyMMdd.setTimeZone(timezone);
		Date start = null;
		Date end = null;
		try {
			start = yyyyMMdd.parse(startDate);
			end = yyyyMMdd.parse(endDate);
		} catch (ParseException e) {
			throw new ServiceException(SystemMessageCode.DATE_FORMAT_ERROR);
		}

		if ((end.getTime() - start.getTime()) < 0) {
			throw new ServiceException(SystemMessageCode.DATE_RANGE_ERROR);
		}

		long interval = ((end.getTime() - start.getTime()) / DAY_IN_MS) + 1;

		for (int i = 0; i < interval; i++) {
			if (i == 0) {
				days.add(startDate);
				continue;
			}
			Date temp = new Date(start.getTime() + (i * DAY_IN_MS));
			String next = yyyyMMdd.format(temp);
			days.add(next);

		}
		return days;
	}
	
	/**
	 * 对日期进行排序
	 * 
	 * @param days 日期的Set集合
	 * @return 日期的排序后得到的List
	 */
	public static List<String> sortedDate(Set<String> days) {
		// 对日期进行排序
		List<String> sortedDaysList = new ArrayList<String>(days);
		Collections.sort(sortedDaysList);
		return sortedDaysList;
	}

	/**
	 * 对日期进行排序
	 * 
	 * @param days 日期数组
	 * @return 日期的排序后得到的List
	 */
	public static List<String> sortedDate(String[] days) {
		List<String> sortedDaysList = Arrays.asList(days);
		Collections.sort(sortedDaysList);
		return sortedDaysList;
	}

    /**
     * 处理一天的开始时间和结束时间
     *
     * @param date
     * @param start
     * @return
     */
    public static String formatStartOrEndDate(String date, boolean start) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatStr = new SimpleDateFormat("yyyyMMdd");
        int dayMis = 1000 * 60 * 60 * 24;
        try {
            Long dateLong = null;
            if (start) {
                dateLong = formatStr.parse(date).getTime();
            } else {
                dateLong = formatStr.parse(date).getTime() + dayMis -1;
            }
            date = format.format(new Date(dateLong));
        } catch (Exception e) {
            log.info("FormatDate Exception : " + e.getMessage());
        }
        return date;
    }

}
