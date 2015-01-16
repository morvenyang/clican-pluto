package com.chinatelecom.xysq.json;

public class RegisterJson {
	private boolean success;

	private String message;
	
	private UserJson user;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserJson getUser() {
		return user;
	}

	public void setUser(UserJson user) {
		this.user = user;
	}

	
	
	
}
