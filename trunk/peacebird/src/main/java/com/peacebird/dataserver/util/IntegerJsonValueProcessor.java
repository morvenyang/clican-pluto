package com.peacebird.dataserver.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class IntegerJsonValueProcessor implements JsonValueProcessor {

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}
	
	private Object process(Object value) {
		if (value == null) {
			return "0";
		}
		return ""+((Integer)value).intValue();
	}

}
