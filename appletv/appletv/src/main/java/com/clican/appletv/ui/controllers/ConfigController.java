package com.clican.appletv.ui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			response.getOutputStream().write(value.getBytes("utf-8"));
		}
	}
}
