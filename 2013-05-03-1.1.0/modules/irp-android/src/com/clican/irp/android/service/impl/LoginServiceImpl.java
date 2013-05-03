package com.clican.irp.android.service.impl;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clican.irp.android.enumeration.ApplicationUrl;
import com.clican.irp.android.exception.HttpException;
import com.clican.irp.android.exception.NotLoginException;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.service.LoginService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginServiceImpl implements LoginService {

	@Inject
	private HttpGateway httpGateway;

	public void login(String userName, String password, String token)
			throws SocketTimeoutException, JSONException, NotLoginException,
			HttpException {
		httpGateway.login(userName, password, token);
	}

	@Override
	public Map<String, String> queryLoginServers()
			throws SocketTimeoutException, JSONException, HttpException {
		JSONObject json = httpGateway.invoke(ApplicationUrl.LOGIN_SERVER_LIST
				.getUrl());
		if (json != null) {
			JSONArray array = json.getJSONArray("customers");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject server = array.getJSONObject(i);
				map.put(server.getString("customerName"),
						server.getString("url"));
			}
			return map;
		}
		return null;
	}

}
