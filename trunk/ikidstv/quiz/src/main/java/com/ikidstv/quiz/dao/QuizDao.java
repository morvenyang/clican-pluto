package com.ikidstv.quiz.dao;

import java.util.List;

import com.ikidstv.quiz.enumeration.Device;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public interface QuizDao {

	public List<Quiz> findQuizByUserId(Long userId);

	public List<Quiz> findAuditingQuiz();

	public void saveQuiz(Quiz quiz);

	public void saveMetadata(Metadata metadata);

	public Quiz findQuizById(Long id);

	public Metadata getMetadata(TemplateId templateId, Long metadataId);

	public void deleteQuiz(Quiz quiz);

	public void deleteMetadata(Metadata metadata);

	public void deleteQuizLearningPointRel(Quiz quiz);

	public List<Quiz> findPlacementQuiz();

	public List<Quiz> findQuizBySeason(String seasonId,String level, Device device);
	
	public List<Quiz> findQuizByEpisode(String episodeId,String level, Device device);

}
