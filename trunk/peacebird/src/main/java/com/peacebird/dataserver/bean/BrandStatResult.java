package com.peacebird.dataserver.bean;

import java.util.List;

public class BrandStatResult {
	
	private int result;
	
	private String message;
	
	private List<BrandResult> channels;
	
	private BrandResult brandResult;
	
	private String brand;
	
	private List<BrandLineChartResult> dailyLineChart;
	private List<BrandLineChartResult> weeklyLineChart;
	private List<BrandLineChartResult> monthlyLineChart;
	private List<BrandLineChartResult> yearlyLineChart;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BrandResult> getChannels() {
		return channels;
	}

	public void setChannels(List<BrandResult> channels) {
		this.channels = channels;
	}

	public BrandResult getBrandResult() {
		return brandResult;
	}

	public void setBrandResult(BrandResult brandResult) {
		this.brandResult = brandResult;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	public List<BrandLineChartResult> getDailyLineChart() {
		return dailyLineChart;
	}

	public void setDailyLineChart(List<BrandLineChartResult> dailyLineChart) {
		this.dailyLineChart = dailyLineChart;
	}

	public List<BrandLineChartResult> getWeeklyLineChart() {
		return weeklyLineChart;
	}

	public void setWeeklyLineChart(List<BrandLineChartResult> weeklyLineChart) {
		this.weeklyLineChart = weeklyLineChart;
	}

	public List<BrandLineChartResult> getMonthlyLineChart() {
		return monthlyLineChart;
	}

	public void setMonthlyLineChart(List<BrandLineChartResult> monthlyLineChart) {
		this.monthlyLineChart = monthlyLineChart;
	}

	public List<BrandLineChartResult> getYearlyLineChart() {
		return yearlyLineChart;
	}

	public void setYearlyLineChart(List<BrandLineChartResult> yearlyLineChart) {
		this.yearlyLineChart = yearlyLineChart;
	}
	

}
