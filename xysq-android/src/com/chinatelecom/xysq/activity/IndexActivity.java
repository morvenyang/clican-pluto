package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexActivity extends Activity {

	private TextView communityNameTextView;
	
	private Button changeCommunityButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		communityNameTextView = (TextView) findViewById(R.id.communityName);
		changeCommunityButton = (Button) findViewById(R.id.changeCommunity);
		this.loadCommunityData();
	}

	@Override
	protected void onResume() {
		loadCommunityData();
		super.onResume();
	}
	
	private void loadCommunityData() {
		String communityName = KeyValueUtils.getStringValue(this, Constants.COMMUNITY_NAME);
		if (StringUtils.isEmpty(communityName)) {
			communityNameTextView.setText("请选择小区");
		} else {
			communityNameTextView.setText(communityName);
		}
		changeCommunityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this, CommunitySelectActivity.class);
				Log.d("IndexActivity", "changeCommunityButton is clicked, start CommunitySelectActivity");
				startActivity(intent);
			}
		});
	}
}
