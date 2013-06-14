package com.clican.appletv.ui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.apns.ApnsService;

@Controller
public class ApnsController {

	@Autowired
	private ApnsService apnsService;

	@RequestMapping("/apns/register.do")
	public void register(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "token") String token) throws IOException {
		apnsService.registerToken(token);
	}

	@RequestMapping("/apns/sendMessage.do")
	public void sendMessage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "message") String message) throws IOException {
		apnsService.sendMessage(message);
	}
}
