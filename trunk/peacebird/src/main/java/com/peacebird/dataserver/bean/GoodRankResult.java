package com.peacebird.dataserver.bean;

public class GoodRankResult {

	private String name;
	private Long amount;
	private Integer count;
	
	
	
	public GoodRankResult(String name, Long amount, Integer count) {
		this.name = name;
		this.amount = amount;
		this.count = count;
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
