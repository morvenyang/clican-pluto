package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.User;

public class XqncActivity extends BaseActivity {

	private Button applyXqncButton;
	private Button shareFriendButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xqnc);
		applyXqncButton = (Button) this.findViewById(R.id.xqnc_applyButton);
		applyXqncButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		shareFriendButton = (Button) this
				.findViewById(R.id.xqnc_shareFriendButton);
		shareFriendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	protected void onResume() {
		User user = this.getUser();
		if (user != null && user.isApplyXqnc()) {
			applyXqncButton.setVisibility(View.GONE);
			shareFriendButton.setVisibility(View.VISIBLE);
		} else {
			shareFriendButton.setVisibility(View.GONE);
			applyXqncButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected String getPageName() {
		return "小区挪车";
	}

}
