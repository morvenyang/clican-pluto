package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.Lottery;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.AwardRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.shake.OnShakeListener;
import com.chinatelecom.xysq.shake.ShakeEventManager;
import com.chinatelecom.xysq.util.AlertUtil;

public class YyyIndexActivity extends BaseActivity implements OnShakeListener,
		HttpCallback {

	private ShakeEventManager sem;

	@Override
	protected String getPageName() {
		return "摇一摇首页";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yyy_index);
		XysqApplication application = (XysqApplication) getApplication();
		sem = application.getShakeEventManager();
		Button backButton = (Button) findViewById(R.id.yyy_index_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onShake() {
		Log.d("XYSQ", "摇一摇触发");
		User user = getUser();
		if (user == null) {
			AlertUtil.alert(YyyIndexActivity.this, "请先登录");
		} else {
			AwardRequest.lottery(user, this);
		}
	}

	@Override
	public void success(String url, Object data) {
		Lottery lottery = (Lottery)data;
		Intent intent = new Intent(this, YyyResultActivity.class);
		intent.putExtra("lottery", lottery);
		this.startActivity(intent);
	}

	@Override
	public void failure(String url, int code, String message) {
		sem.setRecoding(false);
		AlertUtil.alert(this, message);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sem.setRecoding(false);
		sem.registerOnShakeListener(this);
	}

	@Override
	protected void onStop() {
		sem.unregisterOnShakeListener(this);
		super.onStop();
	}

}
