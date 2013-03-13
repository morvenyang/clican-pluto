var yyetsSearchApi = "http://ziyuan.kehuduan.rryingshi.com:20066/resources";
var yyetsVideoApi = "http://ziyuan.kehuduan.rryingshi.com:20066/resources/";
var yyetsClient = {
		yyetsChannels:
		[
		 {label:"电影",value:"movie"},
		 {label:"电视剧",value:"tv"},
		 {label:"纪录片",value:"documentary"},
		 {label:"公开课",value:"openclass"},
		 {label:"搜索",value:"search"}
		],
			
		yyetsChannelMap:
		{
		 "tv":{label:"电视剧",value:"tv"},
		 "movie":{label:"电影",value:"movie"},
		 "documentary":{label:"纪录片",value:"documentary"},
		 "openclass":{label:"公开课",value:"openclass"},
		 "search":{label:"搜索",value:"search"}
		},
		
		loadIndexPage:function(keyword,page,channelId){
			var url = yyetsSearchApi+"?c="+channelId+"&page="+page;
			var channel = this.yyetsChannelMap[channelId];
			appletv.makeRequest(url,function(htmlContent){
				appletv.logToServer(htmlContent);
				if (htmlContent == null) {
					return;
				}
				var videos = [];
				var imgs = appletv.findall('<div class="img"><a href="/resources/\\d*" class="resources-list-resource-url"><img src="([^"]*)"',htmlContent);
				var idAndTitles = appletv.findall('<h2 class="fl"><a href="/resources/(\\d*)" class="resources-list-resource-url">([^<]*)</a>',htmlContent);
				for (var i = 0; i < imgs.length; i++) {
					pic = imgs[i][0];
                    id = idAndTitles[i][0];
                    title = idAndTitles[i][1];
                    var video = {"title":title,"id":id,"pic":pic};
                    videos.push(video);
                };
                yyetsClient.generateIndexPage(keyword,page,channel,videos);
			});
		},
		
		generateIndexPage: function(keyword,page,channel,videos){
			var begin= 1;
			var end = 1;
			if (page < 90) {
				begin = page;
				end = page + 9;
			} else {
				end = 99;
				begin = 90;
			}
			var data = {'channel':channel,'keyword':keyword,'begin':begin,'end':end,'channels':yyetsClient.yyetsChannels,'serverurl':appletv.serverurl,'videos':videos};
			var xml = new EJS({url: appletv.serverurl+'/template/yyets/index.ejs'}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage: function(id){
			//atv.loadXML(appletv.makeDialog('加载中...','Loading...'));
			var url = yyetsVideoApi + itemid;
			appletv.makeRequest(url,function(htmlContent){
				if (htmlContent == null) {
					return;
				}
				var video;
				var title = appletv.find('<h2>(.*)</h2>',htmlContent);
				var pic = appletv.find('<img src="(.*)" />',htmlContent);
				var score = appletv.find('<font class="point">(.*)</font>',htmlContent);
				var year = appletv.find('<li><span>年代：</span>(.*)</li>',htmlContent);
				var type = appletv.find('<li><span>类型：</span>(.*)</li>',htmlContent);
				var dctor = appletv.find('<li><span>导演：</span>(.*)</li>',htmlContent);
				var actor = appletv.find('<li><span>主演：</span>(.*)</li>',htmlContent);
				var desc = appletv.find('<div class="f5">(.*|\r\n*)</div>',htmlContent);
				var items = [];
				var video = {'serverurl':appletv.serverurl,video:{'id':id,actor:actor,area:'',dctor:dctor,pic:pic,score:score,title:title,year:year,desc:desc},'items':items};
				var xml = new EJS({url: appletv.serverurl+'/template/yyets/video.ejs'}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		},
		

}