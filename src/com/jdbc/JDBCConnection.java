package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
	public Connection getMYSQLConnection() throws Exception{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/taskify","root","SqlRoot3!"); 
			return con;

		} catch (SQLException e){
			throw e;
		}
		catch (Exception e){
			throw e;
		}
	}
}
