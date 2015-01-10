package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.ClientRequest;

public class CommunitySelectActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community_select);
		this.loadCommunityData();
	}

	private void loadCommunityData() {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.cs_progressBar);
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		ClientRequest.queryAreaAndCommunity();
		progressBar.setVisibility(View.INVISIBLE);
		Button changeCommunityButton = (Button) findViewById(R.id.cs_changeArea);
		ClientRequest.queryAreaAndCommunity();
		changeCommunityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CommunitySelectActivity", "changeAreaButton is clicked");
			}
		});
	}
}
