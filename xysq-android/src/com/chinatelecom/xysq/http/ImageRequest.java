package com.chinatelecom.xysq.http;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.chinatelecom.xysq.other.Constants;

public class ImageRequest {

	private static Map<String, String> cahcedImageFiles = new HashMap<String, String>();

	private static String writeTempFile(HttpEntity entity) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH",
				Locale.ENGLISH);
		File tempFolderPath = new File(Constants.TEMP_FILE_PATH + "/image/"
				+ sdf.format(new Date()));
		// 如果这个文件不存在的话就开始创建临时文件
		if (!tempFolderPath.exists()) {
			tempFolderPath.mkdirs();
		}
		File tempFile = new File(tempFolderPath + "/"
				+ UUID.randomUUID().toString());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempFile);
			entity.writeTo(fos);
		} catch (Exception e) {
			Log.e("XYSQ", "", e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				Log.e("XYSQ", "", e);
			}
		}
		return tempFile.getPath();
	}

	public static void requestImage(final ImageView imageView, final String url) {
		String filePath = cahcedImageFiles.get(url);
		if (filePath != null) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
		} else {
			AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
				@Override
				protected String doInBackground(String... params) {
					HttpClient httpclient = new DefaultHttpClient();
					try {
						HttpResponse response = httpclient.execute(new HttpGet(
								url));
						StatusLine statusLine = response.getStatusLine();
						if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
							String filePath = writeTempFile(response
									.getEntity());
							cahcedImageFiles.put(url, filePath);
							return filePath;
						} else {
							response.getEntity().getContent().close();
						}
						return null;
					} catch (Exception e) {
						Log.e("XYSQ", "requestImage for url:" + url, e);
					}
					return null;
				}

				@Override
				protected void onPostExecute(String filePath) {
					if (filePath != null) {
						imageView.setImageBitmap(BitmapFactory
								.decodeFile(filePath));
					}
				}
			};
			task.execute(new String[] {});
		}

	}
}
