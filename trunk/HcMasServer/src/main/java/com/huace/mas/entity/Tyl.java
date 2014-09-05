package com.huace.mas.entity;

import java.io.Serializable;

public class Tyl extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2363754387446141119L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
