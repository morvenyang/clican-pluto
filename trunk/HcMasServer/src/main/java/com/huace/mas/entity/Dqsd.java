package com.huace.mas.entity;

import java.io.Serializable;

public class Dqsd extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3295449447354289503L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
