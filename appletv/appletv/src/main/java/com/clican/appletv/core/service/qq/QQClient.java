package com.clican.appletv.core.service.qq;

import java.util.List;

import com.clican.appletv.core.service.qq.enumeration.Channel;
import com.clican.appletv.core.service.qq.model.QQAlbum;
import com.clican.appletv.core.service.qq.model.QQVideo;

public interface QQClient {

	public List<QQVideo> queryVideos(String keyword, Channel channel,
			Integer page);

	public QQAlbum queryAlbum(String coverId);
	
	public List<String> queryKeywords(String q);

}
