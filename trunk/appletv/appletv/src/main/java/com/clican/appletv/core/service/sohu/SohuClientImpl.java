package com.clican.appletv.core.service.sohu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;

public class SohuClientImpl extends BaseClient implements SohuClient {

	@Override
	public String getPlayURL(String url) {
		String htmlContent = this.httpGet(url, null, null);
		Pattern pattern = Pattern.compile(springProperty.getSohuIdPattern(),
				Pattern.DOTALL);
		Matcher matcher = pattern.matcher(htmlContent);
		String code = null;
		if (matcher.matches()) {
			code = matcher.group(1);
		}

		if (StringUtils.isNumeric(code)) {
			String apiURL = springProperty.getSohuVideoApi().replace(
					"vid.json", code + ".json");
			String jsonContent = this.httpGet(apiURL, null, null);
			String m3u8URL = JSONObject.fromObject(jsonContent)
					.getJSONObject("data").getString("url_high");
			return m3u8URL;
		} else {
			return null;
		}
	}

}
