package com.clican.appletv.core.service.tudou.enumeration;

public enum Area {

	ALL(-1, "所有"),
	China(2,"大陆"),
	Taiwan(3,"台湾"),
	HongKong(4,"香港"),
	Korea(5,"韩国"),
	Japan(6,"日本"),
	USA(7,"美国"),
	Franch(8,"法国"),
	England(10,"英国"),
	Other(1,"其他");

	private int value;
	private String label;

	private Area(int value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static Area convertToArea(Integer areaId){
		for(Area area:values()){
			if(area.getValue()==areaId){
				return area;
			}
		}
		return null;
	}
	
}
