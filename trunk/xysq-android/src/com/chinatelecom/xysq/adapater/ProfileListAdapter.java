package com.chinatelecom.xysq.adapater;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.activity.FeedbackActivity;
import com.chinatelecom.xysq.activity.ProfileActivity;
import com.chinatelecom.xysq.http.UpdateRequest;
import com.chinatelecom.xysq.listener.HtmlLinkOnClickListener;
import com.chinatelecom.xysq.other.Constants;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.fragment.FeedbackFragment;

public class ProfileListAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private ProfileActivity activity;

	public ProfileListAdapter(ProfileActivity activity, LayoutInflater inflater) {
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return 6;
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
			convertView = inflater.inflate(R.layout.profile_select_row, null);
		}
		TextView titleTextView = (TextView) convertView
				.findViewById(R.id.profile_select_row_titleTextView);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.profile_select_row_iconImageView);
		if (position == 0) {
			titleTextView.setText("邀请安装免费送流量");
			imageView.setImageResource(R.drawable.icon_1);
		}else if (position == 1) {
			titleTextView.setText("我要兑换");
			imageView.setImageResource(R.drawable.icon_2);
			convertView.setOnClickListener(new HtmlLinkOnClickListener(
					Constants.BASE_URL+"/android/aboutUs.html", "关于我们",activity, false));
		}else if (position == 2) {
			titleTextView.setText("兑换历史");
			imageView.setImageResource(R.drawable.icon_3);
			convertView.setOnClickListener(new HtmlLinkOnClickListener(
					Constants.BASE_URL+"/android/aboutUs.html", "关于我们",activity, false));
		} else if (position == 3) {
			titleTextView.setText("意见反馈");
			imageView.setImageResource(R.drawable.icon_4);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					FeedbackAgent agent = new FeedbackAgent(activity);
//					agent.startFeedbackActivity();
					String id = new FeedbackAgent(activity).getDefaultConversation().getId();
					Intent intent = new Intent(activity,
							FeedbackActivity.class);
					intent.putExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID, id);
					Log.d("XYSQ",
							"意见反馈 is clicked, start ConversationActivity");
					activity.startActivity(intent);
				}
			});
		} else if (position == 4) {
			titleTextView.setText("检查更新");
			imageView.setImageResource(R.drawable.icon_5);
			convertView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					UpdateRequest.checkUpdate(activity);
				}
			});
		} else if (position == 5) {
			titleTextView.setText("关于我们");
			imageView.setImageResource(R.drawable.icon_6);
			convertView.setOnClickListener(new HtmlLinkOnClickListener(
					Constants.BASE_URL+"/android/aboutUs.html", "关于我们",activity, false));
		}
		return convertView;
	}

}
