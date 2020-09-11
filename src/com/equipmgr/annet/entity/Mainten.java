package com.equipmgr.annet.entity;

import java.sql.Date;

public class Mainten {

	private int ID;
	private String MaintenContent;
	private String MaintenPerson;
	private Date MaintenTime;
	private String Modality;
	private String Device;
	private String ConfirmPerson;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getMaintenContent() {
		return MaintenContent;
	}
	public void setMaintenContent(String maintenContent) {
		MaintenContent = maintenContent;
	}
	public String getMaintenPerson() {
		return MaintenPerson;
	}
	public void setMaintenPerson(String maintenPerson) {
		MaintenPerson = maintenPerson;
	}
	public String getModality() {
		return Modality;
	}
	public Date getMaintenTime() {
		return MaintenTime;
	}
	public void setMaintenTime(Date maintenTime) {
		MaintenTime = maintenTime;
	}
	public void setModality(String modality) {
		Modality = modality;
	}
	public String getDevice() {
		return Device;
	}
	public void setDevice(String device) {
		Device = device;
	}
	public String getConfirmPerson() {
		return ConfirmPerson;
	}
	public void setConfirmPerson(String confirmPerson) {
		ConfirmPerson = confirmPerson;
	}
}
