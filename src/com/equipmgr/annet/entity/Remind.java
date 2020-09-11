package com.equipmgr.annet.entity;

import java.sql.Date;

public class Remind {
	private int id;
	private String Modality;
	private String Device;
	private String msg;
	private Date StartDate;
	private Date EndDate;
	private String Status;
	private String type;
	private String circle;
	private String ContinueTime;
	private Date FirstRemaind;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevice() {
		return Device;
	}
	public void setDevice(String device) {
		Device = device;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getStartDate() {
		return StartDate;
	}
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	public Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}
	public String getModality() {
		return Modality;
	}
	public void setModality(String modality) {
		Modality = modality;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getContinueTime() {
		return ContinueTime;
	}
	public void setContinueTime(String continueTime) {
		ContinueTime = continueTime;
	}
	public Date getFirstRemaind() {
		return FirstRemaind;
	}
	public void setFirstRemaind(Date firstRemaind) {
		FirstRemaind = firstRemaind;
	}
	
}
