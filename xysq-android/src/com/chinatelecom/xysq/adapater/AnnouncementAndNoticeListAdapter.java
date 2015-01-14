package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.AnnouncementAndNoticeDetailActivity;
import com.chinatelecom.xysq.bean.AnnouncementAndNotice;

public class AnnouncementAndNoticeListAdapter extends BaseAdapter {

	private List<AnnouncementAndNotice> announcementAndNoticeList;

	private LayoutInflater inflater;

	private Activity activity;

	public AnnouncementAndNoticeListAdapter(
			List<AnnouncementAndNotice> announcementAndNoticeList,
			Activity activity, LayoutInflater inflater) {
		this.announcementAndNoticeList = announcementAndNoticeList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return announcementAndNoticeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.announcement_and_notice_select_row, null);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.announcement_and_notice_select_row_title);
		final AnnouncementAndNotice aan = announcementAndNoticeList
				.get(position);
		title.setText(aan.getTitle());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(activity,
						AnnouncementAndNoticeDetailActivity.class);
				intent.putExtra("announcementAndNotice", aan);
				Log.d("XYSQ",
						"announcementAndNotice is clicked, start AnnouncementAndNoticeActivity");
				activity.startActivity(intent);
			}
		});
		return convertView;
	}

}
