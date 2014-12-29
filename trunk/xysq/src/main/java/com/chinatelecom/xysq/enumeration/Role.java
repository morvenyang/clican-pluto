package com.chinatelecom.xysq.enumeration;

public enum Role implements LabelAndValue{

	ADMIN(1,"系统管理员"),
	
	AREA_ADMIN(2,"小区管理员"),
	
	USER(3,"用户");
	
	private int value;
	
	private String label;
	
	private Role(int value,String label){
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
