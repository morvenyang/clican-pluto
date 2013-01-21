package com.clican.appletv.core.service.xiami;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clican.appletv.core.service.BaseClient;

public class XiamiClientImpl extends BaseClient implements XiamiClient {

	@Override
	public String getMp3Url(String id) {
		try {
			String xmlContent = this.httpGet(springProperty.getXiamiMusicApi()
					+ id, null, null);
			Pattern pattern = Pattern
					.compile(".*\\<location\\>\\<\\!\\[CDATA\\[(.*)\\]\\]\\>\\</location\\>.*",Pattern.DOTALL);
			Matcher matcher = pattern.matcher(xmlContent);
			String location = null;
			if (matcher.matches()) {
				location = matcher.group(1);
			}
			int squall = Integer.parseInt(location.substring(0, 1));
			char[] array = location.substring(1).toCharArray();
			int length = array.length / squall;
			int rest = array.length % squall;
			int biglengh = length + (rest == 0 ? 0 : 1);
			String result = "";
			for (int i = 0; i < biglengh; i++) {
				for (int j = 0; j < squall; j++) {
					if (i == length) {
						if (j < rest) {
							result += array[i + j * length + j];
						}
					} else {
						if (j <= rest) {
							result += array[i + j * length + j];
						} else {
							result += array[i + j * length + rest];
						}
					}

				}
			}
			result = URLDecoder.decode(result, "utf-8").replaceAll("\\^", "0");
			return result;
		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

}
