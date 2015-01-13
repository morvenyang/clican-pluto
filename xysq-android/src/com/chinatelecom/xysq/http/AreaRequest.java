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

import com.chinatelecom.xysq.bean.Area;
import com.chinatelecom.xysq.bean.Community;

public class AreaRequest {

	private final static String BASE_URL = "http://192.168.1.100:9000/xysq";

	private static void buildAreas(JSONArray areaListJson, List<Area> areaList) {
		try {
			for (int i = 0; i < areaListJson.length(); i++) {
				JSONObject json = areaListJson.getJSONObject(i);
				Area area = new Area();
				areaList.add(area);
				area.setId(json.getLong("id"));
				area.setName(json.getString("name"));
				area.setPinyin(json.getString("pinyin"));
				area.setShortPinyin(json.getString("shortPinyin"));
				area.setFullName(json.getString("fullName"));
			}
		} catch (Exception e) {
			Log.e("XYSQ", "build area tree error", e);
		}

	}
	
	private static void buildCommunities(JSONArray communityListJson, List<Community> communityList) {
		try {
			for (int i = 0; i < communityListJson.length(); i++) {
				JSONObject json = communityListJson.getJSONObject(i);
				Community community = new Community();
				communityList.add(community);
				community.setId(json.getLong("id"));
				community.setName(json.getString("name"));
				community.setPinyin(json.getString("pinyin"));
				community.setDetailAddress(json.getString("detailAddress"));
				community.setShortPinyin(json.getString("shortPinyin"));
			}
		} catch (Exception e) {
			Log.e("XYSQ", "build area tree error", e);
		}

	}

	public static void queryCommunityByArea(final HttpCallback callback,
			final Long areaId) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpResponse response = httpclient.execute(new HttpGet(
							BASE_URL + "/queryCommunityByArea.do?areaId="
									+ areaId));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONArray jsonObj = new JSONArray(responseString);
						List<Community> result = new ArrayList<Community>();
						buildCommunities(jsonObj, result);
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载小区数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryCommunityByArea", e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				callback.success("/queryCommunityByArea.do", result.getResult());
			}
		};
		task.execute(new String[] {});
	}

	public static void queryCityAreas(final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpResponse response = httpclient.execute(new HttpGet(
							BASE_URL + "/queryCityAreas.do"));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONArray jsonObj = new JSONArray(responseString);
						List<Area> result = new ArrayList<Area>();
						buildAreas(jsonObj, result);
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载城市数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryCityAreas", e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				callback.success("/queryCityAreas.do", result.getResult());
			}
		};
		task.execute(new String[] {});
	}
}
