package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.UserRequest;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.Callback;

public class RegisterActivity extends Activity implements HttpCallback {

	private CheckBox agreeCheckBox;
	private EditText userNameEditText;
	private EditText verifyCodeEditText;
	private EditText passwordEditText;
	private Button getVerifyCodeButton;

	private CountDownTimer cdt;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		agreeCheckBox = (CheckBox) findViewById(R.id.register_agreeCheckBox);
		userNameEditText = (EditText) findViewById(R.id.register_userNameEditText);
		verifyCodeEditText = (EditText) findViewById(R.id.register_verifyCodeEditText);
		passwordEditText = (EditText) findViewById(R.id.register_passwordEditText);
		Button backButton = (Button) findViewById(R.id.register_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});

		Button submitButton = (Button) findViewById(R.id.register_submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (agreeCheckBox.isChecked()) {
					String userName = userNameEditText.getText().toString();
					String verifyCode = verifyCodeEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					if (StringUtils.isEmpty(userName)) {
						AlertUtil.alert(RegisterActivity.this, "手机号不能为空");
						return;
					}
					if (StringUtils.isEmpty(verifyCode)) {
						AlertUtil.alert(RegisterActivity.this, "请输入6位验证码");
						return;
					}
					if (StringUtils.isEmpty(password)) {
						AlertUtil.alert(RegisterActivity.this, "密码不能为空");
						return;
					}
					if (cdt != null) {
						cdt.cancel();
					}
					getVerifyCodeButton.setText("获取验证码");
					getVerifyCodeButton.setEnabled(true);
					UserRequest.register(userName, password, userName,
							verifyCode, RegisterActivity.this);
				} else {
					AlertUtil.alert(RegisterActivity.this, "请先阅读并同意用户协议");
				}
			}
		});

		getVerifyCodeButton = (Button) findViewById(R.id.register_getVerifyCodeButton);
		this.setGetVerifyCodeButton();
	}

	@Override
	protected void onStop() {
		if (cdt != null) {
			cdt.cancel();
		}
		super.onStop();
	}

	private void setGetVerifyCodeButton() {
		getVerifyCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cdt = new CountDownTimer(60 * 1000, 1000) {
					// 第一个参数是总的倒计时时间
					// 第二个参数是每隔多少时间(ms)调用一次onTick()方法
					public void onTick(long millisUntilFinished) {
						getVerifyCodeButton.setText(millisUntilFinished / 1000
								+ "s后重新发送");
						getVerifyCodeButton.setEnabled(false);
					}

					public void onFinish() {
						getVerifyCodeButton.setText("重新获取验证码");
						getVerifyCodeButton.setEnabled(true);
					}
				};
				cdt.start();
			}
		});
	}

	@Override
	public void success(String url, Object data) {
		User user = (User) data;
		XysqApplication application = (XysqApplication) getApplication();
		application.setUser(user);
		AlertUtil.alert(this, "注册成功", new Callback() {
			@Override
			public void exec() {
				Intent intent = new Intent(RegisterActivity.this,
						IndexActivity.class);
				intent.putExtra("finish", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Log.d("XYSQ", "start IndexActivity");
				startActivity(intent);
			}
		});
	}

	@Override
	public void failure(String url, int code, String message) {
		AlertUtil.alert(this, message);
	}

}
