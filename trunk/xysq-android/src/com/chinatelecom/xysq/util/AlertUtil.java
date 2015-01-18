package com.chinatelecom.xysq.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertUtil {

	public static void alert(Activity activity, String message) {
		new AlertDialog.Builder(activity)
				.setTitle("警告")
				.setMessage(message)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}
}
