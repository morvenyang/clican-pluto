package com.chinatelecom.xysq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
		userNameTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProfileActivity.this,
						LoginActivity.class);
				Log.d("XYSQ", "start LoginActivity");
				startActivity(intent);
			}

		});
		msisdnTextView.setText("");
	}
}
