package com.equipmgr.annet.entity;

public class Attach {
	private int iD;
	private int attachType;
	private int relativeID;
	private String attachName;
	private String attachPath;
	public int getiD() {
		return iD;
	}
	public void setiD(int iD) {
		this.iD = iD;
	}
	public int getAttachType() {
		return attachType;
	}
	public void setAttachType(int attachType) {
		this.attachType = attachType;
	}
	public int getRelativeID() {
		return relativeID;
	}
	public void setRelativeID(int relativeID) {
		this.relativeID = relativeID;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	
}
