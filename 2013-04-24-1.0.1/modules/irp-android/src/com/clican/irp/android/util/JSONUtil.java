package com.clican.irp.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertJSON2Map(JSONObject json)
			throws JSONException {
		Iterator<String> it = json.keys();
		Map<String, Object> result = new HashMap<String, Object>();
		while (it.hasNext()) {
			String key = it.next();
			result.put(key, json.get(key));
		}
		return result;
	}

	public static List<Map<String, Object>> convertJSON2ListMap(
			JSONArray jsonArray) throws JSONException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(convertJSON2Map(jsonArray.getJSONObject(i)));
		}
		return list;
	}
}
