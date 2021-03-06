/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.dcjet.logAnalysis.phoenix;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dcjet.logAnalysis.common.APIReponse;
import com.dcjet.logAnalysis.common.APIRequest;
import com.dcjet.logAnalysis.common.BaseBlo;
import com.dcjet.logAnalysis.common.FrontendGridResult;
import com.dcjet.logAnalysis.common.IConnectionHelper;
import com.dcjet.logAnalysis.common.PhoenixHelper;
import com.dcjet.logAnalysis.entity.LogEntity;
import com.dcjet.logAnalysis.entity.LogSearchEntity;

public class PhoenixBlo extends BaseBlo{
	
	public FrontendGridResult getList(LogSearchEntity searchEntity) throws IOException{
		String quickSearch = searchEntity.getQuick();
		
		List<LogEntity> lst = getDataSource(searchEntity);
		
		int total = 1000;
		if("0".equals(quickSearch)){
			total = getTotalCount(searchEntity);
		}
		FrontendGridResult result = new FrontendGridResult(total, lst);
	 
		return result;
	}
	
	public APIReponse get(String oid) {
		APIReponse reponse = new APIReponse();
		LogEntity entity = getById(oid);
		reponse.setSuccess(true);
		reponse.setResult(entity);
		return reponse;
	}
	
	private int getTotalCount(LogSearchEntity searchEntity) {
		String strSql = "select count(oid) from t_bd_ztyth_log where 1=1 " + getSearchCondition(searchEntity);
		
		System.out.println(strSql);
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int total = 0;
		IConnectionHelper iConnHelper = null;
		try {
			iConnHelper = createConnectionHelper();
			conn = iConnHelper.getConnection();
			ps = conn.prepareStatement(strSql);
			rs = ps.executeQuery();
			if (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn) {
					iConnHelper.closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return total;
	}

	private List<LogEntity> getDataSource(LogSearchEntity searchEntity) {
		int pageIndex = searchEntity.getPage();
		int pageSize = searchEntity.getRows();

		String strSql = "select * from t_bd_ztyth_log where 1=1 " + getSearchCondition(searchEntity) + " order by oid asc "
				+ "LIMIT " + pageSize + " OFFSET " + ((pageIndex - 1) * pageSize);

		System.out.println(strSql);
		
		List<LogEntity> list = new LinkedList<LogEntity>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int total = 0;
		IConnectionHelper iConnHelper = null;
		try {
			iConnHelper = createConnectionHelper();
			conn = iConnHelper.getConnection();
			ps = conn.prepareStatement(strSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				LogEntity entity = new LogEntity();
				entity.setOid(rs.getString(1));
				entity.setCorpcode(rs.getString(9));
				entity.setUsername(rs.getString(10));
				entity.setPagename(rs.getString(5));
				entity.setFunctionname(rs.getString(7));
				entity.setIp(rs.getString(8));
				entity.setCreatedate(rs.getString(3));
				entity.setMessage(rs.getString(11));
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn) {
					iConnHelper.closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}
	
	public List<LogEntity> getDataSourceExport(LogSearchEntity searchEntity) {
		int pageIndex = searchEntity.getPage();
		int pageSize = searchEntity.getRows();

		String strSql = "select * from t_bd_ztyth_log where 1=1 " + getSearchCondition(searchEntity) + " order by oid asc ";

		System.out.println(strSql);
		
		List<LogEntity> list = new LinkedList<LogEntity>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int total = 0;
		IConnectionHelper iConnHelper = null;
		try {
			iConnHelper = createConnectionHelper();
			conn = iConnHelper.getConnection();
			ps = conn.prepareStatement(strSql);
			rs = ps.executeQuery();
			while (rs.next()) {
				LogEntity entity = new LogEntity();
				entity.setOid(rs.getString(1));
				entity.setCorpcode(rs.getString(9));
				entity.setUsername(rs.getString(10));
				entity.setPagename(rs.getString(5));
				entity.setFunctionname(rs.getString(7));
				entity.setIp(rs.getString(8));
				entity.setCreatedate(rs.getString(3));
				entity.setMessage(rs.getString(11));
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn) {
					iConnHelper.closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return list;
	}
	
	private LogEntity getById(String oid){
		String strSql = "select OID,CORPCODE,USERNAME,PAGENAME,FUNCTIONNAME,IP,CREATEDATE,MESSAGE from T_BD_ZTYTH_LOG where OID='"+oid+"'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LogEntity entity = null;
		IConnectionHelper iConnHelper = null;
		try {
			iConnHelper = createConnectionHelper();
			conn = iConnHelper.getConnection();
			ps = conn.prepareStatement(strSql);
			rs = ps.executeQuery();
			if (rs.next()) {
				entity = new LogEntity();
				entity.setOid(rs.getString(1));
				entity.setCorpcode(rs.getString(2));
				entity.setUsername(rs.getString(3));
				entity.setPagename(rs.getString(4));
				entity.setFunctionname(rs.getString(5));
				entity.setIp(rs.getString(6));
				entity.setCreatedate(rs.getString(7));
				entity.setMessage(rs.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != rs) {
					rs.close();
				}
				if (null != ps) {
					ps.close();
				}
				if (null != conn) {
					iConnHelper.closeConnection(conn);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}
	private IConnectionHelper createConnectionHelper(){
		return new PhoenixHelper();
	}
}
