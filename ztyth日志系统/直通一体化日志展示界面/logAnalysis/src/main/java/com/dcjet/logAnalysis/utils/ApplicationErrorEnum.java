package com.dcjet.logAnalysis.utils;

public enum ApplicationErrorEnum{
	/**
	 * -61000������ʧ�ܣ��޷�������������
	 */
	NoDataToExportError(-61000, "����ʧ�ܣ��޷�������������"),
	
	/**
	 * -61001������ʧ�ܣ��ļ����·��Ϊ��
	 */
	ExportPathEmptyError(-61001, "����ʧ�ܣ��ļ����·��Ϊ��"),
	
	/**
	 * -90000��Ӧ�ó������[${1}]
	 */
	ApplicationError(-90000, "Ӧ�ó���������[${1}]"),
			
	/**
	 * -90001������[${1}]ΪNull
	 */
	ObjectIsNull(-90001, "����[${1}]ΪNull"),
	
	/**
	 * -90002��Bean[${1}]δ����
	 */
	BeanNotDefinition(-90002, "Bean[${1}]δ����"),
	
	/**
	 * -90003���෽��[${1}]��Ҫ��д
	 */
	ClassMethodNeedOverride(-90003, "�෽��[${1}]��Ҫ��д"),
	
	/**
	 * -90004����[${1}]����[${2}]������
	 */
	ClassFieldNotExistError(-90004, "��[${1}]����[${2}]������"),
		  
	/**
	 * -96000����������������Ϣ�Ƿ�[${1}]
	 */
	ConfigInvalidError(-96000, "��������������Ϣ[${1}]�Ƿ�"),
	
	/**
	 * -96001��������������[${1}]Ϊ��
	 */ 
	ConfigEmptyError(-96001, "��������������Ϣ[${1}]Ϊ��"),
	
	/**
	 * -96002��������������[${1}]������
	 */ 
	ConfigNotExistError(-96002, "��������������Ϣ[${1}]������"),
	
	/**
	 * -96003������������ҵ����[${1}]��ҵ��ϵͳ��ʶ[${2}]��Ӧ������Դ������Ϣ������
	 */ 
	CorpDataSourceConfigNotExistError(-96003, "����������ҵ����[${1}]��ҵ��ϵͳ��ʶ[${2}]��Ӧ������Դ������Ϣ������"),
	
	/**
	 * -96004����������ϵͳ[${1}]�ʼ�������Ϣ������
	 */ 
	EmailAddressConfigNotExistError(-96004, "��������ϵͳ[${1}]�ʼ�������Ϣ������"),
	
	/** 
	 * -98000��ʵ����֤ʧ��
	 */
	BeanValidateError(-98000, "ʵ����֤ʧ��"),
	
	/** 
	 * -99999��ϵͳ�����쳣[${1}]
	 */
	SystemException(-99999, "ϵͳ�����쳣[${1}]");
	 
	/**
	 * �������
	 */
	private int code;

	/**
	 * ������Ϣ
	 */
	private String message;
	
	ApplicationErrorEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * ���ش������
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * ���ش�����Ϣ
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}
}