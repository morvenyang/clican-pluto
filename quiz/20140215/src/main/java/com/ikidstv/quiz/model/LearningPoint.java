package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "LEARNING_POINT")
@Entity
public class LearningPoint {

	private Long id;
	private String point;
	private String subPoint;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "POINT")
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	
	@Column(name = "SUB_POINT")
	public String getSubPoint() {
		return subPoint;
	}
	public void setSubPoint(String subPoint) {
		this.subPoint = subPoint;
	}
	
	
}
