package com.chinatelecom.xysq.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class KeyValueUtils {

	public static void setValue(Context ctx,String key, String value) {
		// 存入数据
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getValue(Context ctx,String key) {
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		return sp.getString(key, null);
	}
}
