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
     * �ļ���׺�ָ���"."
     */
    public static final String FILE_PATH_SUFFIX_SEPARATOR_CHAR = ".";
	
	/**
	 * �ж��Ƿ��ǿ���
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
	 * ��ȡ��Ԫ���ֵ
	 * @param cell ��Ԫ��
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			switch (cell.getCellType()) {
	          case Cell.CELL_TYPE_STRING: //�ַ���
	        	  value = cell.getStringCellValue();
	              break;
	          case Cell.CELL_TYPE_NUMERIC: //��ֵ��
	        	  //����Ǳ�ת��Ϊ�������͵���������
	        	  if(DateUtil.isCellDateFormatted(cell)) {
	        		  value = String.valueOf(cell.getDateCellValue());
	        	  } else {
	        		  value = String.valueOf(cell.getNumericCellValue());
	        	  }
	              break;
	          case Cell.CELL_TYPE_BOOLEAN: //������
	        	  value = String.valueOf(cell.getBooleanCellValue());
	              break;
	          case Cell.CELL_TYPE_FORMULA: //��ʽ 
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
	 * ����Excel���ʹ���������
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
	 * ����Excel��Ĭ��ÿ��������ĵ�0�д�����ͷ���ӵ�1�п�ʼ�������
	 * @param dataList ����Դ
	 * @param fieldMap ���Ӣ�����Ժ�Excel�е����������Ķ�Ӧ��ϵ
	 * @param excelPostfixEnum ��׺���ͣ�office2003-xls��office2010-xlsx��
	 * @param sheetName ����������
	 * @param sheetSize ÿ���������м�¼�����������Ĭ��65535
	 * @param path Excel����ļ�
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
			//log.error("ExcelUtil::listToExcel()�����쳣 -> {}", e);
			e.printStackTrace();
		}
		return actionResult;
	}
	
	/**
	 * ����Excel��Ĭ��ÿ��������ĵ�0�д�����ͷ���ӵ�1�п�ʼ�������
	 * @param dataList ����Դ
	 * @param fieldMap ���Ӣ�����Ժ�Excel�е����������Ķ�Ӧ��ϵ
	 * @param excelPostfixEnum ��׺���ͣ�office2003-xls��office2010-xlsx��
	 * @param sheetName ����������
	 * @param sheetSize ÿ���������м�¼�����������Ĭ��65535
	 * @param out ������
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

		// ���������������͵�OutputStreamָ���ĵط�
		Workbook wb;
		try {
			wb = CreateWorkbook(excelPostfixEnum);

			// 2003��Excelһ����������������65536����¼����ȥ��ͷʣ��65535��
			// ���������¼̫�࣬��Ҫ�ŵ�����������У���ʵ���Ǹ���ҳ�Ĺ���
			// 1.����һ���ж��ٸ�������
			double sheetNum = Math.ceil(dataList.size()
					/ new Integer(sheetSize).doubleValue());

			// 2.������Ӧ�Ĺ��������������������
			for (int i = 0; i < sheetNum; i++) {
				// ���ֻ��һ������������
				if (1 == sheetNum) {
					org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet();
					wb.setSheetName(i, sheetName + (i + 1));
					
					fillSheet(dataList, fieldMap, sheet, 0, dataList.size() - 1);
				} else { // �ж������������
					org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet();
					wb.setSheetName(i, sheetName + (i + 1));

					// ��ȡ��ʼ�����ͽ�������
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
			//log.error("ExcelUtil::listToExcel()�����쳣 -> {}", e);
			e.printStackTrace();
		}
		return actionResult;
	}

	/**
	 * ��乤����
	 * @param dataList ����Դ
	 * @param titleMap
	 * @param sheet ������
	 * @param firstIndex ��ʼ����
	 * @param lastIndex ��������
	 */
	@SuppressWarnings("rawtypes")
	private static <T> void fillSheet(List<T> dataList, LinkedHashMap<String, String> fieldMap,
			org.apache.poi.ss.usermodel.Sheet sheet, int firstIndex, int lastIndex) {
		try {
			//������Ӣ���ֶ����������ֶ���������
			String[] enFields = new String[fieldMap.size()];
			String[] cnFields = new String[fieldMap.size()];
			
			//�������
			int count = 0;
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				enFields[count] = entry.getKey();
				cnFields[count] = entry.getValue();
				count++;
			}
			
			//��0������ͷ
			createHeadRow(cnFields, sheet, 0);
			
			//��1�п�ʼ�������
			int rowIndex = 1;
			Row dataRow = null;
			for (int index = firstIndex; index <= lastIndex; index++) {
				dataRow = sheet.createRow(rowIndex);
				//��ȡ��������
				T item = dataList.get(index);
				//����������List<Map<key, value>>
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
	 * ������ͷ��
	 * @param titleMap ������������->��ͷ��ʾ����
	 * @param sheet ������
	 * @param haedRowIndex ��ͷ������
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
	 * ������Ԫ��
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
	 * ���ݴ�·���򲻴�·������������ȡ����ֵ<br>
	 * ���ܼ�����������userName�ȣ�����ܴ�·��������������student.department.name��
	 * @param fieldNameSequence ��·�������������������
	 * @param obj ����
	 * @return
	 */
	private static Object getFieldValueByNameSequence(String fieldNameSequence, Object obj) {
		Object value = null;

		// ��fieldNameSequence���в��
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1) {
			value = getFieldValueByName(fieldNameSequence, obj);
		} else {
			// ������������ȡ���Զ���
			Object fieldObj = getFieldValueByName(attributes[0], obj);
			String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;
	}
	
	/**
	 * ������������ȡ����ֵ
	 * @param fieldName ������
	 * @param obj ����
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
	 * ������������ȡ����
	 * @param fieldName ������
	 * @param clazz ���������Ե���
	 * @return
	 */
	private static Field getFieldByName(String fieldName, Class<?> clazz) {
		//��ȡ�������������
		Field[] selfFields = clazz.getDeclaredFields();
		
		//��������д��ڸ����ԣ��򷵻�
		for(Field field : selfFields){
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		
		//���򣬲鿴�������Ƿ���ڴ����ԣ�������򷵻�
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class) {
			return getFieldByName(fieldName, superClazz);
		}
		
		//�������͸��඼û�У��򷵻ؿ�
		return null;
	}
}