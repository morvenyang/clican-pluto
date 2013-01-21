package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.xiami.XiamiClient;

public class XiamiClientTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;
	private XiamiClient xiamiClient;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setXiamiClient(XiamiClient xiamiClient) {
		this.xiamiClient = xiamiClient;
	}

	public void testGetMp3Url() throws Exception {
		String mp3Url = xiamiClient.getMp3Url("1769606515");
		assertEquals("http://f1.xiami.net/31406/388159/01%201769606515_1400895.mp3", mp3Url);
	}
}
