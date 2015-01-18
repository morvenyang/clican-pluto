package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.UserRequest;
import com.chinatelecom.xysq.util.AlertUtil;

public class LoginActivity extends Activity implements HttpCallback {

	private EditText msisdnEditText;
	
	private EditText passwordEditText;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		msisdnEditText = (EditText)findViewById(R.id.login_msisdnEditText);
		passwordEditText = (EditText)findViewById(R.id.login_passwordEditText);
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
				String msisdn = msisdnEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				if(StringUtils.isEmpty(msisdn)){
					AlertUtil.alert(LoginActivity.this, "手机号不能为空");
					return;
				}
				if(StringUtils.isEmpty(password)){
					AlertUtil.alert(LoginActivity.this, "密码不能为空");
					return;
				}
				UserRequest.login(msisdn, password, LoginActivity.this);
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
