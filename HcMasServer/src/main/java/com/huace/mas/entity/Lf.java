package com.huace.mas.entity;

import java.io.Serializable;

public class Lf extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312416518348152694L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
