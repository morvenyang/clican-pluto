package com.huace.mas.entity;

import java.util.Date;

public abstract class Kpi {

	private String __type;
	
	private boolean isAlert;
	
	private int alertGrade;
	
	private Date dacTime;
	
	private int deviceID;
	
	private String pointName;
	
	private String projectName;
	
	private double red_value;
	
	private double yellow_value;
	
	private double orange_value;
	
	private Double v1;
	
	public String get__type() {
		return __type;
	}

	public void set__type(String __type) {
		this.__type = __type;
	}

	public int getAlertGrade() {
		return alertGrade;
	}

	public void setAlertGrade(int alertGrade) {
		this.alertGrade = alertGrade;
	}

	public Date getDacTime() {
		return dacTime;
	}

	public void setDacTime(Date dacTime) {
		this.dacTime = dacTime;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public double getRed_value() {
		return red_value;
	}

	public void setRed_value(double red_value) {
		this.red_value = red_value;
	}

	public double getYellow_value() {
		return yellow_value;
	}

	public void setYellow_value(double yellow_value) {
		this.yellow_value = yellow_value;
	}

	public double getOrange_value() {
		return orange_value;
	}

	public void setOrange_value(double orange_value) {
		this.orange_value = orange_value;
	}

	public Double getV1() {
		return v1;
	}

	public void setV1(Double v1) {
		this.v1 = v1;
	}
	
	public boolean isAlert() {
		return isAlert;
	}
	public void setAlert(boolean isAlert) {
		this.isAlert = isAlert;
	}
}
