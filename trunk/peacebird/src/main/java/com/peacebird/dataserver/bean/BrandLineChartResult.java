package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandLineChartResult implements Comparable<BrandLineChartResult>{
	
	private Double amount;
	
	private Date date;

	public BrandLineChartResult(Number amount, Date date) {
		super();
		if(amount!=null){
			this.amount = amount.doubleValue();
		}
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(BrandLineChartResult o) {
		return date.compareTo(o.getDate());
	}


}
