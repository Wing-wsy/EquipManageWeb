package com.equipmgr.annet.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Repair {
	private int id;
	private Timestamp arrive_time;
	private String repair_content;
	private String replace;
	private String cuase;
	private String result;
	private Timestamp restore_time;
	private String engineer;
	private String Confirm;
	private String ConfirmPerson;
	private Timestamp occur_time;
	private Timestamp report_time;
	private String phenomena;
	private String extent;
	private String report_person;
	private String Modality;
	private String Device;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	
	public String getArrive_time_string()
	{
		return this.arrive_time == null? "": simpleDateFormat.format(this.arrive_time);
	}
	
	public String getOccur_time_string()
	{
		return this.occur_time == null? "": simpleDateFormat.format(this.occur_time);
	}
	
	public String getReport_time_string()
	{
		return this.report_time == null? "": simpleDateFormat.format(this.report_time);
	}
	
	public String getRestore_time_string()
	{
		return this.restore_time == null? "": simpleDateFormat.format(this.restore_time);
	}

	public Timestamp getArrive_time() {
		return arrive_time;
	}
	public void setArrive_time(Timestamp arrive_time) {
		this.arrive_time = arrive_time;
	}
	public Timestamp getRestore_time() {
		return restore_time;
	}
	public void setRestore_time(Timestamp restore_time) {
		this.restore_time = restore_time;
	}
	public Timestamp getOccur_time() {
		return occur_time;
	}
	public void setOccur_time(Timestamp occur_time) {
		this.occur_time = occur_time;
	}
	public Timestamp getReport_time() {
		return report_time;
	}
	public void setReport_time(Timestamp report_time) {
		this.report_time = report_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRepair_content() {
		return repair_content;
	}
	public void setRepair_content(String repair_content) {
		this.repair_content = repair_content;
	}
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public String getCuase() {
		return cuase;
	}
	public void setCuase(String cuase) {
		this.cuase = cuase;
	}
	public String getEngineer() {
		return engineer;
	}
	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}
	public String getConfirm() {
		return Confirm;
	}
	public void setConfirm(String confirm) {
		Confirm = confirm;
	}
	public String getConfirmPerson() {
		return ConfirmPerson;
	}
	public void setConfirmPerson(String confirmPerson) {
		ConfirmPerson = confirmPerson;
	}
	public String getPhenomena() {
		return phenomena;
	}
	public void setPhenomena(String phenomena) {
		this.phenomena = phenomena;
	}
	public String getExtent() {
		return extent;
	}
	public void setExtent(String extent) {
		this.extent = extent;
	}
	public String getReport_person() {
		return report_person;
	}
	public void setReport_person(String report_person) {
		this.report_person = report_person;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


}
