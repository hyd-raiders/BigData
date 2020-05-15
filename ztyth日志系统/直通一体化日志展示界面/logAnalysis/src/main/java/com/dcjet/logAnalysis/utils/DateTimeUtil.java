package com.dcjet.logAnalysis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Copyright (c) 2017, 苏州神州数码捷通科技有限公司
 * All rights reserved.
 * 
 * <h3>日期时间工具类</h3>
 * @version 1.0
 * @author yjcai 2017/2/6 初次创建
 * 
 */
public class DateTimeUtil {
	private static String defaultDatePattern = null;
	/**
	 * EEE MMM dd HH:mm:ss zzz yyyy 示例：Tue May 09 00:00:00 CST 2017
	 */
	public static final String CST_DATETIME_PATTERN= "EEE MMM dd HH:mm:ss zzz yyyy";

	/**
	 * yyyy-MM-dd HH:mm:ss 示例： 2016-11-05 16:41:49
	 */
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyyMMdd 示例： 20161105
	 */
	public static final String D8_DATETIME_PATTERN = "yyyyMMdd";
	
	/**
	 * yyyyMMddHH 示例： 2016110516
	 */
	public static final String D10_DATETIME_PATTERN  = "yyyyMMddHH";
	
	/**
	 * yyyyMMddHHmm 示例： 201611051641
	 */
	public static final String D12_DATETIME_PATTERN = "yyyyMMddHHmm";
	
	/**
	 * yyyyMMddHHmmss 示例： 20161105164149
	 */
	public static final String D14_DATETIME_PATTERN = "yyyyMMddHHmmss";
	
	/**
	 * yyyyMMddHHmmssSSS 示例： 20161105164149678
	 */
	public static final String D17_DATETIME_PATTERN = "yyyyMMddHHmmssSSS";
	
	/**
	 * yyyy-MM-dd 示例： 2016-11-05
	 */
	public static final String D8SP_DATETIME_PATTERN = "yyyy-MM-dd";
	
	/**
	 * yyyy-MM-dd HH:mm:ss 示例： 2016-11-05 16:41:49
	 */
	public static final String D14SP_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS 示例： 2016-11-05 16:41:49.678
	 */
	public static final String D17SP_DATETIME_PATTERN  = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * HH:mm:ss 示例：16:41:49
	 */
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	
	/**
	 * 获取默认日期格式化模板
	 * @return
	 */
	public static synchronized String getDatePattern() {
		defaultDatePattern = DEFAULT_DATETIME_PATTERN;
		return defaultDatePattern;
	}
	
	/**
	 * 按照指定模板格式化日期，返回字符串
	 * @param pattern 格式化模板
	 * @param date 指定的日期
	 * @return
	 */
	public static final String convertDateToString(String pattern, Date date) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (date != null && !StringUtils.isBlank(pattern)) {
			df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}

		return returnValue;
	}
	
	/**
	 * 根据默认模板（yyyy-MM-dd HH:mm:ss）格式化日期，返回字符串
	 * @param date 指定的日期
	 * @return
	 */
	public static final String convertDateToString(Date date) {
		return convertDateToString(getDatePattern(), date);
	}

	/**
	 * 转换字符串到日期类型
	 * @param pattern 格式化模板
	 * @param strDate 日期字符串
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String pattern, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		if (!StringUtils.isBlank(pattern) && !StringUtils.isBlank(strDate)) {
			df = new SimpleDateFormat(pattern);
			date = df.parse(strDate);
		}

		return date;
	}
	
	/**
	 * 根据默认格式化模板yyyy-MM-dd HH:mm:ss转换字符串到日期类型
	 * @param strDate
	 * @return
	 */
	public static Date convertStringToDate(String strDate) {
		Date date = null;
		try {
			date = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}

		return date;
	}
	
	/**
	 * 根据东八区时间格式化模板转换字符串到日期类型
	 * @param strDate
	 * @return
	 */
	public static Date convertCSTStringToDate(String strDate) {
		Date date = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(CST_DATETIME_PATTERN, Locale.US);
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}

		return date;
	}
	
	/**
	 * 转换17位字符串到日期类型
	 * @param strDate
	 * @return
	 */
	public static Date convertD17StringToDate(String strDate) {
		Date date = null;
		try {
			if (!StringUtils.isBlank(strDate)) {
				strDate = strDate.substring(0, strDate.length() - 3);
				date = convertStringToDate(D14_DATETIME_PATTERN, strDate);
			}
		} catch (Exception pe) {
			pe.printStackTrace();
			return date;
		}

		return date;
	}

	/**
	 * 获取指定年份共有多少周
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekNumOfYear(calendar.getTime());
    }
	
	/**
	 * 获取某天是一年中的第几周
	 * @param date
	 * @return
	 */
	public static int getWeekNumOfYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
	
	/**
	 * 获取某天所在周的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }
	
	/**
	 * 获取某天所在周的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        return calendar.getTime();
    }

	/**
	 * 日期增加指定天数
	 * @param date 指定的日期
	 * @param amount 天数
	 * @return
	 */
	public static Date addDays(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE ,amount);
		return calendar.getTime();
	}
	
	/**
	 * 日期增加指定周数
	 * @param date 指定的日期
	 * @param amount 周数
	 * @return
	 */
	public static Date addWeeks(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR ,amount);
		return calendar.getTime();
	}
	
	/**
	 * 计算两个日期间的天数差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDay(Date date1, Date date2) {
		return Integer.parseInt(String.valueOf(((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000))));
	}
	
	/**
	 * 计算两个日期间的小时差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffHour(Date date1, Date date2) {
		return (int)(date2.getTime() - date1.getTime())/1000/60/60;
	}
	
	/**
	 * 计算两个日期间的分差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffMinute(Date date1, Date date2) {
		return (int)(date2.getTime() - date1.getTime())/1000/60;
	}
	
	/**
	 * 计算两个日期间的毫秒差
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long diffMilliSecond(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}
}
