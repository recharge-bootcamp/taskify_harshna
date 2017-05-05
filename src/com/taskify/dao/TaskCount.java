package com.taskify.dao;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskCount {
	int count;
	String status;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
