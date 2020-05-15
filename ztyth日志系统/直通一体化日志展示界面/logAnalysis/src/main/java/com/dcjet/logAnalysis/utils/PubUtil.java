package com.dcjet.logAnalysis.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Copyright (c) 2017, �������������ͨ�Ƽ����޹�˾
 * All rights reserved.
 * 
 * <h3>ͨ�ù�����</h3>
 * @version 1.0
 * @author yjcai 2017/2/6 ���δ���
 * 
 */
public class PubUtil {
	/**
	 * ����UUID
	 * @return
	 */
	public static final String generateUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	/**
	 * ��ȡ�̶����ȵ�������ַ�����λ������ʱ��ǰ�油0
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

