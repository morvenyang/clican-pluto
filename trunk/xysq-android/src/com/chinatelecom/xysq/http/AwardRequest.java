package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.chinatelecom.xysq.bean.Award;
import com.chinatelecom.xysq.bean.AwardUser;
import com.chinatelecom.xysq.bean.Lottery;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;

public class AwardRequest {

	public static void lottery(final User user, final HttpCallback callback) {

		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/lottery.do";
					HttpGet get = new HttpGet(url);

					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient.execute(get);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						Lottery lottery = new Lottery();
						lottery.setLottery(jsonObj.getInt("lottery"));
						lottery.setMoney(jsonObj.getInt("money"));
						lottery.setTotalMoney(jsonObj.getInt("totalMoney"));
						lottery.setShare(jsonObj.getBoolean("share"));
						return new TaskResult(1, null, lottery);
					} else if (statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
						user.setJsessionid("");
						return new TaskResult(-1, "请先登录", null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "抽奖失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "lottery", e);
				}
				return new TaskResult(-1, "抽奖失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/lottery.do", result.getResult());
				} else {
					callback.failure("/lottery.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});

	}

	public static void queryAwardUser(final User user,
			final HttpCallback callback) {

		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/queryAwardUser.do";
					HttpGet get = new HttpGet(url);

					get.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					HttpResponse response = httpclient.execute(get);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						AwardUser au = new AwardUser();
						au.setMoney(jsonObj.getInt("money"));
						JSONArray awardsJson = jsonObj.getJSONArray("awards");
						List<Award> awards = new ArrayList<Award>();
						for(int i=0;i<awardsJson.length();i++){
							JSONObject awardJson = awardsJson.getJSONObject(i);
							Award award = new Award();
							award.setAmount(awardJson.getInt("amount"));
							award.setCost(awardJson.getInt("cost"));
							award.setId(awardJson.getLong("id"));
							award.setName(awardJson.getString("name"));
							award.setRealGood(awardJson.getBoolean("realGood"));
							awards.add(award);
						}
						au.setAwards(awards);
						return new TaskResult(1, null, au);
					} else if (statusLine.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
						user.setJsessionid("");
						return new TaskResult(-1, "请先登录", null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载兑换品数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryAwardUser", e);
				}
				return new TaskResult(-1, "加载兑换品数据失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryAwardUser.do", result.getResult());
				} else {
					callback.failure("/queryAwardUser.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});

	}
}
