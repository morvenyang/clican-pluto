package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.ExchangeAward;

public class ExchangeAwardActivity extends BaseActivity {

	private ExchangeAward exchangeAward;

	@Override
	protected String getPageName() {
		return "兑换成功";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_award);
		exchangeAward = this.getIntent().getParcelableExtra("exchangeAward");
		TextView name = (TextView) findViewById(R.id.exchange_award_name);
		TextView code = (TextView) findViewById(R.id.exchange_award_code);
		TextView action = (TextView) findViewById(R.id.exchange_award_action);
		TextView desc = (TextView) findViewById(R.id.exchange_award_desc);
		name.setText(exchangeAward.getName());
		code.setText("asd");
		if (exchangeAward.isRealGood()) {
			action.setText("门店地址");
			desc.setText("使用说明：\n实物奖励请去线下指定门店，向营业员出示串码即可兑换");
		} else {
			action.setText("已激活");
			desc.setText("使用说明：\n您兑换的流量将于24小时内充值到您的账户，请放心使用");
		}
	}

}
