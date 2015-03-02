package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.CommunitySelectActivity;
import com.chinatelecom.xysq.bean.Community;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class CommunityListAdapter extends BaseAdapter {

	private List<Community> communityList;

	private LayoutInflater inflater;

	private CommunitySelectActivity activity;

	public CommunityListAdapter(List<Community> communityList,
			CommunitySelectActivity activity, LayoutInflater inflater) {
		this.communityList = communityList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return communityList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.community_select_row, null);
		}
		TextView communityName = (TextView) convertView
				.findViewById(R.id.community_select_row_communityName);
		TextView communityAddress = (TextView) convertView
				.findViewById(R.id.community_select_row_communityAddress);
		final Community c = communityList.get(position);
		communityName.setText(c.getName());
		communityAddress.setText(c.getDetailAddress());
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				KeyValueUtils.setStringValue(activity, Constants.COMMUNITY_NAME,c.getName());
				KeyValueUtils.setLongValue(activity, Constants.COMMUNITY_ID,c.getId());
				activity.finish();
			}
		});
		return convertView;
	}

}
