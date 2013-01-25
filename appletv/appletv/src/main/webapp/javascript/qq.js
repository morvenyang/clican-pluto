var serverurl = 'http://127.0.0.1/appletv';
var qqClient ={
		qqChannels:
		[
		 {label:"推荐",value:3,platform:5},
		 {label:"搜索",value:1001,platform:5},
		 {label:"电视剧",value:15,platform:5},
		 {label:"电影",value:14,platform:5},
		 {label:"动漫",value:16,platform:5},
		 {label:"综艺",value:17,platform:5},
		 {label:"新闻",value:22,platform:5},
		 {label:"电视直播",value:24,platform:7},
		 {label:"美剧",value:48,platform:7},
		 {label:"娱乐",value:21,platform:5},
		 {label:"微讲堂",value:37,platform:5},
		 {label:"体育",value:20,platform:5},
		 {label:"纪录片",value:19,platform:5}
		],
		
		qqChannelMap:
		{
		 "3":{label:"推荐",value:3,platform:5},
		 "1001":{label:"搜索",value:1001,platform:5},
		 "15":{label:"电视剧",value:15,platform:5},
		 "14":{label:"电影",value:14,platform:5},
		 "16":{label:"动漫",value:16,platform:5},
		 "17":{label:"综艺",value:17,platform:5},
		 "22":{label:"新闻",value:22,platform:5},
		 "24":{label:"电视直播",value:24,platform:7},
		 "48":{label:"美剧",value:48,platform:7},
		 "21":{label:"娱乐",value:21,platform:5},
		 "37":{label:"微讲堂",value:37,platform:5},
		 "20":{label:"体育",value:20,platform:5},
		 "19":{label:"纪录片",value:19,platform:5}
		},
		
		loadIndexPage: function(keyword,page,channelId){
			alert(1);
			var channel = this.qqChannelMap[channelId];
			var data = {'channel':channel,'channels':this.qqChannels,'serverurl':serverurl};
			var xml = new EJS({url: serverurl+'/template/qq/index.ejs'}).render(data);
			alert(2);
			appletv.makePostRequest(serverurl+'/tudou/log.do',xml,function(callback){});
		},
		
	
		
};