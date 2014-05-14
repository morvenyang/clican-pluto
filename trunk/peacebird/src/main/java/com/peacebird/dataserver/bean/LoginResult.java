package com.peacebird.dataserver.bean;

public class LoginResult {

	private int result;
	private String message;
	private int expiredDays;
	
	public LoginResult(int result, String message, int expiredDays) {
		super();
		this.result = result;
		this.message = message;
		this.expiredDays = expiredDays;
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
	public int getExpiredDays() {
		return expiredDays;
	}
	public void setExpiredDays(int expiredDays) {
		this.expiredDays = expiredDays;
	}
	
	
}
