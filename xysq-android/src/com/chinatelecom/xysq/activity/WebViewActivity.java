package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.chinatelecom.xysq.R;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		String url = this.getIntent().getStringExtra("url");
		webView.loadUrl(url);
	}
}
