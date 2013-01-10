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
import com.clican.appletv.core.service.qq.model.QQAlbum;
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
			content = content.substring(0, content.length() - 1);
			JSONObject qzOutputJson = JSONObject.fromObject(content);
			if (channel == Channel.Recommand) {
				JSONArray datas = qzOutputJson.getJSONArray("data");
				for (int i = 0; i < datas.size(); i++) {
					JSONArray contents = datas.getJSONObject(i).getJSONArray(
							"contents");
					for (int j = 0; j < contents.size(); j++) {
						JSONObject cover = contents.getJSONObject(j);
						QQVideo video = new QQVideo();
						video.setPic(cover.getString("v_pic"));
						video.setCoverId(cover.getString("id"));
						video.setTitle(cover.getString("title"));
						result.add(video);
					}
				}
			} else {
				JSONArray array = qzOutputJson.getJSONArray("cover");
				for (int i = 0; i < array.size(); i++) {
					JSONObject cover = array.getJSONObject(i);
					QQVideo video = new QQVideo();
					video.setPic(cover.getString("c_pic"));
					video.setCoverId(cover.getString("c_cover_id"));
					video.setTitle(cover.getString("c_title"));
					result.add(video);
				}
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

	@Override
	public QQAlbum queryAlbum(String coverId) {
		String url = springProperty.getQqVideoApi().replace("cidjson",
				coverId.subSequence(0, 1) + "/" + coverId + ".json");
		if (log.isDebugEnabled()) {
			log.debug("query album url=" + url);
		}
		String jsonStr = httpGet(url, null);
		JSONObject albumJson = JSONObject.fromObject(jsonStr);
		QQAlbum album = new QQAlbum();
		
		album.setActor(StringUtils.join(albumJson.getJSONArray("actor").iterator(), ","));
		album.setArea(albumJson.getString("area"));
		album.setDctor(StringUtils.join(albumJson.getJSONArray("dctor").iterator(), ","));
		album.setId(albumJson.getString("id"));
		album.setPic(albumJson.getString("pic"));
		album.setScore(albumJson.getString("score"));
		album.setTt(albumJson.getString("tt"));
		album.setYear(albumJson.getInt("year"));
		album.setDesc(albumJson.getString("desc"));
		
		List<String> vids = new ArrayList<String>();
		JSONArray videos = albumJson.getJSONArray("videos");
		for (int i = 0; i < videos.size(); i++) {
			vids.add(((JSONObject) videos.get(i)).getString("vid"));
		}
		album.setSize(vids.size());
		album.setVids(vids);
		return album;
	}

}
