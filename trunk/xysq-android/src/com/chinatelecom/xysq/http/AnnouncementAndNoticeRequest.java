package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

import com.chinatelecom.xysq.bean.AnnouncementAndNotice;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.other.Constants;

public class AnnouncementAndNoticeRequest {

	private static void buildAnnouncementAndNotices(JSONArray aanListJson,
			List<AnnouncementAndNotice> aanList) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
			for (int i = 0; i < aanListJson.length(); i++) {
				JSONObject aanJson = aanListJson.getJSONObject(i);
				AnnouncementAndNotice aan = new AnnouncementAndNotice();
				aan.setId(aanJson.getLong("id"));
				aan.setTitle(aanJson.getString("title"));
				aan.setContent(aanJson.getString("content"));
				aan.setModifyTime(sdf.parse(aanJson.getString("modifyTime")));
				aan.setNoticeCategory(aanJson.getString("noticeCategory"));
				aan.setInnerModule(aanJson.getString("innerModule"));
				aanList.add(aan);
			}
		} catch (Exception e) {
			Log.e("XYSQ", "build buildAnnouncementAndNotices error", e);
		}

	}

	public static void queryAnnouncementAndNotice(final HttpCallback callback,
			final Long communityId, final boolean announcement,final NoticeCategory noticeCategory, final int page,
			final int pageSize) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/queryAnnouncementAndNotice.do?communityId="
							+ communityId + "&announcement=" + announcement
							+ "&page=" + page + "&pageSize=" + pageSize;
					if(noticeCategory!=null){
						url = url+"&noticeCategory="+noticeCategory.name();
					}
					Log.d("XYSQ", "queryAnnouncementAndNoticeUrl:"+url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONArray jsonObj = new JSONArray(responseString);
						List<AnnouncementAndNotice> result = new ArrayList<AnnouncementAndNotice>();
						buildAnnouncementAndNotices(jsonObj, result);
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载公告和业主须知失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryAnnouncementAndNotice", e);
				}
				return new TaskResult(-1, "加载公告和业主须知失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryAnnouncementAndNotice.do",
							result.getResult());
				} else {
					callback.failure("/queryAnnouncementAndNotice.do",
							result.getCode(), result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
}
