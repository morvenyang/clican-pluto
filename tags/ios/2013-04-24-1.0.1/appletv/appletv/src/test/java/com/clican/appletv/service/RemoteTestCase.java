package com.clican.appletv.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.service.tudou.TudouClientImpl;
import com.clican.appletv.remote.Response;
import com.clican.appletv.remote.ResponseParser;

public class RemoteTestCase extends BaseServiceTestCase {

	private TudouClientImpl tudouClient;
	private Map<String, String> headers = new HashMap<String, String>();

	public RemoteTestCase() {
		headers.put("Accept-Language", "zh-cn");
		headers.put("Viewer-Only-Client", "1");
		headers.put("Pragma", "no-cache");
		headers.put("Client-DAAP-Version", "3.11");
		headers.put("User-Agent", "Remote/599.20");
		headers.put("Client-iTunes-Sharing-Version",
				"Client-iTunes-Sharing-Version");
		headers.put("Accept", "*/*");
		headers.put("Client-ATV-Sharing-Version", "1.2");
		headers.put("Connection", "keep-alive");
		headers.put("Accept-Encoding", "gzip");
		headers.put("Accept", "*/*");
		headers.put("Accept", "*/*");
	}

	public void setTudouClient(TudouClientImpl tudouClient) {
		this.tudouClient = tudouClient;
	}

	public void testGetServerInfo() throws Exception {
		String url = "http://10.0.1.4:3689/server-info";
		byte[] data1 = tudouClient.httpGetByData(url, headers, null);
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("remote/server-info");
		byte[] data2 = new byte[is.available()];
		is.read(data2);
		boolean isEqual = true;
		for (int i = 0; i < data1.length && i < data2.length; i++) {
			if (data1[i] != data2[i]) {
				isEqual = false;
			}
		}
		assertTrue(isEqual);
		Response resp = ResponseParser.performParse(data1);
		log.debug(resp.toString());
	}

	public void testSelect() throws Exception {
		String hsgid = "00000000-4342-b555-cb30-ff3a56dd45f9";
		String loginurl = "http://10.0.1.4:3689/login?hasFP=1&hsgid=" + hsgid;
		byte[] data = tudouClient.httpGetByData(loginurl, headers, null);
		Response login = ResponseParser.performParse(data);
		String sessionId = login.getNested("mlog").getNumberString("mlid");
		log.debug("sessionId:" + sessionId);
		
		String controlpromptentry = "http://10.0.1.4:3689/ctrl-int/1/controlpromptentry?prompt-id=48&session-id="+sessionId;

		
		
		Map<String,String> hs = new HashMap<String,String>(headers);
		hs.put("Content-Type", "application/x-www-form-urlencoded");
		InputStream is1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("remote/select");
		byte[] select = new byte[is1.available()];
		is1.read(select);
		
		tudouClient.httpPost(controlpromptentry, null, select, null, null, null, hs, null);

	}
	
	public void testMenu() throws Exception {
		String hsgid = "00000000-4342-b555-cb30-ff3a56dd45f9";
		String loginurl = "http://10.0.1.4:3689/login?hasFP=1&hsgid=" + hsgid;

		byte[] data = tudouClient.httpGetByData(loginurl, headers, null);
		Response login = ResponseParser.performParse(data);
		String sessionId = login.getNested("mlog").getNumberString("mlid");
		log.debug("sessionId:" + sessionId);
		
		String controlpromptentry = "http://10.0.1.4:3689/ctrl-int/1/controlpromptentry?prompt-id=48&session-id="+sessionId;

		Map<String,String> hs = new HashMap<String,String>(headers);
		hs.put("Content-Type", "application/x-www-form-urlencoded");
		InputStream is2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("remote/menu");
		byte[] menu = new byte[is2.available()];
		is2.read(menu);
		tudouClient.httpPost(controlpromptentry, null, menu, null, null, null, hs, null);

	}
}
