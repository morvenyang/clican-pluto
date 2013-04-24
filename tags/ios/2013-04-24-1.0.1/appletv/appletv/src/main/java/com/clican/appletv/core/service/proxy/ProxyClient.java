package com.clican.appletv.core.service.proxy;

import com.clican.appletv.core.service.proxy.model.M3u8Download;

public interface ProxyClient {

	public M3u8Download getM3u8Download();

	public void seekDownloadLine(String localPath);
	
	public void startM3u8();

	public String doSyncRequestByM3U8Url(String url, boolean start);

}
