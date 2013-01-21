package com.clican.appletv.core.service.sina;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.sina.model.SinaMusic;

public class SinaClientImpl extends BaseClient implements SinaClient {

	@Override
	public SinaMusic getMusic(String musicId) {

		String url = this.springProperty.getSinaMusicApi();
		String jsonContent = this.httpPost(url, "id[]=2841754",
				"application/x-www-form-urlencoded", "utf-8", null, null);
		if (StringUtils.isEmpty(jsonContent)) {
			return null;
		}
		JSONObject result = JSONObject.fromObject(jsonContent).getJSONObject(
				"result");
		SinaMusic sinaMusic = new SinaMusic();
		sinaMusic.setName(result.getString("NAME"));
		sinaMusic.setSingerName(result.getString("SINGERCNAME"));
		sinaMusic.setSingerPhoto(result.getString("SINGERPHOTO"));
		sinaMusic.setStyle(result.getString("STYLE"));
		sinaMusic.setAlbumName(result.getString("ALBUMCNAME"));
		sinaMusic.setMp3Url(result.getString("MP3_URL"));

		return sinaMusic;
	}

	@Override
	public String getMp3Url(String url) {
		String content = this.httpGet(url, null, null);
		if (StringUtils.isNotEmpty(content)) {
			content = content.trim();
			content = content.replaceAll("iask_music_song_url=\"", "");
			content = content.substring(0, content.length() - 2);
			content = content.trim();
		}
		return content;
	}
}
