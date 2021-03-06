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

	private static void populateUser(User user, JSONObject userJson)
			throws Exception {
		user.setId(userJson.getLong("id"));
		user.setNickName(userJson.getString("nickName"));
		user.setMsisdn(userJson.getString("msisdn"));
		user.setJsessionid(userJson.getString("jsessionid"));
		user.setAddress(userJson.getString("address"));
		user.setCarNumber(userJson.getString("carNumber"));
		user.setApplyXqnc(userJson.getBoolean("applyXqnc"));
	}

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
							user.setPassword(password);
							populateUser(user,jsonObj.getJSONObject("user"));
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
							populateUser(user,jsonObj.getJSONObject("user"));
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

	public static void forgetPassword(final String password,
			final String msisdn, final String verifyCode,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/forgetPassword.do?"
							+ "password="
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
							populateUser(user,jsonObj.getJSONObject("user"));
							return new TaskResult(1,
									jsonObj.getString("message"), user);
						} else {
							return new TaskResult(-1,
									jsonObj.getString("message"), null);
						}
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "更新密码失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "forgetPassword", e);
				}
				return new TaskResult(-1, "更新密码失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/forgetPassword.do", result.getResult());
				} else {
					callback.failure("/forgetPassword.do", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}

	public static void updateProfile(final String nickName,
			final String address, final String carNumber,final User user,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/updateProfile.do?"
							+ "nickName="
							+ URLEncoder.encode(nickName, "utf-8")
							+ "&address=" + URLEncoder.encode(address, "utf-8")
							+ "&carNumber=" + carNumber;
					Log.d("XYSQ", url);
					
					HttpGet get = new HttpGet(url);
					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient
							.execute(get);
					
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						if (jsonObj.getBoolean("success")) {
							User user = new User();
							populateUser(user,jsonObj.getJSONObject("user"));
							return new TaskResult(1,
									jsonObj.getString("message"), user);
						} else {
							return new TaskResult(-1,
									jsonObj.getString("message"), null);
						}
					}else if(statusLine.getStatusCode()==HttpStatus.SC_UNAUTHORIZED){
						user.setJsessionid("");
						return new TaskResult(-1, "请先登录", null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "更新失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "updateProfile", e);
				}
				return new TaskResult(-1, "更新失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/updateProfile.do", result.getResult());
				} else {
					callback.failure("/updateProfile.do", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}
	
	public static void enableXqnc(final User user,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/enableXqnc.do";
					Log.d("XYSQ", url);
					HttpGet get = new HttpGet(url);
					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient
							.execute(get);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						if (jsonObj.getBoolean("success")) {
							return new TaskResult(1,
									jsonObj.getString("message"), null);
						} else {
							return new TaskResult(-1,
									jsonObj.getString("message"), null);
						}
					}else if(statusLine.getStatusCode()==HttpStatus.SC_UNAUTHORIZED){
						user.setJsessionid("");
						return new TaskResult(-1, "请先登录", null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "申请失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "enableXqnc", e);
				}
				return new TaskResult(-1, "申请失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/enableXqnc.do", result.getResult());
				} else {
					callback.failure("/enableXqnc.do", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}
}
