package com.peacebird.dataserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {

	private String datePattern;

	public DateJsonValueProcessor(String datePattern) {
		this.datePattern = datePattern;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if (value == null) {
			return "";
		}
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		return dateFormat.format((Date) value);

	}

}
