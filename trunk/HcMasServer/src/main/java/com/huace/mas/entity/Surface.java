package com.huace.mas.entity;

import java.io.Serializable;

public class Surface extends Kpi implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280487808419986522L;
	
	private Double v2;//y方向值
	private Double v3;//h方向值
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
	private Double dis_x;//x方向差值
	private Double dis_y;//y方向差值
	private Double dis_h;//y方向差值
	
	private double todayChangeValue;
	private double yesterdayChangeValue;
	private double weekChangeValue;
	
	private Double d2;
	private Double d3;
	
	
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
	
	public Double getDis_x() {
		return dis_x;
	}
	public void setDis_x(Double disX) {
		dis_x = disX;
	}
	public Double getDis_y() {
		return dis_y;
	}
	public void setDis_y(Double disY) {
		dis_y = disY;
	}
	public Double getDis_h() {
		return dis_h;
	}
	public void setDis_h(Double disH) {
		dis_h = disH;
	}
	public double getInit_x() {
		return init_x;
	}
	public void setInit_x(double initX) {
		init_x = initX;
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
	public double getInit_y() {
		return init_y;
	}
	public void setInit_y(double initY) {
		init_y = initY;
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
	public double getInit_h() {
		return init_h;
	}
	public void setInit_h(double initH) {
		init_h = initH;
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
	public Double getV2() {
		return v2;
	}
	public void setV2(Double v2) {
		this.v2 = v2;
	}
	public Double getV3() {
		return v3;
	}
	public void setV3(Double v3) {
		this.v3 = v3;
	}
	public double getTodayChangeValue() {
		return todayChangeValue;
	}
	public void setTodayChangeValue(double todayChangeValue) {
		this.todayChangeValue = todayChangeValue;
	}
	public double getYesterdayChangeValue() {
		return yesterdayChangeValue;
	}
	public void setYesterdayChangeValue(double yesterdayChangeValue) {
		this.yesterdayChangeValue = yesterdayChangeValue;
	}
	public double getWeekChangeValue() {
		return weekChangeValue;
	}
	public void setWeekChangeValue(double weekChangeValue) {
		this.weekChangeValue = weekChangeValue;
	}
	public Double getD2() {
		return d2;
	}
	public void setD2(Double d2) {
		this.d2 = d2;
	}
	public Double getD3() {
		return d3;
	}
	public void setD3(Double d3) {
		this.d3 = d3;
	}

	
	
}
