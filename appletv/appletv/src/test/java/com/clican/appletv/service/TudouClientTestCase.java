package com.clican.appletv.service;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class TudouClientTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}
	
	public void testHtml() throws Exception {
		String content =tudouClient.httpGet("http://www.tudou.com/cate/ach22a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe3pa1.html");
		System.out.println(content);
	}
}
