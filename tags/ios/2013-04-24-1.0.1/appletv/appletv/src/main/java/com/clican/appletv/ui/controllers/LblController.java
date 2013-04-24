package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.lbl.LblClient;

@Controller
public class LblController {

	@Autowired
	private LblClient lblClient;

	@RequestMapping("/lbl/getImage.do")
	public void getImage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("url") String url)
			throws IOException {
		String imgUrl = lblClient.getImageUrl(url);
		if (imgUrl != null) {
			response.sendRedirect(imgUrl);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@RequestMapping("/lbl/getImages.json")
	public void getImages(HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String content = this.getContent(request);
		List<String> result = new ArrayList<String>();	
		if(StringUtils.isNotEmpty(content)){
			JSONArray array = JSONArray.fromObject(content);
			for(int i=0;i<array.size();i++){
				String url = array.getString(i);
				String imgUrl = lblClient.getImageUrl(url);
				result.add(imgUrl);
			}
		}
		String jsonContent = JSONArray.fromObject(result).toString();
		response.getOutputStream().write(jsonContent.getBytes("utf-8"));
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
