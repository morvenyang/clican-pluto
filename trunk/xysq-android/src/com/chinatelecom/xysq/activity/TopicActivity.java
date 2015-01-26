package com.chinatelecom.xysq.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.ForumPostListAdapter;
import com.chinatelecom.xysq.adapater.PhotoAdapter;
import com.chinatelecom.xysq.bean.ForumPost;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.http.ForumRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

public class TopicActivity extends BaseActivity implements
		OnRefreshListener2<ListView>, OnLastItemVisibleListener, HttpCallback {

	private PullToRefreshListView mPullRefreshListView;
	private ForumPostListAdapter adapter;

	private List<ForumPost> forumPostList = new ArrayList<ForumPost>();

	private int page = 1;

	private ForumTopic topic;

	private GridView photosGridView;

	@Override
	protected String getPageName() {
		return "帖子";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic);
		topic = this.getIntent().getParcelableExtra("topic");
		Button backButton = (Button) findViewById(R.id.topic_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView nickNameTextView = (TextView) findViewById(R.id.topic_nickNameTextView);
		nickNameTextView.setText(topic.getSubmitter().getNickName());

		TextView descriptionTextView = (TextView) findViewById(R.id.topic_descriptionTextView);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		descriptionTextView.setText("发表:" + sdf.format(topic.getCreateTime())
				+ " 回复:" + topic.getPostNum());

		TextView titleTextView = (TextView) findViewById(R.id.topic_topic_titleTextView);
		titleTextView.setText(topic.getTitle());

		TextView contentTextView = (TextView) findViewById(R.id.topic_topic_contentTextView);
		contentTextView.setText(topic.getContent());
		photosGridView = (GridView) findViewById(R.id.topic_photos_gridView);
		if(this.topic.getImages()==null||this.topic.getImages().size()==0){
			photosGridView.setVisibility(View.INVISIBLE);
		}else{
			photosGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			PhotoAdapter photoAdapter = new PhotoAdapter(this,
					this.topic.getImages());
			photosGridView.setAdapter(photoAdapter);
		}

		Button replyButton = (Button) findViewById(R.id.topic_replyButton);
		replyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TopicActivity.this,
						TopicAndPostActivity.class);
				intent.putExtra("topic", false);
				intent.putExtra("replyTopic", true);
				intent.putExtra("topicId", topic.getId());
				TopicActivity.this.startActivity(intent);
			}
		});
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.topic_post_listView);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.getRefreshableView().setDividerHeight(2);
		mPullRefreshListView.getRefreshableView()
				.setFooterDividersEnabled(true);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(this);

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(this);

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		adapter = new ForumPostListAdapter(
				forumPostList,
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
		ForumRequest.queryPost(this, this.topic.getId(), page, 20);
	}

	@Override
	protected void onResume() {
		page=1;
		forumPostList.clear();
		ForumRequest.queryPost(this, this.topic.getId(), page, 20);
		super.onResume();
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
		forumPostList.clear();
		// Do work to refresh the list here.
		ForumRequest.queryPost(this, this.topic.getId(), page, 20);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// Do work to refresh the list here.
		page++;
		ForumRequest.queryPost(this, this.topic.getId(), page, 20);
	}

	@Override
	public void onLastItemVisible() {
		Toast.makeText(this, "没有更多回帖", Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		List<ForumPost> moreData = (List<ForumPost>) data;
		forumPostList.addAll(moreData);
		adapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public void failure(String url, int code, String message) {
		mPullRefreshListView.onRefreshComplete();
	}
}
