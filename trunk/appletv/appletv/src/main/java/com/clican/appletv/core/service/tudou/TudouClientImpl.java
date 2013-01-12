package com.clican.appletv.core.service.tudou;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.tudou.enumeration.Channel;
import com.clican.appletv.core.service.tudou.model.ListView;
import com.clican.appletv.core.service.tudou.model.TudouAlbum;
import com.clican.appletv.core.service.tudou.model.TudouVideo;

public class TudouClientImpl extends BaseClient implements TudouClient {

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private List<ListView> convertToVideos(String jsonStr, Channel channel) {
		List<ListView> result = new ArrayList<ListView>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONArray itemArray = null;
			JSONArray albumArray = null;
			if (channel.isAlbum()) {
				albumArray = JSONObject.fromObject(jsonStr)
						.getJSONObject("wirelessAlbum").getJSONArray("albums");
			} else if (channel == Channel.Search) {
				JSONObject wirelessSearchResult = JSONObject
						.fromObject(jsonStr).getJSONObject(
								"wirelessSearchResult");
				albumArray = wirelessSearchResult.getJSONArray("albums");
				itemArray = wirelessSearchResult.getJSONArray("items");
			} else {
				itemArray = JSONObject.fromObject(jsonStr)
						.getJSONArray("items");
			}
			if (albumArray != null) {
				for (int i = 0; i < albumArray.size(); i++) {
					JSONObject obj = albumArray.getJSONObject(i);
					TudouAlbum tv = (TudouAlbum) JSONObject.toBean(obj,
							TudouAlbum.class);
					tv.setAreaDesc(obj.getString("areas_desc"));
					tv.setTypeDesc(obj.getString("type_desc"));
					result.add(tv);
				}
			}

			if (itemArray != null) {
				for (int i = 0; i < itemArray.size(); i++) {
					JSONObject obj = itemArray.getJSONObject(i);
					TudouVideo tv = (TudouVideo) JSONObject.toBean(obj,
							TudouVideo.class);
					result.add(tv);
				}
			}

		}
		return result;
	}

	@Override
	public TudouAlbum queryAlbum(String data) {
		List<ListView> result = new ArrayList<ListView>();
		JSONObject albumJson = JSONObject.fromObject(data);
		TudouAlbum album = (TudouAlbum) JSONObject.toBean(albumJson,
				TudouAlbum.class);
		album.setAreaDesc(albumJson.getString("area_desc"));
		album.setTypeDesc(albumJson.getString("type_desc"));
		JSONArray array = albumJson.getJSONArray("albumitems");
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			ListView tv = (ListView) JSONObject.toBean(obj, ListView.class);
			result.add(tv);
		}
		album.setAlbumitems(result);
		return album;
	}

	@Override
	public List<ListView> queryVideos(String keyword, Channel channel,
			Integer page) {

		this.checkCache();

		String jsonStr;
		String url = null;
		if (channel == Channel.Recommand) {
			url = springProperty.getTudouRecommendApi() + "&page=" + page;
		} else if (channel == Channel.Search) {
			try {
				url = springProperty.getTudouSearchApi() + "&pageNo="
						+ (page + 1) + "&kw="
						+ URLEncoder.encode(keyword, "utf-8");
			} catch (Exception e) {
				log.error("", e);
			}

		} else if (channel.isAlbum()) {
			url = springProperty.getTudouAlbumChannelApi() + "&cid="
					+ channel.getValue() + "&page=" + page;
		} else {
			url = springProperty.getTudouChannelApi() + "&columnid="
					+ channel.getValue() + "&page=" + page;
		}

		if (log.isDebugEnabled()) {
			log.debug(url);
		}
		if (cacheMap.containsKey(url)) {
			jsonStr = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					jsonStr = cacheMap.get(url);
				} else {
					if (channel == Channel.Search) {
						jsonStr = httpGet(url, null,
								springProperty.getSystemHttpconnectionTimeout());
					} else {
						jsonStr = httpGet(url, null, null);
					}
					List<ListView> result = convertToVideos(jsonStr, channel);
					if (channel != Channel.Search && result.size() > 0) {
						cacheMap.put(url, jsonStr);
					}
					return result;
				}
			}
		}
		List<ListView> result = convertToVideos(jsonStr, channel);
		return result;

	}

	@Override
	public List<String> queryKeywords(String q) {
		String url = springProperty.getTudouKeywordSearchApi() + "?q=";
		try {
			url += URLEncoder.encode(q, "utf-8");
		} catch (Exception e) {
			log.error("", e);
		}
		String jsonStr = httpGet(url, null, null);
		JSONArray array = JSONArray.fromObject(jsonStr);
		if (log.isDebugEnabled()) {
			log.debug("keywrod size=" + array.size());
		}
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			result.add(array.getString(i));
		}
		return result;
	}

}
