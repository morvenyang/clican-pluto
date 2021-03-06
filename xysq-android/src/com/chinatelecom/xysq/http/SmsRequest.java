package com.chinatelecom.xysq.http;

import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

public class SmsRequest {

	private static HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	public static void requestSmsCode(final String msisdn,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				HttpClient httpclient = getNewHttpClient();
				try {
					HttpPost post = new HttpPost(
							"https://leancloud.cn/1.1/requestSmsCode");
					post.setHeader("X-AVOSCloud-Application-Id",
							"zgdiillmtdo07gx2zwu5xlhubqu0ob6jf4pmd6d80o4r63jr");
					post.setHeader("X-AVOSCloud-Application-Key",
							"8nc8zg36bmlqp00auc8usbz5k641vsym4k5sanlrcclgikzr");
					post.setHeader("Content-Type", "application/json");
					post.setEntity(new StringEntity(
							"{\"mobilePhoneNumber\": \"" + msisdn + "\"}"));
					HttpResponse response = httpclient.execute(post);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						return new TaskResult(1, null, null);
					} else {
						response.getEntity().getContent().close();
						return new TaskResult(-1, statusLine.getReasonPhrase(), null);
					}
				} catch (Exception e) {
					Log.e("XYSQ", "requestSmsCode", e);
				}
				return new TaskResult(-1, "手机验证码发送失败", null);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/requestSmsCode", "success");
				} else {
					callback.failure("/requestSmsCode", result.getCode(),
							result.getMessage());
				}

			}
		};
		task.execute(new String[] {});
	}

}
