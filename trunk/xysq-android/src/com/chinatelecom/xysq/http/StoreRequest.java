package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

import com.chinatelecom.xysq.bean.Store;
import com.chinatelecom.xysq.other.Constants;

public class StoreRequest {

	public static void queryStore(final HttpCallback callback,
			final Long storeId) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/queryStore.do?storeId="
							+ storeId;
					Log.d("XYSQ", "queryStore:" + url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONObject jsonObj = new JSONObject(responseString);
						Store store = new Store();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
						store.setName(jsonObj.getString("name"));
						store.setDescription(jsonObj.getString("description"));
						store.setAddress(jsonObj.getString("address"));
						store.setCreateTime(sdf.parse(jsonObj
								.getString("createTime")));
						store.setId(jsonObj.getLong("id"));
						store.setModifyTime(sdf.parse(jsonObj
								.getString("modifyTime")));
						store.setImages(new ArrayList<String>());
						JSONArray imagesJson = jsonObj.getJSONArray("images");
						for (int j = 0; j < imagesJson.length(); j++) {
							store.getImages().add(imagesJson.getString(j));
						}
						return new TaskResult(1, null, store);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载回帖数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryStore", e);
				}
				return new TaskResult(-1, "加载回帖数据失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryStore.do", result.getResult());
				} else {
					callback.failure("/queryStore.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
}
