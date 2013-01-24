package com.clican.appletv.service;

import com.clican.appletv.common.SpringProperty;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;

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
		ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setQ("ATV3");
		req.setFields("approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id");
		ItemsOnsaleGetResponse response = taobaoRestClient.execute(req,
				"620170796559902ZZ91146eead9cdcd0e1696a77da5779c82478075");
		log.debug(response);
	}
}
