package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TopatsItemcatsGetRequest;
import com.taobao.api.request.TopatsResultGetRequest;
import com.taobao.api.response.TopatsItemcatsGetResponse;
import com.taobao.api.response.TopatsResultGetResponse;

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
		//Long taskId = response.getTask().getTaskId();
		//response.getTask().getStatus();
		TopatsResultGetRequest req1 = new TopatsResultGetRequest();
		req1.setTaskId(3557812L);
		TopatsResultGetResponse rep2 = taobaoRestClient.execute(req1);
		log.debug(rep2);
	}
}
