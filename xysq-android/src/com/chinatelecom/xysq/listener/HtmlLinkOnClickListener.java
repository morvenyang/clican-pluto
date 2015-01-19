package com.chinatelecom.xysq.listener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.util.AlertUtil;

public class HtmlLinkOnClickListener implements OnClickListener {

	private String url;

	private Activity activity;

	public HtmlLinkOnClickListener(String url, Activity activity) {
		this.url = url;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		XysqApplication application = (XysqApplication) activity
				.getApplication();
		User user = application.getUser();
		if (user == null) {
			AlertUtil.alert(activity, "请先登录");
		} else {

			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url
					+ "&openId=" + user.getJsessionid()));
			activity.startActivity(browserIntent);
		}

	}

}
