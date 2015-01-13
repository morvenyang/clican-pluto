package com.chinatelecom.xysq.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PosterPagerAdapter;
import com.chinatelecom.xysq.bean.Index;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexActivity extends Activity implements HttpCallback {

	private TextView communityNameTextView;

	private Button changeCommunityButton;

	private ViewPager posterViewPager;

	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		communityNameTextView = (TextView) findViewById(R.id.index_communityName);
		changeCommunityButton = (Button) findViewById(R.id.index_changeCommunity);
		posterViewPager = (ViewPager) findViewById(R.id.index_posterViewPager);
		this.loadCommunityData();
	}

	@Override
	protected void onResume() {
		loadCommunityData();
		super.onResume();
	}

	@Override
	public void success(String url, Object data) {
		Index index = (Index) data;
		posterViewPager
				.setAdapter(new PosterPagerAdapter(
						index.getPosters(),
						this,
						(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	private void loadCommunityData() {
		String communityName = KeyValueUtils.getStringValue(this,
				Constants.COMMUNITY_NAME);
		if (StringUtils.isEmpty(communityName)) {
			communityNameTextView.setText("请选择小区");
		} else {
			communityNameTextView.setText(communityName);
		}
		changeCommunityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndexActivity.this,
						CommunitySelectActivity.class);
				Log.d("IndexActivity",
						"changeCommunityButton is clicked, start CommunitySelectActivity");
				startActivity(intent);
			}
		});
	}
}
