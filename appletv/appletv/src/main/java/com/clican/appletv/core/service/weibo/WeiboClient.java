package com.clican.appletv.core.service.weibo;

import weibo4j.model.StatusWapper;

public interface WeiboClient {

	public boolean saveUserInfo(String accessToken, String deviceId);

	public String getAccessToken(String deviceId);

	public String getUid(String deviceId);

	public void processLongUrl(StatusWapper statusWapper,String accessToken);
}
