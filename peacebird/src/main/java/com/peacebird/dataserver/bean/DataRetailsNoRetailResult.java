package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

public class DataRetailsNoRetailResult {

	private List<DataRetailsChannelNoRetailResult> noRetails;

	private int result;

	private String message;

	private Date date;

	public List<DataRetailsChannelNoRetailResult> getNoRetails() {
		return noRetails;
	}

	public void setNoRetails(List<DataRetailsChannelNoRetailResult> noRetails) {
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
