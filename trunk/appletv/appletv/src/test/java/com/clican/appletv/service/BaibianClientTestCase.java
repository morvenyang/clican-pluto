package com.clican.appletv.service;

import java.util.regex.Pattern;

import com.clican.appletv.core.service.BaibianClient;

public class BaibianClientTestCase extends BaseServiceTestCase {

	private BaibianClient baibianClient;

	public void setBaibianClient(BaibianClient baibianClient) {
		this.baibianClient = baibianClient;
	}

	public void testQueryVideos() throws Exception {
		String html = "<embed src='http://player.56.com/v_ODUwOTczNzI.swf/1030_irockys.swf'  type='application/x-shockwave-flash' width='720' height='405' allowFullScreen='true' allowNetworking='all' allowScriptAccess='always'></embed>";
		Pattern fiveSixPattern = Pattern.compile(
				".*http://.*\\.56\\.com/.*(vid-|v_)(\\p{Alnum}*)\\.swf.*", Pattern.DOTALL);
		System.out.println(fiveSixPattern.matcher(html).matches());
	}
}
