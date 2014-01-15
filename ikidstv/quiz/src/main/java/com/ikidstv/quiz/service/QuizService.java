package com.ikidstv.quiz.service;

import java.util.List;

import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;

public interface QuizService {

	public List<Quiz> findQuizByUserId(Long userId, String contentId);

	public void saveQuiz(Quiz quiz,Metadata metadata);
}
