package com.dcjet.logAnalysis.utils;

import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class ExcelExtendUtil extends ExcelUtil{

	/**
	 * 导出Excel（xls格式）到浏览器，默认每个工作表的第0行创建表头，从第1行开始填充数据
	 * @param dataList 数据源
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系
	 * @param excelPostfixEnum Excel的类型
	 * @param sheetName 工作表名称
	 * @param sheetSize 每个工作表中记录的最大行数
	 * @param Excel输出文件名
	 * @param response
	 */
	public static <T> ActionResult listToExcel(List<T> dataList,  LinkedHashMap<String, String> fieldMap,
			ExcelPostfixEnum excelPostfixEnum, String sheetName, int sheetSize, String fileName, HttpServletResponse response) {
		ActionResult actionResult = new ActionResult();
		if(StringUtils.isBlank(fileName)) {
			// 设置默认文件名
			fileName = DateTimeUtil.convertDateToString(DateTimeUtil.D17_DATETIME_PATTERN, new Date()) + PubUtil.getFixLengthRandomString(5);
		}
		
		//设置response头信息
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
