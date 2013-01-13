package com.clican.appletv.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OtherController {
	@RequestMapping("/releasenote.xml")
	public String releasenote(){
		return "releasenote";
	}
}
