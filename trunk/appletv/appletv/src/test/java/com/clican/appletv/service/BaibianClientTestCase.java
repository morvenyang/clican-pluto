package com.clican.appletv.service;

import java.util.List;

import com.clican.appletv.core.model.Baibian;
import com.clican.appletv.core.service.BaibianClient;

public class BaibianClientTestCase extends BaseServiceTestCase {

	private BaibianClient baibianClient;

	public void setBaibianClient(BaibianClient baibianClient) {
		this.baibianClient = baibianClient;
	}

	public void testQueryVideos() throws Exception {
//		List<Baibian> result = baibianClient.queryVideos(0);
//		assertEquals(0, result.size());
	}
}
