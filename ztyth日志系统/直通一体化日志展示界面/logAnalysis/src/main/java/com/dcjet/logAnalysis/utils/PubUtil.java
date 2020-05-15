package com.dcjet.logAnalysis.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Copyright (c) 2017, 苏州神州数码捷通科技有限公司
 * All rights reserved.
 * 
 * <h3>通用工具类</h3>
 * @version 1.0
 * @author yjcai 2017/2/6 初次创建
 * 
 */
public class PubUtil {
	/**
	 * 生成UUID
	 * @return
	 */
	public static final String generateUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	/**
	 * 获取固定长度的随机数字符串，位数不足时在前面补0
	 * @param length
	 * @return
	 */
	public static String getFixLengthRandomString(int length) {
        Random rm = new Random();
        double pross = (1 + rm.nextDouble()) * Math.pow(10, length);
        String fixLenthString = String.valueOf(pross);

        return fixLenthString.substring(1, length + 1);
    }
}

