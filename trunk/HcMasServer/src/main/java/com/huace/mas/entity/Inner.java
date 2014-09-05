package com.huace.mas.entity;

import java.io.Serializable;

public class Inner extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5088103690731636195L;
	
	private double v2;//y方向值
	private double v3;//h方向值
	//x 初始值和相关等级值
	private double init_x ;
	private double yellow_x ;
	private double orange_x ;
	private double red_x ;
	
	//y 初始值和相关等级值
	private double init_y;//y初始值
	private double yellow_y;
	private double orange_y;
	private double red_y;
	
	//h 初始值和相关等级值
	private double init_h;
	private double yellow_h;
	private double orange_h ;
	private double red_h  ;
	
	//警报等级
	private int alertGrade_x;
	private int alertGrade_y;
	private int alertGrade_h;
	
	//x,y,h方向位移差值
	private double dis_x;//x方向差值
	private double dis_y;//y方向差值
	private double dis_h;//y方向差值
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
	public double getInit_x() {
		return init_x;
	}
	public void setInit_x(double init_x) {
		this.init_x = init_x;
	}
	public double getYellow_x() {
		return yellow_x;
	}
	public void setYellow_x(double yellow_x) {
		this.yellow_x = yellow_x;
	}
	public double getOrange_x() {
		return orange_x;
	}
	public void setOrange_x(double orange_x) {
		this.orange_x = orange_x;
	}
	public double getRed_x() {
		return red_x;
	}
	public void setRed_x(double red_x) {
		this.red_x = red_x;
	}
	public double getInit_y() {
		return init_y;
	}
	public void setInit_y(double init_y) {
		this.init_y = init_y;
	}
	public double getYellow_y() {
		return yellow_y;
	}
	public void setYellow_y(double yellow_y) {
		this.yellow_y = yellow_y;
	}
	public double getOrange_y() {
		return orange_y;
	}
	public void setOrange_y(double orange_y) {
		this.orange_y = orange_y;
	}
	public double getRed_y() {
		return red_y;
	}
	public void setRed_y(double red_y) {
		this.red_y = red_y;
	}
	public double getInit_h() {
		return init_h;
	}
	public void setInit_h(double init_h) {
		this.init_h = init_h;
	}
	public double getYellow_h() {
		return yellow_h;
	}
	public void setYellow_h(double yellow_h) {
		this.yellow_h = yellow_h;
	}
	public double getOrange_h() {
		return orange_h;
	}
	public void setOrange_h(double orange_h) {
		this.orange_h = orange_h;
	}
	public double getRed_h() {
		return red_h;
	}
	public void setRed_h(double red_h) {
		this.red_h = red_h;
	}
	public int getAlertGrade_x() {
		return alertGrade_x;
	}
	public void setAlertGrade_x(int alertGrade_x) {
		this.alertGrade_x = alertGrade_x;
	}
	public int getAlertGrade_y() {
		return alertGrade_y;
	}
	public void setAlertGrade_y(int alertGrade_y) {
		this.alertGrade_y = alertGrade_y;
	}
	public int getAlertGrade_h() {
		return alertGrade_h;
	}
	public void setAlertGrade_h(int alertGrade_h) {
		this.alertGrade_h = alertGrade_h;
	}
	public double getDis_x() {
		return dis_x;
	}
	public void setDis_x(double dis_x) {
		this.dis_x = dis_x;
	}
	public double getDis_y() {
		return dis_y;
	}
	public void setDis_y(double dis_y) {
		this.dis_y = dis_y;
	}
	public double getDis_h() {
		return dis_h;
	}
	public void setDis_h(double dis_h) {
		this.dis_h = dis_h;
	}
	
}
