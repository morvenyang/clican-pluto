package com.huace.mas.entity;

import java.io.Serializable;

public class Yl extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126285065837309331L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
