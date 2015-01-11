package com.chinatelecom.xysq.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class KeyValueUtils {

	public static void setStringValue(Context ctx,String key, String value) {
		// 存入数据
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringValue(Context ctx,String key) {
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		return sp.getString(key, null);
	}
	
	public static void setLongValue(Context ctx,String key, Long value) {
		// 存入数据
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static Long getLongValue(Context ctx,String key) {
		SharedPreferences sp = ctx.getSharedPreferences("SP", Activity.MODE_PRIVATE);
		long longValue = sp.getLong(key, Long.MIN_VALUE);
		if(longValue==Long.MIN_VALUE){
			return null;
		}else{
			return longValue;
		}
	}
}
