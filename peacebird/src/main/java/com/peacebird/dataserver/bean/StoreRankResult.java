package com.peacebird.dataserver.bean;

public class StoreRankResult {

	private String name;
	private Long amount;
	private Double rate;
	
	public StoreRankResult(String name, Long amount,Double rate) {
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
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
