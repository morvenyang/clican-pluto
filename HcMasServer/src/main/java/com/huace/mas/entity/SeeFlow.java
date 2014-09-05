package com.huace.mas.entity;

import java.io.Serializable;

public class SeeFlow extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224510382285948750L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
	
}
