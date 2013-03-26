package com.clican.appletv.service;

import java.util.HashMap;
import java.util.Map;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class TudouClientTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}
	
	public void testHtml() throws Exception {
		Map<String,String> headers = new HashMap<String,String>();
		
		//http://www.youku.com/v_olist/c_96_a__s__g__r__lg__im__st__mt__tg__d_1_et_0_fv_0_fl__fc__fe__o_7_p_1.html
		//http://www.tudou.com/cate/ach22a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe3pa1.html
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22");
		String content =tudouClient.httpGet("http://www.youku.com/v_olist/c_96_a__s__g__r__lg__im__st__mt__tg__d_1_et_0_fv_0_fl__fc__fe__o_7_p_1.html",headers,null);
		System.out.println(content);
	}
}
