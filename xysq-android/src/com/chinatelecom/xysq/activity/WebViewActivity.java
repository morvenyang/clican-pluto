package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;

public class WebViewActivity extends BaseActivity {

	private WebView webView;

	private TextView titleTextView;

	private String title;

	@Override
	protected String getPageName() {
		return title;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		titleTextView = (TextView) this.findViewById(R.id.webview_title);
		String title = this.getIntent().getStringExtra("title");
		titleTextView.setText(title);
		Button backButton = (Button) this.findViewById(R.id.webview_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		webView = (WebView) this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false);  
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
			
			
		});
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
		});
		String url = this.getIntent().getStringExtra("url");
		webView.loadUrl(url);
	}
}
