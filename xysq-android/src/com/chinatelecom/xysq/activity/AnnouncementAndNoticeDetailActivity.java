package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;

public class AnnouncementAndNoticeDetailActivity extends BaseActivity {

	private AnnouncementAndNotice announcementAndNotice;

	private TextView headTextView;

	private TextView titleTextView;

	private TextView contentTextView;
	
	private boolean announcement;

	@Override
	protected String getPageName() {
		if(this.announcement){
			return "小区公告详细";
		}else{
			return "业主须知详细";
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement_and_notice_detail);
		Intent intent = this.getIntent();
		announcementAndNotice = (AnnouncementAndNotice) intent
				.getParcelableExtra("announcementAndNotice");
		this.announcement = announcementAndNotice.getInnerModule().equals("ANNOUNCEMENT")?true:false;
		Button backButton = (Button)findViewById(R.id.announcement_and_notice_detail_backButton);
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AnnouncementAndNoticeDetailActivity.this.finish();
			}
		});
		this.headTextView = (TextView) this
				.findViewById(R.id.announcement_and_notice_detail_headTextView);
		this.titleTextView = (TextView)this.findViewById(R.id.announcement_and_notice_detail_titleTextView);
		this.contentTextView = (TextView)this.findViewById(R.id.announcement_and_notice_detail_contentTextView);
		if(this.announcement){
			this.headTextView.setText("小区公告");
		}else{
			this.headTextView.setText("业主须知");
		}
		this.titleTextView.setText(announcementAndNotice.getTitle());
		this.contentTextView.setText(announcementAndNotice.getContent());
	}
}
