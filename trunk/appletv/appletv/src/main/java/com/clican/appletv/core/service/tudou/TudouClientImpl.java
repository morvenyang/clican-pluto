package com.clican.appletv.core.service.tudou;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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

	private Date lastExpireTime = DateUtils.truncate(new Date(),
			Calendar.DAY_OF_MONTH);
	private Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	private List<TudouVideo> convertToVideos(String jsonStr) {
		List<TudouVideo> result = new ArrayList<TudouVideo>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONArray array = JSONObject.fromObject(jsonStr).getJSONArray(
					"items");
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
	public List<TudouVideo> queryVideos(String url) {
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (!current.equals(lastExpireTime)) {
			cacheMap.clear();
		}
		String jsonStr;
		if (cacheMap.containsKey(url)) {
			jsonStr = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					jsonStr = cacheMap.get(url);
				} else {
					jsonStr = httpGet(url);
					List<TudouVideo> result = convertToVideos(jsonStr);
					if (result.size() > 0) {
						cacheMap.put(url, jsonStr);
					}
					return result;
				}
			}
		}
		List<TudouVideo> result = convertToVideos(jsonStr);
		return result;

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
