package com.huace.mas.entity;

import java.io.Serializable;

public class Saturation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1679052802336866576L;
	//配置文件节点对应
	public static String S_saturationLine = "saturationLine";
	public static String S_pointName = "pointName";
	public static String S_deviceID = "deviceID";
	public static String S_damElevation = "damElevation";
	public static String S_yellow_value = "yellow_value";
	public static String S_orange_value = "orange_value";
	public static String S_red_value = "red_value";
	
	//表字段对应
	private int deviceID;//设备ID
	private double v1;//表示水位高程
	private String dacTime;//时间
	private String projectName;//工程名称
	
	private String pointName;//浸润线监测点名称
	
	//配置文件设定值
	private double damElevation ;//坝面高程
	private Double yellow_value ;//水位黄色警戒值
	private Double orange_value ;//水位橙色警戒值
	private Double red_value ;//水位红色警戒值
	
	//水面到坝面的高差
	private double dis_hg;
	
	//警报等级
	private int alertGrade;
	
	
	public int getAlertGrade() {
		return alertGrade;
	}
	public void setAlertGrade(int alertGrade) {
		this.alertGrade = alertGrade;
	}
	public double getDis_hg() {
		return dis_hg;
	}
	public void setDis_hg(double disHg) {
		dis_hg = disHg;
	}
	
	public double getDamElevation() {
		return damElevation;
	}
	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
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
	public Double getYellow_value() {
		return yellow_value;
	}
	public void setYellow_value(Double yellowValue) {
		yellow_value = yellowValue;
	}
	public Double getOrange_value() {
		return orange_value;
	}
	public void setOrange_value(Double orangeValue) {
		orange_value = orangeValue;
	}
	public Double getRed_value() {
		return red_value;
	}
	public void setRed_value(Double redValue) {
		red_value = redValue;
	}
	
}
