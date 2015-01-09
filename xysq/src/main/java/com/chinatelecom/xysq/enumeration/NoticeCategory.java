package com.chinatelecom.xysq.enumeration;

public enum NoticeCategory implements LabelAndValue {

	JU_WEI_HUI("居委会",1),
	
	BAN_ZHENG_CAI_LIAO("办证材料",2),
	
	WU_YE_XIN_XI("物业信息",3),
	
	PAI_CHU_SUO("派出所",4);
	
	private String label;
	private int value;
	private NoticeCategory(String label,int value){
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public int getValue() {
		return value;
	}
	
	
}
