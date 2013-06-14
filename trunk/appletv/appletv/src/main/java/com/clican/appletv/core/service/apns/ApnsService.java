package com.clican.appletv.core.service.apns;

public interface ApnsService {

	public void registerToken(String token);

	public void sendMessage(String message);
}
