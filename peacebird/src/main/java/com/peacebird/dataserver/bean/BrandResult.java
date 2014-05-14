package com.peacebird.dataserver.bean;

public class BrandResult {

	private String brand;
	
	private Integer dayAmount;

	public BrandResult(String brand, Number dayAmount) {
		this.brand = brand;
		this.dayAmount = dayAmount.intValue();
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
