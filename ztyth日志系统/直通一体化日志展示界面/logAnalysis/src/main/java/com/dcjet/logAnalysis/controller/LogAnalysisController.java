package com.dcjet.logAnalysis.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dcjet.logAnalysis.common.APIReponse;
import com.dcjet.logAnalysis.common.APIRequest;
import com.dcjet.logAnalysis.common.BaseBlo;
import com.dcjet.logAnalysis.common.FrontendGridResult;
import com.dcjet.logAnalysis.common.IConnectionHelper;
import com.dcjet.logAnalysis.common.ImpalaHelper;
import com.dcjet.logAnalysis.common.PhoenixHelper;
import com.dcjet.logAnalysis.entity.LogEntity;
import com.dcjet.logAnalysis.entity.LogSearchEntity;
import com.dcjet.logAnalysis.impala.ImpalaBlo;
import com.dcjet.logAnalysis.phoenix.PhoenixBlo;
import com.dcjet.logAnalysis.utils.DateTimeUtil;
import com.dcjet.logAnalysis.utils.ExcelExtendUtil;
import com.dcjet.logAnalysis.utils.ExcelPostfixEnum;
import com.dcjet.logAnalysis.utils.PubUtil;

@Controller
@RequestMapping(value = "logHome")
public class LogAnalysisController {
	
	@ResponseBody
	@RequestMapping(value = "/getList")
	public FrontendGridResult getList(LogSearchEntity searchEntity) throws IOException {
		BaseBlo blo = CreateBaseBlo();
		FrontendGridResult result = blo.getList(searchEntity);
	 
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value ="/get")
	public APIReponse get(String oid) {
		BaseBlo blo = CreateBaseBlo();
		
		APIReponse reponse = blo.get(oid);
		return reponse;
	}
	
	@RequestMapping("/export")
	public void exportExcel(HttpServletResponse response, LogSearchEntity searchEntity) throws IOException {
		BaseBlo blo = CreateBaseBlo();
		
		//获取导出数据
		List<LogEntity> lst = blo.getDataSourceExport(searchEntity);
		//导出数据项
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("corpcode", "企业编码");
		fieldMap.put("username", "用户名");
		fieldMap.put("pagename", "模块名称");
		fieldMap.put("functionname", "操作类型");
		fieldMap.put("ip", "IP地址");
		fieldMap.put("createdate", "操作时间");
		fieldMap.put("message", "日志详情");
		
        //Execl中sheet名称
		String sheetName = "日志记录";
		
		try{
			String fileName = DateTimeUtil.convertDateToString(
					DateTimeUtil.D14_DATETIME_PATTERN, new Date())
					+ PubUtil.getFixLengthRandomString(5);
			//调用导出公用方法
			ExcelExtendUtil.listToExcel(lst, fieldMap,ExcelPostfixEnum.Office2007Xlsx, sheetName, 60000, fileName, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private BaseBlo CreateBaseBlo(){
		//return new ImpalaBlo();
		return new PhoenixBlo();
	}

}
