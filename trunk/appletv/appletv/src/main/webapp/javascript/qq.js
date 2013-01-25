var serverurl = 'http://127.0.0.1/appletv';
var qqSearchApi = "http://ncgi.video.qq.com/tvideo/fcgi-bin/srh_ipad?num=30&tabid=0&plat=4&pver=2&sort=0&filter=18&otype=json&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN";
var qqChannelApi = "http://sns.video.qq.com/fcgi-bin/dlib/dataout?sort=2&iarea=-1&itype=-1&iyear=-1&iedition=-1&pagesize=30&itrailer=-1&otype=json&version=20000&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN&timeout=0";
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
			var channel = this.qqChannelMap[channelId];
			var videos = [];
			if(channelId==1001){
				//搜索
				var search1 = qqSearchApi+"&comment=1&cur="+page+"&query="+keywrod;
				appletv.makeRequest(search1,function(content){
					if(content!=null&&content.length>0){
						var jsonContent = content.substring("QZOutputJson=".length,content.length-1);
						var videos = JSON.parse(jsonContent);
						
					} else {
						//atv.loadXML(this.makeDialog('加载失败',''));
					}
				});
			}else{
				var queryUrl = qqChannelApi+"&page="+page+"&auto_id="+channelId+"&platform="+channel['platform'];
				appletv.makePostRequest(serverurl+'/tudou/log.do',queryUrl,function(callback){});
				appletv.makeRequest(queryUrl,function(content){
					if(content!=null&&content.length>0){
						var jsonContent = content.substring("QZOutputJson=".length,content.length-1);
						var result = JSON.parse(jsonContent);
						if(channelId==3){
							//推荐
							var datas = result['data'];
							var content;
							var video;
							for(var i=0;i<datas.length;i++){
								var contents = datas[i]['contents'];
								for(var j=0;j<contents.length;j++){
									content = contents[j];
									if(content['id_type']!=null&&content['id_type']=='t'){
										continue;
									}
									video = {pic:content['v_pic'],id:content['id'],title:content['title']};
									videos.push(video);
								}
							}
						}
					} else {
						//atv.loadXML(this.makeDialog('加载失败',''));
					}
				});
			}
			
			var data = {'channel':channel,'channels':this.qqChannels,'serverurl':serverurl,'videos':videos};
			var xml = new EJS({url: serverurl+'/template/qq/index.ejs'}).render(data);
			appletv.makePostRequest(serverurl+'/tudou/log.do',xml,function(callback){});
		},
		
	
		
};