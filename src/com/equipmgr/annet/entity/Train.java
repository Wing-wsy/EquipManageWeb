package com.equipmgr.annet.entity;

import java.sql.Date;

public class Train {
	private int id;
	private String personName;
	private String trainPlace;
	private String trainContent;
	private Date trainDate;
	private String DeptName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getTrainPlace() {
		return trainPlace;
	}
	public void setTrainPlace(String trainPlace) {
		this.trainPlace = trainPlace;
	}
	public String getTrainContent() {
		return trainContent;
	}
	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
	}
	public Date getTrainDate() {
		return trainDate;
	}
	public void setTrainDate(Date trainDate) {
		this.trainDate = trainDate;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	
	
}
