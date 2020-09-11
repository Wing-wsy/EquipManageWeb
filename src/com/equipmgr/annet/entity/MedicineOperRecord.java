package com.equipmgr.annet.entity;

import java.util.Date;

public class MedicineOperRecord {

	private int ManageID;
	private String MedicineBatch;
	private String MedicineCode;
	private String MedicineName;
	private String RecordName;
	private Date OperDate;
	private int OperNum;
	private int RecordType;
	public int getManageID() {
		return ManageID;
	}
	public void setManageID(int manageID) {
		ManageID = manageID;
	}
	public String getMedicineBatch() {
		return MedicineBatch;
	}
	public void setMedicineBatch(String medicineBatch) {
		MedicineBatch = medicineBatch;
	}
	public String getMedicineCode() {
		return MedicineCode;
	}
	public void setMedicineCode(String medicineCode) {
		MedicineCode = medicineCode;
	}
	public String getMedicineName() {
		return MedicineName;
	}
	public void setMedicineName(String medicineName) {
		MedicineName = medicineName;
	}
	public String getRecordName() {
		return RecordName;
	}
	public void setRecordName(String recordName) {
		RecordName = recordName;
	}
	public Date getOperDate() {
		return OperDate;
	}
	public void setOperDate(Date operDate) {
		OperDate = operDate;
	}
	public int getOperNum() {
		return OperNum;
	}
	public void setOperNum(int operNum) {
		OperNum = operNum;
	}
	public int getRecordType() {
		return RecordType;
	}
	public void setRecordType(int recordType) {
		RecordType = recordType;
	}
	
}