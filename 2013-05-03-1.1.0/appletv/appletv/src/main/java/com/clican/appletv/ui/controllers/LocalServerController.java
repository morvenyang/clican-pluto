package com.clican.appletv.ui.controllers;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.localserver.LocalServerService;

@Controller
public class LocalServerController {

	private final static Log log = LogFactory
			.getLog(LocalServerController.class);
	@Autowired
	private LocalServerService localServerService;

	public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	public static final Pattern pattern = Pattern.compile("^(?:" + _255
			+ "\\.){3}" + _255 + "$");

	public static String longToIpV4(long longIp) {
		int octet3 = (int) ((longIp >> 24) % 256);
		int octet2 = (int) ((longIp >> 16) % 256);
		int octet1 = (int) ((longIp >> 8) % 256);
		int octet0 = (int) ((longIp) % 256);
		return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
	}

	public long ipV4ToLong(String ip) {
		String[] octets = ip.split("\\.");
		return (Long.parseLong(octets[0]) << 24)
				+ (Integer.parseInt(octets[1]) << 16)
				+ (Integer.parseInt(octets[2]) << 8)
				+ Integer.parseInt(octets[3]);
	}

	public boolean isIPv4Private(String ip) {
		long longIp = ipV4ToLong(ip);
		return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
				|| (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
				|| longIp >= ipV4ToLong("192.168.0.0")
				&& longIp <= ipV4ToLong("192.168.255.255");
	}

	public boolean isIPv4Valid(String ip) {
		return pattern.matcher(ip).matches();
	}

	public String getIpFromRequest(HttpServletRequest request) {
		String ip;
		boolean found = false;
		if ((ip = request.getHeader("x-forwarded-for")) != null) {
			StrTokenizer tokenizer = new StrTokenizer(ip, ",");
			while (tokenizer.hasNext()) {
				ip = tokenizer.nextToken().trim();
				if (isIPv4Valid(ip) && !isIPv4Private(ip)
						&& !ip.equals("127.0.0.1")) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@RequestMapping("/localserver/register.do")
	public void registerLocalServer(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("innerIP") String innerIP) {
		String outerIP = this.getIpFromRequest(request);
		if (log.isDebugEnabled()) {
			log.debug("register local server, outerIP:" + outerIP
					+ ", innerIP:" + innerIP);
		}
		localServerService.registerLocalServer(outerIP, innerIP);
	}

	@RequestMapping("/localserver/retrive.do")
	public void retriveLocalServer(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String outerIP = this.getIpFromRequest(request);
		String innerIP = localServerService.retriveLocalServer(outerIP);
		if (log.isDebugEnabled()) {
			log.debug("retrive local server, outerIP:" + outerIP + ", innerIP:"
					+ innerIP);
		}
		if (StringUtils.isNotEmpty(innerIP)) {
			response.getOutputStream().write(innerIP.getBytes("utf-8"));
		}
	}
}
