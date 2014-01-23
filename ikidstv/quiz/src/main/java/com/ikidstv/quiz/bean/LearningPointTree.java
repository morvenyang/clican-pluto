package com.ikidstv.quiz.bean;

import java.util.List;

import com.ikidstv.quiz.model.LearningPoint;

public class LearningPointTree {

	private String point;
	private String subPoint;
	private Long id;
	private LearningPoint learningPoint;
	
	private List<LearningPointTree> subTree;

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getSubPoint() {
		return subPoint;
	}

	public void setSubPoint(String subPoint) {
		this.subPoint = subPoint;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LearningPointTree> getSubTree() {
		return subTree;
	}

	public void setSubTree(List<LearningPointTree> subTree) {
		this.subTree = subTree;
	}

	public LearningPoint getLearningPoint() {
		return learningPoint;
	}

	public void setLearningPoint(LearningPoint learningPoint) {
		this.learningPoint = learningPoint;
	}
	
	
}
