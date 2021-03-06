package com.clican.appletv.service;

import com.clican.appletv.common.Music;
import com.clican.appletv.core.service.xiami.XiamiClient;

public class XiamiClientTestCase extends BaseServiceTestCase {

	private XiamiClient xiamiClient;

	public void setXiamiClient(XiamiClient xiamiClient) {
		this.xiamiClient = xiamiClient;
	}

	public void testGetMp3Url() throws Exception {
		Music musci = xiamiClient.getMusic("1769606515");
		assertEquals(
				"http://f1.xiami.net/31406/388159/01%201769606515_1400895.mp3",
				musci.getMp3Url());
	}
}
