package com.chinatelecom.xysq.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.bean.PhotoItem;

public class TopicAndPostActivity extends BaseActivity {

	private TextView headTextView;
	private EditText titleEditText;
	private EditText contentEditText;
	private ForumTopic forumTopic;
	private LinearLayout buttonLayout;
	private boolean topic;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_and_post);
		Intent intent = this.getIntent();

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
		this.headTextView = (TextView) this
				.findViewById(R.id.topicAndPost_titleTextView);
		this.titleEditText = (EditText) this
				.findViewById(R.id.topicAndPost_titleEditText);
		this.contentEditText = (EditText) this
				.findViewById(R.id.topicAndPost_contentEditText);
		buttonLayout = (LinearLayout) this
				.findViewById(R.id.topicAndPost_buttonLayout);
	}

	@Override
	protected String getPageName() {
		if (topic) {
			return "发贴";
		} else {
			return "回贴";
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		List<PhotoItem> selectedBitList = data
				.getParcelableArrayListExtra("selectedBitList");
		
		super.onActivityResult(requestCode, resultCode, data);
	}

}
