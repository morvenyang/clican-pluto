package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.ImageRequest;

public class AnnouncementAndNoticeContentAdapter extends BaseAdapter {

	private List<String> contents;

	private LayoutInflater inflater;

	private Activity activity;
	
	public AnnouncementAndNoticeContentAdapter(List<String> contents,
			Activity activity,LayoutInflater inflater) {
		this.contents = contents;
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return contents.size();
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
			convertView = inflater.inflate(R.layout.announcement_and_notice_detail_row, null);
		}
		final String content = contents.get(position);
		TextView textView = (TextView) convertView
				.findViewById(R.id.announcement_and_notice_detail_row_text);
		ImageView imageView = (ImageView)convertView
				.findViewById(R.id.announcement_and_notice_detail_row_image);
		if(content.startsWith("http://")){
			textView.setVisibility(View.GONE);
			ImageRequest.requestImage(imageView, content);
		}else{
			imageView.setVisibility(View.GONE);
			textView.setText(content);
		}
		return convertView;
	}

}
