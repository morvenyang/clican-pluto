package com.huace.mas.entity;

import java.io.Serializable;

public class Saturation extends Kpi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1679052802336866576L;
	private double damElevation ;//坝面高程
	
	public double getDamElevation() {
		return damElevation;
	}
	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}

	
}
