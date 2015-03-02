package com.chinatelecom.xysq.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;

public class XqncRequest {

	public static void applyXqnc(final HttpCallback callback,
			final String carNumber,final User user) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/applyXqnc.do?carNumber=" + carNumber;
					Log.d("XYSQ", "applyXqnc:" + url);
					HttpGet get =new HttpGet(url);
					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient
							.execute(get);
					
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						return new TaskResult(1, null, true);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "申请小区挪车失败", false);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "applyXqnc", e);
				}
				return new TaskResult(-1, "申请小区挪车失败", false);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/applyXqnc.do", result.getResult());
				} else {
					callback.failure("/applyXqnc.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
	
	public static void cancelXqnc(final HttpCallback callback,final User user) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/cancelXqnc.do";
					Log.d("XYSQ", "cancelXqnc:" + url);
					HttpGet get =new HttpGet(url);
					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient
							.execute(get);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						return new TaskResult(1, null, true);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "取消小区挪车失败", false);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "cancelXqnc", e);
				}
				return new TaskResult(-1, "取消小区挪车失败", false);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/cancelXqnc.do", result.getResult());
				} else {
					callback.failure("/cancelXqnc.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
}
