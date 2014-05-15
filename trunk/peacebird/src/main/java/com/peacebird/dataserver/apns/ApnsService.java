package com.peacebird.dataserver.apns;

public interface ApnsService {

	public void sendMessage(String message, String token);
}
