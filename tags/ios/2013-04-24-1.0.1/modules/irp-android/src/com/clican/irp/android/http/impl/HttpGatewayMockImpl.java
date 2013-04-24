package com.clican.irp.android.http.impl;

import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectResource;
import android.util.Log;

import com.clican.irp.android.R;
import com.clican.irp.android.enumeration.ApplicationUrl;
import com.clican.irp.android.exception.HttpException;
import com.clican.irp.android.exception.NotLoginException;
import com.clican.irp.android.http.HttpGateway;
import com.google.inject.Singleton;

@Singleton
public class HttpGatewayMockImpl implements HttpGateway {

	@InjectResource(R.string.mock_reports)
	private String mockReports;
	@InjectResource(R.string.mock_login_servers)
	private String mockLoginServers;

	public JSONObject invokeBySession(String url)
			throws SocketTimeoutException, JSONException, NotLoginException,
			HttpException {
		if (url.contains(ApplicationUrl.QUERY_REPORT.getUrl())) {
			return new JSONObject(mockReports);
		}
		return null;
	}

	@Override
	public JSONObject invoke(String url) throws SocketTimeoutException,
			JSONException, HttpException {
		if (url.contains(ApplicationUrl.LOGIN_SERVER_LIST.getUrl())) {
			return new JSONObject(mockLoginServers);
		}
		return null;
	}

	public void login(String userName, String password, String token)
			throws SocketTimeoutException, HttpException {
		Log.d("Mock Login Success", "Mock Login Success");
	}

	@Override
	public byte[] downloadConentBySession(String url)
			throws SocketTimeoutException, NotLoginException, HttpException {
		return null;
	}

}
