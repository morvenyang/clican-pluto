package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnLoadListener;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnRefreshListener;
import com.chinatelecom.xysq.comp.pullrefresh.scroller.impl.RefreshListView;

public class AnnouncementActivity extends Activity implements
		OnRefreshListener, OnLoadListener {

	private ProgressBar progressBar;

	private RefreshListView announcementListView;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		progressBar = (ProgressBar) findViewById(R.id.announcement_progressBar);
		announcementListView = (RefreshListView) this
				.findViewById(R.id.announcement_listView);
		announcementListView.setOnLoadListener(this);
		announcementListView.setOnRefreshListener(this);
		this.setListView();
	}

	private void loadAnnouncementData() {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);

	}

	@Override
	public void onLoadMore() {
		Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT)
				.show();
		announcementListView.postDelayed(new Runnable() {

			@Override
			public void run() {
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
				announcementListView.refreshComplete();
			}
		}, 1500);
	}

	private void setListView() {
		List<String> sList = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			sList.add("Item - " + i);
		}

		// 获取ListView, 这里的listview就是Content view
		announcementListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sList));

	}
}
