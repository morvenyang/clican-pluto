package com.ikidstv.quiz.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ikidstv.quiz.dao.LearningPointDao;
import com.ikidstv.quiz.model.LearningPoint;
import com.ikidstv.quiz.service.LearningPointService;

public class LearningPointServiceImpl implements LearningPointService {

	private LearningPointDao learningPointDao;

	public void setLearningPointDao(LearningPointDao learningPointDao) {
		this.learningPointDao = learningPointDao;
	}

	public Map<String, List<LearningPoint>> getLearningPointWithTreeMap() {
		List<LearningPoint> allLearningPoint = this.learningPointDao
				.getAllLearningPoint();
		Map<String, List<LearningPoint>> learningPointWithTreeMap = new TreeMap<String, List<LearningPoint>>();
		for (LearningPoint lp : allLearningPoint) {
			if (!learningPointWithTreeMap.containsKey(lp.getPoint())) {
				learningPointWithTreeMap.put(lp.getPoint(),
						new ArrayList<LearningPoint>());
			}
			learningPointWithTreeMap.get(lp.getPoint()).add(lp);
		}
		return learningPointWithTreeMap;
	}

}
