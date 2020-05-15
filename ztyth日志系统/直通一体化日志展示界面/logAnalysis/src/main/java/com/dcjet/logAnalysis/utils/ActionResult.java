package com.dcjet.logAnalysis.utils;

/**
 * Copyright (c) 2017, �������������ͨ�Ƽ����޹�˾
 * All rights reserved.
 * 
 * <h3>�������</h3>
 * @version 1.0
 * @author yjcai 2017/2/10 ���δ���
 * 
 */
public class ActionResult {
	/**
	 * �Ƿ�ɹ�
	 */
	private boolean success;
	
	/**
	 * ��Ϣ����
	 */
	private String code;
	
	/**
	 * ��Ϣ����
	 */
	private String message;

	/**
	 * ��ȡ�Ƿ�ɹ�
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * �����Ƿ�ɹ�
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/**
	 * ��ȡ��Ϣ����
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * ������Ϣ����
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * ��ȡ��Ϣ����
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ������Ϣ����
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * ����ʧ��״̬�ʹ�����Ϣ
	 * @param errorMessage ������Ϣ����
	 */
    public void setError(String errorMessage) {
    	this.success = false;
    	this.message = errorMessage;
    }
    
    /**
     * ����ʧ��״̬��������Ϣ����ʹ�����Ϣ
     * @param code ������Ϣ����
     * @param errorMessage ������Ϣ����
     */
    public void setError(String code, String errorMessage) {
    	this.success = false;
    	this.code = code;
    	this.message = errorMessage;
    }
}