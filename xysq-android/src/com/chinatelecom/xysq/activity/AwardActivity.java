package com.chinatelecom.xysq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.AwardListAdapter;
import com.chinatelecom.xysq.bean.AwardUser;
import com.chinatelecom.xysq.bean.ExchangeAward;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.http.AwardRequest;
import com.chinatelecom.xysq.http.HttpCallback;
import com.chinatelecom.xysq.util.AlertUtil;

public class AwardActivity extends BaseActivity implements HttpCallback {

	private ListView listView;

	private TextView money;

	@Override
	protected String getPageName() {
		return "我要兑换";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.award);
		this.money = (TextView) findViewById(R.id.award_money);
		this.listView = (ListView) findViewById(R.id.award_list);
		Button backButton = (Button) findViewById(R.id.award_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void success(String url, Object data) {
		if (url.equals("/queryAwardUser.do")) {
			AwardUser au = (AwardUser) data;
			money.setText("您现在有：" + au.getMoney() + " 流量币");
			listView.setAdapter(new AwardListAdapter(
					au.getAwards(),
					this,
					(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
					au.getMoney(), getUser()));
		} else {
			ExchangeAward exchangeAward = (ExchangeAward)data;
			Intent intent = new Intent(this, ExchangeAwardActivity.class);
			intent.putExtra("exchangeAward", exchangeAward);
			startActivity(intent);
		}
	}

	@Override
	public void failure(String url, int code, String message) {
		AlertUtil.alert(this, message);
	}

	@Override
	protected void onResume() {
		super.onResume();
		User user = getUser();
		if (user == null) {
			AlertUtil.alert(this, "请先登录");
		} else {
			AwardRequest.queryAwardUser(user, this);
		}
	}

}
