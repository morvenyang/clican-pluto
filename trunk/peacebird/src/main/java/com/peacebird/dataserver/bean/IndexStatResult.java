package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

public class IndexStatResult {

	
	private List<BrandResult> brands;
	
	private int result;
	
	private Date date;
	
	private boolean yesterday;
	
	private String message;
	
	private List<String> availableDates;

	public List<BrandResult> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandResult> brands) {
		this.brands = brands;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isYesterday() {
		return yesterday;
	}

	public void setYesterday(boolean yesterday) {
		this.yesterday = yesterday;
	}

	public List<String> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<String> availableDates) {
		this.availableDates = availableDates;
	}

	
	
	
}
