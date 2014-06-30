package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

public class RetailChartResult {

	private List<RetailResult> dataProvider;
	
	private int height;
	
	private int top;
	
	private long total;
	
	private Date date;

	public List<RetailResult> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(List<RetailResult> dataProvider) {
		this.dataProvider = dataProvider;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
