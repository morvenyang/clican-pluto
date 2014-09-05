package com.huace.mas.entity;

import java.io.Serializable;

public class Dqyl extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8006622430313679127L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
