package com.clican.appletv.core.service.localserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clican.appletv.core.service.BaseClient;

public class LocalServerServiceImpl extends BaseClient implements
		LocalServerService {

	private Map<String, String> map = new ConcurrentHashMap<String, String>();

	@Override
	public void registerLocalServer(String outerIP, String innerIP) {
		map.put(outerIP, innerIP);
	}

	@Override
	public String retriveLocalServer(String outerIP) {
		return map.get(outerIP);
	}

}
