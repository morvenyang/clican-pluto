package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.config.ConfigService;

@Controller
public class ConfigController {

	@Autowired
	private ConfigService configService;

	@RequestMapping("/config/saveConfig.do")
	public void saveConfig(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value)
			throws IOException {
		configService.saveConfig(deviceId, key, value);
	}

	@RequestMapping("/config/getConfig.do")
	public void getConfig(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "deviceId", required = false) String deviceId,
			@RequestParam(value = "key", required = false) String key)
			throws IOException {
		String value = configService.getConfig(deviceId, key);
		if (StringUtils.isNotEmpty(value)) {
			response.setContentType("plan/text;charset=utf-8");
			response.getOutputStream().write(value.getBytes("utf-8"));
		}
	}

	@RequestMapping("/config/config.xml")
	public String loadConfigPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String configContent = this.getContent(request);
		JSONObject json = JSONObject.fromObject(configContent);
		String deviceId = json.getString("deviceId");
		JSONObject configs = json.getJSONObject("configs");
		Map<String, String> configMap = configService.getAllConfig(deviceId);
		Map<String, String> result = new HashMap<String, String>();
		for (Object key : configs.keySet()) {
			result.put((String) key, configs.getString((String) key));
		}
		if (configMap != null) {
			for (String key : configMap.keySet()) {
				if (configs.containsKey(key)) {
					String value = configs.getString(key);
					if (StringUtils.isEmpty(value)) {
						if (StringUtils.isNotEmpty(configMap.get(key))) {
							result.put(key, configMap.get(key));
						}
					}
				}
			}
		}
		request.setAttribute("result", result);
		return "config/config";
	}

	private String getContent(HttpServletRequest request) throws Exception {
		InputStream is = request.getInputStream();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		String content = new String(os.toByteArray(), "UTF-8");
		is.close();
		os.close();
		return content;
	}
}
