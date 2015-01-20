package com.chinatelecom.xysq.bean;

import java.util.Date;

public class BroadbandRemind {

	private boolean exist;
	
	
	private String msisdn;

	private Date expiredDate;

	private String broadBandId;

	private String userName;

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getBroadBandId() {
		return broadBandId;
	}

	public void setBroadBandId(String broadBandId) {
		this.broadBandId = broadBandId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
