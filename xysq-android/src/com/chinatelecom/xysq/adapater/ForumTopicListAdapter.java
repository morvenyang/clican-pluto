package com.chinatelecom.xysq.adapater;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.ForumTopic;
import com.chinatelecom.xysq.util.DateUtil;

public class ForumTopicListAdapter extends BaseAdapter {

	private List<ForumTopic> forumTopicList;

	private LayoutInflater inflater;

	private Activity activity;

	public ForumTopicListAdapter(List<ForumTopic> forumTopicList,
			Activity activity, LayoutInflater inflater) {
		this.forumTopicList = forumTopicList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return forumTopicList.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = inflater.inflate(R.layout.forum_row, null);
		}
		ForumTopic ft = forumTopicList.get(position);
		TextView titleTextView = (TextView) convertView
				.findViewById(R.id.forum_row_titleTextView);
		titleTextView.setText(ft.getTitle());
		TextView nickNameTextView = (TextView) convertView
				.findViewById(R.id.forum_row_nickNameTextView);
		nickNameTextView.setText(ft.getSubmitter().getNickName());
		TextView timeTextView = (TextView) convertView
				.findViewById(R.id.forum_row_timeTextView);
		Date modifyTime=ft.getModifyTime();
		timeTextView.setText(DateUtil.convertDateToBBSTime(modifyTime));
		
		TextView postNumTextView = (TextView) convertView
				.findViewById(R.id.forum_row_postNumTextView);
		postNumTextView.setText(ft.getPostNum()+"回复");
		return convertView;
	}

}
