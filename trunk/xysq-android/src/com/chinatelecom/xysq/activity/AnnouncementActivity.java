package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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


	private RefreshListView announcementListView;

	private Long communityId;

	private List<AnnouncementAndNotice> announcementAndNoticeList = new ArrayList<AnnouncementAndNotice>();

	private int page = 1;

	private boolean noMoreData = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		Button backButton = (Button) findViewById(R.id.announcement_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnnouncementActivity.this.finish();
			}
		});
		Intent intent = getIntent();
		communityId = intent.getLongExtra("communityId", -1);
		this.loadAnnouncementData();
	}

	private void loadAnnouncementData() {
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, true, page, 5);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<AnnouncementAndNotice> moreData = (List<AnnouncementAndNotice>) data;
		if (moreData.size() == 0) {
			noMoreData = true;
		}
		if(this.announcementListView==null){
			announcementListView = (RefreshListView) this
					.findViewById(R.id.announcement_listView);
			announcementListView.setOnLoadListener(this);
			announcementListView.setOnRefreshListener(this);
		}
		announcementAndNoticeList.addAll(moreData);
		announcementListView
				.setAdapter(new AnnouncementAndNoticeListAdapter(
						announcementAndNoticeList,
						this,
						(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
	}

	@Override
	public void failure(String url, int code, String message) {
	}

	@Override
	public void onLoadMore() {
			Toast.makeText(getApplicationContext(), "loading",
					Toast.LENGTH_SHORT).show();
			announcementListView.postDelayed(new Runnable() {

				@Override
				public void run() {
					if(noMoreData){
						announcementListView.loadCompelte();
					}else{
						page++;
						AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(
								AnnouncementActivity.this, communityId, true, page,
								5);
						announcementListView.loadCompelte();
					}
					
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
				page = 1;
				AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(
						AnnouncementActivity.this, communityId, true, page, 5);
				announcementListView.refreshComplete();
			}
		}, 1500);
	}

}
