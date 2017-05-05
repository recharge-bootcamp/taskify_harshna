package com.taskify.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TasksDAO {
	public Tasks[] getTasks(Connection con, int userId ) throws Exception{
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SELECT count(*) FROM task where UserId="+userId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Tasks[] tasks=new Tasks[rs.getInt(1)];
			if(ps!=null)
				ps.close();
			ps = con.prepareStatement("select t.Id,t.UserId,t.StatusId,s.Status,t.DueDate,t.Content "
					+ "from task t,status s where s.Id = t.StatusId and UserId="+userId);
			rs = ps.executeQuery();
			int i=0;
			while (rs.next()) {
				Tasks task=new Tasks();
				task.setTaskId(rs.getInt("Id"));
				task.setUserId(userId);
				task.setStatusId(rs.getInt("StatusId"));
				task.setStatus(rs.getString("Status"));
				task.setDueDate(rs.getDate("DueDate"));
				task.setContent(rs.getString("Content"));
				tasks[i] = task;
				i++;
			}
			return tasks;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
	}
	
	public Tasks getTaskDetails(Connection con, int taskId ) throws Exception{
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("select task.Id,task.UserId,task.StatusId,task.DueDate,task.Content,status.Status "
					+ "from task,status where task.Id=? and status.Id = task.StatusId");
			ps.setInt(1, taskId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Tasks task = new Tasks();
			task.setTaskId(taskId);
			task.setUserId(rs.getInt("UserId"));
			task.setStatusId(rs.getInt("StatusId"));
			task.setStatus(rs.getString("Status"));
			task.setDueDate(rs.getDate("DueDate"));
			task.setContent(rs.getString("Content"));
			return task;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
}
	
public TaskCount[] getTaskCount(Connection con, int userId ) throws Exception{
		PreparedStatement ps = null;
		try {
			updateOverDueCount(con,userId);
			TaskCount[] count = new TaskCount[3];
			ps = con.prepareStatement("select count(Id) from task where UserId=? and StatusId=(select id from `status` where STATUS=?)");
			ps.setInt(1, userId);
			ps.setString(2, "Completed");
			ResultSet rs = ps.executeQuery();
			rs.next();
			TaskCount temp=new TaskCount();
			temp.setCount(rs.getInt(1));
			temp.setStatus("Completed");
			count[0] = temp;
			ps.setString(2, "Pending");
			rs = ps.executeQuery();
			rs.next();
			TaskCount temp1=new TaskCount();
			temp1.setCount(rs.getInt(1));
			temp1.setStatus("Pending");
			count[1] = temp1;
			ps.setString(2, "Overdue");
			rs = ps.executeQuery();
			rs.next();
			TaskCount temp2=new TaskCount();
			temp2.setCount(rs.getInt(1));
			temp2.setStatus("Overdue");
			count[2] = temp2;
			return count;
		} catch (Exception e) {
			throw e;
		}
		finally{
			if(ps!=null)
				ps.close();
		}
	}

public Boolean updateDueDate(Connection con, int taskId, Date duedate ) throws Exception{
	PreparedStatement ps = null;
	try {
		ps = con.prepareStatement("update task set DueDate =? where Id=?");
		ps.setInt(2, taskId);
		ps.setDate(1, duedate);
		int row = ps.executeUpdate();
		if(row == 1)
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

public Boolean updateStatus(Connection con, int taskId, int status ) throws Exception{
	PreparedStatement ps = null;
	try {
		ps = con.prepareStatement("update task set StatusId =? where Id=?");
		ps.setInt(2, taskId);
		ps.setInt(1, status);
		int row = ps.executeUpdate();
		if(row == 1)
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

public Boolean updateContent(Connection con, int taskId, int userId,int statusId,Date dueDate,String content) throws Exception{
	PreparedStatement ps = null;
	try {
		ps = con.prepareStatement("update task set StatusId=?,Content =?,DueDate=? where Id=?");
		ps.setInt(1, statusId);
		ps.setString(2, content);
		ps.setDate(3, dueDate);
		ps.setInt(4, taskId);
		int row = ps.executeUpdate();
		if(row == 1)
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

public Boolean addTask(Connection con, int userID, String content, Date dueDate) throws Exception{
	PreparedStatement ps =null;
	try {
		ps = con.prepareStatement("insert into task values(default,?,1,?,?)");
		ps.setInt(1, userID);
		ps.setString(3, content);
		ps.setDate(2, dueDate);
		int row = ps.executeUpdate();
		if(row == 1)
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

public void updateOverDueCount(Connection con, int userId) throws Exception{
	PreparedStatement ps = null;
	try {
		ps = con.prepareStatement("update task set StatusId=3 where UserId=? and DATE(DueDate) < DATE(NOW());");
		ps.setInt(1, userId);
		ps.executeUpdate();
	} catch (Exception e) {
		throw e;
	}
	finally{
		if(ps!=null)
			ps.close();
	}
}

}
