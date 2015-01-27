package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.SmsRequest;
import com.chinatelecom.xysq.http.UserRequest;
import com.chinatelecom.xysq.listener.HtmlLinkOnClickListener;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.Callback;

public class ForgetPasswordActivity extends BaseActivity implements HttpCallback {
	private EditText msisdnEditText;
	private EditText verifyCodeEditText;
	private EditText passwordEditText;
	private Button getVerifyCodeButton;

	private CountDownTimer cdt;

	@Override
	protected String getPageName() {
		return "忘记密码";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		msisdnEditText = (EditText) findViewById(R.id.forget_password_msisdnEditText);
		verifyCodeEditText = (EditText) findViewById(R.id.forget_password_verifyCodeEditText);
		passwordEditText = (EditText) findViewById(R.id.forget_password_passwordEditText);
		Button backButton = (Button) findViewById(R.id.forget_password_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button registerAgreeButton = (Button)findViewById(R.id.forget_password_agreeButton);
		registerAgreeButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
		registerAgreeButton.setOnClickListener(new HtmlLinkOnClickListener(Constants.BASE_URL+"/android/agree.html","用户协议",this,false));
		Button submitButton = (Button) findViewById(R.id.forget_password_submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
					String msisdn = msisdnEditText.getText().toString();
					String verifyCode = verifyCodeEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					if (StringUtils.isEmpty(msisdn)) {
						AlertUtil.alert(ForgetPasswordActivity.this, "手机号不能为空");
						return;
					}
					if (StringUtils.isEmpty(verifyCode)) {
						AlertUtil.alert(ForgetPasswordActivity.this, "请输入6位验证码");
						return;
					}
					if (StringUtils.isEmpty(password)) {
						AlertUtil.alert(ForgetPasswordActivity.this, "密码不能为空");
						return;
					}
					
					if (cdt != null) {
						cdt.cancel();
					}
					getVerifyCodeButton.setText("获取验证码");
					getVerifyCodeButton.setEnabled(true);
					UserRequest.forgetPassword(password, msisdn,
							verifyCode, ForgetPasswordActivity.this);
				
			}
		});

		getVerifyCodeButton = (Button) findViewById(R.id.forget_password_getVerifyCodeButton);
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
					AlertUtil.alert(ForgetPasswordActivity.this, "手机号不能为空");
					return;
				}
				SmsRequest.requestSmsCode(msisdn, ForgetPasswordActivity.this);
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
			AlertUtil.alert(this, "更新密码成功", new Callback() {
				@Override
				public void exec() {
					Intent intent = new Intent(ForgetPasswordActivity.this,
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
