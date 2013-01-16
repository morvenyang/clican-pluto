package com.clican.appletv.core.service.youku;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.youku.model.YoukuAlbum;

public class YoukuClientImpl extends BaseClient implements YoukuClient {

	@Override
	public YoukuAlbum queryAlbum(String showid) {
		String url = springProperty.getQqVideoApi() + "&id=" + showid;
		if (log.isDebugEnabled()) {
			log.debug("query album url=" + url);
		}
		String jsonStr = httpGet(url, null, null);
		JSONObject albumJson = JSONObject.fromObject(jsonStr).getJSONObject("detail");
		YoukuAlbum album = new YoukuAlbum();

		album.setPerformer(StringUtils.join(albumJson.getJSONArray("performer")
				.iterator(), ","));
		album.setArea(albumJson.getString("area"));
		album.setDirector(StringUtils.join(albumJson.getJSONArray("director")
				.iterator(), ","));
		album.setShowid(albumJson.getString("showid"));
		album.setImg(albumJson.getString("img"));
		album.setTitle(albumJson.getString("title"));
		album.setShowdate(albumJson.getString("showdate"));
		album.setDesc(albumJson.getString("desc"));

		return album;
	}

}
