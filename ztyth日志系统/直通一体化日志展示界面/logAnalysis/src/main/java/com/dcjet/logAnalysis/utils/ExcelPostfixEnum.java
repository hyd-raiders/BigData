package com.dcjet.logAnalysis.utils;

public enum ExcelPostfixEnum {
	/**
	 * Office2003类型[xls]
	 */
	Office2003Xls("xls"),
	
	/**
	 * Office2007或更高版本类型[xlsx]
	 */
	Office2007Xlsx("xlsx");
	
	/**
	 * 常量值
	 */
	private String value;

	ExcelPostfixEnum(String value) {
		this.value = value;
	}

	/**
	 * 返回常量值
	 *  
	 * @return
	 */
	public String getConstValue() {
		return this.value;
	}
	
	public boolean compareValue(String compare) {
		if (compare == null) {
			return false;
		}
		return this.getConstValue().equals(compare);
	}
}

