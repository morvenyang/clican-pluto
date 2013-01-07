package com.clican.appletv.core.service.tudou;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.tudou.enumeration.Channel;
import com.clican.appletv.core.service.tudou.model.ListView;
import com.clican.appletv.core.service.tudou.model.TudouAlbum;
import com.clican.appletv.core.service.tudou.model.TudouVideo;

public class TudouClientImpl implements TudouClient {

	private final static Log log = LogFactory.getLog(TudouClientImpl.class);

	private Date lastExpireTime = DateUtils.truncate(new Date(),
			Calendar.DAY_OF_MONTH);
	private Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private List<ListView> convertToVideos(String jsonStr, Channel channel) {
		List<ListView> result = new ArrayList<ListView>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONArray array = null;
			if (channel != null && channel.isAlbum()) {
				array = JSONObject.fromObject(jsonStr)
						.getJSONObject("wirelessAlbum").getJSONArray("albums");
			} else {
				array = JSONObject.fromObject(jsonStr).getJSONArray("items");
			}
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (channel != null && channel.isAlbum()) {
					TudouAlbum tv = (TudouAlbum) JSONObject.toBean(obj,
							TudouAlbum.class);
					tv.setAreaDesc(obj.getString("areas_desc"));
					tv.setTypeDesc(obj.getString("type_desc"));

					result.add(tv);
				} else {
					TudouVideo tv = (TudouVideo) JSONObject.toBean(obj,
							TudouVideo.class);
					result.add(tv);
				}
			}
		}
		return result;
	}

	@Override
	public TudouAlbum queryAlbum(Channel channel, Long itemid, Integer hd) {
		String url = springProperty.getTudouAlbumVideosApi() + "columnid="
				+ channel.getValue() + "&itemid=" + itemid + "&ishd=" + hd;
		String jsonStr = null;
		boolean cache = false;
		if (cacheMap.containsKey(url)) {
			jsonStr = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					jsonStr = cacheMap.get(url);
				} else {
					jsonStr = httpGet(url, null);
					cache = true;
				}
			}
		}
		List<ListView> result = new ArrayList<ListView>();
		JSONObject albumJson = JSONObject.fromObject(jsonStr);
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
		if (result.size() > 0 && cache) {
			cacheMap.put(url, jsonStr);
		}
		album.setAlbumitems(result);
		return album;
	}

	@Override
	public List<ListView> queryVideos(Channel channel, Integer page) {
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (!current.equals(lastExpireTime)) {
			cacheMap.clear();
		}
		String jsonStr;
		String url = null;
		if (channel == null) {
			url = springProperty.getTudouRecommendApi() + "&page=" + page;
		} else if (channel.isAlbum()) {
			url = springProperty.getTudouAlbumChannelApi() + "&cid="
					+ channel.getValue() + "&page=" + page;
		} else {
			url = springProperty.getTudouChannelApi() + "&columnid="
					+ channel.getValue() + "&page=" + page;
		}
		if (cacheMap.containsKey(url)) {
			jsonStr = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					jsonStr = cacheMap.get(url);
				} else {
					jsonStr = httpGet(url, null);
					List<ListView> result = convertToVideos(jsonStr, channel);
					if (result.size() > 0) {
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
		String url = springProperty.getTudouKeywordSearchApi() + "?q=" + q;
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept-Charset", "utf-8");
		String jsonStr = httpGet(url, headers);
		JSONArray array = JSONArray.fromObject(jsonStr);
		if(log.isDebugEnabled()){
			log.debug("keywrod size="+array.size());
		}
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < array.size(); i++) {
			result.add(array.getString(i));
		}
		return result;
	}

	private String httpGet(String url, Map<String, String> headers) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					new HttpHost("web-proxy.corp.hp.com", 8080, "http"));
			HttpGet httpGet = new HttpGet(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			HttpResponse response = client.execute(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + response.getStatusLine() + " for url:"
						+ url);
			}

			HttpEntity entity = response.getEntity();
			for (Header header : response.getAllHeaders()) {
				if (log.isDebugEnabled()) {
					log.debug(header.getName() + ":" + header.getValue());
				}
			}
			Header contentType = response.getFirstHeader("Content-Type");
			is = entity.getContent();
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			if (contentType != null) {
				if (contentType.getValue().toLowerCase().contains("gbk")) {
					return new String(os.toByteArray(), "GBK");
				}
			}
			return new String(os.toByteArray(), "UTF-8");
		} catch (Exception e) {
			log.error("", e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

}
