package com.huace.mas.entity;

import java.io.Serializable;

public class DryBeach implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6125308560054520771L;

	//配置文件节点对应
	public static String D_dryBeach = "dryBeach";
	public static String D_deviceID = "deviceID";
	public static String D_pointName = "pointName";
	public static String D_yellow_value= "yellow_value";
	public static String D_orange_value= "orange_value";
	public static String D_red_value= "red_value";
	
	//表字段对应
	private int deviceID;//设备ID
	private double v1;//干滩长度
	private String dacTime;//时间
	private String projectName;//工程名称
	
	private String pointName;//干滩监测点名称
	
	// 相关等级值
	private double yellow_value ;
	private double orange_value ;
	private double red_value ;
	
	//相关等级是否警报
	private int alertGrade;

	
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

	public double getV1() {
		return v1;
	}

	public void setV1(double v1) {
		this.v1 = v1;
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
