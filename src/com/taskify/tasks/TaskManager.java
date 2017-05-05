package com.taskify.tasks;

import java.sql.Connection;
import java.sql.Date;

import com.jdbc.JDBCConnection;
import com.taskify.dao.TaskCount;
import com.taskify.dao.Tasks;
import com.taskify.dao.TasksDAO;

public class TaskManager {
	public Tasks[] getTasksForUser(int userId)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			Tasks[] tasks= taskdao.getTasks(connection, userId);
			return tasks;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public TaskCount[] getTaskCount(int userId)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			TaskCount[] tasks= taskdao.getTaskCount(connection, userId);
			return tasks;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean updateDueDate(int taskId,Date dueDate)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			return taskdao.updateDueDate(connection, taskId, dueDate);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean updateStatus(int taskId,int statusId)throws Exception {
		Connection connection =null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			return taskdao.updateStatus(connection, taskId, statusId);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean updateContent(int taskId, int userId,int statusId,Date dueDate,String content)throws Exception {
		Connection connection =null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			return taskdao.updateContent(connection, taskId, userId, statusId, dueDate, content);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Boolean addTask(int userID,String content,Date dueDate)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			return taskdao.addTask(connection, userID, content, dueDate);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
	
	public Tasks getTaskDetails(int taskId)throws Exception {
		Connection connection = null;
		try {
			JDBCConnection database= new JDBCConnection();
			connection = database.getMYSQLConnection();
			TasksDAO taskdao= new TasksDAO();
			return taskdao.getTaskDetails(connection, taskId);
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(connection!=null)
			connection.close();
		}
	}
}
