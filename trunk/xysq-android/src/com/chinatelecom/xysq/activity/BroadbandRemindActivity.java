package com.chinatelecom.xysq.activity;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.BroadbandRemind;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.BroadbandRemindRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.util.AlertUtil;

public class BroadbandRemindActivity extends Activity implements HttpCallback {
	private ProgressBar progressBar;

	private TextView titleTextView;

	private TextView broadbandIdTextView;

	private TextView broadbandUserNameTextView;

	private TextView broadbandExpireDateTextView;
	
	private TableLayout tableLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.broadband_remind);

		progressBar = (ProgressBar) this
				.findViewById(R.id.broadband_remind_progressBar);
		Button backButton = (Button) this
				.findViewById(R.id.broadband_remind_backButton);

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tableLayout = (TableLayout)this.findViewById(R.id.broadband_remind_table);
		titleTextView = (TextView) this
				.findViewById(R.id.broadband_remind_title_textView);
		broadbandIdTextView = (TextView) this
				.findViewById(R.id.boradband_remind_broadbandId_textView);
		broadbandUserNameTextView = (TextView) this
				.findViewById(R.id.broadband_remind_userName_textView);
		broadbandExpireDateTextView = (TextView) this
				.findViewById(R.id.broadband_remind_expiredDate_textView);
		loadBroadbandRemindData();
	}

	@Override
	public void success(String url, Object data) {
		progressBar.setVisibility(View.INVISIBLE);
		BroadbandRemind remind = (BroadbandRemind) data;
		if (remind.isExist()) {
			tableLayout.setVisibility(View.VISIBLE);
			titleTextView.setText("你的宽带到期时间为");
			broadbandIdTextView.setText(remind.getBroadBandId());
			broadbandUserNameTextView.setText(remind.getUserName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.ENGLISH);
			broadbandExpireDateTextView.setText(sdf.format(remind.getExpiredDate()));
		} else {
			tableLayout.setVisibility(View.INVISIBLE);
			titleTextView.setText("没有你相关的宽带提醒数据");
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		progressBar.setVisibility(View.INVISIBLE);
		AlertUtil.alert(this, message);
	}

	private void loadBroadbandRemindData() {
		XysqApplication application = (XysqApplication) this.getApplication();
		if (application.getUser() == null) {
			AlertUtil.alert(this, "请先登录");
			return;
		} else {
			User user = application.getUser();
			progressBar.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			BroadbandRemindRequest.queryBroadbandRemind(this, user.getMsisdn());
		}
	}
}
