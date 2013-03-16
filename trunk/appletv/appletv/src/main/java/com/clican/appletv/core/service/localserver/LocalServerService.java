package com.clican.appletv.core.service.localserver;

public interface LocalServerService {

	public void registerLocalServer(String outerIP, String innerIP);

	public String retriveLocalServer(String outerIP);
}
