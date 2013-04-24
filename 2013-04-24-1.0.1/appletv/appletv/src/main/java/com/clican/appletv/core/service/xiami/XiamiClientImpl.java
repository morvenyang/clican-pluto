package com.clican.appletv.core.service.xiami;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.commons.digester3.Digester;

import com.clican.appletv.common.Music;
import com.clican.appletv.core.service.BaseClient;

public class XiamiClientImpl extends BaseClient implements XiamiClient {

	@Override
	public Music getMusic(String id) {
		String xmlContent = this.httpGet(
				springProperty.getXiamiMusicApi() + id, null, null);
		Digester digester = new Digester();
		InputStream is = null;
		Music music = null;
		try {
			is = new ByteArrayInputStream(xmlContent.getBytes("utf-8"));
			digester.addObjectCreate("trackList/track", Music.class.getName());
			digester.addCallMethod("trackList/track/song_name",
					"setName", 0);
			digester.addCallMethod("trackList/track/album_cover",
					"setSingerPhoto", 0);
			digester.addCallMethod("trackList/track/album_name",
					"setAlbumName", 0);
			digester.addCallMethod("trackList/track/artist_name",
					"setSingerName", 0);
			digester.addCallMethod("trackList/track/location",
					"setMp3Url", 0);
			music = digester.parse(is);
			music.setId(id);
			music.setMp3Url(getMp3Url(music.getMp3Url()));
		} catch (Exception e) {
			log.error("", e);
		}
		return music;
	}

	private String getMp3Url(String location) {
		try {
			int squall = Integer.parseInt(location.substring(0, 1));
			char[] array = location.substring(1).toCharArray();
			int length = array.length / squall;
			int rest = array.length % squall;
			int biglengh = length + (rest == 0 ? 0 : 1);
			String result = "";
			for (int i = 0; i < biglengh; i++) {
				for (int j = 0; j < squall; j++) {
					if (i == length) {
						if (j < rest) {
							result += array[i + j * length + j];
						}
					} else {
						if (j <= rest) {
							result += array[i + j * length + j];
						} else {
							result += array[i + j * length + rest];
						}
					}

				}
			}
			result = URLDecoder.decode(result, "utf-8").replaceAll("\\^", "0");
			return result;
		} catch (Exception e) {
			log.error("", e);
		}

		return null;
	}

}
