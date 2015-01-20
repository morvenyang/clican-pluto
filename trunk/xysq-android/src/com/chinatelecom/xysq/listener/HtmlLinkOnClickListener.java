package com.chinatelecom.xysq.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinatelecom.xysq.activity.WebViewActivity;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.util.AlertUtil;

public class HtmlLinkOnClickListener implements OnClickListener {

	private String url;
	
	private String title;

	private Activity activity;

	private boolean requireLogin;

	public HtmlLinkOnClickListener(String url, String title,Activity activity,
			boolean requireLogin) {
		this.url = url;
		this.title = title;
		this.activity = activity;
		this.requireLogin = requireLogin;
	}

	@Override
	public void onClick(View v) {
		XysqApplication application = (XysqApplication) activity
				.getApplication();
		User user = application.getUser();
		if (requireLogin) {
			if (user == null) {
				AlertUtil.alert(activity, "请先登录");
				return;
			}
		}

		Intent intent = new Intent(activity, WebViewActivity.class);
		intent.putExtra("title", title);
		if (requireLogin) {
			intent.putExtra("url", url + "&openId=" + user.getJsessionid());
		} else {
			intent.putExtra("url", url);
		}
		activity.startActivity(intent);
	}

}
