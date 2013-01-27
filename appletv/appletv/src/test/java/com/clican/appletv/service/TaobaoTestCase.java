package com.clican.appletv.service;

import java.util.List;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.model.TaobaoCategory;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;

public class TaobaoTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;
	private TaobaoClient taobaoRestClient;
	private com.clican.appletv.core.service.TaobaoClient taobaoClient;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setTaobaoRestClient(TaobaoClient taobaoRestClient) {
		this.taobaoRestClient = taobaoRestClient;
	}

	public void setTaobaoClient(
			com.clican.appletv.core.service.TaobaoClient taobaoClient) {
		this.taobaoClient = taobaoClient;
	}

	public void testGetCategorys() throws Exception {
		List<TaobaoCategory> list = taobaoClient.getTopCategories();
		StringBuffer sb = new StringBuffer();
		TaobaoCategory.toString(list, sb, "\n");
		log.debug(sb.toString());
	}
	
	public void testGetCategorysByParentId() throws Exception {
		ItemcatsGetRequest req = new ItemcatsGetRequest();
		req.setFields("cid,parent_cid,name,is_parent");
		req.setParentCid(50035978L);
		//req.setCids("50035978");
		ItemcatsGetResponse response = taobaoRestClient.execute(req);
		List<ItemCat> itemCats = response.getItemCats();
		log.debug(itemCats.size());
	}
}
