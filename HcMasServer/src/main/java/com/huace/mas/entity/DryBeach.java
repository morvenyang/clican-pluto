package com.huace.mas.entity;

import java.io.Serializable;

public class DryBeach extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6125308560054520771L;

	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
