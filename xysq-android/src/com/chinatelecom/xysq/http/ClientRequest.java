package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class ClientRequest {

	private final static String BASE_URL = "http://192.168.1.100:9000/xysq";

	public static void queryAreaAndCommunity() {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(new HttpGet(BASE_URL
					+ "/queryAreaAndCommunity.do"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				JSONObject jsonObj = new JSONObject(responseString);
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {
			Log.e("ClientRequest", "queryAreaAndCommunity", e);
		}
	}
}
