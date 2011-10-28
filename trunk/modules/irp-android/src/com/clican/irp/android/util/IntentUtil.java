package com.clican.irp.android.util;

import android.os.Bundle;

import com.clican.irp.android.enumeration.IntentName;

public class IntentUtil {

	@SuppressWarnings("unchecked")
	public static <T> T get(Bundle savedInstanceState,
			Bundle extras, IntentName intentName) {
		T t = (savedInstanceState == null) ? null : (T) savedInstanceState
				.get(intentName.name());
		if (t == null) {
			t = extras != null ? (T) extras.get(intentName.name()) : null;
		}
		return t;
	}
}
