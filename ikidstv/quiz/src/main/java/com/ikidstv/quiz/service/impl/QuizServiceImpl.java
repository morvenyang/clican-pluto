package com.ikidstv.quiz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ikidstv.quiz.dao.QuizDao;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;
import com.ikidstv.quiz.service.ContentService;
import com.ikidstv.quiz.service.QuizService;

public class QuizServiceImpl implements QuizService {

	private QuizDao quizDao;

	private ContentService contentService;

	public void setQuizDao(QuizDao quizDao) {
		this.quizDao = quizDao;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public List<Quiz> findQuizByUserId(Long userId, String contentId) {
		List<Quiz> dbResult = quizDao.findQuizByUserId(userId);
		List<Quiz> result = new ArrayList<Quiz>();
		Set<String> episodeIds = contentService.findEpisodeIds(contentId);
		for (Quiz quiz : dbResult) {
			if (episodeIds.contains(quiz.getEpisodeId())) {
				result.add(quiz);
			}
		}
		return result;
	}

	public void saveQuiz(Quiz quiz,Metadata metadata) {
		this.quizDao.saveMetadata(metadata);
		quiz.setMetadataId(metadata.getId());
		this.quizDao.saveQuiz(quiz);
	}

}