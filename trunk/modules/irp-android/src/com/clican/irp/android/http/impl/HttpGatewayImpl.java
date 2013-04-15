package com.clican.irp.android.http.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.clican.irp.android.exception.HttpException;
import com.clican.irp.android.exception.NotLoginException;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.model.HttpProxy;
import com.clican.irp.android.service.ConfigurationService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HttpGatewayImpl implements HttpGateway {

	private static final int SOCKET_TIME_OUT = 10000;
	private static final int CONN_TIME_OUT = 10000;
	public static final int DOWNLOAD_CONN_TIME_OUT = 8000;
	private static final String LOGIN_URL = "/apple/login.do";
	public static String URL_PREFIX = null;

	private String jsessionid;

	@Inject
	private ConfigurationService configurationService;

	public JSONObject invokeBySession(String url)
			throws SocketTimeoutException, JSONException, NotLoginException,
			HttpException {
		if (jsessionid == null) {
			throw new NotLoginException();
		}
		if (!url.contains("?")) {
			url = url + ";jsessionid=" + jsessionid;
		} else {
			url = url.substring(0, url.indexOf("?")) + ";jsessionid="
					+ jsessionid + url.substring(url.indexOf("?"));
		}
		Log.d("read url : ", "" + URL_PREFIX + url);
		HttpGet get = new HttpGet(URL_PREFIX + url);
		JSONObject result = convertStreamToJSONObject(connect(get,
				CONN_TIME_OUT, SOCKET_TIME_OUT));
		if (result.has("code") && result.has("message")) {
			throw new HttpException(result.getString("message"));
		}
		return result;
	}

	@Override
	public byte[] downloadConentBySession(String url)
			throws SocketTimeoutException, NotLoginException, HttpException {
		if (jsessionid == null) {
			throw new NotLoginException();
		}
		if (!url.contains("?")) {
			url = url + ";jsessionid=" + jsessionid;
		} else {
			url = url.substring(0, url.indexOf("?")) + ";jsessionid="
					+ jsessionid + url.substring(url.indexOf("?"));
		}
		Log.d("read url : ", "" + URL_PREFIX + url);
		HttpGet get = new HttpGet(URL_PREFIX + url);
		InputStream is = null;
		ByteArrayOutputStream os = null;
		byte[] data;
		try {
			is = connect(get, CONN_TIME_OUT, SOCKET_TIME_OUT);
			os = new ByteArrayOutputStream();
			data = new byte[is.available()];
			byte[] buffer = new byte[2048];
			int r = -1;
			while ((r = is.read(buffer)) != -1) {
				os.write(buffer, 0, r);
			}
			data = os.toByteArray();
			return data;
		} catch (IOException e) {
			throw new HttpException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					throw new HttpException(e);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					throw new HttpException(e);
				}
			}
		}

	}

	@Override
	public JSONObject invoke(String url) throws SocketTimeoutException,
			JSONException, HttpException {
		Log.d("read url : ", "" + url);
		HttpGet get = new HttpGet(url);
		try {
			JSONObject result = convertStreamToJSONObject(connect(get,
					CONN_TIME_OUT, SOCKET_TIME_OUT));
			if (result.has("code") && result.has("message")) {
				throw new HttpException(result.getString("message"));
			}
			return result;
		} catch (NotLoginException e) {
			throw new HttpException(e);
		}

	}

	public void login(String userName, String password, String token)
			throws SocketTimeoutException, HttpException {
		try {
			JSONObject loginResult = invoke(URL_PREFIX + LOGIN_URL
					+ "?username=" + userName + "&password=" + password
					+ "&token=" + token);
			if (loginResult != null) {
				if (loginResult.has("error")) {
					String message = loginResult.getJSONObject("error")
							.getString("message");
					throw new HttpException(message);
				} else {
					jsessionid = loginResult.getJSONObject("user").getString(
							"jsessionid");
				}
			} else {
				throw new HttpException("Login Failure");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private InputStream connect(HttpUriRequest request, int timeoutConnection,
			int timeoutSocket) throws SocketTimeoutException, NotLoginException {
		Log.d("Set timeout", "Set httpClient's timeout.");

		request.setHeader("User-Agent", "Android");

		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);

		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpProxy httpProxy = configurationService.getHttpProxy();
		if (httpProxy != null) {
			HttpHost proxy = new HttpHost(httpProxy.getProxyHost(),
					httpProxy.getProxyPort());
			httpParameters.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 401) {
				throw new NotLoginException();
			}
			Log.d("Connect success! response status is: ",
					"" + response.getStatusLine());

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				return instream;
			}
		} catch (ClientProtocolException e) {
			throw new SocketTimeoutException("ClientProtocolException.");
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw new SocketTimeoutException("IOException.");
		}

		return null;
	}

	private static JSONObject convertStreamToJSONObject(InputStream is)
			throws JSONException {
		if (null == is) {
			return null;
		}

		InputStreamReader in = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(in);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String streamContent = sb.toString();
		Log.d("RestClient.convertStreamToString", "" + streamContent);
		if (streamContent == null || streamContent.trim().length() == 0) {
			return null;
		}
		return new JSONObject(streamContent);
	}
}