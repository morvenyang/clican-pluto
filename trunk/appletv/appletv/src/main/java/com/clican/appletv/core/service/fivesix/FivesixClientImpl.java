package com.clican.appletv.core.service.fivesix;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;

public class FivesixClientImpl extends BaseClient implements FivesixClient {

	@Override
	public String getPlayURL(String code) {
		String url = springProperty.getFivesixVideoHtmlApi().replace("code",
				code);
		String htmlContent = this.httpGet(url, null, null);
		int start = htmlContent.indexOf("user_ding(");
		if (start == -1) {
			return null;
		} else {
			start = htmlContent.indexOf("user_ding(", start + 1);
			if (start == -1) {
				return null;
			}
		}
		start = start + "user_ding(".length();
		int end = htmlContent.indexOf(",", start);
		if (end == -1) {
			return null;
		}
		String matcherContent = htmlContent.substring(start, end).trim();
		if (StringUtils.isNumeric(matcherContent)) {
			String apiURL = springProperty.getFivesixVideoApi().replace("id",
					matcherContent);
			String jsonContent = this.httpGet(apiURL, null, null);
			JSONArray array = JSONObject.fromObject(jsonContent)
					.getJSONObject("info").getJSONArray("rfiles");
			String playurl = null;
			if (array.size() > 0) {
				playurl = array.getJSONObject(0).getString("url");
			}
			return playurl;
		} else {
			return null;
		}
	}

}
