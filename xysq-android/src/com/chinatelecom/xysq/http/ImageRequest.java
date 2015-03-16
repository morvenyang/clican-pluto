package com.chinatelecom.xysq.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
		InputStream is = null;
		byte[] buffer= new byte[2048];
		try {
			fos = new FileOutputStream(tempFile);
			is = entity.getContent();
			int read = -1;
			while((read=is.read(buffer))!=-1){
				fos.write(buffer,0,read);
			}
		} catch (Exception e) {
			Log.e("XYSQ", "", e);
		} finally {
			try {
				if(fos!=null){
					fos.close();
				}
				
			} catch (Exception e) {
				Log.e("XYSQ", "", e);
			}
			try {
				if(is!=null){
					is.close();
				}
			} catch (Exception e) {
				Log.e("XYSQ", "", e);
			}
		}
		return tempFile.getPath();
	}

	public static void requestImage(final ImageView imageView, final String url,final int width,final int height) {
		String imagePath = cahcedImageFiles.get(url);
		if (imagePath != null) {
			imageView.setImageBitmap(PhotoRequest.getImageThumbnail(imagePath, width, height));
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
				protected void onPostExecute(String imagePath) {
					if (imagePath != null) {
						imageView.setImageBitmap(PhotoRequest.getImageThumbnail(imagePath, width, height));
					}
				}
			};
			task.execute(new String[] {});
		}

	}
	
	
	public static void requestImage(final ImageView imageView, final String url) {
		String imagePath = cahcedImageFiles.get(url);
		if (imagePath != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
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
				protected void onPostExecute(String imagePath) {
					if (imagePath != null) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
					}
				}
			};
			task.execute(new String[] {});
		}

	}
}
