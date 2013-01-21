package com.clican.appletv.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.sina.SinaClient;

public class SinaClientTestCase extends BaseServiceTestCase {

	private SinaClient sinaClient;
	
	private SpringProperty springProperty;

	public void setSinaClient(SinaClient sinaClient) {
		this.sinaClient = sinaClient;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void testSinaIdPattern() throws Exception {
		
		String url1 = "http://music.sina.com.cn/yueku/i/2841754.html";
		String url2 = "http://music.weibo.com/t/i/100059158.html";
		String url3 = "http://music.s.com/t/i/100059158.html";
		Pattern pattern = Pattern
				.compile("http://music\\.(sina|weibo).*/i/(\\p{Alnum}*).html");
		Matcher m1 = pattern.matcher(url1);
		String id1 = null;
		if (m1.matches()) {
			id1 = m1.group(2);
		}
		assertEquals("2841754", id1);
		
		Matcher m2 = pattern.matcher(url2);
		String id2 = null;
		if (m2.matches()) {
			id2 = m2.group(2);
		}
		assertEquals("100059158", id2);
		Matcher m3 = pattern.matcher(url3);
		
		assertFalse(m3.matches());
	}

	public void testGetSinaMusic() throws Exception {
		BaseClient client = (BaseClient) sinaClient;
		String url = "http://music.sina.com.cn/yueku/intro/musina_mpw_playlist.php";
		String result = client.httpPost(url, "id[]=100059209",
				"application/x-www-form-urlencoded", "utf-8", null, null);
		System.out.println(result);
	}
}
