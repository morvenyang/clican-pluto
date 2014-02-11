package com.ikidstv.quiz.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.ikidstv.quiz.bean.Constants;
import com.ikidstv.quiz.bean.SpringProperty;

public class ImageViewServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7740197783039083786L;

	private SpringProperty springProperty;

	@Override
	public void init() throws ServletException {
		super.init();
		springProperty = (SpringProperty) Constants.ctx
				.getBean("springProperty");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String imagePath = req.getParameter("imagePath");
		String audioPath = req.getParameter("recordPath");
		if (StringUtils.isNotEmpty(imagePath)) {
			byte[] data = FileUtils.readFileToByteArray(new File(springProperty
					.getImagePath() + "/" + imagePath));
			resp.getOutputStream().write(data);
			if (imagePath.toLowerCase().endsWith("jpeg")) {
				resp.setContentType("image/jpeg");
			} else if (imagePath.toLowerCase().endsWith("png")) {
				resp.setContentType("image/png");
			} else if (imagePath.toLowerCase().endsWith("gif")) {
				resp.setContentType("image/gif");
			} else if (imagePath.toLowerCase().endsWith("jpg")) {
				resp.setContentType("image/jpg");
			}
			resp.getOutputStream().flush();
		} else if (StringUtils.isNotEmpty(audioPath)) {
			byte[] data = FileUtils.readFileToByteArray(new File(springProperty
					.getRecordingPath() + "/" + audioPath));
			resp.getOutputStream().write(data);
			resp.getOutputStream().flush();
		}

	}

}
