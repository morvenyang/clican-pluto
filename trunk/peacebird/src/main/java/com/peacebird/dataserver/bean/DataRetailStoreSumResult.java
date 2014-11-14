package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.model.DataRetailStoreSum;

public class DataRetailStoreSumResult {

	private List<DataRetailStoreSum> monthlySums;
	
	private List<DataRetailStoreSum> yearlySums;
	
	private int result;

	private String message;

	private Date date;

	public List<DataRetailStoreSum> getMonthlySums() {
		return monthlySums;
	}

	public void setMonthlySums(List<DataRetailStoreSum> monthlySums) {
		this.monthlySums = monthlySums;
	}

	public List<DataRetailStoreSum> getYearlySums() {
		return yearlySums;
	}

	public void setYearlySums(List<DataRetailStoreSum> yearlySums) {
		this.yearlySums = yearlySums;
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
	
	
}
