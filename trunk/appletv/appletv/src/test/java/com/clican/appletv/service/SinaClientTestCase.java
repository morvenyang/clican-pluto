package com.clican.appletv.service;

import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.sina.SinaClient;

public class SinaClientTestCase extends BaseServiceTestCase{

	private SinaClient sinaClient;
	
	public void setSinaClient(SinaClient sinaClient) {
		this.sinaClient = sinaClient;
	}

	public void testGetSinaMusic() throws Exception {
		BaseClient client = (BaseClient)sinaClient;
		String url = "http://music.sina.com.cn/yueku/intro/musina_mpw_playlist.php";
		String result = client.httpPost(url, "id[]=2841754", "application/x-www-form-urlencoded", "utf-8", null, null);
		System.out.println(result);
	}
}
