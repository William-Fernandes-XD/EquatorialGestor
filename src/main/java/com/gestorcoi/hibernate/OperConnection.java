package com.gestorcoi.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.gestorcoi.constantes.OperDatabase;

public class OperConnection {

public Connection getConnection() throws SQLException {
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return DriverManager.getConnection(OperDatabase.getURL(),
				OperDatabase.getUSER(),
				OperDatabase.getPASSWORD());
	}
}
