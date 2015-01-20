package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.chinatelecom.xysq.bean.BroadbandRemind;
import com.chinatelecom.xysq.other.Constants;

public class BroadbandRemindRequest {

	public static void queryBroadbandRemind(final HttpCallback callback,
			final String msisdn) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/queryBroadbandRemind.do?msisdn=" + msisdn;

					Log.d("XYSQ", "queryBroadbandRemindUrl:" + url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						BroadbandRemind remind = new BroadbandRemind();
						if (jsonObj.getBoolean("exist")) {
							remind.setExist(true);
							remind.setBroadBandId(jsonObj
									.getString("broadBandId"));
							remind.setMsisdn(jsonObj.getString("msisdn"));
							remind.setUserName(jsonObj.getString("userName"));
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd", Locale.ENGLISH);
							try {
								remind.setExpiredDate(sdf.parse(jsonObj
										.getString("expiredDate")));
							} catch (Exception e) {
								Log.e("XYSQ", "", e);
							}
						} else {
							remind.setExist(false);
						}
						return new TaskResult(1, "", remind);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "查询宽带提醒失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryBroadbandRemind", e);
				}
				return new TaskResult(-1, "查询宽带提醒失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryBroadbandRemind.do",
							result.getResult());
				} else {
					callback.failure("/queryBroadbandRemind.do",
							result.getCode(), result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
}
