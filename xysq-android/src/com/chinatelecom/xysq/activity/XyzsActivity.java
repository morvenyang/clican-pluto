package com.chinatelecom.xysq.activity;

import android.os.Bundle;

import com.chinatelecom.xysq.R;

public class XyzsActivity extends BaseActivity {

	@Override
	protected String getPageName() {
		return "小翼助手";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xyzs);
	}
}
