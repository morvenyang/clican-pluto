package com.huace.mas.bean;

import java.util.Date;

public class DiffData {

	private Double oneDayData;

	private Double twoDayData;

	private Double sevenDayData;

	private String deviceID;

	private Date today;

	public DiffData(Date today) {
		this.today = today;
	}

	public Double getOneDayData() {
		return oneDayData;
	}

	public void setOneDayData(Double oneDayData) {
		this.oneDayData = oneDayData;
	}

	public Double getTwoDayData() {
		return twoDayData;
	}

	public void setTwoDayData(Double twoDayData) {
		this.twoDayData = twoDayData;
	}

	public Double getSevenDayData() {
		return sevenDayData;
	}

	public void setSevenDayData(Double sevenDayData) {
		this.sevenDayData = sevenDayData;
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
