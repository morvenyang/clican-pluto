package com.ikidstv.quiz.service;

import java.util.List;
import java.util.Map;

import com.ikidstv.quiz.bean.LearningPointTree;
import com.ikidstv.quiz.model.LearningPoint;

public interface LearningPointService {

	public Map<String,List<LearningPoint>> getLearningPointWithTreeMap();
	
	public LearningPointTree getLearningPointTree();
	
	public void saveLearningPoint(LearningPoint learningPoint);
}
