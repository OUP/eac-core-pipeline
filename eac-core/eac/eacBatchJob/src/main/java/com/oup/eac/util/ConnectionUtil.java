package com.oup.eac.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class ConnectionUtil {
	
	private static final Logger LOGGER = Logger.getLogger(ConnectionUtil.class);
	
	public static Connection getConnection()
	{	
		String erConnStr = BatchJobProperties.ER_CONN_STR;
		//LOGGER.info(erConnStr);
		Connection erConn = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");   
			erConn = DriverManager.getConnection(erConnStr);
			erConn.setAutoCommit(false);
		} catch(SQLException ex) {
			LOGGER.error(ex.getMessage(),ex);
		} catch (ClassNotFoundException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return erConn;
	}
}
