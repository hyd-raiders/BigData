package com.dcjet.logAnalysis.utils;

public enum ExcelPostfixEnum {
	/**
	 * Office2003����[xls]
	 */
	Office2003Xls("xls"),
	
	/**
	 * Office2007����߰汾����[xlsx]
	 */
	Office2007Xlsx("xlsx");
	
	/**
	 * ����ֵ
	 */
	private String value;

	ExcelPostfixEnum(String value) {
		this.value = value;
	}

	/**
	 * ���س���ֵ
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

