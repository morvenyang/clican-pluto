package com.chinatelecom.xysq.enumeration;

public enum NoticeCategory {

	JU_WEI_HUI("居委会", 1),

	BAN_ZHENG_CAI_LIAO("办证材料", 2),

	WU_YE_XIN_XI("物业信息", 3),

	PAI_CHU_SUO("派出所", 4);

	private String label;
	private int value;

	private NoticeCategory(String label, int value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}

	public static NoticeCategory convert(String noticeCategoryName) {
		for (NoticeCategory member : values()) {
			if (member.name().equals(noticeCategoryName)) {
				return member;
			}
		}
		return null;
	}
	
	public static NoticeCategory convert(int value){
		for (NoticeCategory member : values()) {
			if (member.getValue()==value) {
				return member;
			}
		}
		return null;
	}
}
