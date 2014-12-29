package com.chinatelecom.xysq.hibernate;

public class ImprovedNamingStrategy extends
		org.hibernate.cfg.ImprovedNamingStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8873165587326090277L;

	@Override
	public String tableName(String tableName) {
		return super.tableName(tableName).toUpperCase();
	}

}
