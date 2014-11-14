package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandResult implements Comparable<BrandResult> {

	private String brand;

	private String channel;

	private Date date;

	private Long dayAmount;
	
	
	public BrandResult(String brand, String channel, Number dayAmount) {
		this.brand = brand;
		this.channel = channel;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Long getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Long dayAmount) {
		this.dayAmount = dayAmount;
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

	private int getIndex() {
		if (channel == null) {
			return 0;
		}
		if (channel.equals("自营")||channel.equals("直营")) {
			return 1;
		} else if (channel.equals("加盟")) {
			return 2;
		} else if (channel.equals("联营")) {
			return 3;
		} else if (channel.equals("分销")) {
			return 4;
		} else if (channel.equals("魔法")) {
			return 5;
		} else if (channel.equals("其他")) {
			return 6;
		} else if (channel.equals("电商")) {
			return 100;
		} else {
			return 8;
		}
	}

	@Override
	public int compareTo(BrandResult o) {
		int diff = this.getIndex() - o.getIndex();
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
