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
		
		//��ȡ��������
		List<LogEntity> lst = blo.getDataSourceExport(searchEntity);
		//����������
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("corpcode", "��ҵ����");
		fieldMap.put("username", "�û���");
		fieldMap.put("pagename", "ģ������");
		fieldMap.put("functionname", "��������");
		fieldMap.put("ip", "IP��ַ");
		fieldMap.put("createdate", "����ʱ��");
		fieldMap.put("message", "��־����");
		
        //Execl��sheet����
		String sheetName = "��־��¼";
		
		try{
			String fileName = DateTimeUtil.convertDateToString(
					DateTimeUtil.D14_DATETIME_PATTERN, new Date())
					+ PubUtil.getFixLengthRandomString(5);
			//���õ������÷���
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
