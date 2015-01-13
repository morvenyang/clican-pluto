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

import com.chinatelecom.xysq.bean.Index;
import com.chinatelecom.xysq.bean.Poster;
import com.chinatelecom.xysq.other.Constants;

public class IndexRequest {

	private static void buildPosters(JSONArray posterListJson, List<Poster> posterList) {
		try {
			for (int i = 0; i < posterListJson.length(); i++) {
				JSONObject json = posterListJson.getJSONObject(i);
				Poster poster = new Poster();
				posterList.add(poster);
				poster.setId(json.getLong("id"));
				poster.setName(json.getString("name"));
				poster.setHtml5Link(json.getString("html5"));
				poster.setImagePath(json.getString("imagePath"));
				poster.setInnerModule(json.getString("innerModule"));
				if(!json.isNull("storeId")){
					poster.setStoreId(json.getLong("storeId"));
				}
				poster.setType(json.getString("type"));
			}
		} catch (Exception e) {
			Log.e("XYSQ", "build area tree error", e);
		}

	}
	
	public static void queryIndex(final HttpCallback callback,
			final Long communityId) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = null;
					if(communityId==null){
						url = Constants.BASE_URL + "/queryIndex.do";
					}else{
						url = Constants.BASE_URL + "/queryIndex.do?communityId="
								+ communityId;
					}
					HttpResponse response = httpclient.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						List<Poster> posterList = new ArrayList<Poster>();
						buildPosters(jsonObj.getJSONArray("posters"), posterList);
						Index result = new Index();
						result.setPosters(posterList);
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载首页数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryIndex", e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				callback.success("/queryIndex.do", result.getResult());
			}
		};
		task.execute(new String[] {});
	}
}
