package com.peacebird.dataserver.bean;

public class LoginResult {

	private int result;
	private String message;
	private int expiredDays;
	private String jsessionid;
	private int timeoutInterval;
	private boolean reset;
	
	public LoginResult(int result, String message, int expiredDays,int timeoutInterval,boolean reset) {
		super();
		this.result = result;
		this.message = message;
		this.expiredDays = expiredDays;
		this.timeoutInterval = timeoutInterval;
		this.reset = reset;
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
	public String getJsessionid() {
		return jsessionid;
	}
	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}
	public int getTimeoutInterval() {
		return timeoutInterval;
	}
	public void setTimeoutInterval(int timeoutInterval) {
		this.timeoutInterval = timeoutInterval;
	}
	public boolean isReset() {
		return reset;
	}
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	
	
}
