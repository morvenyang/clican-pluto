package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.ProfileListAdapter;

public class ProfileActivity extends Activity {

	private TextView userNameTextView;

	private TextView msisdnTextView;

	private ListView listView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		userNameTextView = (TextView) findViewById(R.id.profile_userNameTextView);
		msisdnTextView = (TextView) findViewById(R.id.profile_msisdnTextView);
		listView = (ListView) findViewById(R.id.profile_listView);
		listView.setAdapter(new ProfileListAdapter(
				this,
				(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
		userNameTextView.setText("请先登录");
		msisdnTextView.setText("");
	}
}
