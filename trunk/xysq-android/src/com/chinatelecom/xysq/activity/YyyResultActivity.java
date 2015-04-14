package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.Lottery;

public class YyyResultActivity extends BaseActivity {

	private Lottery lottery;

	@Override
	protected String getPageName() {
		return "摇一摇结果页面";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yyy_result);
		Button backButton = (Button) findViewById(R.id.yyy_result_backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.lottery = (Lottery) this.getIntent().getParcelableExtra("lottery");

		TextView msg1 = (TextView) findViewById(R.id.yyy_result_msg1);
		TextView msg2 = (TextView) findViewById(R.id.yyy_result_msg2);
		TextView msg3 = (TextView) findViewById(R.id.yyy_result_msg3);

		Button actionButton = (Button) findViewById(R.id.yyy_result_next_action);
		msg1.setVisibility(View.VISIBLE);
		msg2.setVisibility(View.VISIBLE);
		msg3.setVisibility(View.VISIBLE);
		if (lottery.getMoney() > 0) {
			msg1.setText("恭喜！经你 石破天惊一摇 获得了");
			msg2.setText(lottery.getMoney()+" 流量币");
			msg3.setText("你现在拥有"+lottery.getTotalMoney()+"个流量币");
			actionButton.setText("再摇一次");
			actionButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		} else {
			if(this.lottery.isShare()){
				msg1.setText("今天摇的次数已经用完咯！");
				msg2.setVisibility(View.GONE);
				msg3.setVisibility(View.GONE);
				actionButton.setText("分享给朋友还可以获得一次机会");
				actionButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						//TOTO
					}
				});
			}else{
				msg1.setText("摇的机会已经用完咯！");
				msg2.setText("明天再来吧!");
				msg3.setVisibility(View.GONE);
				actionButton.setText("好的");
				actionButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						finish();
					}
				});
			}
		}

	}
}
