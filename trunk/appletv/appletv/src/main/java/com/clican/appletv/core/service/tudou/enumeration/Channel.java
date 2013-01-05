package com.clican.appletv.core.service.tudou.enumeration;

public enum Channel {
	
	DianShiJu("电视剧",30,1),
	DianYing("电影",22,2),
	ZongYi("综艺",31,3),
	GaoXiao("搞笑",5,4),
	ReDian("热点",29,5),
	YouXi("游戏",10,6),
	DongHua("动画",9,7),
	YuanChuang("原创",99,8),
	YuLe("娱乐",1,9),
	NvXing("女性",27,10),
	TiYu("体育",15,11),
	QiChe("汽车",26,12),
	KeJi("科技",21,13),
	FengShang("风尚",32,14),
	LeHuo("乐活",3,15),
	JiaoYu("教育",25,16),
	CaiFu("财富",24,17);
	
	private String label;
	private int value;
	private int order;
	
	private Channel(String label,int value,int order){
		this.label=label;
		this.value=value;
		this.order=order;
	}

	public String getLabel() {
		return label;
	}

	public int getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}
	
	
}
