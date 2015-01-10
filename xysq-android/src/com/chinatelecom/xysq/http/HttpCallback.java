package com.chinatelecom.xysq.http;


public interface HttpCallback {

	public void success(String url,Object data);
	
	public void failure(String url,int code, String message);
}
