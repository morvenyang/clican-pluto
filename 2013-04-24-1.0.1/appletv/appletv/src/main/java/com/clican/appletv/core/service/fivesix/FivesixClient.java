package com.clican.appletv.core.service.fivesix;

import java.util.List;

public interface FivesixClient {

	public String getPlayURL(String code);
	
	public List<Object> queryVideos(int channel);
}
