package com.clican.appletv.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.tudou.TudouClient;

public class ClientTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;

	private TudouClient tudouClient;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setTudouClient(TudouClient tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testTudouPattern() throws Exception {
		Pattern pattern = Pattern.compile(springProperty.getTudouCodePattern());
		Matcher matcher = pattern
				.matcher("http://www.tudou.com/programs/view/r1rJ-fxW_hE/?resourceId=95575290_06_05_99");
		String code = null;
		if (matcher.matches()) {
			code = matcher.group(1);
		}
		assertEquals("r1rJ-fxW_hE", code);

		matcher = pattern
				.matcher("http://www.tudou.com/programs/view/r1rJ-fxW_hE/");
		code = null;
		if (matcher.matches()) {
			code = matcher.group(1);
		}
		assertEquals("r1rJ-fxW_hE", code);
	}

	public void testSohuUrlPattern() throws Exception {
		Pattern pattern = Pattern.compile(springProperty.getSohuURLPattern());
		Matcher tudouMatcher = pattern
				.matcher("http://my.tv.sohu.com/u/vw/51930969?xuid=e6762432");

		assertTrue(tudouMatcher.matches());
	}

	public void testSohuIdPattern() throws Exception {
		Pattern pattern = Pattern.compile(springProperty.getSohuIdPattern(),
				Pattern.DOTALL);
		String html = "        var vid = '51930969';\r\n"
				+ "var _uid = '5866814';\r\n" + "var cateId ='124105';\r\n"
				+ "var _playListId='';\r\n";
		Matcher matcher = pattern.matcher(html);
		String code = null;
		if (matcher.matches()) {
			code = matcher.group(1);
		}
		assertEquals(code, "51930969");
	}

	public void testFivesixIdPattern() throws Exception {
		String s1 = "http://www.56.com/w22/play_album-aid-9938908_vid-ODQ0MDc4OTM.html/1030_yesweibo.html";
		String s2 = "http://www.56.com/u24/v_ODA1NDE2NjE.html";
		String s3= "http://www.56.com/u95/v_NTAxNTY5MTY.html/1030_zhrdy001.html";
		Pattern pattern = Pattern
				.compile("http://www\\.56\\.com/.*(vid-|/v_)(\\p{Alnum}*)\\.html.*");
		Matcher matcher = pattern.matcher(s1);
		String code = null;
		if (matcher.matches()) {
			code = matcher.group(2);
		}
		assertEquals("ODQ0MDc4OTM", code);

		matcher = pattern.matcher(s2);
		if (matcher.matches()) {
			code = matcher.group(2);
		}
		assertEquals("ODA1NDE2NjE", code);

		matcher = pattern.matcher(s3);
		if (matcher.matches()) {
			code = matcher.group(2);
		}
		assertEquals("NTAxNTY5MTY", code);
		
	}

	public void testQQIdPattern() throws Exception {
		String s1 = "http://v.qq.com/cover/k/k4aj50t7k1d3re0.html";
		Pattern pattern = Pattern
				.compile("http://v\\.qq\\.com/.*/(\\p{Alnum}*)\\.html");
		Matcher matcher = pattern.matcher(s1);
		String code = null;
		if (matcher.matches()) {
			code = matcher.group(1);
		}
		assertEquals("k4aj50t7k1d3re0", code);

	}

	public void testYoukuCodePattern() throws Exception {
		String s2="http://player.youku.com/player.php/sid/XNTAzNzgyNjA0/v.swf";
		String s1 = "http://v.youku.com/v_show/id_XMzA5MzY=.html";
		Pattern pattern = Pattern.compile(springProperty
				.getYoukuShowidPattern());
		Matcher matcher1 = pattern.matcher(s1);
		String code1 = null;
		if (matcher1.matches()) {
			code1 = matcher1.group(1);
		}
		assertEquals("XMzA5MzY=", code1);
		Matcher matcher2 = pattern.matcher(s2);
		String code2 = null;
		if (matcher2.matches()) {
			code2 = matcher2.group(2);
		}
		assertEquals("XNTAzNzgyNjA0", code2);
	}

}
