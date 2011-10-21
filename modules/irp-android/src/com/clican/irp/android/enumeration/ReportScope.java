package com.clican.irp.android.enumeration;

public enum ReportScope {

	ALL(0),

	INNER(1),

	OUTER(2),

	CARE(3),

	FAVORITE(4),

	HISTORY(5);

	private int scope;

	private ReportScope(int scope) {
		this.scope = scope;
	}

	public int getScope() {
		return scope;
	}
	
}
