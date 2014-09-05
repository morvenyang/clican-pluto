package com.huace.mas.entity;

import java.io.Serializable;

public class Fs extends Kpi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4462220831245450403L;
	private double damElevation;// 坝面高程

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
}
