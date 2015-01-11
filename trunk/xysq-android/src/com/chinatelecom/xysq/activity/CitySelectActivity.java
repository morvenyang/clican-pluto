package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.HttpCallback;

public class CitySelectActivity extends Activity implements HttpCallback {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
	}
	
	@Override
	public void success(String url, Object data) {

	}

	@Override
	public void failure(String url, int code, String message) {

	}

}
