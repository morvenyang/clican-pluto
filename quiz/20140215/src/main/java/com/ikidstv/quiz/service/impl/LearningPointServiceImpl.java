package com.ikidstv.quiz.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ikidstv.quiz.bean.LearningPointTree;
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

	public LearningPointTree getLearningPointTree() {
		List<LearningPoint> allLearningPoint = this.learningPointDao
				.getAllLearningPoint();
		LearningPointTree root = new LearningPointTree();
		root.setSubTree(new ArrayList<LearningPointTree>());
		Map<String, LearningPointTree> map = new HashMap<String, LearningPointTree>();
		for (LearningPoint lp : allLearningPoint) {
			LearningPointTree pointTreeNode = map.get(lp.getPoint());
			if (pointTreeNode == null) {
				pointTreeNode = new LearningPointTree();
				pointTreeNode.setSubTree(new ArrayList<LearningPointTree>());
				pointTreeNode.setPoint(lp.getPoint());
				root.getSubTree().add(pointTreeNode);
				map.put(lp.getPoint(), pointTreeNode);
			}
			LearningPointTree subPointTreeNode = new LearningPointTree();
			subPointTreeNode.setPoint(lp.getPoint());
			subPointTreeNode.setId(lp.getId());
			subPointTreeNode.setSubPoint(lp.getSubPoint());
			subPointTreeNode.setLearningPoint(lp);
			pointTreeNode.getSubTree().add(subPointTreeNode);
		}
		return root;
	}

	public void saveLearningPoint(LearningPoint learningPoint) {
		this.learningPointDao.saveLearningPoint(learningPoint);
	}
	
	public void deleteLearningPoint(LearningPoint learningPoint){
		this.learningPointDao.deleteLearningPoint(learningPoint);
	}

}
