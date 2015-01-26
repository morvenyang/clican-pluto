package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAdapter;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.ForumRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.PhotoRequest;
import com.chinatelecom.xysq.util.AlertUtil;

public class TopicAndPostActivity extends BaseActivity implements HttpCallback {

	private TextView headTextView;
	private EditText titleEditText;
	private EditText contentEditText;
	private Button selectPhotoButton;
	private Button takePhotoButton;
	private Button sendButton;
	private GridView selectedPhotosGridView;
	private PhotoAdapter selectedPhotoAdapter;
	private List<PhotoItem> selectedBitList;
	private boolean topic;
	private ProgressBar progressBar;
	private Long communityId;
	private Long topicId;
	private String replyContent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_and_post);
		Intent intent = this.getIntent();
		communityId = intent.getLongExtra("communityId", -1);

		this.progressBar = (ProgressBar) this
				.findViewById(R.id.topicAndPost_progressBar);
		this.sendButton = (Button) this
				.findViewById(R.id.topicAndPost_sendButton);
		this.sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressBar.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				String title = titleEditText.getEditableText().toString();
				String content = contentEditText.getEditableText().toString();
				User user = getUser();
				if (user == null) {
					AlertUtil.alert(TopicAndPostActivity.this, "请先登录");
				} else {
					if (topic) {
						ForumRequest.saveTopic(TopicAndPostActivity.this, user,
								communityId, title, content, selectedBitList);
					} else {
						ForumRequest.savePost(TopicAndPostActivity.this, user, topicId, content, replyContent, selectedBitList);
					}

				}
			}
		});
		this.topic = intent.getBooleanExtra("topic", true);
		this.topicId = intent.getLongExtra("topicId", -1);
		this.replyContent = intent.getStringExtra("replyContent");
		Button backButton = (Button) findViewById(R.id.topicAndPost_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.selectPhotoButton = (Button) findViewById(R.id.topicAndPost_selectPhotoButton);
		selectPhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TopicAndPostActivity.this,
						PhotoAlbumActivity.class);
				startActivity(intent);
			}
		});
		this.takePhotoButton = (Button) findViewById(R.id.topicAndPost_takePhotoButton);
		this.headTextView = (TextView) this
				.findViewById(R.id.topicAndPost_titleTextView);
		this.titleEditText = (EditText) this
				.findViewById(R.id.topicAndPost_titleEditText);
		if (this.topic) {
			this.headTextView.setText("发贴");
			this.titleEditText.setVisibility(View.VISIBLE);
		} else {
			this.headTextView.setText("回复");
			this.titleEditText.setVisibility(View.GONE);
		}
		this.contentEditText = (EditText) this
				.findViewById(R.id.topicAndPost_contentEditText);

		selectedPhotosGridView = (GridView) findViewById(R.id.topicAndPost_selected_photos_gridView);
		selectedPhotosGridView
				.setSelector(new ColorDrawable(Color.TRANSPARENT));
		selectedBitList = new ArrayList<PhotoItem>();
		selectedPhotoAdapter = new PhotoAdapter(this, this.selectedBitList,
				true);
		selectedPhotosGridView.setAdapter(selectedPhotoAdapter);
		progressBar.setVisibility(View.INVISIBLE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		if (url.equals("/saveTopic.do")||url.equals("/savePost.do")) {
			this.finish();
		} else {
			List<PhotoItem> bitList = (List<PhotoItem>) data;
			this.selectedBitList.clear();
			this.selectedBitList.addAll(bitList);
			selectedPhotoAdapter.notifyDataSetChanged();
		}
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		progressBar.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		List<PhotoItem> selectedBitList = intent
				.getParcelableArrayListExtra("selectedBitList");
		PhotoRequest.prepareThumbnail(this, this, selectedBitList);

		super.onNewIntent(intent);
	}

	@Override
	protected String getPageName() {
		if (topic) {
			return "发贴";
		} else {
			return "回复";
		}

	}

}
