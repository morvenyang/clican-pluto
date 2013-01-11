package com.clican.appletv.core.service.qq;

import java.util.List;

import com.clican.appletv.core.service.qq.enumeration.Channel;
import com.clican.appletv.core.service.qq.model.QQAlbum;

public interface QQClient {

	public List<Object> queryVideos(String keyword, Channel channel,
			Integer page);

	public QQAlbum queryAlbum(String coverId);
	
	public List<String> queryKeywords(String q);

}
