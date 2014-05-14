package com.peacebird.dataserver.bean;

import java.util.List;

public class IndexResult {

	
	private List<IndexBrandResult> brands;
	
	private int result;
	
	private String message;

	public List<IndexBrandResult> getBrands() {
		return brands;
	}

	public void setBrands(List<IndexBrandResult> brands) {
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
	
	
}
