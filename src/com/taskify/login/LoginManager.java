package com.taskify.login;

import java.sql.Connection;

import com.jdbc.JDBCConnection;
import com.taskify.dao.LoginDAO;
import com.taskify.dao.User;

public class LoginManager {
	public User getUser(String username,String password)throws Exception {
		User user;
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			LoginDAO logindao= new LoginDAO();
			user= logindao.getUser(connection,username,password);
			return user;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean addUser(String firstName,String lastName,String username,String password)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			LoginDAO logindao= new LoginDAO();
			return logindao.addUser(connection, firstName, lastName, username, password);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean updateToken(int userId,String token)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			LoginDAO logindao= new LoginDAO();
			return logindao.updateToken(connection, userId, token);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean verifyToken(String token)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			LoginDAO logindao= new LoginDAO();
			return logindao.verifyToken(connection, token);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
}
