package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.sohu.SohuClient;

@Controller
public class SohuController {

	@Autowired
	private SohuClient sohuClient;

	@RequestMapping("/sohu/playVideoByURL.xml")
	public void playVideoByURL(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("url") String url)
			throws IOException {
		String m3u8URL = sohuClient.getPlayURL(url);
		if (StringUtils.isEmpty(m3u8URL)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpFileVideoAsset id=\"play\"><mediaURL>"
				+ m3u8URL
				+ "</mediaURL><title></title><description></description></httpFileVideoAsset></videoPlayer></body></atv>";
		byte[] data = playXml.getBytes("utf-8");
		OutputStream os = null;
		try {
			response.setContentType("text/xml");
			response.setContentLength(data.length);
			os = response.getOutputStream();
			os.write(data);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"System Error");
			return;
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
