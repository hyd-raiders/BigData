package com.dcjet.logAnalysis.common;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionHelper {
	Connection getConnection();
	void closeConnection(Connection conn) throws SQLException;
}
