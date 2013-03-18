package com.clican.appletv.ui.controllers;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.proxy.ProxyClient;
import com.clican.appletv.core.service.proxy.model.M3u8Download;

@Controller
public class ProxyController {

	@Autowired
	private SpringProperty springProperty;

	@Autowired
	private ProxyClient proxyClient;

	@RequestMapping("/proxy/play.m3u8")
	public void playM3u8(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url)
			throws Exception {
		if (!springProperty.isSystemProxyPlay()) {
			response.sendRedirect(url);
		} else {

		}
	}

	@RequestMapping("/proxy/temp/m3u8/*.ts")
	public void playTs(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "m3u8Url", required = false) String m3u8Url)
			throws Exception {
		String requestUrl = request.getRequestURI().toString();
		String localPath = requestUrl.replace(
				springProperty.getSystemServerUrl() + "/proxy/temp",
				springProperty.getSystemTempPath());
		M3u8Download m3u8Download = proxyClient.getM3u8Download();
		if (StringUtils.isEmpty(m3u8Download.getM3u8Url())
				|| !m3u8Download.getM3u8Url().equals(m3u8Url)) {
			proxyClient.doSyncRequestByM3U8Url(m3u8Url, false);
			proxyClient.seekDownloadLine(localPath);
			proxyClient.startM3u8();
		}
		boolean seek = true;
		while (true) {
			File file = new File(localPath);
			if (file.exists()) {
				response.getOutputStream().write(
						FileUtils.readFileToByteArray(file));
			} else {
				if (seek) {
					seek = false;
					proxyClient.seekDownloadLine(localPath);
				}
				Thread.sleep(1000);
			}
		}
	}

	@RequestMapping("/proxy/play.mp4")
	public void playMp4(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url)
			throws Exception {
		if (!springProperty.isSystemProxyPlay()) {
			response.sendRedirect(url);
		} else {

		}
	}
}
