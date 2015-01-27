package com.chinatelecom.xysq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.ProfileListAdapter;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;

public class ProfileActivity extends BaseActivity {

	private TextView nickNameTextView;

	private TextView msisdnTextView;

	private ListView listView;
	
	private RelativeLayout profileLayout;

	@Override
	protected String getPageName() {
		return "个人中心";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		nickNameTextView = (TextView) findViewById(R.id.profile_nickNameTextView);
		msisdnTextView = (TextView) findViewById(R.id.profile_msisdnTextView);
		listView = (ListView) findViewById(R.id.profile_listView);
		listView.setAdapter(new ProfileListAdapter(
				this,
				(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
		profileLayout = (RelativeLayout)findViewById(R.id.profile_layout);
		profileLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User user = getUser();
				if(user!=null){
					
				}else{
					Intent intent = new Intent(ProfileActivity.this,
							LoginActivity.class);
					Log.d("XYSQ", "start LoginActivity");
					startActivity(intent);
				}
			}
		});
		
		

	}

	@Override
	protected void onResume() {
		super.onResume();
		XysqApplication application = (XysqApplication) this.getApplication();
		User user = application.getUser();
		if (user != null) {
			nickNameTextView.setText(user.getNickName());
			msisdnTextView.setText(user.getMsisdn());
		} else {
			nickNameTextView.setText("登录/注册");
			msisdnTextView.setText("");
		}
	}

	

}
