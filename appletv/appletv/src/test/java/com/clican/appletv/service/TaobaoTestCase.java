package com.clican.appletv.service;

import java.net.URLDecoder;
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
		nameValueMap
				.put("ua",
						"029YlJgThc3UTYWOBgrdkVwQXNAd0BxRmY5Zg==|YVJ8T35JckN1QnZGdk16SRY=|YFB+JwdGLFwxXjkZNxcnETEfP2oBbQtmE2JCHUI=|Z1Z4IRM9DyESJhQmCDwHNxkqHiweMAs5CyUWIhAiDDYCMG8w|ZlxyUnIt|ZV5wUHAv|ZFdkShMzWS9ZK1piT2IMYQRvAy9ZOlc3VDkVdBl2WzRTPFw7S2YIZQBrBytDKV8wXn5QcFAPUA==|a1hoRmYIZQBrByd4|alliTBUkCjkKOw4gET8ffB8pCVYJ|aVpoRmY/DjsBOwkneVlqW2xWYFNlVmRXYldlGykFMwY0DzoPNQI1ATIDNQI1BVt7VWY5GUY=|aFtqRB09aANvCWQRYEJ3V3lZDEcLKRopBzAeLxwyATAFJXol|b1h2Lw8vAVhjUmVLeEJxLgAyHDwcMgg5Al0C|blt1LAxaCEYbbB16CmYFag1QY0NtXnBLfEYZRg==|bVp0LQ0tA1phUGdJekBzLAIyHDwcMgEzBzdoNw==|bFl3Lg5YCkQZbh94CGQHaA9SYUFvXXNAckZ3KHc=|c0V3RWtZd0dpXXNHdVtuWnRHdENtXm9adEVyQ21WZUt4T3RaaV9xQHVbaFh2R3Av");
		nameValueMap.put("TPL_checkcode", "");
		nameValueMap.put("TPL_redirect_url", "http://www.taobao.com/");
		nameValueMap.put("from", "tbTop");
		nameValueMap.put("fc", "default");
		nameValueMap.put("style", "default");
		nameValueMap.put("css_style", "");
		nameValueMap
				.put("tid",
						"XOR_1_000000000000000000000000000000_635833224C0D7C777275037E");
		PostResponse pr = client.httpPost(
				"https://login.taobao.com/member/login.jhtml", null,
				nameValueMap, "application/x-www-form-urlencoded", "utf-8",
				null, null);
		// Map<String, String> header = new HashMap<String, String>();
		// header.put("Cookie", pr.getCookie());
		// String content =
		// client.httpGet("http://i.taobao.com/my_taobao.htm",header,null);
		// log.debug(content);
		Map<String, String> header2 = new HashMap<String, String>();
		header2.put("Cookie", pr.getCookies());
		// String content2 =
		// client.httpGet("http://favorite.taobao.com/collect_list.htm",header2,null);
		//http://favorite.taobao.com/json/collect_list_chunk.htm?itemtype=1&isBigImgShow=true&orderby=time&startrow=0&chunkSize=12&chunkNum=1&deleNum=0
		PostResponse pr2 = client
				.httpGetForCookie(
						"http://favorite.taobao.com/collect_list.htm",
						header2, null);
		log.debug(pr2.getContent());
		int start = pr2.getCookies().indexOf("_tb_token_=")
				+ "_tb_token_=".length();
		int end = pr2.getCookies().indexOf(";", start);
		String token = pr2.getCookies().substring(start, end);
		
	}

	public void testAddFavorite() throws Exception {
		taobaoClient.addFavorite(1, 1, null, null, 18180872462L,
				null, null);
	}
}
