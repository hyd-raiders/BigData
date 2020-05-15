package com.dcjet.logAnalysis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Copyright (c) 2017, �������������ͨ�Ƽ����޹�˾
 * All rights reserved.
 * 
 * <h3>����ʱ�乤����</h3>
 * @version 1.0
 * @author yjcai 2017/2/6 ���δ���
 * 
 */
public class DateTimeUtil {
	private static String defaultDatePattern = null;
	/**
	 * EEE MMM dd HH:mm:ss zzz yyyy ʾ����Tue May 09 00:00:00 CST 2017
	 */
	public static final String CST_DATETIME_PATTERN= "EEE MMM dd HH:mm:ss zzz yyyy";

	/**
	 * yyyy-MM-dd HH:mm:ss ʾ���� 2016-11-05 16:41:49
	 */
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyyMMdd ʾ���� 20161105
	 */
	public static final String D8_DATETIME_PATTERN = "yyyyMMdd";
	
	/**
	 * yyyyMMddHH ʾ���� 2016110516
	 */
	public static final String D10_DATETIME_PATTERN  = "yyyyMMddHH";
	
	/**
	 * yyyyMMddHHmm ʾ���� 201611051641
	 */
	public static final String D12_DATETIME_PATTERN = "yyyyMMddHHmm";
	
	/**
	 * yyyyMMddHHmmss ʾ���� 20161105164149
	 */
	public static final String D14_DATETIME_PATTERN = "yyyyMMddHHmmss";
	
	/**
	 * yyyyMMddHHmmssSSS ʾ���� 20161105164149678
	 */
	public static final String D17_DATETIME_PATTERN = "yyyyMMddHHmmssSSS";
	
	/**
	 * yyyy-MM-dd ʾ���� 2016-11-05
	 */
	public static final String D8SP_DATETIME_PATTERN = "yyyy-MM-dd";
	
	/**
	 * yyyy-MM-dd HH:mm:ss ʾ���� 2016-11-05 16:41:49
	 */
	public static final String D14SP_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS ʾ���� 2016-11-05 16:41:49.678
	 */
	public static final String D17SP_DATETIME_PATTERN  = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * HH:mm:ss ʾ����16:41:49
	 */
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	
	/**
	 * ��ȡĬ�����ڸ�ʽ��ģ��
	 * @return
	 */
	public static synchronized String getDatePattern() {
		defaultDatePattern = DEFAULT_DATETIME_PATTERN;
		return defaultDatePattern;
	}
	
	/**
	 * ����ָ��ģ���ʽ�����ڣ������ַ���
	 * @param pattern ��ʽ��ģ��
	 * @param date ָ��������
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
	 * ����Ĭ��ģ�壨yyyy-MM-dd HH:mm:ss����ʽ�����ڣ������ַ���
	 * @param date ָ��������
	 * @return
	 */
	public static final String convertDateToString(Date date) {
		return convertDateToString(getDatePattern(), date);
	}

	/**
	 * ת���ַ�������������
	 * @param pattern ��ʽ��ģ��
	 * @param strDate �����ַ���
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
	 * ����Ĭ�ϸ�ʽ��ģ��yyyy-MM-dd HH:mm:ssת���ַ�������������
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
	 * ���ݶ�����ʱ���ʽ��ģ��ת���ַ�������������
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
	 * ת��17λ�ַ�������������
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
	 * ��ȡָ����ݹ��ж�����
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekNumOfYear(calendar.getTime());
    }
	
	/**
	 * ��ȡĳ����һ���еĵڼ���
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
	 * ��ȡĳ�������ܵĵ�һ��
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
	 * ��ȡĳ�������ܵ����һ��
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
	 * ��������ָ������
	 * @param date ָ��������
	 * @param amount ����
	 * @return
	 */
	public static Date addDays(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE ,amount);
		return calendar.getTime();
	}
	
	/**
	 * ��������ָ������
	 * @param date ָ��������
	 * @param amount ����
	 * @return
	 */
	public static Date addWeeks(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR ,amount);
		return calendar.getTime();
	}
	
	/**
	 * �����������ڼ��������
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDay(Date date1, Date date2) {
		return Integer.parseInt(String.valueOf(((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000))));
	}
	
	/**
	 * �����������ڼ��Сʱ��
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffHour(Date date1, Date date2) {
		return (int)(date2.getTime() - date1.getTime())/1000/60/60;
	}
	
	/**
	 * �����������ڼ�ķֲ�
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffMinute(Date date1, Date date2) {
		return (int)(date2.getTime() - date1.getTime())/1000/60;
	}
	
	/**
	 * �����������ڼ�ĺ����
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long diffMilliSecond(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}
}
