package com.peacebird.dataserver.bean;

import java.util.List;

public class BrandStatResult {
	
	private int result;
	
	private String message;
	
	private List<BrandResult> weeks;
	private List<BrandResult> channels;
	
	private BrandResult brandResult;
	
	private String brand;
	
	private List<BrandLineChartResult> dailyLineChart;

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

	public List<BrandResult> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<BrandResult> weeks) {
		this.weeks = weeks;
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
	

}
