package com.ikidstv.quiz.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ikidstv.quiz.enumeration.Device;
import com.ikidstv.quiz.service.QuizService;

@Controller
public class QuizController {

	private final static Log log = LogFactory.getLog(QuizController.class);

	private QuizService quizService;

	public void setQuizService(QuizService quizService) {
		this.quizService = quizService;
	}

	@RequestMapping("/checkQuizExistence")
	public void checkQuizExistence(
			@RequestParam(value = "seasonId", required = false) String seasonId,
			@RequestParam(value = "episodeId", required = false) String episodeId,
			@RequestParam(value = "level") String level,
			@RequestParam(value = "device") String device,
			@RequestParam(value = "version") String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("checkQuizExistence \n\tseasonId:" + seasonId
					+ "\n\tepisodeId:" + episodeId + "\n\tlevel:" + level
					+ "\n\tdevice:" + device + "\n\tversion:" + version);
		}
		String result;
		if (StringUtils.isNotEmpty(seasonId)) {
			result = quizService.checkQuizExistenceForSeason(seasonId, level,
					Device.convert(device), version);
		} else if (StringUtils.isNotEmpty(episodeId)) {
			result = quizService.checkQuizExistenceForEpisode(episodeId, level,
					Device.convert(device), version);
		} else {
			result = "{message:'The seasonId or episode is required.',code='200000'}";
		}
		try {
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryQuiz")
	public void queryQuiz(@RequestParam(value = "episodeId") String episodeId,
			@RequestParam(value = "level") String level,
			@RequestParam(value = "device") String device,
			@RequestParam(value = "version") String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("queryQuiz \n\tepisodeId:" + episodeId + "\n\tlevel:"
					+ level + "\n\tdevice:" + device + "\n\tversion:" + version);
		}
		String result = quizService.findQuizByEpisode(episodeId, level,
				Device.convert(device), version);
		try {
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
