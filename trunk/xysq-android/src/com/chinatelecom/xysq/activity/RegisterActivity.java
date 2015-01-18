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
import com.chinatelecom.xysq.http.SmsRequest;
import com.chinatelecom.xysq.http.UserRequest;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.Callback;

public class RegisterActivity extends Activity implements HttpCallback {

	private CheckBox agreeCheckBox;
	private EditText msisdnEditText;
	private EditText verifyCodeEditText;
	private EditText passwordEditText;
	private EditText nickNameEditText;
	private Button getVerifyCodeButton;

	private CountDownTimer cdt;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		agreeCheckBox = (CheckBox) findViewById(R.id.register_agreeCheckBox);
		msisdnEditText = (EditText) findViewById(R.id.register_msisdnEditText);
		verifyCodeEditText = (EditText) findViewById(R.id.register_verifyCodeEditText);
		passwordEditText = (EditText) findViewById(R.id.register_passwordEditText);
		nickNameEditText  =  (EditText) findViewById(R.id.register_nickNameEditText);
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
					String msisdn = msisdnEditText.getText().toString();
					String verifyCode = verifyCodeEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					String nickName = nickNameEditText.getText().toString();
					if (StringUtils.isEmpty(msisdn)) {
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
					if (StringUtils.isEmpty(nickName)) {
						AlertUtil.alert(RegisterActivity.this, "昵称不能为空");
						return;
					}
					if (cdt != null) {
						cdt.cancel();
					}
					getVerifyCodeButton.setText("获取验证码");
					getVerifyCodeButton.setEnabled(true);
					UserRequest.register(nickName, password, msisdn,
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
				String msisdn = msisdnEditText.getText().toString();
				if (StringUtils.isEmpty(msisdn)) {
					AlertUtil.alert(RegisterActivity.this, "手机号不能为空");
					return;
				}
				SmsRequest.requestSmsCode(msisdn, RegisterActivity.this);
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
		if(url.equals("/requestSmsCode")){
			Log.d("XYSQ", "手机验证码发送成功");
		}else{
			User user = (User) data;
			XysqApplication application = (XysqApplication) getApplication();
			application.setUser(user);
			AlertUtil.alert(this, "注册成功", new Callback() {
				@Override
				public void exec() {
					Intent intent = new Intent(RegisterActivity.this,
							ProfileActivity.class);
					intent.putExtra("finish", true);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Log.d("XYSQ", "start IndexActivity");
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void failure(String url, int code, String message) {
		if(url.equals("/requestSmsCode")){
			if(cdt!=null){
				cdt.cancel();
			}
			getVerifyCodeButton.setText("重新获取验证码");
			getVerifyCodeButton.setEnabled(true);
			AlertUtil.alert(this, message);
		}else{
			AlertUtil.alert(this, message);
		}
		
	}

}
