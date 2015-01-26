package com.chinatelecom.xysq.adapater;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.ForumPost;

public class ForumPostListAdapter extends BaseAdapter {

	private List<ForumPost> forumPostList;

	private LayoutInflater inflater;

	private Activity activity;

	public ForumPostListAdapter(List<ForumPost> forumPostList,
			Activity activity, LayoutInflater inflater) {
		this.forumPostList = forumPostList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return forumPostList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.post_row, null);
		}
		ForumPost forumPost = forumPostList.get(position);
		TextView nickNameTextView = (TextView) convertView
				.findViewById(R.id.post_row_nickNameTextView);
		nickNameTextView.setText(forumPost.getSubmitter().getNickName());
		
		
		TextView floorTextView = (TextView) convertView
				.findViewById(R.id.post_row_floorTextView);
		floorTextView.setText((position+1)+"楼");
		
		
		TextView contentTextView = (TextView) convertView
				.findViewById(R.id.post_row_contentTextView);
		contentTextView.setText(forumPost.getContent());
		
		TextView createTimeTextView = (TextView) convertView
				.findViewById(R.id.post_row_createTimeTextView);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		createTimeTextView.setText(sdf.format(forumPost.getCreateTime()));
		
		Button button = (Button)convertView.findViewById(R.id.post_row_replyButton);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
		return convertView;
	}

}
