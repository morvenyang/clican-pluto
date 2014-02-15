package com.ikidstv.quiz.service;

import java.util.List;

import com.ikidstv.quiz.enumeration.Device;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public interface QuizService {

	public List<Quiz> findQuizByUserId(Long userId, String contentId);

	public List<Quiz> findAuditingQuiz();

	public void saveQuiz(Quiz quiz, Metadata metadata);

	public void updateQuiz(Quiz quiz);

	public Quiz findQuizById(Long id);

	public Metadata getMetadataForQuiz(Quiz quiz);

	public void deleteQuiz(Quiz quiz);

	public List<Quiz> findPlacementQuiz();

	public String checkQuizExistence(String seasonId, Integer minAge,
			Integer maxAge, String level, Device device, String version);
	
	public String findQuizByEpisode(String episodeId, Integer minAge,
			Integer maxAge, String level, Device device,String version);
}
