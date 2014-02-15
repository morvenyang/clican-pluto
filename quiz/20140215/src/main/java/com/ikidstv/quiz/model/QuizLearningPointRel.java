package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Table(name = "QUIZ_LEARNING_POINT_REL")
@Entity
public class QuizLearningPointRel {

	private Long id;
	private Quiz quiz;
	private LearningPoint learningPoint;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="QUIZ_ID", nullable=false)
	public Quiz getQuiz() {
		return quiz;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	@ManyToOne
	@JoinColumn(name="LEARNING_POINT_ID", nullable=false)
	public LearningPoint getLearningPoint() {
		return learningPoint;
	}
	public void setLearningPoint(LearningPoint learningPoint) {
		this.learningPoint = learningPoint;
	}
	
	
}
