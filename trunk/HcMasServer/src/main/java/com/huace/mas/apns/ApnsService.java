package com.huace.mas.apns;

public interface ApnsService {

	public void sendMessage(String message, String token);
}
