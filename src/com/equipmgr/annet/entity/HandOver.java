package com.equipmgr.annet.entity;
import java.util.Date;

import com.google.gson.JsonArray;

public class HandOver {
	private int errorFlag;
	private Date HandoverDate;
	private String UserName;
	private JsonArray ja;
	
	public JsonArray getJa() {
		return ja;
	}
	public void setJa(JsonArray ja) {
		this.ja = ja;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public int getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(int errorFlag) {
		this.errorFlag = errorFlag;
	}
	public Date getHandoverDate() {
		return HandoverDate;
	}
	public void setHandoverDate(Date handoverDate) {
		HandoverDate = handoverDate;
	}

}
