package com.huace.mas.bean;

import java.util.Date;

public class KpiData {

	private Date dacTime;
	
	private Double v1;
	
	private Double v2;
	
	private Double v3;
	
	private Double d2;
	
	private Double d3;
	
	private Double todayChangeValue;
	private Double yesterdayChangeValue;
	private Double weekChangeValue;
	

	public Date getDacTime() {
		return dacTime;
	}

	public void setDacTime(Date dacTime) {
		this.dacTime = dacTime;
	}

	public Double getV1() {
		return v1;
	}

	public void setV1(Double v1) {
		this.v1 = v1;
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

	public Double getTodayChangeValue() {
		return todayChangeValue;
	}

	public void setTodayChangeValue(Double todayChangeValue) {
		this.todayChangeValue = todayChangeValue;
	}

	public Double getYesterdayChangeValue() {
		return yesterdayChangeValue;
	}

	public void setYesterdayChangeValue(Double yesterdayChangeValue) {
		this.yesterdayChangeValue = yesterdayChangeValue;
	}

	public Double getWeekChangeValue() {
		return weekChangeValue;
	}

	public void setWeekChangeValue(Double weekChangeValue) {
		this.weekChangeValue = weekChangeValue;
	}
	
	

	
}
