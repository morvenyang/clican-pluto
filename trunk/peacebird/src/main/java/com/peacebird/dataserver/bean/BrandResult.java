package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandResult {

	private String brand;

	private String channel;

	private Date date;

	private Integer dayAmount;

	private Integer weekAmount;

	private Integer yearAmount;

	private Double weekLike;

	private Double yearLike;

	public BrandResult(String brand, String channel, Number dayAmount) {
		this.brand = brand;
		this.channel = channel;
		this.dayAmount = dayAmount.intValue();
	}

	public BrandResult(String brand, Date date, Number dayAmount) {
		this.brand = brand;
		this.date = date;
		this.dayAmount = dayAmount.intValue();
	}

	public BrandResult(String brand, Number dayAmount, Number weekAmount,
			Number yearAmount, Number weekLike, Number yearLike) {
		super();
		this.brand = brand;
		this.dayAmount = dayAmount.intValue();
		this.weekAmount = weekAmount.intValue();
		this.yearAmount = yearAmount.intValue();
		this.weekLike = weekLike.doubleValue();
		this.yearLike = yearLike.doubleValue();
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}

	public Integer getWeekAmount() {
		return weekAmount;
	}

	public void setWeekAmount(Integer weekAmount) {
		this.weekAmount = weekAmount;
	}

	public Integer getYearAmount() {
		return yearAmount;
	}

	public void setYearAmount(Integer yearAmount) {
		this.yearAmount = yearAmount;
	}

	public Double getWeekLike() {
		return weekLike;
	}

	public void setWeekLike(Double weekLike) {
		this.weekLike = weekLike;
	}

	public Double getYearLike() {
		return yearLike;
	}

	public void setYearLike(Double yearLike) {
		this.yearLike = yearLike;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
