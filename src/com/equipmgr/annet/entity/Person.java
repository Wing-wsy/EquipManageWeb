package com.equipmgr.annet.entity;

import java.sql.Date;

public class Person {

	private int id;
	private String name;
	private int sex;
	private String education;
	private String title;
	private String duty;
	private String school;
	private String degree;
	private int radiationAge;
	private Date birthday;
	private Date startJob;
	private Date ruzhiDate;
	private String deptName;
	private String jobCategory;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getStartJob() {
		return startJob;
	}
	public Date getRuzhiDate() {
		return ruzhiDate;
	}
	public void setRuzhiDate(Date ruzhiDate) {
		this.ruzhiDate = ruzhiDate;
	}
	public void setStartJob(Date startJob) {
		this.startJob = startJob;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getRadiationAge() {
		return radiationAge;
	}
	public void setRadiationAge(int radiationAge) {
		this.radiationAge = radiationAge;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getJobCategory() {
		return jobCategory;
	}
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}
}
