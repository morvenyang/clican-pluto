package com.chinatelecom.xysq.activity;

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
import com.chinatelecom.xysq.util.Callback;

public class ProfileDetailActivity extends BaseActivity implements HttpCallback {

	private EditText nickNameEditText, carNumberEditText, addressEditText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_detail);
		Button backButton = (Button) findViewById(R.id.profile_detail_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button saveButton = (Button) findViewById(R.id.profile_detail_saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				User user = getUser();
				if (user != null) {
					UserRequest.updateProfile(user.getId(), nickNameEditText
							.getText().toString(), addressEditText.getText()
							.toString(), carNumberEditText.getText().toString(),
							ProfileDetailActivity.this);
				} else {
					AlertUtil.alert(ProfileDetailActivity.this, "请先登录");
				}
			}
		});

		Button logoutButton = (Button) findViewById(R.id.profile_detail_logoutButton);
		logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				XysqApplication application = (XysqApplication) getApplication();
				application.setUser(null);
				Intent intent = new Intent(ProfileDetailActivity.this,
						ProfileActivity.class);
				intent.putExtra("finish", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Log.d("XYSQ", "start ProfileActivity");
				startActivity(intent);
			}
		});
		this.nickNameEditText = (EditText) findViewById(R.id.profile_detail_nickNameEditText);
		this.addressEditText = (EditText) findViewById(R.id.profile_detail_addressEditText);
		this.carNumberEditText = (EditText) findViewById(R.id.profile_detail_carNumberEditText);
		User user = getUser();
		if (user != null) {
			this.nickNameEditText.setText(user.getNickName());
			this.addressEditText.setText(user.getAddress());
			this.carNumberEditText.setText(user.getCarNumber());
		}
	}

	@Override
	public void success(String url, Object data) {
		User user = (User) data;
		XysqApplication application = (XysqApplication) getApplication();
		application.setUser(user);
		AlertUtil.alert(this, "更新成功", new Callback() {
			@Override
			public void exec() {
				Intent intent = new Intent(ProfileDetailActivity.this,
						ProfileActivity.class);
				intent.putExtra("finish", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Log.d("XYSQ", "start ProfileActivity");
				startActivity(intent);
			}
		});
	}

	@Override
	public void failure(String url, int code, String message) {
		AlertUtil.alert(this, message);
	}

	@Override
	protected String getPageName() {
		return "个人资料详细";
	}

}
