package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.TudouClientImpl;

@Controller
public class XunleiController {

	@Autowired
	private TudouClient tudouClient;
	
	@RequestMapping("/xunlei/getsession.do")
	public void getXunleiSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String value = "{\"userid\":\"5663595\",\"sessionid\":\"75F30341DD84F450A07B5F048941BDA785F6AC3A12F9A04ADD949849CD339CC26B882B5F062969E7F71BF99995D9719814CB2E481F15545E89D85D6161F9649A\"}";
		response.getOutputStream().write(value.getBytes("utf-8"));
	}

	@RequestMapping("/xunlei/geturl.do")
	public void getXunleiUrl(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xunleiurl = getContent(request);
		String refer = "http://61.147.76.6/iplay.html";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Referer", refer);
		PostResponse pr = ((TudouClientImpl) tudouClient).httpGetForCookie(
				xunleiurl, header, null);
		response.getOutputStream().write(pr.getContent().getBytes("utf-8"));
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
