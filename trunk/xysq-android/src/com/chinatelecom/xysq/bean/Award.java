package com.chinatelecom.xysq.bean;

public class Award {

	
	private Long id;
	
	private String name;
	
	private int cost;
	
	private int amount;
	
	private boolean realGood;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isRealGood() {
		return realGood;
	}

	public void setRealGood(boolean realGood) {
		this.realGood = realGood;
	}
	
	
}
