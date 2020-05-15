
package com.dcjet.logAnalysis.entity;

public class LogSearchEntity{
	private static final long serialVersionUID = 1L;
	
	private String corpcode;

	private String username;

	private String pagename;

	private String functionname;

	private String createdatebegin;

	private String createdateend;

	private String message;
	
	private int page;
	
	private int rows;
	
	private String quick;

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getFunctionname() {
		return functionname;
	}

	public void setFunctionname(String functionname) {
		this.functionname = functionname;
	}

	public String getCreatedatebegin() {
		return createdatebegin;
	}

	public void setCreatedatebegin(String createdatebegin) {
		this.createdatebegin = createdatebegin;
	}

	public String getCreatedateend() {
		return createdateend;
	}

	public void setCreatedateend(String createdateend) {
		this.createdateend = createdateend;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getQuick() {
		return quick;
	}

	public void setQuick(String quick) {
		this.quick = quick;
	}
}