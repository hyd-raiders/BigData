package com.dcjet.logAnalysis.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ImpalaHelper implements IConnectionHelper {
	static String JDBC_DRIVER = "com.cloudera.impala.jdbc41.Driver";
    static String CONNECTION_URL = "jdbc:impala://192.168.112.191:21050/default;AuthMech=0";
    
    public Connection getConnection(){
    	Connection con = null;
    	try{
    		Class.forName(JDBC_DRIVER);
        	con = DriverManager.getConnection(CONNECTION_URL);
    	}
    	catch(Exception e){
    		 e.printStackTrace();
    	}
    	return con;
    }
    /**
     * 关闭数据源
     * @throws SQLException 
     */
    public void closeConnection(Connection conn) throws SQLException {
        if (null != conn) {
            conn.close();
        }
    }
}
