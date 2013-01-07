package com.clican.appletv.core.service.tudou;

import java.util.List;

import com.clican.appletv.core.service.tudou.enumeration.Channel;
import com.clican.appletv.core.service.tudou.model.ListView;
import com.clican.appletv.core.service.tudou.model.TudouAlbum;

public interface TudouClient {

	/**
	 * 根据url获得土豆的视频列表
	 * 
	 * @param url
	 * @return
	 */
	public List<ListView> queryVideos(Channel channel, Integer page);

	public TudouAlbum queryAlbum(Channel channel, Long itemid, Integer hd);

}
