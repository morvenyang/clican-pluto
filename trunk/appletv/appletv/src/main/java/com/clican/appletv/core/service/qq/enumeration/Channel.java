package com.clican.appletv.core.service.qq.enumeration;

public enum Channel {

	Recommand("推荐",3),
	DianShiJu("电视剧",15),
	DianYing("电影",14),
	DongMan("动漫",16),
	ZongYi("综艺",17),
	XinWen("新闻",22),
	TiYu("体育",20),
	DianShiZhiBo("电视直播",24),
	MeiJu("美剧",48),
	YuLe("娱乐",21),
	JiLuPian("纪录片",19),
	WeiJiangTang("微讲堂",37);
	
	private String label;
	private int value;
	
	private Channel(String label,int value){
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
