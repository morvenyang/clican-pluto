package com.chinatelecom.xysq.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum InnerModule implements LabelAndValue {

	ANNOUNCEMENT(1, "小区公告"),

	NOTICE(2, "业主须知"),

	BBS(3, "小区论坛"),
	
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
	
	public static List<InnerModule> getInnerModules(){
		List<InnerModule> innerModules = new ArrayList<InnerModule>();
		for(InnerModule member:values()){
			innerModules.add(member);
		}
		return innerModules;
	}
}
