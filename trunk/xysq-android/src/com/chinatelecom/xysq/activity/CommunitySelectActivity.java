package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CommunitySelectActivity extends Activity implements HttpCallback {

	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community_select);
		progressBar = (ProgressBar) findViewById(R.id.communitySelect_progressBar);
		this.loadCommunityData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object object) {
		progressBar.setVisibility(View.INVISIBLE);

	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void loadCommunityData() {
		//progressBar.setVisibility(View.GONE);
		//progressBar.setVisibility(View.VISIBLE);
		String areaName = KeyValueUtils.getValue(this, Constants.AREA_NAME);
		String areaId = KeyValueUtils.getValue(this, Constants.AREA_ID);
		Button changeArea = (Button) findViewById(R.id.communitySelect_changeAreaButton);
		if (StringUtils.isNotEmpty(areaName) && StringUtils.isNotEmpty(areaId)) {
			changeArea.setText(areaName);
		}
		changeArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CommunitySelectActivity.this,
						CitySelectActivity.class);
				Log.d("CommunitySelectActivity", "changeAreaButton is clicked");
				startActivity(intent);
			}
		});
	}
}
