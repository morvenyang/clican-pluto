package com.peacebird.dataserver.bean;

import java.util.Date;

public class BrandLineChartResult implements Comparable<BrandLineChartResult> {

	private Double amount;

	private Double preAmount;

	private Date date;

	private String color;

	private String dateStr;

	private String fullDateStr;

	public BrandLineChartResult(Number amount, Number preAmount, Date date) {
		super();
		if (amount != null) {
			this.amount = amount.doubleValue() / 10000;
		}
		if (preAmount != null) {
			this.preAmount = preAmount.doubleValue() / 10000;
		}
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPreAmount() {
		return preAmount;
	}

	public void setPreAmount(Double preAmount) {
		this.preAmount = preAmount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getLike() {
		if (preAmount == null || amount == null || preAmount == 0) {
			return 0.0;
		}

		Double like = (amount / preAmount - 1) * 100;
		return new Double(like.intValue());
	}

	@Override
	public int compareTo(BrandLineChartResult o) {
		return date.compareTo(o.getDate());
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getFullDateStr() {
		return fullDateStr;
	}

	public void setFullDateStr(String fullDateStr) {
		this.fullDateStr = fullDateStr;
	}

	public String getBallonValue() {
		String da;
		if (amount >= 1) {
			da = String.format("%.0f", amount);
		} else {
			da = String.format("%.2f", amount);
		}
		String p = String.format("%.1f", getLike()) + "%";
		while (p.length() < 7) {
			p = " " + p;
		}
		String description = da + "万元" + p;
		return description;
	}

}
