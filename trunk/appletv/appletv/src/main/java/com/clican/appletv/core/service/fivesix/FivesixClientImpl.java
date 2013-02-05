package com.clican.appletv.core.service.fivesix;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.baibian.model.BaseClient;

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

	@Override
	public List<Object> queryVideos(int channel) {
		Map<String, String> map = new TreeMap<String, String>();
		map.put("page_size", "20");
		map.put("page", "1");
		map.put("v", "1.0");
		map.put("platform", "ios");
		map.put("cid", channel+"");
		String queryString ="";
		for(String key:map.keySet()){
			if(!queryString.endsWith("&")&&StringUtils.isNotEmpty(queryString)){
				queryString=queryString+"&";
			}
			queryString=queryString+key+"="+map.get(key);
		}
		int call_id = (int)Calendar.getInstance().getTimeInMillis();
		String md5str = DigestUtils.md5Hex(queryString);
		String sign = DigestUtils.md5Hex(md5str + "#6bd49bbd29d24b0e972cfbd1f677593b#"+call_id);
		String url = "http://api.m.renren.com/api/v56/getOperaOrVideoByCid?" + queryString + "&api_key=6bd49bbd29d24b0e972cfbd1f677593b&call_id="+call_id+"&sig=" + sign;
		String response = this.httpGet(url, null, null);
		if (log.isDebugEnabled()) {
			log.debug(response);
		}
		return null;
	}

}
