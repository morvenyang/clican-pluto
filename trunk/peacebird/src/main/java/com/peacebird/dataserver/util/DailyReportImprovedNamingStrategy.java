package com.peacebird.dataserver.util;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class DailyReportImprovedNamingStrategy extends ImprovedNamingStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8873165587326090277L;

	@Override
	public String tableName(String tableName) {
		return super.tableName(tableName).toUpperCase();
	}

}
