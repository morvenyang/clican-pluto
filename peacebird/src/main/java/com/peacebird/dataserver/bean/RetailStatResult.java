package com.peacebird.dataserver.bean;

import java.util.Date;
import java.util.List;

public class RetailStatResult {

	private List<RetailResult> channelRetail;
	private List<RetailResult> sortRetail;
	private List<RetailResult> regionRetail;
	private Integer totalChannelAmount = 0;
	private Integer totalSortAmount = 0;
	private Integer totalRegionAmount = 0;
	private Date date;

	private int result;
	private String message;

	public List<RetailResult> getChannelRetail() {
		return channelRetail;
	}

	public void setChannelRetail(List<RetailResult> channelRetail) {
		this.channelRetail = channelRetail;
		for (RetailResult rr : channelRetail) {
			totalChannelAmount += rr.getDayAmount();
		}
	}

	public List<RetailResult> getSortRetail() {
		return sortRetail;
	}

	public void setSortRetail(List<RetailResult> sortRetail) {
		this.sortRetail = sortRetail;
		for (RetailResult rr : sortRetail) {
			totalSortAmount += rr.getDayAmount();
		}
	}

	public List<RetailResult> getRegionRetail() {
		return regionRetail;
	}

	public void setRegionRetail(List<RetailResult> regionRetail) {
		this.regionRetail = regionRetail;
		for (RetailResult rr : regionRetail) {
			totalRegionAmount += rr.getDayAmount();
		}
	}

	public Integer getTotalChannelAmount() {
		return totalChannelAmount;
	}

	public void setTotalChannelAmount(Integer totalChannelAmount) {
		this.totalChannelAmount = totalChannelAmount;
	}

	public Integer getTotalSortAmount() {
		return totalSortAmount;
	}

	public void setTotalSortAmount(Integer totalSortAmount) {
		this.totalSortAmount = totalSortAmount;
	}

	public Integer getTotalRegionAmount() {
		return totalRegionAmount;
	}

	public void setTotalRegionAmount(Integer totalRegionAmount) {
		this.totalRegionAmount = totalRegionAmount;
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
