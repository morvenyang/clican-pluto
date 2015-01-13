package com.chinatelecom.xysq.http;

import java.io.ByteArrayOutputStream;
import java.net.URL;

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

public class ImageRequest {

	
	public static void requestImage(final ImageView imageView,final String url){
		AsyncTask<String, Void, byte[]> task = new AsyncTask<String, Void, byte[]>() {
			@Override
			protected byte[] doInBackground(String... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpResponse response = httpclient.execute(new HttpGet(url));
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						byte[] data = out.toByteArray();
						return data;
					} else {
						response.getEntity().getContent().close();
					}
					return null;
				} catch (Exception e) {
					Log.e("XYSQ", "requestImage for url:"+url, e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(byte[] data) {
				if(data!=null){
					imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
				}
			}
		};
		task.execute(new String[] {});
	}
}
