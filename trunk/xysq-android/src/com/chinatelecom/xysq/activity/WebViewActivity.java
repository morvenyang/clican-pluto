package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;

public class WebViewActivity extends Activity {

	private WebView webView;
	
	private TextView titleTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		titleTextView = (TextView)this.findViewById(R.id.webview_title);
		titleTextView.setText(this.getIntent().getStringExtra("title"));
		Button backButton = (Button)this.findViewById(R.id.webview_backButton);
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		webView = (WebView) this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		String url = this.getIntent().getStringExtra("url");
		webView.loadUrl(url);
	}
}
