package com.clican.appletv.core.service.tudou;

import java.util.List;

import com.clican.appletv.core.service.tudou.model.TudouVideo;

public interface TudouClient {

	/**
	 * 根据url获得土豆的视频列表
	 * 
	 * @param url
	 * @return
	 */
	public List<TudouVideo> queryVideos(String url);
	

}
