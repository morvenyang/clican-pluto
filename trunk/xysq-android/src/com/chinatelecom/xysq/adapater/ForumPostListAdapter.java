package com.chinatelecom.xysq.adapater;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.TopicAndPostActivity;
import com.chinatelecom.xysq.bean.ForumPost;
import com.chinatelecom.xysq.bean.ForumTopic;

public class ForumPostListAdapter extends BaseAdapter {

	private List<ForumPost> forumPostList;

	private LayoutInflater inflater;

	private Activity activity;

	private ForumTopic topic;

	public ForumPostListAdapter(List<ForumPost> forumPostList,
			Activity activity, LayoutInflater inflater, ForumTopic topic) {
		this.forumPostList = forumPostList;
		this.activity = activity;
		this.inflater = inflater;
		this.topic = topic;
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

	private int getHeight(Context context, String text, int textSize, int deviceWidth) {
	    TextView textView = new TextView(context);
	    textView.setText(text);
	    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.AT_MOST);
	    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
	    textView.measure(widthMeasureSpec, heightMeasureSpec);
	    return textView.getMeasuredHeight();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.post_row, null);
		}
		final ForumPost forumPost = forumPostList.get(position);
		TextView nickNameTextView = (TextView) convertView
				.findViewById(R.id.post_row_nickNameTextView);
		nickNameTextView.setText(forumPost.getSubmitter().getNickName());

		TextView floorTextView = (TextView) convertView
				.findViewById(R.id.post_row_floorTextView);
		floorTextView.setText((position + 1) + "楼");

		TextView contentTextView = (TextView) convertView
				.findViewById(R.id.post_row_contentTextView);
		contentTextView.setText(forumPost.getContent());

		TextView createTimeTextView = (TextView) convertView
				.findViewById(R.id.post_row_createTimeTextView);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		createTimeTextView.setText(sdf.format(forumPost.getCreateTime()));

		ImageView replyContentFlagImageView = (ImageView) convertView
				.findViewById(R.id.post_row_replyContentFlagImageView);
		TextView replyContentTextView = (TextView)convertView.findViewById(R.id.post_row_replyContentTextView);
		if(StringUtils.isEmpty(forumPost.getReplyContent())){
			replyContentTextView.setVisibility(View.GONE);
			replyContentFlagImageView.setVisibility(View.GONE);
		}else{
			replyContentTextView.setVisibility(View.VISIBLE);
			replyContentFlagImageView.setVisibility(View.VISIBLE);
			replyContentTextView.setText(forumPost.getReplyContent());
		}
		
		Button button = (Button) convertView
				.findViewById(R.id.post_row_replyButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, TopicAndPostActivity.class);
				intent.putExtra("topic", false);
				intent.putExtra("replyContent", (position+1)+"楼"+" "+forumPost.getSubmitter().getNickName()+"\n"+forumPost.getContent());
				intent.putExtra("topicId", topic.getId());
				activity.startActivity(intent);
			}
		});
		return convertView;
	}

}