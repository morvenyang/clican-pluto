package com.clican.appletv.core.service.youku;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.baibian.model.BaseClient;
import com.clican.appletv.core.service.youku.model.YoukuAlbum;

public class YoukuClientImpl extends BaseClient implements YoukuClient {

	@Override
	public YoukuAlbum queryAlbum(String showid) {
		String url = springProperty.getYoukuVideoApi() + "&id=" + showid;
		if (log.isDebugEnabled()) {
			log.debug("query album url=" + url);
		}
		String jsonStr = httpGet(url, null, null);
		JSONObject albumJson = JSONObject.fromObject(jsonStr).getJSONObject("detail");
		YoukuAlbum album = new YoukuAlbum();

		if(albumJson.containsKey("performer")){
			album.setPerformer(StringUtils.join(albumJson.getJSONArray("performer")
					.iterator(), ","));
		}
		
		if(albumJson.containsKey("director")){
			album.setPerformer(StringUtils.join(albumJson.getJSONArray("director")
					.iterator(), ","));
		}
		
		if(albumJson.containsKey("area")){
			album.setArea(albumJson.getString("area"));
		}
		
		if(albumJson.containsKey("showdate")){
			album.setArea(albumJson.getString("showdate"));
		}
		if(albumJson.containsKey("desc")){
			album.setArea(albumJson.getString("desc"));
		}
		album.setShowid(showid);
		album.setImg(albumJson.getString("img"));
		album.setTitle(albumJson.getString("title"));

		return album;
	}

}
