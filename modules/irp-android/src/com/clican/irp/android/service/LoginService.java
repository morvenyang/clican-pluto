package com.clican.irp.android.service;

import java.net.SocketTimeoutException;
import java.util.Map;

import org.json.JSONException;

import com.clican.irp.android.exception.HttpException;
import com.clican.irp.android.exception.NotLoginException;

public interface LoginService {

	public void login(String userName, String password, String token)
			throws SocketTimeoutException, JSONException, NotLoginException,
			HttpException;

	public Map<String, String> queryLoginServers()
			throws SocketTimeoutException, JSONException, HttpException;
}
