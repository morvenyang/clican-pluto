package com.chinatelecom.xysq.json;

public class UserJson {

	private String nickName;

	private String msisdn;

	private Long id;

	private String jsessionid;

	private String address;
	private String carNumber;
	private boolean applyXqnc;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public boolean isApplyXqnc() {
		return applyXqnc;
	}

	public void setApplyXqnc(boolean applyXqnc) {
		this.applyXqnc = applyXqnc;
	}

}
