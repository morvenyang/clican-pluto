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
import com.chinatelecom.xysq.http.IndexRequest;
import com.chinatelecom.xysq.listener.HtmlLinkOnClickListener;
import com.chinatelecom.xysq.listener.IndexOnClickListener;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexActivity extends Activity implements HttpCallback,
		OnClickListener {

	private TextView communityNameTextView;

	private Button communityChangeButton;

	private ViewPager posterViewPager;

	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		communityNameTextView = (TextView) findViewById(R.id.index_communityName);
		communityChangeButton = (Button) findViewById(R.id.index_communityChangeButton);
		posterViewPager = (ViewPager) findViewById(R.id.index_posterViewPager);
		progressBar = (ProgressBar) findViewById(R.id.index_progressBar);
		this.addListenerForButtons();
	}

	private void addListenerForButtons() {
		Button announcementButton = (Button) findViewById(R.id.index_xqggButton);
		announcementButton.setOnClickListener(new IndexOnClickListener(this,
				AnnouncementActivity.class, false, true));
		Button noticeButton = (Button) findViewById(R.id.index_yzxxButton);
		noticeButton.setOnClickListener(new IndexOnClickListener(this,
				NoticeActivity.class, false, true));
		Button bmffButton = (Button) findViewById(R.id.index_bmffButton);
		bmffButton.setOnClickListener(new HtmlLinkOnClickListener(
				"http://218.244.138.36/mobile/serve.do?channel=6","便民服务", this, true));

		Button zpzsButton = (Button) findViewById(R.id.index_zpzsButton);
		zpzsButton.setOnClickListener(new HtmlLinkOnClickListener(
				"http://218.244.138.36/mobile/shop.do?channel=6","宅便宅送", this, true));
		Button kdtxButton = (Button) findViewById(R.id.index_kdtxButton);
		kdtxButton.setOnClickListener(new IndexOnClickListener(this,
				BroadbandRemindActivity.class, true, false));
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
		Long communityId = KeyValueUtils.getLongValue(this,
				Constants.COMMUNITY_ID);
		if (StringUtils.isEmpty(communityName)) {
			communityNameTextView.setText("请选择小区");
		} else {
			communityNameTextView.setText(communityName);
		}
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);

		IndexRequest.queryIndex(this, communityId);
		communityNameTextView.setOnClickListener(this);
		communityChangeButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(IndexActivity.this,
				CommunitySelectActivity.class);
		Log.d("XYSQ",
				"changeCommunityButton is clicked, start CommunitySelectActivity");
		startActivity(intent);

	}
}
