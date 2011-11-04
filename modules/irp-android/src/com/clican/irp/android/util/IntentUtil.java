package com.clican.irp.android.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.clican.irp.android.enumeration.IntentName;

public class IntentUtil {

	@SuppressWarnings("unchecked")
	public static <T> T get(Bundle savedInstanceState, Bundle extras,
			IntentName intentName) {
		T t = (savedInstanceState == null) ? null : (T) savedInstanceState
				.get(intentName.name());
		if (t == null) {
			t = extras != null ? (T) extras.get(intentName.name()) : null;
		}
		return t;
	}

	public static Intent getPdfFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri localUri = Uri.fromFile(new File(param));
		intent.setDataAndType(localUri, "application/pdf");
		return intent;
	}
}
