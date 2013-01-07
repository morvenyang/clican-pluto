package com.clican.appletv.core.service.tudou;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

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
	public List<ListView> queryVideos(String keyword, Channel channel,
			Integer page) {
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (!current.equals(lastExpireTime)) {
			lastExpireTime = current;
			cacheMap.clear();
		}
		String jsonStr;
		String url = null;
		if (channel == Channel.Recommand) {
			url = springProperty.getTudouRecommendApi() + "&page=" + page;
		} else if (channel == Channel.Search) {
			url = springProperty.getTudouSearchApi() + "&pageNo=" + (page + 1)
					+ "&kw=" + keyword;
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
					jsonStr = httpGet(url, null);
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
		String url = springProperty.getTudouKeywordSearchApi() + "?q=" + q;
		String jsonStr = httpGet(url, null);
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

	private String httpGet(String url, Map<String, String> headers) {
		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					new HttpHost("web-proxy.china.hp.com", 8080, "http"));
			HttpGet httpGet = new HttpGet(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			httpGet.addHeader("Accept-Encoding", "gzip");
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
			Header contentTypeHeader = response.getFirstHeader("Content-Type");
			Header contentEncodingHeader = response
					.getFirstHeader("Content-Encoding");
			String contentType = contentTypeHeader.getValue();
			String charset = "UTF-8";
			String contentEncoding = null;
			if (StringUtils.isNotEmpty(contentType)) {
				int index = contentType.indexOf("charset=");
				if (index != -1) {
					charset = contentType.substring(index + 8).trim()
							.toLowerCase();
				}
				index = contentType.indexOf(";");
				if (index != -1) {
					contentType = contentType.substring(0, index).trim()
							.toLowerCase();
				}
			}
			if (contentEncodingHeader != null) {
				contentEncoding = contentEncodingHeader.getValue();
			}
			is = entity.getContent();
			os1 = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os1.write(buffer, 0, read);
			}
			if (StringUtils.isNotEmpty(contentEncoding)
					&& contentEncoding.equals("gzip")) {
				os2 = new ByteArrayOutputStream();
				gis = new GZIPInputStream(new ByteArrayInputStream(
						os1.toByteArray()));
				buffer = new byte[1024];
				read = -1;
				while ((read = gis.read(buffer)) != -1) {
					os2.write(buffer, 0, read);
				}
				return new String(os2.toByteArray(), charset);
			} else {
				return new String(os1.toByteArray(), charset);
			}

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
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os1 != null) {
				try {
					os1.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os2 != null) {
				try {
					os2.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

}
