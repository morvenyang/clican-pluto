package com.clican.appletv.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clican.appletv.common.SpringProperty;

public class ClientTestCase extends BaseServiceTestCase {

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void testTudouPattern() throws Exception {
		Pattern pattern = Pattern.compile(springProperty.getTudouCodePattern());
		Matcher tudouMatcher = pattern
				.matcher("http://www.tudou.com/programs/view/r1rJ-fxW_hE/?resourceId=95575290_06_05_99");
		String code = null;
		if (tudouMatcher.matches()) {
			code = tudouMatcher.group(1);
		}
		assertEquals(code, "r1rJ-fxW_hE");
	}
	
	
	public void testSohuPattern() throws Exception {
		Pattern pattern = Pattern.compile(springProperty.getSohuURLPattern());
		Matcher tudouMatcher = pattern
				.matcher("http://my.tv.sohu.com/u/vw/51930969?xuid=e6762432");
		
		assertTrue(tudouMatcher.matches());
	}
	
}
