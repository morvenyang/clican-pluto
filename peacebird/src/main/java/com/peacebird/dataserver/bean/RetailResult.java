package com.peacebird.dataserver.bean;

public class RetailResult {

	private String type;//channel, sort, region
	
	private String name;
	
	private Integer dayAmount;

	public RetailResult(String type, String name, Number dayAmount) {
		this.type = type;
		this.name = name;
		if(dayAmount!=null){
			this.dayAmount = dayAmount.intValue();
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}
	
	
}
