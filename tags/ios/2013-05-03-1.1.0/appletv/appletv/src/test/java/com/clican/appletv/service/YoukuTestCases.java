package com.clican.appletv.service;

import java.net.URLDecoder;
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
		String url5 = "http://iface2.iqiyi.com/php/xyz/iface/?key=8e48946f144759d86a50075555fd5862&did=05dbf3d6abcf6d73&type=json&id=01d0721c86d8ff7f84174ac08e715dfd560871c0&deviceid=01d0721c86d8ff7f84174ac08e715dfd560871c0&version=4.1.3&os=6.1&ua=iPhone4,1&network=1&screen_status=2&udid=794077d012dd611022aac14764f2dd1a46a322bd&ss=2&getother=0&ad=1";
		String url6 = "http://www.longbuluo.com/index.php?s=%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89&submit=%E5%85%A8%E7%AB%99%E6%90%9C%E7%B4%A2";
		System.out.println(URLDecoder.decode("%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89", "UTF-8"));
		System.out.println(URLDecoder.decode("%E5%85%A8%E7%AB%99%E6%90%9C%E7%B4%A2", "UTF-8"));
		//System.out.println(tudouClient.httpGet(url1));
		//System.out.println(tudouClient.httpGet(url2));
		System.out.println(tudouClient.httpGet(url6));
		String gbkkeyword = URLEncoder.encode("行尸走肉", "GBK");
		System.out.println(gbkkeyword);
	}

}
