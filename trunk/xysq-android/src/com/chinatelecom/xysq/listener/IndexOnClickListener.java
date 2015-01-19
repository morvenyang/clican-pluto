package com.chinatelecom.xysq.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.AlertUtil;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexOnClickListener implements OnClickListener {

	private Class<?> activityClass;
	private IndexActivity indexActivity;

	public IndexOnClickListener(IndexActivity indexActivity,
			Class<?> activityClass) {
		this.activityClass = activityClass;
		this.indexActivity = indexActivity;
	}

	@Override
	public void onClick(View v) {
		Long communityId = KeyValueUtils.getLongValue(indexActivity,
				Constants.COMMUNITY_ID);
		if (communityId == null) {
			AlertUtil.alert(indexActivity, "请先选择小区");
		} else {
			Intent intent = new Intent(indexActivity, activityClass);
			intent.putExtra("communityId", communityId);
			Log.d("XYSQ", ((Button) v).getText() + " is clicked, start "
					+ activityClass.getSimpleName());
			indexActivity.startActivity(intent);
		}
	}

}
