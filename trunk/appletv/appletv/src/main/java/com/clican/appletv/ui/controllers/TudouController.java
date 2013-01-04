package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.model.TudouVideo;

@Controller
public class TudouController {

	private final static Log log = LogFactory.getLog(TudouController.class);

	@Autowired
	private TudouClient tudouClient;

	@RequestMapping("/recommend.do")
	public void indexPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("access tudou recommend page");
		}
		List<TudouVideo> videos = tudouClient
				.queryVideos("http://minterface.tudou.com/ih?sessionid=GTR7J672EMAAA&page=0&pagesize=30&type=recommend");
		String result = tudouClient.convertToATVXml(videos);
		byte[] data = result.getBytes("utf-8");
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
