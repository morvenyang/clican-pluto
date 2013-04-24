package com.clican.appletv.service;

import com.clican.appletv.core.service.qq.QQClient;
import com.clican.appletv.core.service.qq.enumeration.Channel;

public class QQClientTestCase extends BaseServiceTestCase {

	private QQClient qqClient;

	public void setQqClient(QQClient qqClient) {
		this.qqClient = qqClient;
	}

	public void testQueryKeywords() throws Exception {
		qqClient.queryKeywords("jb");
	}
	
	public void testQueryVideos() throws Exception {
		qqClient.queryVideos(null, Channel.Recommand, 0);
	}
}
