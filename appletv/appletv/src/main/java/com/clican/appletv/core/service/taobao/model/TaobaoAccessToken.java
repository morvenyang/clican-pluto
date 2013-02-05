package com.clican.appletv.core.service.taobao.model;

public class TaobaoAccessToken {

	private String userId;
	private String userNick;
	private String refreshToken;
	private String accessToken;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNick() {
		return userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@Override
	public String toString() {
		return "TaobaoAccessToken [userId=" + userId + ", userNick=" + userNick
				+ ", refreshToken=" + refreshToken + ", accessToken="
				+ accessToken + "]";
	}
	
}
