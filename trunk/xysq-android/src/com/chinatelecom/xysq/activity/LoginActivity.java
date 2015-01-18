package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.util.AlertUtil;

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

		Button registerButton = (Button) findViewById(R.id.login_registerButton);
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				Log.d("XYSQ", "start RegisterActivity");
				startActivity(intent);
			}
		});
	}

	@Override
	public void success(String url, Object data) {
		User user = (User)data;
		XysqApplication application = (XysqApplication) getApplication();
		application.setUser(user);
		this.finish();
	}

	@Override
	public void failure(String url, int code, String message) {
		AlertUtil.alert(this, message);
	}

}
