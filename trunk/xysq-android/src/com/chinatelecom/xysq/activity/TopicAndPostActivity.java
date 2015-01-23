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
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.PhotoRequest;

public class TopicAndPostActivity extends BaseActivity implements HttpCallback {

	private TextView headTextView;
	private EditText titleEditText;
	private EditText contentEditText;
	private ForumTopic forumTopic;
	private Button selectPhotoButton;
	private Button takePhotoButton;
	private GridView selectedPhotosGridView;
	private PhotoAdapter selectedPhotoAdapter;
	private List<PhotoItem> selectedBitList;
	private boolean topic;
	private ProgressBar progressBar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_and_post);
		Intent intent = this.getIntent();
		this.progressBar=(ProgressBar)this.findViewById(R.id.topicAndPost_progressBar);
		this.topic = intent.getBooleanExtra("topic", true);
		if (this.topic) {

		} else {

		}
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
		
		List<PhotoItem> bitList = (List<PhotoItem>)data;	
		this.selectedBitList.clear();
		this.selectedBitList.addAll(bitList);
		selectedPhotoAdapter.notifyDataSetChanged();
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
			return "回贴";
		}

	}

}
