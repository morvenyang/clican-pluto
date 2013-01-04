package com.clican.appletv.core.service.tudou;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.clican.appletv.core.service.tudou.model.TudouVideo;

public class TudouClientImpl implements TudouClient {

	private final static Log log = LogFactory.getLog(TudouClientImpl.class);

	@Override
	public List<TudouVideo> queryVideos(String url) {
		String jsonStr = httpGet(url);
		List<TudouVideo> result = new ArrayList<TudouVideo>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONArray array = JSONArray.fromObject(jsonStr);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TudouVideo tv = (TudouVideo) JSONObject.toBean(obj,
						TudouVideo.class);
				result.add(tv);
			}
		}
		return result;
	}

	@Override
	public String convertToATVXml(List<TudouVideo> videos) {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><scroller id=\"com.sample.movie-shelf\"><items><shelf id=\"shelf_1\"><sections><shelfSection><items>";
		String end = "</items></shelfSection></sections></shelf>";
		StringBuffer result = new StringBuffer(str);
		for (int i = 0; i < videos.size(); i++) {
			TudouVideo tv = videos.get(i);
			String s = "<moviePoster id=\"shelf_item_"
					+ i
					+ "\" accessibilityLabel=\"\" onSelect=\"atv.loadURL('http://vr.tudou.com/v2proxy/v2.m3u8?st=2&it="
					+ tv.getItemid()
					+ "');\" onPlay=\"atv.loadURL('http://vr.tudou.com/v2proxy/v2.m3u8?st=2&it="
					+ tv.getItemid()
					+ "');\"><title>"
					+ tv.getTitle()
					+ "</title><image>"
					+ tv.getPicurl()
					+ "</image><defaultImage>resource://Poster.png</defaultImage></moviePoster>";
			result.append(s);
		}
		result.append(end);
		return result.toString();
	}

	private String httpGet(String url) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + response.getStatusLine() + " for url:"
						+ url);
			}

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			return new String(os.toByteArray(), "utf-8");
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
