package com.clican.appletv.core.service.sina;

import com.clican.appletv.common.Music;

public interface SinaClient {

	public Music getMusic(String musicId);
	
	public String getMp3Url(String url);
}
