package com.huace.mas.entity;

import java.io.Serializable;

public class SeeFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224510382285948750L;

	//配置文件节点对应
	public static String S_seeFlow = "seeFlow";
	public static String S_deviceID = "deviceID";
	public static String S_pointName = "pointName";
	public static String S_yellow_value= "yellow_value";
	public static String S_orange_value= "orange_value";
	public static String S_red_value= "red_value";
	
	//表字段对应
	private int deviceID;//设备ID
	private double v2;//流速
	private String dacTime;//时间
	private String projectName;//工程名称
	
	private String pointName;//渗流监测点名称
	
	// 相关等级值
	private double yellow_value ;
	private double orange_value ;
	private double red_value ;
	
	//相关等级是否警报
	private int alertGrade;

	
	public double getV2() {
		return v2;
	}

	public void setV2(double v2) {
		this.v2 = v2;
	}

	public double getYellow_value() {
		return yellow_value;
	}

	public void setYellow_value(double yellowValue) {
		yellow_value = yellowValue;
	}

	public double getOrange_value() {
		return orange_value;
	}

	public void setOrange_value(double orangeValue) {
		orange_value = orangeValue;
	}

	public double getRed_value() {
		return red_value;
	}

	public void setRed_value(double redValue) {
		red_value = redValue;
	}

	public int getAlertGrade() {
		return alertGrade;
	}

	public void setAlertGrade(int alertGrade) {
		this.alertGrade = alertGrade;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}


	public String getDacTime() {
		return dacTime;
	}

	public void setDacTime(String dacTime) {
		this.dacTime = dacTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
	
}
