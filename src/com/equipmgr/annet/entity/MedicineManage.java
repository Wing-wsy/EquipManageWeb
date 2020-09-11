package com.equipmgr.annet.entity;

import java.util.Date;

public class MedicineManage {

	private int ManageID;
	private String OEMCode;
	private String OEMName;
	private String MedicineBatch;
	private String MedicineCode;
	private String MedicineName;
	private String MedicineUnit;
	private Date MedicineDeadline;
	private int InventoryNum;
	private int ThresholdNum;
	private int MedicineType;
	public int getManageID() {
		return ManageID;
	}
	public void setManageID(int manageID) {
		ManageID = manageID;
	}
	public String getOEMCode() {
		return OEMCode;
	}
	public void setOEMCode(String oEMCode) {
		OEMCode = oEMCode;
	}
	public String getOEMName() {
		return OEMName;
	}
	public void setOEMName(String oEMName) {
		OEMName = oEMName;
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
	public String getMedicineUnit() {
		return MedicineUnit;
	}
	public void setMedicineUnit(String medicineUnit) {
		MedicineUnit = medicineUnit;
	}
	public Date getMedicineDeadline() {
		return MedicineDeadline;
	}
	public void setMedicineDeadline(Date sqlDate) {
		MedicineDeadline = sqlDate;
	}
	public int getInventoryNum() {
		return InventoryNum;
	}
	public void setInventoryNum(int inventoryNum) {
		InventoryNum = inventoryNum;
	}
	public int getThresholdNum() {
		return ThresholdNum;
	}
	public void setThresholdNum(int thresholdNum) {
		ThresholdNum = thresholdNum;
	}
	public int getMedicineType() {
		return MedicineType;
	}
	public void setMedicineType(int medicineType) {
		MedicineType = medicineType;
	}
	
	
}
