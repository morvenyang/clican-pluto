package com.huace.mas.entity;

import java.io.Serializable;

public class Rxwy extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220962216370508335L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
