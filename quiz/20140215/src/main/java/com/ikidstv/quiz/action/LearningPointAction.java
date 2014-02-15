package com.ikidstv.quiz.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.ikidstv.quiz.bean.LearningPointTree;
import com.ikidstv.quiz.model.LearningPoint;



@Scope(ScopeType.PAGE)
@Name("learningPointAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class LearningPointAction extends BaseAction {

	
	private LearningPointTree learningPointTree;
	
	private LearningPoint learningPoint;
	
	
	public void listLearningPoints() {
		learningPointTree = this.getLearningPointService().getLearningPointTree();
	}
	
	public void addPoint(){
		learningPoint = new LearningPoint();
	}
	
	public void addPoint(String point){
		learningPoint = new LearningPoint();
		learningPoint.setPoint(point);
	}

	public void editPoint(LearningPoint learningPoint){
		this.learningPoint = learningPoint;
	}
	
	public void savePoint(){
		this.getLearningPointService().saveLearningPoint(learningPoint);
		learningPointTree = this.getLearningPointService().getLearningPointTree();
	}

	public void deletePoint(LearningPoint learningPoint){
		this.getLearningPointService().deleteLearningPoint(learningPoint);
		learningPointTree = this.getLearningPointService().getLearningPointTree();
	}
	public LearningPointTree getLearningPointTree() {
		return learningPointTree;
	}


	public void setLearningPointTree(LearningPointTree learningPointTree) {
		this.learningPointTree = learningPointTree;
	}


	public LearningPoint getLearningPoint() {
		return learningPoint;
	}


	public void setLearningPoint(LearningPoint learningPoint) {
		this.learningPoint = learningPoint;
	}
	
	
}
