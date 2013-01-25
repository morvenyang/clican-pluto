package com.clican.appletv.service;

import com.clican.appletv.core.service.qq.QQClient;

public class QQClientTestCase extends BaseServiceTestCase {

	private QQClient qqClient;

	public void setQqClient(QQClient qqClient) {
		this.qqClient = qqClient;
	}

	public void testQueryKeywords() throws Exception {
		qqClient.queryKeywords("jb");
	}
}
