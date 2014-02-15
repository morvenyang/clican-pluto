package com.ikidstv.quiz.dao;

import java.util.List;

import com.ikidstv.quiz.model.LearningPoint;

public interface LearningPointDao {

	
	public List<LearningPoint> getAllLearningPoint();
	
	public void saveLearningPoint(LearningPoint learningPoint);
	
	public void deleteLearningPoint(LearningPoint learningPoint);
}
