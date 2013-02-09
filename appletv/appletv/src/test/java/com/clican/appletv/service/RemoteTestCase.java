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

	public void testAllFlow() throws Exception {
		String hsgid = "00000000-4342-b555-cb30-ff3a56dd45f9";
		String loginurl = "http://10.0.1.4:3689/login?hasFP=1&hsgid=" + hsgid;
		String initurl = "http://10.0.1.4:3689/server-info?hsgid=" + hsgid;

		byte[] data = tudouClient.httpGetByData(loginurl, headers, null);
		Response login = ResponseParser.performParse(data);
		String sessionId = login.getNested("mlog").getNumberString("mlid");
		log.debug("sessionId:" + sessionId);
		tudouClient.httpGetByData(initurl, headers, null);
		String fdsetupurl = "http://10.0.1.4:3689/fp-setup?session-id="
				+ sessionId + "&hsgid=" + hsgid;
		Map<String, String> mapForFpsetup = new HashMap<String, String>(headers);
		mapForFpsetup.put("Content-Type", "application/octet-stream");
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("remote/fpsetup.txt");
		byte[] fpsetupData = new byte[is.available()];
		is.read(fpsetupData);
		PostResponse pr = tudouClient.httpPost(fdsetupurl, null, fpsetupData, null, null, null, mapForFpsetup,null);
		log.debug(pr.getContent());
		String homeshareurl = "http://10.0.1.4:3689/home-share-verify?hspid=0&session-id="
				+ sessionId + "&hsgid=" + hsgid;

		tudouClient.httpGetByData(homeshareurl, headers, null);

	}
}
