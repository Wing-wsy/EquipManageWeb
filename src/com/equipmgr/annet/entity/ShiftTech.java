package com.equipmgr.annet.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ShiftTech {
	private int handoverID;
	private int forwardID;
	private int fitFlag;
	private int handComfirFlag;
	private int period;
	private int errorFlag;
	
	private String fitComment;
	private String device;
	private String spare;
	private String etcSafe;
	private String handoverComment;
	private String name;
	private String errorComment;
	
	//private Date handoverDate;
	private Timestamp handoverDate;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public int getHandoverID() {
		return handoverID;
	}

	public void setHandoverID(int handoverID) {
		this.handoverID = handoverID;
	}

	public int getForwardID() {
		return forwardID;
	}

	public void setForwardID(int forwardID) {
		this.forwardID = forwardID;
	}

	public int getFitFlag() {
		return fitFlag;
	}

	public void setFitFlag(int fitFlag) {
		this.fitFlag = fitFlag;
	}

	public int getHandComfirFlag() {
		return handComfirFlag;
	}

	public void setHandComfirFlag(int handComfirFlag) {
		this.handComfirFlag = handComfirFlag;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(int errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getFitComment() {
		return fitComment;
	}

	public void setFitComment(String fitComment) {
		this.fitComment = fitComment;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}

	public String getEtcSafe() {
		return etcSafe;
	}

	public void setEtcSafe(String etcSafe) {
		this.etcSafe = etcSafe;
	}

	public String getHandoverComment() {
		return handoverComment;
	}

	public void setHandoverComment(String handoverComment) {
		this.handoverComment = handoverComment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErrorComment() {
		return errorComment;
	}

	public void setErrorComment(String errorComment) {
		this.errorComment = errorComment;
	}

	public Timestamp getHandoverDate() {
		return handoverDate;
	}
	
	public String getHandoverDate_string() {
		return this.handoverDate == null? "": simpleDateFormat.format(this.handoverDate);
	}

	public void setHandoverDate(Timestamp handoverDate) {
		this.handoverDate = handoverDate;
	}

	
}
