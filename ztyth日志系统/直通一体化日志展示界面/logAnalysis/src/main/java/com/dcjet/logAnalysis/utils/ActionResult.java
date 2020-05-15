package com.dcjet.logAnalysis.utils;

/**
 * Copyright (c) 2017, 苏州神州数码捷通科技有限公司
 * All rights reserved.
 * 
 * <h3>操作结果</h3>
 * @version 1.0
 * @author yjcai 2017/2/10 初次创建
 * 
 */
public class ActionResult {
	/**
	 * 是否成功
	 */
	private boolean success;
	
	/**
	 * 消息代码
	 */
	private String code;
	
	/**
	 * 消息内容
	 */
	private String message;

	/**
	 * 获取是否成功
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * 设置是否成功
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/**
	 * 获取消息代码
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 设置消息代码
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取消息内容
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 设置消息内容
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 设置失败状态和错误消息
	 * @param errorMessage 错误消息内容
	 */
    public void setError(String errorMessage) {
    	this.success = false;
    	this.message = errorMessage;
    }
    
    /**
     * 设置失败状态、错误消息代码和错误消息
     * @param code 错误消息代码
     * @param errorMessage 错误消息内容
     */
    public void setError(String code, String errorMessage) {
    	this.success = false;
    	this.code = code;
    	this.message = errorMessage;
    }
}