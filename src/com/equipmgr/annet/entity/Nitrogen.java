package com.equipmgr.annet.entity;

import java.sql.Date;

public class Nitrogen {
	private int ID;
	private String Modality;
	private String Device;
	private String amount;
	private Date RecordDate;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getModality() {
		return Modality;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Date getRecordDate() {
		return RecordDate;
	}
	public void setRecordDate(Date recordDate) {
		RecordDate = recordDate;
	}
}
