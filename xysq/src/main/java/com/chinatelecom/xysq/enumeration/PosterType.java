package com.chinatelecom.xysq.enumeration;

import java.util.ArrayList;
import java.util.List;

public enum PosterType implements LabelAndValue {

	
	HTML5(1,"HTML5"),
	
	INNER_MODULE(2,"内部模块"),
	
	STORE_DETAIL(3,"商家详情");

	private int value;
	
	private String label;
	
	private PosterType(int value,String label){
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
	
	public static List<PosterType> getPosterTypes(){
		List<PosterType> posterTypes = new ArrayList<PosterType>();
		for(PosterType pt:values()){
			posterTypes.add(pt);
		}
		return posterTypes;
	}
	
}
