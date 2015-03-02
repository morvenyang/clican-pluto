package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.XqncRequest;
import com.chinatelecom.xysq.util.AlertUtil;

public class XqncCancelActivity extends BaseActivity implements HttpCallback {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xqnc_cancel);
		Button backButton = (Button) this
				.findViewById(R.id.xqnc_cancel_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(XqncCancelActivity.this,
						IndexActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		Button xqncCancelButton = (Button) this
				.findViewById(R.id.xqnc_cancelButton);
		xqncCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				XqncRequest.cancelXqnc(XqncCancelActivity.this, getUser());
			}
		});

	}

	@Override
	public void success(String url, Object data) {
		this.getUser().setApplyXqnc(false);
		this.getUser().setCarNumber(null);
		finish();
		Intent intent = new Intent(this, XqncApplyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void failure(String url, int code, String message) {
		AlertUtil.alert(this, message);
	}

	@Override
	protected String getPageName() {
		return "小区挪车";
	}

}
