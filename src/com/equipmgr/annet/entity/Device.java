package com.equipmgr.annet.entity;

import java.sql.Date;

public class Device {

	private String modality;
	private String device;
	private String equNo;
	private String name;
	private String status;
	private String madeIn;
	private String makeBy;
	private String bryPrice;
	private String useUnit;
	private String responseble;
	private String reportNo;
	private String factoryTel;
	private String contactPerson;
	private String contactPhone;
	private String mantanPrice;
	private Date mantanStartDate;
	private Date mantanEndDate;
	private Date installDate;
	private Date useStartDate;

	public Date getMantanStartDate() {
		return mantanStartDate;
	}
	public void setMantanStartDate(Date mantanStartDate) {
		this.mantanStartDate = mantanStartDate;
	}
	public Date getMantanEndDate() {
		return mantanEndDate;
	}
	public void setMantanEndDate(Date mantanEndDate) {
		this.mantanEndDate = mantanEndDate;
	}
	public Date getInstallDate() {
		return installDate;
	}
	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}
	public Date getUseStartDate() {
		return useStartDate;
	}
	public void setUseStartDate(Date useStartDate) {
		this.useStartDate = useStartDate;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getEquNo() {
		return equNo;
	}
	public void setEquNo(String equNo) {
		this.equNo = equNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMadeIn() {
		return madeIn;
	}
	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}
	public String getMakeBy() {
		return makeBy;
	}
	public void setMakeBy(String makeBy) {
		this.makeBy = makeBy;
	}
	public String getBryPrice() {
		return bryPrice;
	}
	public void setBryPrice(String bryPrice) {
		this.bryPrice = bryPrice;
	}
	public String getUseUnit() {
		return useUnit;
	}
	public void setUseUnit(String useUnit) {
		this.useUnit = useUnit;
	}
	public String getResponseble() {
		return responseble;
	}
	public void setResponseble(String responseble) {
		this.responseble = responseble;
	}
	
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getFactoryTel() {
		return factoryTel;
	}
	public void setFactoryTel(String factoryTel) {
		this.factoryTel = factoryTel;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getMantanPrice() {
		return mantanPrice;
	}
	public void setMantanPrice(String mantanPrice) {
		this.mantanPrice = mantanPrice;
	}
	

}
