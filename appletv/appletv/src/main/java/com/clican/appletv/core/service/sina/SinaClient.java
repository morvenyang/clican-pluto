package com.clican.appletv.core.service.sina;

import com.clican.appletv.common.SinaMusic;

public interface SinaClient {

	public SinaMusic getMusic(String musicId);
	
	public String getMp3Url(String url);
}
