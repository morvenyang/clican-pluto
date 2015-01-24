package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.chinatelecom.xysq.bean.ForumPost;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;

public class ForumRequest {

	public static void queryTopic(final HttpCallback callback,
			final Long communityId, final int page, final int pageSize) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/queryTopic.do?communityId=" + communityId
							+ "&page=" + page + "&pageSize=" + pageSize;
					Log.d("XYSQ", "queryTopic:" + url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONArray jsonObj = new JSONArray(responseString);
						List<ForumTopic> result = new ArrayList<ForumTopic>();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
						for (int i = 0; i < jsonObj.length(); i++) {
							JSONObject forumJson = jsonObj.getJSONObject(i);
							ForumTopic forumTopic = new ForumTopic();
							forumTopic.setContent(forumJson
									.getString("content"));
							forumTopic.setCreateTime(sdf.parse(forumJson
									.getString("createTime")));
							forumTopic.setId(forumJson.getLong("id"));
							forumTopic.setModifyTime(sdf.parse(forumJson
									.getString("modifyTime")));
							forumTopic.setPostNum(forumJson.getInt("postNum"));
							forumTopic.setTitle(forumJson.getString("title"));
							forumTopic.setImages(new ArrayList<String>());
							JSONArray imagesJson = forumJson
									.getJSONArray("images");
							for (int j = 0; j < imagesJson.length(); j++) {
								forumTopic.getImages().add(
										imagesJson.getString(j));
							}
							User user = new User();
							user.setNickName(forumJson.getJSONObject(
									"submitter").getString("nickName"));
							user.setMsisdn(forumJson.getJSONObject("submitter")
									.getString("msisdn"));
							forumTopic.setSubmitter(user);
							result.add(forumTopic);
						}
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载论坛数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryTopic", e);
				}
				return new TaskResult(-1, "加载论坛数据失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryTopic.do", result.getResult());
				} else {
					callback.failure("/queryTopic.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
	
	public static void queryPost(final HttpCallback callback,
			final Long topicId, final int page, final int pageSize) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL
							+ "/queryPost.do?topicId=" + topicId
							+ "&page=" + page + "&pageSize=" + pageSize;
					Log.d("XYSQ", "queryPost:" + url);
					HttpResponse response = httpclient
							.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						String responseString = out.toString();
						JSONArray jsonObj = new JSONArray(responseString);
						List<ForumPost> result = new ArrayList<ForumPost>();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
						for (int i = 0; i < jsonObj.length(); i++) {
							JSONObject forumJson = jsonObj.getJSONObject(i);
							ForumPost forumPost = new ForumPost();
							forumPost.setContent(forumJson
									.getString("content"));
							forumPost.setReplyContent(forumJson
									.getString("replyContent"));
							forumPost.setCreateTime(sdf.parse(forumJson
									.getString("createTime")));
							forumPost.setId(forumJson.getLong("id"));
							forumPost.setModifyTime(sdf.parse(forumJson
									.getString("modifyTime")));
							forumPost.setImages(new ArrayList<String>());
							JSONArray imagesJson = forumJson
									.getJSONArray("images");
							for (int j = 0; j < imagesJson.length(); j++) {
								forumPost.getImages().add(
										imagesJson.getString(j));
							}
							User user = new User();
							user.setNickName(forumJson.getJSONObject(
									"submitter").getString("nickName"));
							user.setMsisdn(forumJson.getJSONObject("submitter")
									.getString("msisdn"));
							forumPost.setSubmitter(user);
							result.add(forumPost);
						}
						return new TaskResult(1, null, result);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "加载回帖数据失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "queryPost", e);
				}
				return new TaskResult(-1, "加载回帖数据失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/queryPost.do", result.getResult());
				} else {
					callback.failure("/queryPost.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}

	public static void saveTopic(final HttpCallback callback, final User user,
			final Long communityId, final String title, final String content,
			final List<PhotoItem> photoItemList) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					String url = Constants.BASE_URL + "/saveTopic.do";
					Log.d("XYSQ", "saveTopic:" + url);
					HttpPost post = new HttpPost(url);
					post.setHeader("Cookie",
							"JSESSIONID=" + user.getJsessionid());
					MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
							.create();
					entityBuilder.addTextBody("title", title,
							ContentType.create("plain/text", "utf-8"));
					entityBuilder.addTextBody("content", content,
							ContentType.create("plain/text", "utf-8"));
					entityBuilder.addTextBody("communityId",
							communityId.toString());
					for (int i = 0; i < photoItemList.size(); i++) {
						PhotoItem pi = photoItemList.get(i);
						File f = new File(pi.getFilePath());
						entityBuilder.addBinaryBody(pi.getFileName(), f,
								ContentType.MULTIPART_FORM_DATA,
								pi.getFileName());
					}
					post.setEntity(entityBuilder.build());
					HttpResponse response = httpclient.execute(post);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						return new TaskResult(1, null, null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, "提交帖子失败", null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "saveTopic", e);
				}
				return new TaskResult(-1, "提交帖子失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/saveTopic.do", result.getResult());
				} else {
					callback.failure("/saveTopic.do", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}
}
