package com.clican.irp.android.http;

import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.clican.irp.android.exception.HttpException;
import com.clican.irp.android.exception.NotLoginException;

public interface HttpGateway {

	public JSONObject invokeBySession(String url) throws SocketTimeoutException,
			JSONException, NotLoginException, HttpException;
	
	public JSONObject invoke(String url) throws SocketTimeoutException,
	JSONException, HttpException;

	public void login(String userName, String password, String token)
			throws SocketTimeoutException, HttpException;
	

}
