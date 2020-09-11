package com.equipmgr.annet.entity;

import java.sql.Date;

public class Detection {
	public static int TYPE1 = 1; // �ʿؼ��
	public static int TYPE2 = 2; // �������
	public static int TYPE3 = 3; // �������

	private int ID;
	private int DetectionType;
	private String Device;
	private String DetectionUnit;
	private String DetectionResult;
	private Date DetectionDate;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getDetectionType() {
		return DetectionType;
	}
	public void setDetectionType(int detectionType) {
		DetectionType = detectionType;
	}
	public String getDevice() {
		return Device;
	}
	public void setDevice(String device) {
		Device = device;
	}
	public String getDetectionUnit() {
		return DetectionUnit;
	}
	public void setDetectionUnit(String detectionUnit) {
		DetectionUnit = detectionUnit;
	}
	public String getDetectionResult() {
		return DetectionResult;
	}
	public void setDetectionResult(String detectionResult) {
		DetectionResult = detectionResult;
	}
	public Date getDetectionDate() {
		return DetectionDate;
	}
	public void setDetectionDate(Date detectionDate) {
		DetectionDate = detectionDate;
	}
	
}
