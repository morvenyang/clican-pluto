package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.ForumTopicListAdapter;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.http.AnnouncementAndNoticeRequest;
import com.chinatelecom.xysq.http.ForumRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class ForumActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnLastItemVisibleListener, HttpCallback{

	private PullToRefreshListView mPullRefreshListView;
	private ForumTopicListAdapter adapter;
	
	private Long communityId;

	private List<ForumTopic> forumTopicList = new ArrayList<ForumTopic>();

	private int page = 1;

	@Override
	protected String getPageName() {
		return "论坛";
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum);
		Button backButton = (Button) findViewById(R.id.forum_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

		adapter = new ForumTopicListAdapter(
				forumTopicList,
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
		ForumRequest.queryTopic(this, communityId, page, 20);
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
		forumTopicList.clear();
		// Do work to refresh the list here.
		ForumRequest.queryTopic(this, communityId, page, 20);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// Do work to refresh the list here.
		page++;
		ForumRequest.queryTopic(this, communityId, page, 20);
	}

	@Override
	public void onLastItemVisible() {
		Toast.makeText(this, "没有更多帖子",
				Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<ForumTopic> moreData = (List<ForumTopic>) data;
		forumTopicList.addAll(moreData);
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void failure(String url, int code, String message) {
		mPullRefreshListView.onRefreshComplete();
	}
}
