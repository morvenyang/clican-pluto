package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.AnnouncementAndNoticeListAdapter;
import com.chinatelecom.xysq.adapater.PosterPagerAdapter;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;
import com.chinatelecom.xysq.bean.Index;
import com.chinatelecom.xysq.http.AnnouncementAndNoticeRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.IndexRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AnnouncementActivity extends BaseActivity implements
		OnRefreshListener2<ListView>, OnLastItemVisibleListener, HttpCallback {

	private PullToRefreshListView mPullRefreshListView;
	private AnnouncementAndNoticeListAdapter adapter;

	private Long communityId;
	
	private ViewPager posterViewPager;

	private List<AnnouncementAndNotice> announcementAndNoticeList = new ArrayList<AnnouncementAndNotice>();

	private int page = 1;

	@Override
	protected String getPageName() {
		return "小区公告";
	}

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
		posterViewPager = (ViewPager) findViewById(R.id.announcement_posterViewPager);
		Intent intent = getIntent();
		communityId = intent.getLongExtra("communityId", -1);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.announcement_listView);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setDividerHeight(2);
		mPullRefreshListView.getRefreshableView().setFooterDividersEnabled(true);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(this);

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(this);

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		adapter = new AnnouncementAndNoticeListAdapter(
				announcementAndNoticeList,
				this,
				(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

		

		actualListView.setAdapter(adapter);
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, true,null, page, 5);
		IndexRequest.queryIndex(this, communityId);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getApplicationContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		page = 1;
		announcementAndNoticeList.clear();
		// Do work to refresh the list here.
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, true,null, page, 5);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// Do work to refresh the list here.
		page++;
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, true,null, page, 5);
	}

	@Override
	public void onLastItemVisible() {
		Toast.makeText(AnnouncementActivity.this, "没有更多历史公告",
				Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		if(url.equals("/queryAnnouncementAndNotice.do")){
			List<AnnouncementAndNotice> moreData = (List<AnnouncementAndNotice>) data;
			announcementAndNoticeList.addAll(moreData);
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}else{
			Index index = (Index) data;
			posterViewPager
					.setAdapter(new PosterPagerAdapter(
							index.getPosters(),
							this,
							(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		mPullRefreshListView.onRefreshComplete();
	}

}
