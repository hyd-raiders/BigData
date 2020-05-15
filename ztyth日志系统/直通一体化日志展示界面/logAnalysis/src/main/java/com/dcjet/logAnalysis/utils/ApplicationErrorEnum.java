package com.dcjet.logAnalysis.utils;

public enum ApplicationErrorEnum{
	/**
	 * -61000：导出失败，无符合条件的数据
	 */
	NoDataToExportError(-61000, "导出失败，无符合条件的数据"),
	
	/**
	 * -61001：导出失败，文件输出路径为空
	 */
	ExportPathEmptyError(-61001, "导出失败，文件输出路径为空"),
	
	/**
	 * -90000：应用程序错误[${1}]
	 */
	ApplicationError(-90000, "应用程序发生错误[${1}]"),
			
	/**
	 * -90001：对象[${1}]为Null
	 */
	ObjectIsNull(-90001, "对象[${1}]为Null"),
	
	/**
	 * -90002：Bean[${1}]未定义
	 */
	BeanNotDefinition(-90002, "Bean[${1}]未定义"),
	
	/**
	 * -90003：类方法[${1}]需要重写
	 */
	ClassMethodNeedOverride(-90003, "类方法[${1}]需要重写"),
	
	/**
	 * -90004：类[${1}]属性[${2}]不存在
	 */
	ClassFieldNotExistError(-90004, "类[${1}]属性[${2}]不存在"),
		  
	/**
	 * -96000：发生错误，配置信息非法[${1}]
	 */
	ConfigInvalidError(-96000, "发生错误，配置信息[${1}]非法"),
	
	/**
	 * -96001：发生错误，配置[${1}]为空
	 */ 
	ConfigEmptyError(-96001, "发生错误，配置信息[${1}]为空"),
	
	/**
	 * -96002：发生错误，配置[${1}]不存在
	 */ 
	ConfigNotExistError(-96002, "发生错误，配置信息[${1}]不存在"),
	
	/**
	 * -96003：发生错误，企业编码[${1}]和业务系统标识[${2}]对应的数据源配置信息不存在
	 */ 
	CorpDataSourceConfigNotExistError(-96003, "发生错误，企业编码[${1}]和业务系统标识[${2}]对应的数据源配置信息不存在"),
	
	/**
	 * -96004：发生错误，系统[${1}]邮件配置信息不存在
	 */ 
	EmailAddressConfigNotExistError(-96004, "发生错误，系统[${1}]邮件配置信息不存在"),
	
	/** 
	 * -98000：实体验证失败
	 */
	BeanValidateError(-98000, "实体验证失败"),
	
	/** 
	 * -99999：系统出现异常[${1}]
	 */
	SystemException(-99999, "系统出现异常[${1}]");
	 
	/**
	 * 错误代码
	 */
	private int code;

	/**
	 * 错误信息
	 */
	private String message;
	
	ApplicationErrorEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 返回错误代码
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 返回错误信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}
}