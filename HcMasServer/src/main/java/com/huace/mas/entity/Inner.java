package com.huace.mas.entity;

import java.io.Serializable;

public class Inner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5088103690731636195L;
	
	
	//配置文件节点对应
	public static String I_inner = "inner";
	public static String I_deviceID = "deviceID";
	public static String I_pointName = "pointName";
	
	public static String I_yellow_x ="yellow_x";
	public static String I_orange_x ="orange_x";
	public static String I_red_x ="red_x";
					
	public static String I_yellow_y ="yellow_y";
	public static String I_orange_y ="orange_y";
	public static String I_red_y ="red_y";
					
	public static String I_yellow_h ="yellow_h";
	public static String I_orange_h ="orange_h";
	public static String I_red_h ="red_h";
	
	//表字段对应
	private int DeviceInnerID;//设备ID
	private double v1;//x方向值
	private double v2;//y方向值
	private double v3;//h方向值
	private String dacTime;//时间
	private String projectName;//工程名称
	
	private String pointName;//内部位移监测点名称
	
	//x方向 相关等级值
	private double yellow_x ;
	private double orange_x ;
	private double red_x ;
	
	//y方向 相关等级值
	private double yellow_y;
	private double orange_y;
	private double red_y;
	
	//h方向 相关等级值
	private double yellow_h;
	private double orange_h ;
	private double red_h  ;
	
	//警报等级
	private int alertGrade_x;
	private int alertGrade_y;
	private int alertGrade_h;
	
	
	public int getDeviceInnerID() {
		return DeviceInnerID;
	}
	public void setDeviceInnerID(int deviceInnerID) {
		DeviceInnerID = deviceInnerID;
	}
	public double getV1() {
		return v1;
	}
	public void setV1(double v1) {
		this.v1 = v1;
	}
	public double getV2() {
		return v2;
	}
	public void setV2(double v2) {
		this.v2 = v2;
	}
	public double getV3() {
		return v3;
	}
	public void setV3(double v3) {
		this.v3 = v3;
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
	public double getYellow_x() {
		return yellow_x;
	}
	public void setYellow_x(double yellowX) {
		yellow_x = yellowX;
	}
	public double getOrange_x() {
		return orange_x;
	}
	public void setOrange_x(double orangeX) {
		orange_x = orangeX;
	}
	public double getRed_x() {
		return red_x;
	}
	public void setRed_x(double redX) {
		red_x = redX;
	}
	public double getYellow_y() {
		return yellow_y;
	}
	public void setYellow_y(double yellowY) {
		yellow_y = yellowY;
	}
	public double getOrange_y() {
		return orange_y;
	}
	public void setOrange_y(double orangeY) {
		orange_y = orangeY;
	}
	public double getRed_y() {
		return red_y;
	}
	public void setRed_y(double redY) {
		red_y = redY;
	}
	public double getYellow_h() {
		return yellow_h;
	}
	public void setYellow_h(double yellowH) {
		yellow_h = yellowH;
	}
	public double getOrange_h() {
		return orange_h;
	}
	public void setOrange_h(double orangeH) {
		orange_h = orangeH;
	}
	public double getRed_h() {
		return red_h;
	}
	public void setRed_h(double redH) {
		red_h = redH;
	}
	public int getAlertGrade_x() {
		return alertGrade_x;
	}
	public void setAlertGrade_x(int alertGradeX) {
		alertGrade_x = alertGradeX;
	}
	public int getAlertGrade_y() {
		return alertGrade_y;
	}
	public void setAlertGrade_y(int alertGradeY) {
		alertGrade_y = alertGradeY;
	}
	public int getAlertGrade_h() {
		return alertGrade_h;
	}
	public void setAlertGrade_h(int alertGradeH) {
		alertGrade_h = alertGradeH;
	}
	
	
}
