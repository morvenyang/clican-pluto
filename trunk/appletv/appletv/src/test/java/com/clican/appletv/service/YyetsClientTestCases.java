package com.clican.appletv.service;

import junit.framework.TestCase;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class YyetsClientTestCases extends BaseServiceTestCase {
	
	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testAPI() throws Exception {
		String url = "http://ziyuan.kehuduan.rryingshi.com:20066/resources?c=movie&page=1";
		String content = tudouClient.httpGet(url);
		System.out.println(content);
	}
}
