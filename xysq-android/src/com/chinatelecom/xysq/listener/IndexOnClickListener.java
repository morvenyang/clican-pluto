package com.chinatelecom.xysq.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexOnClickListener implements OnClickListener {

	private Class<?> activityClass;
	private Activity activity;
	private boolean requireLogin;

	private boolean requireCommunity;

	public IndexOnClickListener(Activity activity, Class<?> activityClass,
			boolean requireLogin, boolean requireCommunity) {
		this.activityClass = activityClass;
		this.activity = activity;
		this.requireLogin = requireLogin;
		this.requireCommunity = requireCommunity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(activity, activityClass);
		XysqApplication application = (XysqApplication) activity
				.getApplication();
		User user = application.getUser();
		if (requireLogin) {
			if (user == null) {
				AlertUtil.alert(activity, "请先登录");
				return;
			}
		}
		Long communityId = KeyValueUtils.getLongValue(activity,
				Constants.COMMUNITY_ID);
		if (requireCommunity) {
			if (communityId == null) {
				AlertUtil.alert(activity, "请先选择小区");
				return;
			} else {
				intent.putExtra("communityId", communityId);
			}
		}
		activity.startActivity(intent);

	}

}
