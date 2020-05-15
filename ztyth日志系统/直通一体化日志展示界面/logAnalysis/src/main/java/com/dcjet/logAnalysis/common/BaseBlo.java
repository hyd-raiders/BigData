package com.dcjet.logAnalysis.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dcjet.logAnalysis.entity.LogEntity;
import com.dcjet.logAnalysis.entity.LogSearchEntity;

public abstract class BaseBlo {
	public abstract FrontendGridResult getList(LogSearchEntity searchEntity) throws IOException;
	
	public abstract List<LogEntity> getDataSourceExport(LogSearchEntity searchEntity);
	
	public abstract APIReponse get(String oid);
	
	public String getSearchCondition(LogSearchEntity searchEntity) {
		String strWhere = "";
		
		String corpCode = searchEntity.getCorpcode();
		String userName = searchEntity.getUsername();
		String pageName = searchEntity.getPagename();
		String functionName = searchEntity.getFunctionname();
		String message = searchEntity.getMessage();
		String createDateBegin = searchEntity.getCreatedatebegin();
		String createDateEnd = searchEntity.getCreatedateend();
		
//		String corpCode = (String) request.getParameter("corpcode");
//		String userName = (String) request.getParameter("username");
//		String pageName = (String) request.getParameter("pagename");
//		String functionName = (String) request.getParameter("functionname");
//		String message = (String) request.getParameter("message");
//		String createDateBegin = (String) request.getParameter("createdatebegin");
//		String createDateEnd = (String) request.getParameter("createdateend");

		if (corpCode != null && !corpCode.equals("")) {
			strWhere += " and CORPCODE ='" + corpCode + "'";
		}
		if (userName != null && !userName.equals("")) {
			strWhere += " and USERNAME ='" + userName + "'";
		}
		if (pageName != null && !pageName.equals("")) {
			strWhere += " and PAGENAME ='" + pageName + "'";
		}
		if (functionName != null && !functionName.equals("")) {
			strWhere += " and FUNCTIONNAME ='" + functionName + "'";
		}
		if (message != null && !message.equals("")) {
			strWhere += " and MESSAGE like '%" + message + "%'";
		}
		if (createDateBegin != null && !createDateBegin.equals("")) {
			//strWhere += " and OID >='" + createDateBegin + " 00:00:00' ";
			strWhere += " and OID >='" + createDateBegin + "' ";
		}
		if (createDateEnd != null && !createDateEnd.equals("")) {
			//strWhere += " and OID <='" + createDateEnd + " 23:59:59' ";
			strWhere += " and OID <='" + createDateEnd + "' ";
		}
		return strWhere;
	}

}
