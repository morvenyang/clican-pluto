package com.ikidstv.quiz.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "FIND_DIFFERENCE")
@Entity
public class FindDifference extends Metadata{
	
	private Long id;
	
	private String picture1;
	private String picture2;
	
	private int x1;
	private int x2;
	private int x3;
	private int x4;
	private int x5;
	
	private int y1;
	private int y2;
	private int y3;
	private int y4;
	private int y5;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getPicture1() {
		return picture1;
	}
	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}
	@Column
	public String getPicture2() {
		return picture2;
	}
	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}
	@Column
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	@Column
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	@Column
	public int getX3() {
		return x3;
	}
	public void setX3(int x3) {
		this.x3 = x3;
	}
	@Column
	public int getX4() {
		return x4;
	}
	public void setX4(int x4) {
		this.x4 = x4;
	}
	@Column
	public int getX5() {
		return x5;
	}
	public void setX5(int x5) {
		this.x5 = x5;
	}
	@Column
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	@Column
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	@Column
	public int getY3() {
		return y3;
	}
	public void setY3(int y3) {
		this.y3 = y3;
	}
	@Column
	public int getY4() {
		return y4;
	}
	public void setY4(int y4) {
		this.y4 = y4;
	}
	@Column
	public int getY5() {
		return y5;
	}
	public void setY5(int y5) {
		this.y5 = y5;
	}
	
	
	
}
