package com.equipmgr.annet.entity;

public class User {
	private String Name;
	private String sLevel;
	private String UserName;
	private String DeptName;
	private String Modality;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getsLevel() {
		return sLevel;
	}
	public void setsLevel(String sLevel) {
		this.sLevel = sLevel;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	public String getModality() {
		return Modality;
	}
	public void setModality(String modality) {
		Modality = modality;
	}
	
}
