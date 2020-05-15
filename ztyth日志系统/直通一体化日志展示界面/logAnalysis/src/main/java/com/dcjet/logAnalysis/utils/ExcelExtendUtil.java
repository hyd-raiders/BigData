package com.dcjet.logAnalysis.utils;

import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class ExcelExtendUtil extends ExcelUtil{

	/**
	 * ����Excel��xls��ʽ�����������Ĭ��ÿ��������ĵ�0�д�����ͷ���ӵ�1�п�ʼ�������
	 * @param dataList ����Դ
	 * @param fieldMap ���Ӣ�����Ժ�Excel�е����������Ķ�Ӧ��ϵ
	 * @param excelPostfixEnum Excel������
	 * @param sheetName ����������
	 * @param sheetSize ÿ���������м�¼���������
	 * @param Excel����ļ���
	 * @param response
	 */
	public static <T> ActionResult listToExcel(List<T> dataList,  LinkedHashMap<String, String> fieldMap,
			ExcelPostfixEnum excelPostfixEnum, String sheetName, int sheetSize, String fileName, HttpServletResponse response) {
		ActionResult actionResult = new ActionResult();
		if(StringUtils.isBlank(fileName)) {
			// ����Ĭ���ļ���
			fileName = DateTimeUtil.convertDateToString(DateTimeUtil.D17_DATETIME_PATTERN, new Date()) + PubUtil.getFixLengthRandomString(5);
		}
		
		//����responseͷ��Ϣ
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		response.setHeader("Content-Type", "application/force-download");
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("UTF-8");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=\"" + new String((fileName + FILE_PATH_SUFFIX_SEPARATOR_CHAR 
					+ excelPostfixEnum.getConstValue()).getBytes("GBK"), "ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();
			actionResult = listToExcel(dataList, fieldMap, excelPostfixEnum, sheetName, sheetSize, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actionResult;
	}
}
