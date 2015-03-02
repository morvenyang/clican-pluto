package com.chinatelecom.xysq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.http.XqncRequest;
import com.chinatelecom.xysq.util.AlertUtil;

public class XqncFormActivity extends BaseActivity implements HttpCallback {

	private EditText carNumberEditText;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xqnc_form);
		Button backButton = (Button) this.findViewById(R.id.xqnc_form_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button xqncApplyButton = (Button) this.findViewById(R.id.xqnc_applyButton);
		carNumberEditText = (EditText)this.findViewById(R.id.xqnc_carNumberEditText);
		xqncApplyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String carNumber = carNumberEditText.getText().toString();
				XqncRequest.applyXqnc(XqncFormActivity.this, carNumber,getUser());
			}
		});

	}
	
	@Override
	public void success(String url, Object data) {
		this.getUser().setApplyXqnc(true);
		this.getUser().setCarNumber(carNumberEditText.getText().toString());
		Intent intent = new Intent(this,XqncCancelActivity.class);
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
