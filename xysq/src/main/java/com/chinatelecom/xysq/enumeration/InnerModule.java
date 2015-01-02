package com.chinatelecom.xysq.enumeration;

public enum InnerModule implements LabelAndValue {

	ANNOUNCEMENT(1, "小区公告"),

	NOTICE(2, "业主须知"),

	CONTACT_US(3, "联系我们"),
	
	PARKING(4, "小区挪车"),
	
	BROADBAND_REMIND(5, "宽带提醒");

	private int value;

	private String label;

	private InnerModule(int value, String label) {
		this.value = value;
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int getValue() {
		return value;
	}
}
