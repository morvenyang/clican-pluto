package com.clican.appletv.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.TudouClientImpl;

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
		assertEquals(code, "r1rJ-fxW_hE");
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

}
