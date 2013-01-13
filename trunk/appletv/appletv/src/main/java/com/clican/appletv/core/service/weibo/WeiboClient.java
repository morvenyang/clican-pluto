package com.clican.appletv.core.service.weibo;

public interface WeiboClient {

	public boolean saveUserInfo(String accessToken, String deviceId);

	public String getAccessToken(String deviceId);
	
	public String getUid(String deviceId);
}
