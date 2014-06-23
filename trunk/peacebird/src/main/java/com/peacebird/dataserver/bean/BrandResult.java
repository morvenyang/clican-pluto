package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandResult implements Comparable<BrandResult> {

	private String brand;

	private String channel;

	private Date date;

	private Long dayAmount;
	
	private Long monthAmount;

	private Long weekAmount;

	private Long yearAmount;

	private Double weekLike;
	
	private Double monthLike;

	private Double yearLike;
	
	private Double dayLike;

	public BrandResult(String brand, String channel, Number dayAmount) {
		this.brand = brand;
		this.channel = channel;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
	}
	
	public BrandResult(String brand, String channel, Number dayAmount,Number dayLike,Number weekLike,Number monthLike,Number yearLike) {
		this.brand = brand;
		this.channel = channel;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
		if (dayLike != null) {
			this.dayLike = dayLike.doubleValue();
		}
		if (weekLike != null) {
			this.weekLike = weekLike.doubleValue();
		}
		if (monthLike != null) {
			this.monthLike = monthLike.doubleValue();
		}
		if (yearLike != null) {
			this.yearLike = yearLike.doubleValue();
		}
	}

	public BrandResult(String brand, Date date, Number dayAmount) {
		this.brand = brand;
		this.date = date;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
	}

	public BrandResult(String brand, Number dayAmount, Number weekAmount,Number monthAmount,
			Number yearAmount) {
		super();
		this.brand = brand;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
		}
		if (weekAmount != null) {
			this.weekAmount = weekAmount.longValue();
		}
		if (monthAmount != null) {
			this.monthAmount = monthAmount.longValue();
		}
		if (yearAmount != null) {
			this.yearAmount = yearAmount.longValue();
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

	public Long getWeekAmount() {
		return weekAmount;
	}

	public void setWeekAmount(Long weekAmount) {
		this.weekAmount = weekAmount;
	}

	public Long getYearAmount() {
		return yearAmount;
	}

	public void setYearAmount(Long yearAmount) {
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

	public Double getDayLike() {
		return dayLike;
	}

	public void setDayLike(Double dayLike) {
		this.dayLike = dayLike;
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

	public Long getMonthAmount() {
		return monthAmount;
	}

	public void setMonthAmount(Long monthAmount) {
		this.monthAmount = monthAmount;
	}

	public Double getMonthLike() {
		return monthLike;
	}

	public void setMonthLike(Double monthLike) {
		this.monthLike = monthLike;
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
		} else {
			return 6;
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
