package com.clican.appletv.ui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}
