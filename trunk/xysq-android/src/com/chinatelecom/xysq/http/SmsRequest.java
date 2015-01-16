package com.chinatelecom.xysq.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class SmsRequest {

	public static void requestSmsCode(final String msisdn,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpPost post = new HttpPost(
							"https://leancloud.cn/1.1/requestSmsCode");
					post.setHeader("X-AVOSCloud-Application-Id",
							"zgdiillmtdo07gx2zwu5xlhubqu0ob6jf4pmd6d80o4r63jr");
					post.setHeader("X-AVOSCloud-Application-Key",
							"8nc8zg36bmlqp00auc8usbz5k641vsym4k5sanlrcclgikzr");
					post.setHeader("Content-Type", "application/json");
					post.setEntity(new StringEntity(
							"{\"mobilePhoneNumber\": \"" + msisdn + "\"}"));
					HttpResponse response = httpclient.execute(post);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						return new TaskResult(1, null, null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "faliure", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "requestSmsCode", e);
				}
				return new TaskResult(-1, "faliure", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/requestSmsCode", "success");
				} else {
					callback.failure("/requestSmsCode", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}

}
