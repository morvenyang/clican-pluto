package com.clican.appletv.service;

import junit.framework.TestCase;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class YyetsClientTestCases extends BaseServiceTestCase {
	
	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testListAPI() throws Exception {
		String url = "http://ziyuan.kehuduan.rryingshi.com:20066/resources?c=movie&page=1";
		String content = tudouClient.httpGet(url);
		System.out.println(content);
	}
	
	public void testVideoAPI() throws Exception {
		String url = "http://ziyuan.kehuduan.rryingshi.com:20066/resources/11057";
		String content = tudouClient.httpGet(url);
		System.out.println(content);
	}
}
