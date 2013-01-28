package com.clican.appletv.service;

import java.util.HashMap;
import java.util.Map;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.TaobaoClientImpl;
import com.taobao.api.TaobaoClient;

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
		// List<TaobaoCategory> list = taobaoClient.getTopCategories();
		// StringBuffer sb = new StringBuffer();
		// TaobaoCategory.toString(list, sb, "\n");
		// log.debug(sb.toString());
	}

	public void testGetCategorysByParentId() throws Exception {
		// ItemcatsGetRequest req = new ItemcatsGetRequest();
		// req.setFields("cid,parent_cid,name,is_parent");
		// req.setParentCid(50065355L);
		// ItemcatsGetResponse response = taobaoRestClient.execute(req);
		// List<ItemCat> itemCats = response.getItemCats();
		// log.debug(itemCats.size());
	}

	public void testLogin() throws Exception {
		TaobaoClientImpl client = (TaobaoClientImpl) taobaoClient;
		Map<String, String> nameValueMap = new HashMap<String, String>();
		nameValueMap.put("TPL_username", "clicanclican");
		nameValueMap.put("TPL_password", "clican@810428");
		PostResponse pr = client.httpPost(
				"https://login.taobao.com/member/login.jhtml", null,
				nameValueMap, "application/x-www-form-urlencoded", "utf-8",
				null, null);
		Map<String, String> header2 = new HashMap<String, String>();
		header2.put("Cookie", pr.getCookieString());
		// String content2 =
		// client.httpGet("http://favorite.taobao.com/collect_list.htm",header2,null);
		// http://favorite.taobao.com/json/collect_list_chunk.htm?itemtype=1&isBigImgShow=true&orderby=time&startrow=0&chunkSize=12&chunkNum=1&deleNum=0
		PostResponse pr2 = client.httpGetForCookie(
				"http://favorite.taobao.com/collect_list.htm", header2, null);
		log.debug(pr2.getContent());
		String token = pr2.getCookieMap().get("_tb_token_");
		log.debug("token=" + token);
		assertNotNull(token);
		taobaoClient.addFavorite(1, 1, null, null, 12729301574L,"_tb_token_="+token+";"+ pr.getCookieString(), token);
	}

	public void testAddFavorite() throws Exception {
		taobaoClient.addFavorite(1, 1, null, null, 18180872462L, null, null);
	}
}
