package com.equipmgr.annet.entity;

import java.sql.Timestamp;

public class TimePair {
	private Timestamp start;
	private Timestamp end;
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	
	public void setEnd(Timestamp end) {
		this.end = end;
	}
	
	public Timestamp getEnd() {
		return end;
	}
}
