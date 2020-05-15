package com.dcjet.logAnalysis.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelUtil {
    /**
     * 文件后缀分隔符"."
     */
    public static final String FILE_PATH_SUFFIX_SEPARATOR_CHAR = ".";
	
	/**
	 * 判断是否是空行
	 * @param row
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isEmptyRow(Row row) {
		Cell cell = null;
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
				return false;
		}
		return true;
	}
	
	/**
	 * 读取单元格的值
	 * @param cell 单元格
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
	          case Cell.CELL_TYPE_STRING: //字符型
	        	  value = cell.getStringCellValue();
	              break;
	          case Cell.CELL_TYPE_NUMERIC: //数值型
	        	  //如果是被转换为数字类型的日期类型
	        	  if(DateUtil.isCellDateFormatted(cell)) {
	        		  value = String.valueOf(cell.getDateCellValue());
	        	  } else {
	        		  value = String.valueOf(cell.getNumericCellValue());
	        	  }
	              break;
	          case Cell.CELL_TYPE_BOOLEAN: //布尔型
	        	  value = String.valueOf(cell.getBooleanCellValue());
	              break;
	          case Cell.CELL_TYPE_FORMULA: //公式 
	        	  value = String.valueOf(cell.getCellFormula());
	              break;
	          case Cell.CELL_TYPE_ERROR:
	        	  value = String.valueOf(cell.getErrorCellValue());
	              break;
	          case Cell.CELL_TYPE_BLANK:
	        	  value = null;
	              break;
	          default:
	              break;
	          }
		}
		return value;
	}
	
	
	/**
	 * 根据Excel类型创建工作薄
	 * @param excelPostfixEnum
	 * @return
	 */
	public static Workbook CreateWorkbook(ExcelPostfixEnum excelPostfixEnum) {
		Workbook workbook;
		if(excelPostfixEnum.equals(ExcelPostfixEnum.Office2003Xls)) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook();
		}
		
		return workbook;
	}
	
	/**
	 * 导出Excel，默认每个工作表的第0行创建表头，从第1行开始填充数据
	 * @param dataList 数据源
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系
	 * @param excelPostfixEnum 后缀类型（office2003-xls，office2010-xlsx）
	 * @param sheetName 工作表名称
	 * @param sheetSize 每个工作表中记录的最大行数，默认65535
	 * @param path Excel输出文件
	 */
	public static <T> ActionResult listToExcel(List<T> dataList, LinkedHashMap<String, String> fieldMap,
			ExcelPostfixEnum excelPostfixEnum, String sheetName, int sheetSize, String path) {
		ActionResult actionResult = new ActionResult();
		if (dataList.size() == 0 || dataList == null) {
        	actionResult.setError(ApplicationErrorEnum.NoDataToExportError.getMessage());
        	return actionResult;
		}
		if(StringUtils.isBlank(path)) {
        	actionResult.setError(ApplicationErrorEnum.ExportPathEmptyError.getMessage());
        	return actionResult;
		}
		
		try {
			OutputStream outStream = new FileOutputStream(path);
			actionResult = listToExcel(dataList, fieldMap, excelPostfixEnum, sheetName, sheetSize, outStream);
		} catch (FileNotFoundException e) {
			//log.error("ExcelUtil::listToExcel()发生异常 -> {}", e);
			e.printStackTrace();
		}
		return actionResult;
	}
	
	/**
	 * 导出Excel，默认每个工作表的第0行创建表头，从第1行开始填充数据
	 * @param dataList 数据源
	 * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系
	 * @param excelPostfixEnum 后缀类型（office2003-xls，office2010-xlsx）
	 * @param sheetName 工作表名称
	 * @param sheetSize 每个工作表中记录的最大行数，默认65535
	 * @param out 导出流
	 */
	public static <T> ActionResult listToExcel(List<T> dataList, LinkedHashMap<String, String> fieldMap,
			ExcelPostfixEnum excelPostfixEnum, String sheetName, int sheetSize, OutputStream outStream) {
		ActionResult actionResult = new ActionResult();
		if (dataList.size() == 0 || dataList == null) {
        	actionResult.setError(ApplicationErrorEnum.NoDataToExportError.getMessage());
        	return actionResult;
		}

		if (sheetSize > 65535 || sheetSize < 1) {
			sheetSize = 65535;
		}

		// 创建工作簿并发送到OutputStream指定的地方
		Workbook wb;
		try {
			wb = CreateWorkbook(excelPostfixEnum);

			// 2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
			// 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
			// 1.计算一共有多少个工作表
			double sheetNum = Math.ceil(dataList.size()
					/ new Integer(sheetSize).doubleValue());

			// 2.创建相应的工作表，并向其中填充数据
			for (int i = 0; i < sheetNum; i++) {
				// 如果只有一个工作表的情况
				if (1 == sheetNum) {
					org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet();
					wb.setSheetName(i, sheetName + (i + 1));
					
					fillSheet(dataList, fieldMap, sheet, 0, dataList.size() - 1);
				} else { // 有多个工作表的情况
					org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet();
					wb.setSheetName(i, sheetName + (i + 1));

					// 获取开始索引和结束索引
					int firstIndex = i * sheetSize;
					int lastIndex = (i + 1) * sheetSize - 1 > dataList.size() - 1 ? dataList
							.size() - 1 : (i + 1) * sheetSize - 1;

					fillSheet(dataList, fieldMap, sheet, firstIndex, lastIndex);
				}
			}
			outStream.flush();
			wb.write(outStream);
			wb.close();
			outStream.close();
		} catch (Exception e) {
			//log.error("ExcelUtil::listToExcel()发生异常 -> {}", e);
			e.printStackTrace();
		}
		return actionResult;
	}

	/**
	 * 填充工作表
	 * @param dataList 数据源
	 * @param titleMap
	 * @param sheet 工作表
	 * @param firstIndex 开始索引
	 * @param lastIndex 结束索引
	 */
	@SuppressWarnings("rawtypes")
	private static <T> void fillSheet(List<T> dataList, LinkedHashMap<String, String> fieldMap,
			org.apache.poi.ss.usermodel.Sheet sheet, int firstIndex, int lastIndex) {
		try {
			//定义存放英文字段名和中文字段名的数组
			String[] enFields = new String[fieldMap.size()];
			String[] cnFields = new String[fieldMap.size()];
			
			//填充数组
			int count = 0;
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				enFields[count] = entry.getKey();
				cnFields[count] = entry.getValue();
				count++;
			}
			
			//第0行填充表头
			createHeadRow(cnFields, sheet, 0);
			
			//第1行开始填充数据
			int rowIndex = 1;
			Row dataRow = null;
			for (int index = firstIndex; index <= lastIndex; index++) {
				dataRow = sheet.createRow(rowIndex);
				//获取单个对象
				T item = dataList.get(index);
				//如果结果集是List<Map<key, value>>
				if (item instanceof Map) {
					for (int colunmIndex = 0; colunmIndex < enFields.length; colunmIndex++) {
						Object objValue = ((Map)item).get(enFields[colunmIndex]);
						String fieldValue = objValue == null ? "" : objValue.toString();
						createCell(dataRow, colunmIndex, fieldValue, null);
					}
				} else {
					for (int colunmIndex = 0; colunmIndex < enFields.length; colunmIndex++) {
						Object objValue = getFieldValueByNameSequence(enFields[colunmIndex], item);
						String fieldValue = objValue == null ? "" : objValue.toString();
						createCell(dataRow, colunmIndex, fieldValue, null);
					}
				}

				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建表头行
	 * @param titleMap 对象属性名称->表头显示名称
	 * @param sheet 工作表
	 * @param haedRowIndex 表头行索引
	 */
	public static void createHeadRow(String[] titleFields,
			org.apache.poi.ss.usermodel.Sheet sheet, int headRowIndex) {
		Row headRow = sheet.createRow(headRowIndex);
		for (int colunmIndex = 0; colunmIndex < titleFields.length; colunmIndex++) {
			Cell headCell = headRow.createCell(colunmIndex);
			headCell.setCellValue(titleFields[colunmIndex]);
		}
	}

	/**
	 * 创建单元格
	 * @param row
	 * @param colunmIndex
	 * @param value
	 * @param style
	 */
	@SuppressWarnings("deprecation")
	public static void createCell(Row row, int colunmIndex, String value, CellStyle style) {
		Cell cell = row.createCell(colunmIndex);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		if (style != null) {
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * 根据带路径或不带路径的属性名获取属性值<br>
	 * 接受简单属性名，如userName等，亦接受带路径的属性名，如student.department.name等
	 * @param fieldNameSequence 带路径的属性名或简单属性名
	 * @param obj 对象
	 * @return
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence, Object obj) {
		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, obj);
		} else {
			// 根据属性名获取属性对象
			Object fieldObj = getFieldValueByName(attributes[0], obj);
			String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;
	}
	
	/**
	 * 根据属性名获取属性值
	 * @param fieldName 属性名
	 * @param obj 对象
	 * @return
	 */
	private static Object getFieldValueByName(String fieldName, Object obj) {
		Object value = null;
		Field field = getFieldByName(fieldName, obj.getClass());

		if (field != null) {
			field.setAccessible(true);
			try {
				value = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			//throw ApolloException.getInstance(ApplicationErrorEnum.ClassFieldNotExistError, obj.getClass().getSimpleName(), fieldName);
		}
		
		return value;
	}
	
	/**
	 * 根据属性名获取属性
	 * @param fieldName 属性名
	 * @param clazz 包含该属性的类
	 * @return
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		//获取本类的所有属性
		Field[] selfFields = clazz.getDeclaredFields();
		
		//如果本类中存在该属性，则返回
		for(Field field : selfFields){
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		
		//否则，查看父类中是否存在此属性，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}
		
		//如果本类和父类都没有，则返回空
		return null;
	}
}