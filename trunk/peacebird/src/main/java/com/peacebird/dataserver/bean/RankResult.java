package com.peacebird.dataserver.bean;

public class RankResult {

	private String name;
	private Integer amount;
	private Double rate;
	
	public RankResult(String name, Integer amount,Double rate) {
		this.name = name;
		this.amount = amount;
		this.rate = rate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
