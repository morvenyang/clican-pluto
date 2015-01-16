package com.chinatelecom.xysq.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.activity.NoticeActivity;
import com.chinatelecom.xysq.other.Constants;
import com.chinatelecom.xysq.util.KeyValueUtils;

public class IndexOnClickListener implements OnClickListener {

	private Class<?> activityClass;
	private IndexActivity indexActivity;
	public IndexOnClickListener(IndexActivity indexActivity,
			Class<?> activityClass){
		this.activityClass = activityClass;
		this.indexActivity = indexActivity;
	}
	
	@Override
	public void onClick(View v) {
		Long communityId = KeyValueUtils.getLongValue(
				indexActivity, Constants.COMMUNITY_ID);
		if (communityId == null) {
			new AlertDialog.Builder(indexActivity)
		    .setTitle("警告")
		    .setMessage("请先选择小区")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	dialog.dismiss();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		} else {
			Intent intent = new Intent(indexActivity,
					activityClass);
			intent.putExtra("communityId", communityId);
			Log.d("XYSQ",
					((Button)v).getText()+" is clicked, start "+activityClass.getSimpleName());
			indexActivity.startActivity(intent);
		}
	}

}
