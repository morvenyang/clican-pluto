package com.clican.appletv.common;

import java.util.HashMap;
import java.util.Map;

public class PostResponse {

	private String content;
	private Map<String, String> cookieMap = new HashMap<String, String>();
	private Map<String,String> headerMap = new HashMap<String, String>();
	private int status;
	private byte[] data;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, String> getCookieMap() {
		return cookieMap;
	}

	public void setCookieMap(Map<String, String> cookieMap) {
		this.cookieMap = cookieMap;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getCookieString() {
		String result = "";
		for (String key : cookieMap.keySet()) {
			result += key + "=" + cookieMap.get(key) + ";";
		}
		return result;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
