package com.chinatelecom.xysq.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.application.XysqApplication;
import com.chinatelecom.xysq.bean.User;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexOnClickListener implements OnClickListener {

	private Class<?> activityClass;
	private IndexActivity indexActivity;
	private boolean requireLogin;

	private boolean requireCommunity;

	public IndexOnClickListener(IndexActivity indexActivity,
			Class<?> activityClass, boolean requireLogin,
			boolean requireCommunity) {
		this.activityClass = activityClass;
		this.indexActivity = indexActivity;
		this.requireLogin = requireLogin;
		this.requireCommunity = requireCommunity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(indexActivity, activityClass);
		XysqApplication application = (XysqApplication) indexActivity
				.getApplication();
		User user = application.getUser();
		if (requireLogin) {
			if (user == null) {
				AlertUtil.alert(indexActivity, "请先登录");
				return;
			}
		}
		Long communityId = KeyValueUtils.getLongValue(indexActivity,
				Constants.COMMUNITY_ID);
		if (requireCommunity) {
			if (communityId == null) {
				AlertUtil.alert(indexActivity, "请先选择小区");
				return;
			} else {
				intent.putExtra("communityId", communityId);
			}
		}

		Log.d("XYSQ", ((Button) v).getText() + " is clicked, start "
				+ activityClass.getSimpleName());
		indexActivity.startActivity(intent);

	}

}
