package com.clican.appletv.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.model.Baibian;

public class BaibianClientImpl extends BaseClient implements BaibianClient {

	private String token;

	@Override
	public void login() {
		String json = this.httpGet(springProperty.getBaibianLoginApi());
		if (StringUtils.isNotEmpty(json)) {
			String token = JSONObject.fromObject(json).getString("token");
			if (StringUtils.isNotEmpty(token)) {
				this.token = token;
			}
		}
	}

	@Override
	public List<Baibian> queryVideos(int page) {
		this.checkCache();

		String jsonContent;
		String url = springProperty.getBaibianChannelApi() + "&token=" + token
				+ "&&page=" + page;

		if (log.isDebugEnabled()) {
			log.debug(url);
		}
		if (cacheMap.containsKey(url)) {
			jsonContent = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					jsonContent = cacheMap.get(url);
				} else {
					jsonContent = httpGet(url, null, null);
					List<Baibian> result = convertToVideos(jsonContent);
					cacheMap.put(url, jsonContent);
					return result;
				}
			}
		}
		List<Baibian> result = convertToVideos(jsonContent);
		return result;
	}

	private List<Baibian> convertToVideos(String jsonContent) {
		Pattern fiveSixPattern = Pattern.compile(
				springProperty.getFivesixCodePattern(), Pattern.DOTALL);
		List<Baibian> result = new ArrayList<Baibian>();
		JSONArray films = JSONObject.fromObject(jsonContent).getJSONArray(
				"films");
		for (int i = 0; i < films.size(); i++) {
			JSONObject film = films.getJSONObject(i);
			Baibian baibian = new Baibian();
			baibian.setTitle(film.getString("title"));
			baibian.setImageUrl(film.getString("imageUrl"));
			Long id = film.getLong("id");
			baibian.setId(id);
			String contentUrl = film.getString("contentUrl");
			Matcher fiveSixMatcher = fiveSixPattern.matcher(contentUrl);
			if (fiveSixMatcher.matches()) {
				String code = fiveSixMatcher.group(2);
				baibian.setMediaUrl(springProperty.getSystemServerUrl()
						+ "/ctl/fivesix/playVideoByCode.xml?code=" + code
						+ "');");
			}
			result.add(baibian);
		}
		return result;
	}
}
