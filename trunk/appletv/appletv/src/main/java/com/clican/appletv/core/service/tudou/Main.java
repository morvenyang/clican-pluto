package com.clican.appletv.core.service.tudou;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

public class Main {

	public static void test4() throws Exception {
		HttpClient client = new DefaultHttpClient();
		//client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("15.185.225.83", 80, "http"));
		HttpGet httpget = new HttpGet(
				"http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=columnid=30&itemid=151608667&ishd=1");
		httpget.addHeader("x-forwarded-for", "15.185.225.83");
		httpget.addHeader("Proxy-Client-IP", "15.185.225.83");
		httpget.addHeader("WL-Proxy-Client-IP", "15.185.225.83");
		
		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		is.close();
		System.out.println(new String(os.toByteArray()));
		os.close();
	}

	public static void test3() throws Exception {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				new HttpHost("web-proxy.corp.hp.com", 8080, "http"));
		HttpGet httpget = new HttpGet("http://15.185.225.83/1.txt");

		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		OutputStream os = new FileOutputStream("c:/appletv/1.txt");

		byte[] buffer = new byte[1024];

		int read = -1;
		long total = 0;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
			os.flush();
			total += read;
			System.out.println(total / 1024 + " k");
		}
		is.close();
		os.close();
	}

	public static void test1() throws Exception {
		HttpClient client1 = new DefaultHttpClient();

		int id = 154130605;

		HttpGet httpget1 = new HttpGet(
				"http://vr.tudou.com/v2proxy/v2.m3u8?st=2&it=" + id);
		HttpResponse response1 = client1.execute(httpget1);
		System.out.println(response1.getStatusLine());
		ByteArrayOutputStream os1 = new ByteArrayOutputStream();
		response1.getEntity().writeTo(os1);
		System.out.println(new String(os1.toByteArray()));

		System.out.println("http://v3.tudou.com/v2.ts?st=2&it=" + id
				+ "&s=0&e=" + 10);
		HttpGet httpget = new HttpGet("http://v3.tudou.com/v2.ts?st=2&it=" + id
				+ "&s=0&e=" + 10);

		HttpClient client = new DefaultHttpClient();

		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		OutputStream os = new FileOutputStream("d:/appletv/19.ts");

		byte[] buffer = new byte[1024];

		int read = -1;
		long total = 0;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
			os.flush();
			total += read;
			System.out.println(total / 1024 + " k");
		}
		is.close();
		os.close();
	}

	public static void test2() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				"http://58.221.54.206/f4v/97/23138397.cgo.2.f4v?81000&key=2b9ede8b4720f4b3a0ed2850e546eb0040966d0994&playtype=1&tk=155012689724873950510663343&brt=2&bc=0&nt=0&du=1236200&ispid=23&rc=200&inf=1&si=sp&npc=2946&pp=0&ul=2&mt=0&sid=81000&au=0&pc=0&cip=114.92.101.75&hf=0&id=tudou&itemid=119456926&fi=23138397&sz=43243170&posky=N9Jq0CCRUridpd5Xbt8aEf7jTZ10f3&plybgn=1");
		// httpget.addHeader("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// httpget.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		// httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		// httpget.addHeader("Accept-Language", "en,zh-CN;q=0.8,zh;q=0.6");
		// httpget.addHeader("Connection", "keep-alive");
		// httpget.addHeader("Host", "58.221.54.206");
		httpget.addHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");

		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());
		// HttpEntity entity = response.getEntity();
		// InputStream is = entity.getContent();
		// OutputStream os = new FileOutputStream("d:/1.f4v");
		//
		// byte[] buffer = new byte[1024];
		//
		// int read = -1;
		// while ((read = is.read(buffer)) != -1) {
		// os.write(buffer, 0, read);
		// }
		// is.close();
		// os.close();
	}

	public static void test5() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(
				"http://trailers.apple.com:8000/oldxunlei.js");
		httpget.addHeader("Accept", "*/*");
		httpget.addHeader("Cookie",
				"Pod=46; s_vi=[CS]v1|2866D71F8514B857-4000017440052C42[CE]");
		httpget.addHeader("X-Apple-TV-Resolution", "720");
		httpget.addHeader("X-Apple-TV-Version", "5.1");
		httpget.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		httpget.addHeader("Accept-Encoding", "gzip, deflate");
		httpget.addHeader("Accept-Language", "zh_CN;q=0.9, en;q=0.8");
		httpget.addHeader("Connection", "keep-alive");
		httpget.addHeader("Host", "trailers.apple.com");
		httpget.addHeader("User-Agent", "iTunes-AppleTV/5.1 (3; 8GB; dt:12)");

		HttpResponse response = client.execute(httpget);
		System.out.println(response.getStatusLine());

		InputStream is = response.getEntity().getContent();
		byte[] data = new byte[is.available()];
		is.read(data);
		System.out.println(new String(data));
	}

	private static void doUncompressFile(String inFileName) throws Exception {

		GZIPInputStream in = new GZIPInputStream(
				new FileInputStream(inFileName));

		FileOutputStream out = null;

		out = new FileOutputStream("d:/123.js");

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		in.close();
		out.close();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		test4();
	}

}
