package com.clican.appletv.service;

import java.util.HashMap;
import java.util.Map;

import com.clican.appletv.core.service.tudou.TudouClientImpl;

public class XunleiTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testDownloadWithXunleiSessionid() throws Exception {
		Map<String, String> header = new HashMap<String, String>();
		String url = "http://gdl.lixian.vip.xunlei.com/download?fid=oAqeShliK/dQ2PARKpevbbPy6nWnMu4bAAAAAIEhXWTFClYoGjO0D5mJaV/No4mg&mid=666&threshold=150&tid=2600B083104C5944BD438388D91BB1A6&srcid=16&verno=1&g=81215D64C50A56281A33B40F9989695FCDA389A0&scn=u1&dt=17&ui=5663595&s=468595367&n=0A3A43B50148EB5FE87264C8574886B5B03B6AA4C9A406652E8489418C9900672E978D4C83DE3D303396D91DC9B306695F96864AC9B82A5456A1815DC9C15E32348BDD1AD1DD375965879BC95D4A8ABABA36559C0F57E82E66BF9E2DE7F06E0000&it=1363054490&cc=3881433837396347065&p=0";
		tudouClient.httpGetForCookie(url, header, null);
	}
}
