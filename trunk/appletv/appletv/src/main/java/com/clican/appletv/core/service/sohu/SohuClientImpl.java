package com.clican.appletv.core.service.sohu;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;

public class SohuClientImpl extends BaseClient implements SohuClient {

	@Override
	public String getPlayURL(String url) {
		String htmlContent = this.httpGet(url, null, null);
		int start = htmlContent.indexOf("var vid=\"");
		if (start == -1) {
			return null;
		}
		start = start + "var vid=\"".length();
		int end = htmlContent.indexOf("\";", start);
		if (end == -1) {
			return null;
		}
		String matcherContent = htmlContent.substring(start, end);
		if (StringUtils.isNumeric(matcherContent)) {
			String apiURL = springProperty.getSohuVideoApi().replace(
					"vid.json", matcherContent);
			String jsonContent = this.httpGet(apiURL, null, null);
			String m3u8URL = JSONObject.fromObject(jsonContent)
					.getJSONObject("data").getString("url_high");
			return m3u8URL;
		} else {
			return null;
		}
	}

}
