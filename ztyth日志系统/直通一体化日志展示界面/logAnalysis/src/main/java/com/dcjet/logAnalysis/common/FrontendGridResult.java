package com.dcjet.logAnalysis.common;

import java.util.ArrayList;
import java.util.List;
/**
 * Copyright (c) 2017, �������������ͨ�Ƽ����޹�˾
 * All rights reserved.
 * 
 * <h3>ǰ̨���������</h3>
 * @version 1.0
 * @author Administrator
 * 
 */
public class FrontendGridResult<T> {
	/**
	 * �ܼ�¼��
	 */
	private int total;	
	/**
	 * ��¼
	 */
	private List<T> rows;
	
	public FrontendGridResult(int total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

	public FrontendGridResult() {
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
