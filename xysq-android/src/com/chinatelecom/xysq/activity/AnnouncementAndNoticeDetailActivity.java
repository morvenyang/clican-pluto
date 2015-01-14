package com.chinatelecom.xysq.activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;

public class AnnouncementAndNoticeDetailActivity extends Activity {

	private AnnouncementAndNotice announcementAndNotice;

	private TextView headTextView;

	private TextView titleTextView;

	private TextView modifyTimeTextView;

	private TextView contentTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement_and_notice_detail);
		Intent intent = this.getIntent();
		announcementAndNotice = (AnnouncementAndNotice) intent
				.getParcelableExtra("announcementAndNotice");
		boolean announcement = announcementAndNotice.getInnerModule().equals("ANNOUNCEMENT")?true:false;
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
		this.modifyTimeTextView = (TextView)this.findViewById(R.id.announcement_and_notice_detail_modifyTimeTextView);
		this.contentTextView = (TextView)this.findViewById(R.id.announcement_and_notice_detail_contentTextView);
		if(announcement){
			this.headTextView.setText("小区公告");
		}else{
			this.headTextView.setText("业主须知");
		}
		this.titleTextView.setText(announcementAndNotice.getTitle());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		this.modifyTimeTextView.setText(sdf.format(announcementAndNotice.getModifyTime()));
		this.contentTextView.setText(announcementAndNotice.getContent());
	}
}
