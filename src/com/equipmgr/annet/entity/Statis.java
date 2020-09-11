package com.equipmgr.annet.entity;

import java.util.Date;

public class Statis {
	private Date studyDate;
	private int times;
	private int income;
	private int exposureNum;
	
	public int getExposureNum() {
		return exposureNum;
	}
	public void setExposureNum(int exposureNum) {
		this.exposureNum = exposureNum;
	}
	public Date getStudyDate() {
		return studyDate;
	}
	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getIncome() {
		return income;
	}
	public void setIncome(int income) {
		this.income = income;
	}

}
