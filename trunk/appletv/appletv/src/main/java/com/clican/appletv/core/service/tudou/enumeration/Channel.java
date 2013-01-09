package com.clican.appletv.core.service.tudou.enumeration;

public enum Channel {
	
	Recommand("推荐",1000,-1,false),
	Search("搜索",1001,0,false),
	Favorite("收藏",1001,0,false),
	DianShiJu("电视剧",30,1,true),
	DianYing("电影",22,2,false),
	ZongYi("综艺",31,3,false),
	GaoXiao("搞笑",5,4,false),
	ReDian("热点",29,5,false),
	YouXi("游戏",10,6,false),
	DongHua("动画",9,7,true),
	YuanChuang("原创",99,8,false),
	YuLe("娱乐",1,9,false),
	NvXing("女性",27,10,false),
	TiYu("体育",15,11,false),
	QiChe("汽车",26,12,false),
	KeJi("科技",21,13,false),
	FengShang("风尚",32,14,false),
	LeHuo("乐活",3,15,false),
	JiaoYu("教育",25,16,false),
	CaiFu("财富",24,17,false),
	
	;
	
	private String label;
	private int value;
	private int order;
	private boolean isAlbum;
	
	private Channel(String label,int value,int order,boolean isAlbum){
		this.label=label;
		this.value=value;
		this.order=order;
		this.isAlbum = isAlbum;
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
	
	public boolean isAlbum() {
		return isAlbum;
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
