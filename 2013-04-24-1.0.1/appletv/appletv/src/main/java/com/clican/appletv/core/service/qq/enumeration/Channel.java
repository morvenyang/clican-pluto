package com.clican.appletv.core.service.qq.enumeration;


public enum Channel {

	Recommand("推荐",3,5),
	Search("搜索",1001,5),
	DianShiJu("电视剧",15,5),
	DianYing("电影",14,5),
	DongMan("动漫",16,5),
	ZongYi("综艺",17,5),
	XinWen("新闻",22,5),
	DianShiZhiBo("电视直播",24,7),
	MeiJu("美剧",48,7),
	YuLe("娱乐",21,5),
	WeiJiangTang("微讲堂",37,5),
	TiYu("体育",20,5),
	JiLuPian("纪录片",19,5);
	
	private String label;
	private int value;
	private int platform;
	
	private Channel(String label,int value,int platform){
		this.label = label;
		this.value = value;
		this.platform = platform;
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}
	
	public int getPlatform() {
		return platform;
	}

	public static Channel convertToChannel(Integer channelId){
		for(Channel channel:values()){
			if(channel.getValue()==channelId){
				return channel;
			}
		}
		return null;
	}
	
}
