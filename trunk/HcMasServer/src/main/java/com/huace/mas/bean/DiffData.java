package com.huace.mas.bean;

import java.util.Date;

public class DiffData {

	private Double v1Today=0.0;

	private Double v2Today=0.0;

	private Double v3Today=0.0;

	private Double v1Yesterday=0.0;

	private Double v2Yesterday=0.0;

	private Double v3Yesterday=0.0;

	private Double v1Week=0.0;

	private Double v2Week=0.0;

	private Double v3Week=0.0;

	private String deviceID;

	private Date today;

	public DiffData(Date today) {
		this.today = today;
	}

	public Double getV1Today() {
		return v1Today;
	}

	public void setV1Today(Double v1Today) {
		this.v1Today = v1Today;
	}

	public Double getV2Today() {
		return v2Today;
	}

	public void setV2Today(Double v2Today) {
		this.v2Today = v2Today;
	}

	public Double getV3Today() {
		return v3Today;
	}

	public void setV3Today(Double v3Today) {
		this.v3Today = v3Today;
	}

	public Double getV1Yesterday() {
		return v1Yesterday;
	}

	public void setV1Yesterday(Double v1Yesterday) {
		this.v1Yesterday = v1Yesterday;
	}

	public Double getV2Yesterday() {
		return v2Yesterday;
	}

	public void setV2Yesterday(Double v2Yesterday) {
		this.v2Yesterday = v2Yesterday;
	}

	public Double getV3Yesterday() {
		return v3Yesterday;
	}

	public void setV3Yesterday(Double v3Yesterday) {
		this.v3Yesterday = v3Yesterday;
	}

	public Double getV1Week() {
		return v1Week;
	}

	public void setV1Week(Double v1Week) {
		this.v1Week = v1Week;
	}

	public Double getV2Week() {
		return v2Week;
	}

	public void setV2Week(Double v2Week) {
		this.v2Week = v2Week;
	}

	public Double getV3Week() {
		return v3Week;
	}

	public void setV3Week(Double v3Week) {
		this.v3Week = v3Week;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public boolean isSameToday(Date today) {
		return this.today.equals(today);
	}
}
