package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.util.Log;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.shake.OnShakeListener;
import com.chinatelecom.xysq.shake.ShakeEventManager;

public class YyyIndexActivity extends BaseActivity implements OnShakeListener{

	@Override
	protected String getPageName() {
		return "摇一摇首页";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yyy_index);
	}

	@Override
	public void onShake() {
		Log.d("XYSQ", "摇一摇触发");
	}

	@Override
	protected void onResume() {
		super.onResume();
		XysqApplication application = (XysqApplication) 
				getApplication();
		ShakeEventManager sem = application.getShakeEventManager();
		sem.registerOnShakeListener(this);
	}

	@Override
	protected void onStop() {
		XysqApplication application = (XysqApplication) 
				getApplication();
		ShakeEventManager sem = application.getShakeEventManager();
		sem.unregisterOnShakeListener(this);
		super.onStop();
	}
	
	
}
