package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.subtitle.SubTitleClient;

public class SubTitleClientTestCase extends BaseServiceTestCase {

	private SubTitleClient subTitleClient;

	public void setSubTitleClient(SubTitleClient subTitleClient) {
		this.subTitleClient = subTitleClient;
	}

	public void testGenerateHash() throws Exception {
		String hash = subTitleClient.generateHash("http://localhost:8080/2.mkv");
		log.debug(hash);
		//b981183b0c0e5c44a9d9f9f75128a190;eb6654c58c4bd30c41a1eed1732dcf95;d2773d95ecbdd1d9b0ee2a863c1c7eaa;d7ae322d69f09e0276d589bd027264ff
	}
	
	public void testDownloadSubTitle() throws Exception{
		String s1 = "bf8b5f6238481c6b3791d3fc0f767a0f";
		String content = subTitleClient.downloadSubTitle("http://localhost:8080/2.mkv");
		log.debug(content);
	}
}
