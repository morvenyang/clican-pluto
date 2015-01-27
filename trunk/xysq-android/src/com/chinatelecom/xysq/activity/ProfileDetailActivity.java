package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;

public class ProfileDetailActivity extends BaseActivity {

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
	}

	@Override
	protected String getPageName() {
		return "个人资料详细";
	}

}
