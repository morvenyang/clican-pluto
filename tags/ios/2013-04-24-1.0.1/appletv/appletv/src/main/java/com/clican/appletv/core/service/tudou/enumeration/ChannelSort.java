package com.clican.appletv.core.service.tudou.enumeration;

public enum ChannelSort {

	PlayCount("播放次数","v",2),
	PublishTime("发布时间","t",1),
	RecomTime("推荐时间","m",3),
	QualitySort("清晰度","h",4),
	CommentCount("评论数量","c",5),
	FavoriteCount("收藏次数","f",6),
	RateSort("评分","r",7);
	
	
	private String label;
	private String value;
	private int order;
	
	private ChannelSort(String label,String value,int order){
		this.label=label;
		this.value=value;
		this.order=order;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}
	
	
	
}
