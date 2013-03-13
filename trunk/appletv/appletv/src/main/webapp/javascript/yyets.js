var yyetsSearchApi = "http://ziyuan.kehuduan.rryingshi.com:20066/resources";
var yyetsVideoApi = "http://www.yyets.com/php/resource/";
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
				appletv.logToServer('get html content');
				if (htmlContent == null) {
					return;
				}
				var videos = [];
				var dom = htmlparse.HTMLParser(htmlContent);
				htmlparse.each(htmlparse.find(dom,"li.resli"),function(i,value){
					var resli = value;
					var img = htmlparse.find(resli,"img").getAttribute("src");
					var title = htmlparse.find(htmlparse.find(resli,"h2.fl"),"a").textContent;
					var id = htmlparse.find(htmlparse.find(resli,"h2.fl"),"a").getAttribute("href").replace("/resources/","");
					video = {pic:img,id:id,title:title};
					videos.push(video);
				});
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
			atv.loadXML(appletv.makeDialog('加载中...','Loading...'));
			var url = yyetsVideoApi + itemid;
			appletv.makeRequest(url,function(htmlContent){
				if (htmlContent == null) {
					return;
				}
				var video;
				var xml = new EJS({url: appletv.serverurl+'/template/yyets/video.ejs'}).render(video);
				atv.loadAndSwapXML(atv.parseXML(xml));
			});
		},
		

}