package com.dcjet.logAnalysis.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PhoenixHelper implements IConnectionHelper  {
	public static String jdbc_driver = "";
	public static String connection_url = "";
	
	static{
		try {
			Properties prop = PropertiesLoaderUtils.loadAllProperties("/phoenix.properties");
			jdbc_driver = prop.getProperty("phoenix.driverClassName");
			connection_url = prop.getProperty("phoenix.connectionUrl");
			
			if(jdbc_driver == null || jdbc_driver.equals("")){
				jdbc_driver="org.apache.phoenix.jdbc.PhoenixDriver";
			}
			if(connection_url == null || connection_url.equals("")){
				connection_url="jdbc:phoenix:hadoop1,hadoop2,hadoop3:2181";
			}
			
			jdbc_driver = jdbc_driver.trim();
			connection_url = connection_url.trim();
            
			System.out.println("读取/resources/phoenix.properties＝＝＝＝＝＝＝＝＝＝＝＝＝");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public Connection getConnection(){
    	Connection con = null;
    	try{
    		Class.forName(jdbc_driver);
        	con = DriverManager.getConnection(connection_url,"","");
    	}
    	catch(Exception e){
    		 e.printStackTrace();
    	}
    	return con;
    }
    /**
     * �ر�����Դ
     * 
     * @throws SQLException
     */
    public void closeConnection(Connection conn) throws SQLException {
        if (null != conn) {
            conn.close();
        }
    }
}
