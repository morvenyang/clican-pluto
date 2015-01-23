package com.chinatelecom.xysq.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static String convertDateToBBSTime(Date date) {
		Date current = new Date();
		long diff = current.getTime() - date.getTime();
		if (diff < 1000L * 60 * 5) {
			return "刚刚";
		} else if (diff < 1000L * 60 * 60) {
			return diff / (1000L * 60) + "分钟前";
		} else if (diff < 1000L * 60 * 60 * 24) {
			return diff / (1000L * 60 * 60) + "小时前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm",Locale.ENGLISH);
			return sdf.format(date);
		}
	}
}
