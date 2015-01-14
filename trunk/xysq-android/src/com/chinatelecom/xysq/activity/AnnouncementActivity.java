package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.AnnouncementAndNoticeListAdapter;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnLoadListener;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnRefreshListener;
import com.chinatelecom.xysq.comp.pullrefresh.scroller.impl.RefreshListView;
import com.chinatelecom.xysq.http.AnnouncementAndNoticeRequest;
import com.chinatelecom.xysq.http.HttpCallback;

public class AnnouncementActivity extends Activity implements
		OnRefreshListener, OnLoadListener, HttpCallback {

	private ProgressBar progressBar;

	private RefreshListView announcementListView;

	private Long communityId;

	private List<AnnouncementAndNotice> announcementAndNoticeList = new ArrayList<AnnouncementAndNotice>();
	
	private int page = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		progressBar = (ProgressBar) findViewById(R.id.announcement_progressBar);
		announcementListView = (RefreshListView) this
				.findViewById(R.id.announcement_listView);
		announcementListView.setOnLoadListener(this);
		announcementListView.setOnRefreshListener(this);
		Intent intent = getIntent();
		communityId = intent.getLongExtra("communityId", -1);
		this.loadAnnouncementData();
	}

	private void loadAnnouncementData() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, true, page, 5);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<AnnouncementAndNotice> moreData = (List<AnnouncementAndNotice>) data;
		announcementAndNoticeList.addAll(moreData);
		progressBar.setVisibility(View.INVISIBLE);
		announcementListView
				.setAdapter(new AnnouncementAndNoticeListAdapter(
						announcementAndNoticeList,
						this,
						(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onLoadMore() {
		Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT)
				.show();
		announcementListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				page++;
				AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(AnnouncementActivity.this,
						communityId, true, page, 5);
				announcementListView.loadCompelte();
			}
		}, 1500);
	}

	@Override
	public void onRefresh() {
		Toast.makeText(getApplicationContext(), "refreshing",
				Toast.LENGTH_SHORT).show();
		announcementListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				announcementAndNoticeList.clear();
				page=1;
				AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(AnnouncementActivity.this,
						communityId, true, page, 5);
				announcementListView.refreshComplete();
			}
		}, 1500);
	}

}
