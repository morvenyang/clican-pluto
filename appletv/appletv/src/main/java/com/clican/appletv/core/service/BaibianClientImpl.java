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
		if (StringUtils.isEmpty(token)) {
			login();
		}
		String jsonContent;
		String url = springProperty.getBaibianChannelApi() + "&token=" + token
				+ "&page=" + page;

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
					if (StringUtils.isEmpty(jsonContent) && page == 0) {
						login();
						jsonContent = httpGet(url, null, null);
					}
					List<Baibian> result = convertToVideos(jsonContent);
					if (result.size() == 0 && page == 0) {
						login();
						jsonContent = httpGet(url, null, null);
						result = convertToVideos(jsonContent);
					}
					cacheMap.put(url, jsonContent);
					return result;
				}
			}
		}
		List<Baibian> result = convertToVideos(jsonContent);
		return result;
	}

	private List<Baibian> convertToVideos(String jsonContent) {
		List<Baibian> result = new ArrayList<Baibian>();
		try {
			Pattern fiveSixPattern = Pattern.compile(
					".*http://.*\\.56\\.com/.*(vid-|v_)(\\p{Alnum}*)\\.swf.*", Pattern.DOTALL);
			Pattern youkuPattern = Pattern.compile(
					".*http://v\\.youku\\.com/v_show/id_(.*)\\.swf.*", Pattern.DOTALL);
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
							+ "/ctl/fivesix/playVideoByCode.xml?code=" + code);
					result.add(baibian);
				}else{
					Matcher youkuMatcher = youkuPattern.matcher(contentUrl);
					if(youkuMatcher.matches()){
						String code = youkuMatcher.group(1);
						baibian.setMediaUrl(springProperty.getSystemServerUrl()
								+ "/ctl/youku/album.xml?showid="
								+ code);
						result.add(baibian);
					}
				}
			}

		} catch (Exception e) {
			log.error("", e);
		}
		return result;
	}
}
