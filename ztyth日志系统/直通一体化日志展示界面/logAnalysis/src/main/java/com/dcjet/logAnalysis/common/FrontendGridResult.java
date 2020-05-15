package com.dcjet.logAnalysis.common;

import java.util.ArrayList;
import java.util.List;
/**
 * Copyright (c) 2017, 苏州神州数码捷通科技有限公司
 * All rights reserved.
 * 
 * <h3>前台交互结果集</h3>
 * @version 1.0
 * @author Administrator
 * 
 */
public class FrontendGridResult<T> {
	/**
	 * 总记录数
	 */
	private int total;	
	/**
	 * 记录
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
