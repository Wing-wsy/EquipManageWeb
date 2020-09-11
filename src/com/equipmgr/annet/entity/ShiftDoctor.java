package com.equipmgr.annet.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ShiftDoctor {
	private int handoverID; 
	private int type; 
	private int userType; 

	//private Date handoverDatetime; 

	private String deviceDescribe; 
	private String deptDesctibe;
	private String handoverUser; 
	private String remarks;
	
	private Timestamp handoverDatetime;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public int getHandoverID() {
		return handoverID;
	}
	public void setHandoverID(int handoverID) {
		this.handoverID = handoverID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public Timestamp getHandoverDatetime() {
		return handoverDatetime;
	}
	
	public String getHandoverDatetime_string() {
		return this.handoverDatetime == null? "": simpleDateFormat.format(this.handoverDatetime);
	}

	public void setHandoverDatetime(Timestamp handoverDatetime) {
		this.handoverDatetime = handoverDatetime;
	}
	public String getDeviceDescribe() {
		return deviceDescribe;
	}
	public void setDeviceDescribe(String deviceDescribe) {
		this.deviceDescribe = deviceDescribe;
	}
	public String getDeptDesctibe() {
		return deptDesctibe;
	}
	public void setDeptDesctibe(String deptDesctibe) {
		this.deptDesctibe = deptDesctibe;
	}
	public String getHandoverUser() {
		return handoverUser;
	}
	public void setHandoverUser(String handoverUser) {
		this.handoverUser = handoverUser;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	} 
	
	
}
