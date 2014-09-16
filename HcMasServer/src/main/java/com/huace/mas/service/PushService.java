package com.huace.mas.service;

public interface PushService {

	public void pushMsg();

	public void registerToken(String userName,String token,Long projectID);
}
