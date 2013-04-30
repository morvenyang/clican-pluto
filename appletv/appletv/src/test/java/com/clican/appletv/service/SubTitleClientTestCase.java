package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.subtitle.SubTitleClient;

public class SubTitleClientTestCase extends BaseServiceTestCase {

	private SubTitleClient subTitleClient;

	public void setSubTitleClient(SubTitleClient subTitleClient) {
		this.subTitleClient = subTitleClient;
	}

	public void testGenerateHash() throws Exception {
		String hash = subTitleClient.generateHash(SpringProperty.getInstance().getSystemServerUrl()+"/1.mkv");
		log.debug(hash);
	}
}
