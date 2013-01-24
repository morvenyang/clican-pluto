package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TopatsItemcatsGetRequest;
import com.taobao.api.response.TopatsItemcatsGetResponse;

public class TaobaoTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;
	private TaobaoClient taobaoRestClient;

	
	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setTaobaoRestClient(TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
	}

	public void testGetCategorys() throws Exception {
		TopatsItemcatsGetRequest req = new TopatsItemcatsGetRequest();
		TopatsItemcatsGetResponse response = taobaoRestClient.execute(req);
		log.debug(response);
	}
}
