package com.clican.appletv.service;

import java.net.URLEncoder;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class YoukuTestCases extends BaseServiceTestCase {
	
	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}
	
	public void testSearch() throws Exception{
		String keyword = URLEncoder.encode("黑猫警长", "UTF-8");
		String url1 = "http://api.3g.youku.com/layout/phone2/ios/search/"+keyword+"?pg=1&pid=69b81504767483cf&pz=30";
		String url2= "http://api.3g.youku.com/videos/search/"+keyword+"?pg=1&pid=69b81504767483cf&pz=30";
		String url3= "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id=061d6dc26f4b11e19194";
		String url4 = "http://api.3g.youku.com/layout/phone2_1/detail?pid=69b81504767483cf&id=XMzcyNTk3MzY%3D";
		//System.out.println(tudouClient.httpGet(url1));
		//System.out.println(tudouClient.httpGet(url2));
		System.out.println(tudouClient.httpGet(url3));
	}

}
