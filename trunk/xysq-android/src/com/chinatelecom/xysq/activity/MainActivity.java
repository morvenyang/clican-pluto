package com.chinatelecom.xysq.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.chinatelecom.xysq.R;

public class MainActivity extends TabActivity {

	TabHost tabHost;

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化基本属性读写类
		setContentView(R.layout.main);
		tabHost = getTabHost();
		setTabs();
	}

	private void setTabs() {
		addTab("首页", R.drawable.tab_index, IndexActivity.class);
		addTab("小翼助手", R.drawable.tab_index, XyzsActivity.class);
		addTab("个人中心", R.drawable.tab_index, ProfileActivity.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

}
