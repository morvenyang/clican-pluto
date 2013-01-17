package com.clican.appletv.core.service.fivesix;

import com.clican.appletv.core.service.BaseClient;

public class FivesixClientImpl extends BaseClient implements FivesixClient {

	@Override
	public String getPlayURL(String code) {
		String url = springProperty.getFivesixVideoApi().replace("code", code);
		String htmlContent = this.httpGet(url, null, null);
		int end = htmlContent.indexOf("hd_qqvga.mp4");
		if (end == -1) {
			return null;
		}
		end = end + "hd_qqvga.mp4".length();
		int start = htmlContent.lastIndexOf("http", end);
		if (start == -1) {
			return null;
		}
		return htmlContent.substring(start, end);
	}

}
