package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;

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
				Intent intent = new Intent(XqncApplyActivity.this,
						IndexActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		xqncSubmitButton = (Button) this.findViewById(R.id.xqnc_submitButton);
		xqncSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(XqncApplyActivity.this,XqncFormActivity.class);
				startActivity(intent);
			}
		});

	}


	@Override
	protected String getPageName() {
		return "小区挪车";
	}

}
