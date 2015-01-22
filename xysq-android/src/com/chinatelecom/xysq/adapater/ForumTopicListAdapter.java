package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinatelecom.xysq.bean.ForumTopic;

public class ForumTopicListAdapter extends BaseAdapter {

	private List<ForumTopic> forumTopicList;

	private LayoutInflater inflater;

	private Activity activity;

	public ForumTopicListAdapter(
			List<ForumTopic> forumTopicList,
			Activity activity, LayoutInflater inflater) {
		this.forumTopicList = forumTopicList;
		this.activity = activity;
		this.inflater = inflater;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}

}
