package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.User;

public class XqncApplyActivity extends BaseActivity {

	private Button xqncSubmitButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xqnc_apply);
		Button backButton = (Button) this.findViewById(R.id.xqnc_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		xqncSubmitButton = (Button) this.findViewById(R.id.xqnc_submitButton);
		xqncSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		User user = this.getUser();
	}

	@Override
	protected String getPageName() {
		return "小区挪车";
	}

}
