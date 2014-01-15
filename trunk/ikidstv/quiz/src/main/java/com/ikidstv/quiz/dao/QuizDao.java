package com.ikidstv.quiz.dao;

import java.util.List;

import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public interface QuizDao {

	public List<Quiz> findQuizByUserId(Long userId);
	
	public void saveQuiz(Quiz quiz);
	
	public void saveMetadata(Metadata metadata);
}
