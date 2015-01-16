package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.HttpCallback;

public class LoginActivity extends Activity implements HttpCallback {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button backButton = (Button) findViewById(R.id.login_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});

		Button submitButton = (Button) findViewById(R.id.login_submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public void success(String url, Object data) {
		
	}

	@Override
	public void failure(String url, int code, String message) {
		
	}
	
	
}
