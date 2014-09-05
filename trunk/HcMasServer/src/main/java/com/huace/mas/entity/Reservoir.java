package com.huace.mas.entity;

import java.io.Serializable;

public class Reservoir extends Kpi implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8599591245232520202L;
	//配置文件节点对应
	
	private double damElevation;

	public double getDamElevation() {
		return damElevation;
	}

	public void setDamElevation(double damElevation) {
		this.damElevation = damElevation;
	}
	
	
}
