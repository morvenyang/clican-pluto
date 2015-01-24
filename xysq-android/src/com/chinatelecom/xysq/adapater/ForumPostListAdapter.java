package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
