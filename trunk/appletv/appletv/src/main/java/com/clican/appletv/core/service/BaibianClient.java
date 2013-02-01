package com.clican.appletv.core.service;

import java.util.List;

import com.clican.appletv.core.model.Baibian;

public interface BaibianClient {

	
	public void login();
	public List<Baibian> queryVideos(int page);
	
	
}
