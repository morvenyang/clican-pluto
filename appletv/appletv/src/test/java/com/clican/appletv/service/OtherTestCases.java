package com.clican.appletv.service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;

import junit.framework.TestCase;

public class OtherTestCases extends TestCase {

	public void testURL() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1359946174919L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()));
		System.out.println(URLDecoder.decode("http://cart.tmall.com/cart/addCartItems.do?_tb_token_=4P3eIOvKsl&add=%7B%22deliveryCityCode%22%3A%22330100%22%2C%22campaignId%22%3A0%2C%22items%22%3A%5B%7B%22itemId%22%3A%2214177211455%22%2C%22skuId%22%3A%2218742103671%22%2C%22quantity%22%3A%221%22%2C%22serviceInfo%22%3A%22%22%7D%5D%7D&tsid=e61a4f0b9d46d01f9bf1714203a1b1f1&retry=1&callback=xpj2&1359946174919","utf-8"));

	}
	
	public void testDecode() throws Exception{
		String s ="LzEyMDkvMTIwOTEwRTBFQzBCQ0FCMkE2MUZGNS5tM3U4";
		System.out.println(new String(Base64.decodeBase64(s.getBytes()),"utf-8"));
	}
}
