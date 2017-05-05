package com.taskify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
	public User getUser(Connection con,String username,String password) throws Exception{
		User loginUser=new User();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SELECT * FROM user where Email='"+username+"' and Password='"+password+"'");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				loginUser.setUserId(rs.getInt(1));
				loginUser.setUsername(username);
				return loginUser;
			}
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
		return null;	
	}
	
	public Boolean addUser(Connection con,String firstName,String lastName,String username,String password) throws Exception{
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("insert into user values(default,?,?,?,?, NULL)");
			ps.setString(1, lastName);
			ps.setString(2, firstName);
			ps.setString(3, username);
			ps.setString(4, password);
			int count = ps.executeUpdate();
			if(count==1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
	}
	
	public Boolean updateToken(Connection con,int userId,String token) throws Exception{
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update user set Token = ? where ID = ?");
			ps.setString(1, token);
			ps.setInt(2, userId);
			int count = ps.executeUpdate();
			if(count==1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
	}
	
	public Boolean verifyToken(Connection con,String token) throws Exception{
		PreparedStatement ps = null;
		try {
			
			ps = con.prepareStatement("select count(*) from user where Token = ?");
			ps.setString(1, token);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if(count==1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
	}
}
