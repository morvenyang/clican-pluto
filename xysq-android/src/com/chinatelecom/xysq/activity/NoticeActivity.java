package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.AnnouncementAndNoticeListAdapter;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.http.AnnouncementAndNoticeRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class NoticeActivity extends Activity implements
		OnRefreshListener2<ListView>, OnLastItemVisibleListener, HttpCallback,
		OnClickListener {

	private Button tab1Button, tab2Button, tab3Button, tab4Button;

	private PullToRefreshListView mPullRefreshListView;
	private AnnouncementAndNoticeListAdapter adapter;

	private Long communityId;

	private List<AnnouncementAndNotice> announcementAndNoticeList = new ArrayList<AnnouncementAndNotice>();

	private int page = 1;

	private NoticeCategory currentNoticeCategory;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice);
		this.setTabButtons();

		Button backButton = (Button) findViewById(R.id.notice_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NoticeActivity.this.finish();
			}
		});
		Intent intent = getIntent();
		communityId = intent.getLongExtra("communityId", -1);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.notice_listView);
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

		/**
		 * Add Sound Event Listener
		 */
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				this);
		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		mPullRefreshListView.setOnPullEventListener(soundListener);

		actualListView.setAdapter(adapter);
		currentNoticeCategory = NoticeCategory.JU_WEI_HUI;
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, false, currentNoticeCategory, page, 5);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<AnnouncementAndNotice> moreData = (List<AnnouncementAndNotice>) data;
		announcementAndNoticeList.addAll(moreData);
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void failure(String url, int code, String message) {
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void onLastItemVisible() {
		Toast.makeText(NoticeActivity.this, "没有更多历史业主须知", Toast.LENGTH_SHORT)
				.show();
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
				communityId, false, currentNoticeCategory, page, 5);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// Do work to refresh the list here.
		page++;
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, false, currentNoticeCategory, page, 5);
	}

	@Override
	public void onClick(View v) {
		tab1Button.setTextColor(this.getResources().getColor(R.color.tabTextColor));
		tab2Button.setTextColor(this.getResources().getColor(R.color.tabTextColor));
		tab3Button.setTextColor(this.getResources().getColor(R.color.tabTextColor));
		tab4Button.setTextColor(this.getResources().getColor(R.color.tabTextColor));
		if (v == tab1Button) {
			currentNoticeCategory = NoticeCategory.JU_WEI_HUI;
		} else if (v == tab2Button) {

			currentNoticeCategory = NoticeCategory.BAN_ZHENG_CAI_LIAO;
		} else if (v == tab3Button) {
			currentNoticeCategory = NoticeCategory.WU_YE_XIN_XI;
		} else if (v == tab4Button) {
			currentNoticeCategory = NoticeCategory.PAI_CHU_SUO;
		}
		((Button) v).setTextColor(this.getResources().getColor(R.color.lightGreen));
		page = 1;
		announcementAndNoticeList.clear();
		AnnouncementAndNoticeRequest.queryAnnouncementAndNotice(this,
				communityId, false, currentNoticeCategory, page, 5);
	}

	private void setTabButtons() {
		tab1Button = (Button) findViewById(R.id.notice_tab1Button);
		tab2Button = (Button) findViewById(R.id.notice_tab2Button);
		tab3Button = (Button) findViewById(R.id.notice_tab3Button);
		tab4Button = (Button) findViewById(R.id.notice_tab4Button);
		tab1Button.setText(NoticeCategory.JU_WEI_HUI.getLabel());
		tab1Button.setOnClickListener(this);
		tab2Button.setText(NoticeCategory.BAN_ZHENG_CAI_LIAO.getLabel());
		tab2Button.setOnClickListener(this);
		tab3Button.setText(NoticeCategory.WU_YE_XIN_XI.getLabel());
		tab3Button.setOnClickListener(this);
		tab4Button.setText(NoticeCategory.PAI_CHU_SUO.getLabel());
		tab4Button.setOnClickListener(this);
		tab1Button.setTextColor(this.getResources().getColor(R.color.lightGreen));
	}
}
