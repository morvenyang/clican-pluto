package com.clican.appletv.core.service.tudou;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://58.221.54.206/f4v/97/23138397.cgo.2.f4v?81000&key=2b9ede8b4720f4b3a0ed2850e546eb0040966d0994&playtype=1&tk=155012689724873950510663343&brt=2&bc=0&nt=0&du=1236200&ispid=23&rc=200&inf=1&si=sp&npc=2946&pp=0&ul=2&mt=0&sid=81000&au=0&pc=0&cip=114.92.101.75&hf=0&id=tudou&itemid=119456926&fi=23138397&sz=43243170&posky=N9Jq0CCRUridpd5Xbt8aEf7jTZ10f3&plybgn=1");
//		httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		httpget.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
//		httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//		httpget.addHeader("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
//		httpget.addHeader("Connection", "keep-alive");
//		httpget.addHeader("Host", "58.221.54.206");
		httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
		
		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());
//		HttpEntity entity = response.getEntity();
//		InputStream is = entity.getContent();
//		OutputStream os = new FileOutputStream("d:/1.f4v");
//
//		byte[] buffer = new byte[1024];
//
//		int read = -1;
//		while ((read = is.read(buffer)) != -1) {
//			os.write(buffer, 0, read);
//		}
//		is.close();
//		os.close();
	}

}
