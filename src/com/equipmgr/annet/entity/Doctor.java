package com.equipmgr.annet.entity;

import java.sql.Date;

public class Doctor {
	private int ID;
	private String name;
	private String sex;
	private String education;
	private String title;
	private String duty;
	private String school;
	private String degree;
	private String RadiationAge;
	private Date birthday;
	private Date StartJob;
	private Date RuzhiDate;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getRadiationAge() {
		return RadiationAge;
	}
	public void setRadiationAge(String radiationAge) {
		RadiationAge = radiationAge;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getStartJob() {
		return StartJob;
	}
	public void setStartJob(Date startJob) {
		StartJob = startJob;
	}
	public Date getRuzhiDate() {
		return RuzhiDate;
	}
	public void setRuzhiDate(Date ruzhiDate) {
		RuzhiDate = ruzhiDate;
	}
	
}
