package com.clican.appletv.core.service.qq;

import java.text.SimpleDateFormat;
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

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.qq.enumeration.Channel;
import com.clican.appletv.core.service.qq.model.QQVideo;

public class QQClientImpl extends BaseClient implements QQClient {

	private Date lastExpireTime = DateUtils.truncate(new Date(),
			Calendar.DAY_OF_MONTH);

	private Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private List<QQVideo> convertToVideos(String jsonStr, Channel channel) {
		List<QQVideo> result = new ArrayList<QQVideo>();
		if (StringUtils.isNotEmpty(jsonStr)) {
			String content = jsonStr.replaceAll("QZOutputJson=", "");
			JSONArray array = JSONArray.fromObject(content);
			for (int i = 0; i < array.size(); i++) {
				JSONObject cover = array.getJSONObject(i)
						.getJSONObject("cover");
				QQVideo video = (QQVideo) JSONObject.toBean(cover,
						QQVideo.class);
				result.add(video);
			}
		}
		return result;
	}

	@Override
	public List<QQVideo> queryVideos(String keyword, Channel channel,
			Integer page) {

		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (DateUtils.getFragmentInDays(current, Calendar.DAY_OF_MONTH) != DateUtils
				.getFragmentInDays(lastExpireTime, Calendar.DAY_OF_MONTH)) {
			if (log.isDebugEnabled()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				log.debug("clear cache current:" + sdf.format(current)
						+ ",lastExpireTime:" + sdf.format(lastExpireTime));
			}
			lastExpireTime = current;
			cacheMap.clear();
		}
		String jsonStr;
		String url = springProperty.getQqChannelApi() + "&page=" + page
				+ "&auto_id=" + channel.getValue();

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
					List<QQVideo> result = convertToVideos(jsonStr, channel);
					return result;
				}
			}
		}
		List<QQVideo> result = convertToVideos(jsonStr, channel);
		return result;

	}

}
