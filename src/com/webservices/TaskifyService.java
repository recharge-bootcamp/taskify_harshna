package com.webservices;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.taskify.dao.TaskCount;
import com.taskify.dao.Tasks;
import com.taskify.dao.User;
import com.taskify.login.LoginManager;
import com.taskify.tasks.TaskManager;

@Path("/WebService")
public class TaskifyService {
	User loggedInUser = null;
 
@POST
 @Path("/login")
 @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
 public Response login(@FormParam("username") String username,
 @FormParam("password") String password) {
	String secret = username + "qwertymnihtilmt";
	byte[] bytesEncoded = Base64.getEncoder().encode(secret.getBytes());
	String token = new String(bytesEncoded);
	User loggeduser = getUser(username, password);
	if(loggeduser != null){
		updateToken(loggeduser.getUserId(), token);
		return Response.status(200).entity(loggeduser).header(HttpHeaders.AUTHORIZATION, token).build();
	}else{
		return Response.status(401).entity("{\"Status\":\"Invalid User\"}").build();
	}
	
}

@POST
@Path("/addUser")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response addNewUser(@FormParam("lastname") String lastName,
		@FormParam("firstname") String firstName,@FormParam("username") String username,
@FormParam("password") String password) {
	String result = "{\"result\":\""+addUser(firstName, lastName, username, password).toString()+"\"}";
	return Response.status(200).entity(result).build();
}

@GET
@Path("/tasks/{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response getTasks(@Context HttpHeaders header,@PathParam("id") int userId) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token))
		return Response.status(200).entity(getTasks(userId)).build();
	else
		return Response.status(401).build();
		
}

@GET
@Path("/count/{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response getTaskCount(@Context HttpHeaders header,@PathParam("id") int userId) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token))
		return Response.status(200).entity(getCount(userId)).build();
	else
		return Response.status(401).build();
}

@GET
@Path("/updateDate/{id}/{date}")
@Produces(MediaType.APPLICATION_JSON)
public Response updateDueDate(@Context HttpHeaders header,@PathParam("id") int taskId,@PathParam("date") Date dueDate) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token)){
		String result = "{\"result\":\""+updateDate(taskId, dueDate).toString()+"\"}";
		return Response.status(200).entity(result).build();
	}
	else
		return Response.status(401).build();
}

@GET
@Path("/updateStatus/{id}/{status}")
@Produces(MediaType.APPLICATION_JSON)
public Response updateStatus(@Context HttpHeaders header,@PathParam("id") int taskId,@PathParam("status") int StatusId) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token)){
		String result = "{\"result\":\""+updateStatus(taskId, StatusId).toString()+"\"}";
		return Response.status(200).entity(result).build();
	}
	else
		return Response.status(401).build();
}

@GET
@Path("/getTask/{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response getTask(@Context HttpHeaders header,@PathParam("id") int taskId) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token)){
		return Response.status(200).entity(getTaskDetails(taskId)).build();
	}
	else
		return Response.status(401).build();
}

@GET
@Path("/logout/{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response logout(@Context HttpHeaders header,@PathParam("id") int userId) {
	String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
	if(verifyToken(token)){
		updateToken(userId, null);
		return Response.status(200).entity("{\"Status\":\"Logged Out\"}").build();
	}
	else
		return Response.status(401).build();
}

@POST
@Path("/addTask")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response addTask(@Context HttpHeaders header,@FormParam("id") String userID,@FormParam("content") String content,
		 @FormParam("duedate") String dueDate) {
	java.util.Date utilDate;
	try {
		String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		if(verifyToken(token)){
			utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			String result = "{\"result\":\""+addTask(Integer.parseInt(userID), content, sqlDate).toString()+"\"}";
			return Response.status(200).entity(result).build();
		}
		else
			return Response.status(401).build();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return null;
}

@POST
@Path("/updateTask")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response updateTask(@Context HttpHeaders header,@FormParam("id") String taskid,@FormParam("userid") String userID,
		 @FormParam("statusid") String statusid,@FormParam("content") String content,
		 @FormParam("duedate") String dueDate) {
	java.util.Date utilDate;
	try {
		String token = header.getRequestHeader(HttpHeaders.AUTHORIZATION).get(0);
		if(verifyToken(token)){
			utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			String result = "{\"result\":\""+updateContent(Integer.parseInt(taskid), 
					Integer.parseInt(userID), Integer.parseInt(statusid), sqlDate, content)+"\"}";
			return Response.status(200).entity(result).build();
		}
		else
			return Response.status(401).build();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return null;
}

public Tasks[] getTasks(int userId){
	TaskManager task = new TaskManager();
	try {
		Tasks[] tasksForUser = task.getTasksForUser(userId);
		task.getTaskCount(userId);
		if(tasksForUser==null){
			return null;
		}
		else{
			return tasksForUser;
		}	
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public TaskCount[] getCount(int userId){
	TaskManager task = new TaskManager();
	try {
		TaskCount[] count = task.getTaskCount(userId);
		return count;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean updateDate(int taskId,Date dueDate){
	TaskManager task = new TaskManager();
	try {
		return task.updateDueDate(taskId, dueDate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean updateContent(int taskId, int userId,int statusId,Date dueDate,String content){
	TaskManager task = new TaskManager();
	try {
		return task.updateContent(taskId, userId, statusId, dueDate, content);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }


public Boolean updateStatus(int taskId,int StatusId){
	TaskManager task = new TaskManager();
	try {
		return task.updateStatus(taskId, StatusId);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }
 
public User getUser(String username,String password){
	LoginManager login = new LoginManager();
	try {
		User loggedUser = login.getUser(username, password);
		if(loggedUser==null){
			return null;
		}
		else{
			loggedInUser = loggedUser;
			return loggedUser;
		}	
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean addTask(int userId,String content,Date dueDate){
	TaskManager task = new TaskManager();
	try {
		return task.addTask(userId, content, dueDate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean addUser(String firstName, String lastName, String username, String password){
	LoginManager login = new LoginManager();
	try {
		return login.addUser(firstName, lastName, username, password);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean updateToken(int userId, String token){
	LoginManager login = new LoginManager();
	try {
		return login.updateToken(userId, token);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Boolean verifyToken(String token){
	LoginManager login = new LoginManager();
	try {
		return login.verifyToken(token);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

public Tasks getTaskDetails(int taskId){
	TaskManager task = new TaskManager();
	try {
		return task.getTaskDetails(taskId);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
 }

}