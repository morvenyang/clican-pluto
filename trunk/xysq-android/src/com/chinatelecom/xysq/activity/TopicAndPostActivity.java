package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAdapter;
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
	private Button cancelPhotoButton;
	private Button sendButton;
	private GridView selectedPhotosGridView;
	private PhotoAdapter selectedPhotoAdapter;
	private List<PhotoItem> selectedBitList;
	private boolean topic;
	private ProgressBar progressBar;
	private Long communityId;
	private Long topicId;
	private String replyContent;

	private TableLayout selectPhotoTableLayout;

	private int selectedPhotoPosition;

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
						ForumRequest
								.savePost(TopicAndPostActivity.this, user,
										topicId, content, replyContent,
										selectedBitList);
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
				ArrayList<PhotoItem> piList = new ArrayList<PhotoItem>();
				for(PhotoItem pi :selectedBitList){
					if(!pi.isShowEmpty()){
						piList.add(pi);
					}
				}
				selectPhotoTableLayout.setVisibility(View.GONE);
				sendButton.setVisibility(View.VISIBLE);
				intent.putParcelableArrayListExtra("selectedBitList", piList);
				startActivity(intent);
			}
		});
		this.takePhotoButton = (Button) findViewById(R.id.topicAndPost_takePhotoButton);
		this.takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if(intent!=null){
					intent.putExtra("selectedPhotoPosition", selectedPhotoPosition);
				}
				selectPhotoTableLayout.setVisibility(View.GONE);
				sendButton.setVisibility(View.VISIBLE);
			}
		});
		this.cancelPhotoButton = (Button)findViewById(R.id.topicAndPost_cancelPhotoButton);
		this.cancelPhotoButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				selectPhotoTableLayout.setVisibility(View.GONE);
				sendButton.setVisibility(View.VISIBLE);
			}
		});
		this.headTextView = (TextView) this
				.findViewById(R.id.topicAndPost_titleTextView);
		this.titleEditText = (EditText) this
				.findViewById(R.id.topicAndPost_titleEditText);
		if (this.topic) {
			this.headTextView.setText("发布帖子");
			this.titleEditText.setVisibility(View.VISIBLE);
		} else {
			this.headTextView.setText("发布回复");
			this.titleEditText.setVisibility(View.GONE);
		}
		this.contentEditText = (EditText) this
				.findViewById(R.id.topicAndPost_contentEditText);

		selectedPhotosGridView = (GridView) findViewById(R.id.topicAndPost_selected_photos_gridView);
		selectedPhotosGridView
				.setSelector(new ColorDrawable(Color.TRANSPARENT));
		selectedBitList = new ArrayList<PhotoItem>();
		for (int i = 0; i < 4; i++) {
			PhotoItem pi = new PhotoItem(true);
			selectedBitList.add(pi);
		}
		selectedPhotoAdapter = new PhotoAdapter(this, this.selectedBitList,
				true);
		selectedPhotosGridView.setAdapter(selectedPhotoAdapter);
		selectedPhotosGridView.setOnItemClickListener(photoSelectedItemClickListener);
		this.selectPhotoTableLayout = (TableLayout) this
				.findViewById(R.id.topicAndPost_selectPhotoTableLayout);
		progressBar.setVisibility(View.INVISIBLE);
	}

	private OnItemClickListener photoSelectedItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectedPhotoPosition = position;
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			selectPhotoTableLayout.setVisibility(View.VISIBLE);
			sendButton.setVisibility(View.GONE);
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void success(String url, Object data) {
		if (url.equals("/saveTopic.do") || url.equals("/savePost.do")) {
			this.finish();
		} else {
			List<PhotoItem> bitList = (List<PhotoItem>) data;
			this.selectedBitList.clear();
			this.selectedBitList.addAll(bitList);
			while(this.selectedBitList.size()<4){
				this.selectedBitList.add(new PhotoItem(true));
			}
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
			return "发布帖子";
		} else {
			return "发布回复";
		}

	}

}
