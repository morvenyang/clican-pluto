package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

import com.peacebird.dataserver.model.DataRetailsNoRetail;

public class DataRetailsNoRetailResult {

	private List<DataRetailsNoRetail> noRetails;

	private int result;

	private String message;

	private Date date;

	public List<DataRetailsNoRetail> getNoRetails() {
		return noRetails;
	}

	public void setNoRetails(List<DataRetailsNoRetail> noRetails) {
		this.noRetails = noRetails;
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
