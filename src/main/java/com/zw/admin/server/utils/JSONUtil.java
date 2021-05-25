package com.zw.admin.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {

	/**
	 * 将json对象转换为HashMap
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(JSONObject json) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是json数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				// Iterator<JSONObject> it = ((JSONArray) v).iterator();
				Iterator<Object> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = (JSONObject) it.next();
					list.add(parseJSON2Map(json2));
				}
				map.put(k.toString(), list);
			} else if (v instanceof JSONObject) {
				// 如果内层是json对象的话，继续解析
				map.put(k.toString(), parseJSON2Map((JSONObject) v));
			} else {
				// 如果内层是普通对象的话，直接放入map中
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	/**
	 * 将json字符串转换为Map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSONstr2Map(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		Map<String, Object> map = parseJSON2Map(json);
		return map;
	}
}
