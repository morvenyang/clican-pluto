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

public class ClientRequest {

	private final static String BASE_URL = "http://192.168.1.100:9000/xysq";

	private static void buildAreas(JSONArray areaListJson,
			List<Area> areaList) {
		try {
			for (int i = 0; i < areaListJson.length(); i++) {
				JSONObject json = areaListJson.getJSONObject(i);
				Area area = new Area();
				areaList.add(area);
				area.setId(json.getLong("id"));
				area.setName(json.getString("name"));
				area.setPinyin(json.getString("pinyin"));
				area.setShortPinyin(json.getString("shortPinyin"));
				areaList.add(area);
//				JSONArray childrenAreaJson = json.getJSONArray("areas");
//				if (childrenAreaJson != null && childrenAreaJson.length() > 0) {
//					List<Area> childrenAreaList = new ArrayList<Area>();
//					area.setAreas(childrenAreaList);
//					buildAreaTree(childrenAreaJson, childrenAreaList);
//				}
//
//				JSONArray communitiesJson = json.getJSONArray("communities");
//				if (communitiesJson != null && communitiesJson.length() > 0) {
//					List<Community> childrenAreaList = new ArrayList<Community>();
//					area.setCommunities(childrenAreaList);
//					for (int j = 0; j < communitiesJson.length(); j++) {
//						JSONObject cj = communitiesJson.getJSONObject(j);
//						Community community = new Community();
//						community.setId(cj.getLong("id"));
//						community.setName(cj.getString("name"));
//						community.setPinyin(cj.getString("pinyin"));
//						community.setShortPinyin(cj.getString("shortPinyin"));
//						childrenAreaList.add(community);
//					}
//				}

			}
		} catch (Exception e) {
			Log.e("ClientRequest", "build area tree error", e);
		}

	}

	public static <T> void queryCityAreas(final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>(){
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
						return new TaskResult(1,null,result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1,"加载地区和小区数据失败",null);
					}
				} catch (Exception e) {
					Log.e("ClientRequest", "queryAreaAndCommunity", e);
				}
				return null;
			}
			@Override
			protected void onPostExecute(TaskResult result) {
				callback.success("/queryAreaAndCommunity.do", result.getResult());
			}
		};
		task.execute(new String[]{});
	}
}
