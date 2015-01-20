package com.chinatelecom.xysq.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UpdateRequest {

	public static void checkUpdate(final Activity activity) {
		AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
			@Override
			protected Void doInBackground(String... params) {
				UpdateConfig.setDebug(true);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(activity,
									updateInfo);
							break;
						case UpdateStatus.No: // has no update
							Toast.makeText(activity, "没有更新", Toast.LENGTH_SHORT)
									.show();
							break;
						case UpdateStatus.NoneWifi: // none wifi
							Toast.makeText(activity, "没有wifi连接， 只在wifi下更新",
									Toast.LENGTH_SHORT).show();
							break;
						case UpdateStatus.Timeout: // time out
							Toast.makeText(activity, "超时", Toast.LENGTH_SHORT)
									.show();
							break;
						}
					}
				});
				UmengUpdateAgent.forceUpdate(activity);
				return null;
			}

			@Override
			protected void onPostExecute(Void v) {

			}
		};
		task.execute(new String[] {});
	}
}
