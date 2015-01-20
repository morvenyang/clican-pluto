package com.chinatelecom.xysq.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class MainActivity extends TabActivity implements OnTabChangeListener {

	TabHost tabHost;

	private View indexView, xyzsView, profileView;

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化基本属性读写类
		setContentView(R.layout.main);
		tabHost = getTabHost();
		setTabs();
		tabHost.setOnTabChangedListener(this);
		PushAgent.getInstance(this).onAppStart();
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		String device_token = UmengRegistrar.getRegistrationId(this);
		Log.d("XYSQ", "device token:"+device_token);
	}

	private void setTabs() {
		indexView = addTab("首页", R.drawable.tab_home, IndexActivity.class);
		xyzsView = addTab("小翼助手", R.drawable.tab_xiaoyi, XyzsActivity.class);
		profileView = addTab("个人中心", R.drawable.tab_personal,
				ProfileActivity.class);
	}

	private View addTab(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		if (drawableId == R.drawable.tab_home) {
			title.setTextColor(this.getResources().getColor(R.color.lightGreen));
		}
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
		return tabIndicator;
	}

	@Override
	public void onTabChanged(String tabId) {
		View selectedView = null;
		if (tabId.contains("首页")) {
			selectedView = indexView;
		} else if (tabId.contains("小翼助手")) {
			selectedView = xyzsView;
		} else if (tabId.contains("个人中心")) {
			selectedView = profileView;
		}
		TextView title = (TextView) indexView.findViewById(R.id.title);
		title.setTextColor(this.getResources().getColor(R.color.labelTextColor));
		title = (TextView) xyzsView.findViewById(R.id.title);
		title.setTextColor(this.getResources().getColor(R.color.labelTextColor));
		title = (TextView) profileView.findViewById(R.id.title);
		title.setTextColor(this.getResources().getColor(R.color.labelTextColor));

		title = (TextView) selectedView.findViewById(R.id.title);
		title.setTextColor(this.getResources().getColor(R.color.lightGreen));
	}

}
