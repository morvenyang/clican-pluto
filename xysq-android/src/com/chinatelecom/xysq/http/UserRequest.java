package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;

public class UserRequest {

	public static void login(final String userName, final String password,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpResponse response = httpclient.execute(new HttpGet(
							Constants.BASE_URL + "/login.do?" + "userName="
									+ userName + "&password="
									+ URLEncoder.encode(password, "utf-8")));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						if (jsonObj.getBoolean("success")) {
							User user = new User();
							user.setNickName(jsonObj.getJSONObject("user")
									.getString("nickName"));
							user.setMsisdn(jsonObj.getJSONObject("user")
									.getString("msisdn"));
							user.setJsessionid(jsonObj.getJSONObject("user")
									.getString("jsessionid"));
							return new TaskResult(1,
									jsonObj.getString("message"), user);
						} else {
							return new TaskResult(-1,
									jsonObj.getString("message"), null);
						}
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "登录失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "login", e);
				}
				return new TaskResult(-1, "登录失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/login.do", result.getResult());
				} else {
					callback.failure("/login.do", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}

	public static void register(final String nickName, final String password,
			final String msisdn, final String verifyCode,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/register.do?"
							+ "nickName="
							+ URLEncoder.encode(nickName, "utf-8")
							+ "&password="
							+ URLEncoder.encode(password, "utf-8") + "&msisdn="
							+ msisdn + "&verifyCode=" + verifyCode;
					Log.d("XYSQ", url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						if (jsonObj.getBoolean("success")) {
							User user = new User();
							user.setNickName(jsonObj.getJSONObject("user")
									.getString("nickName"));
							user.setMsisdn(jsonObj.getJSONObject("user")
									.getString("msisdn"));
							user.setJsessionid(jsonObj.getJSONObject("user")
									.getString("jsessionid"));
							return new TaskResult(1,
									jsonObj.getString("message"), user);
						} else {
							return new TaskResult(-1,
									jsonObj.getString("message"), null);
						}
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "注册失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "register", e);
				}
				return new TaskResult(-1, "注册失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/register.do", result.getResult());
				} else {
					callback.failure("/register.do", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}
}
