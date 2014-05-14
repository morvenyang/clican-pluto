package com.peacebird.dataserver.bean;

public class IndexAmountResult {

	private String brand;
	
	private Integer dayAmount;

	public IndexAmountResult(String brand, Integer dayAmount) {
		super();
		this.brand = brand;
		this.dayAmount = dayAmount;
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
	
	
}
