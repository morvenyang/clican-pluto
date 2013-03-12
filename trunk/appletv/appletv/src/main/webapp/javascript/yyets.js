var yyetsSearchApi = "http://www.yyets.com/php/resourcelist";

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
			var url = yyetsSearchApi+"?channel="+channelId+"&page="+page;
			var channel = this.yyetsChannelMap[channelId];
			appletv.makeRequest(yyetsSearchApi,function(htmlContent){
				if (htmlContent == null) {
					return;
				}
                var start = d.indexOf('<ul class="boxPadd dashed">');
                var end = d.indexOf('</ul>', start);
                var itemstr = d.substring(start, end);
                var itemlist = itemstr.split('<li class="clearfix">');
                var videos = [];
                for (var i = 1; i < itemlist.length; i++) {
                	var rs = (new RegExp('<a target="_blank" class="imglink" href="http://www.yyets.com/php/resource/(\\d+)"><img src="([^"]*)"></a>')).exec(itemlist[i]);
                	if (!rs) {
                		continue;
                	}
                	var itemid = rs[1];
                	var imgurl = rs[2];
                	var rsn = (new RegExp('<dt><a target="_blank" href="http://www.yyets.com/php/resource/\\d+">【([^<]*)】<strong>《([^<]*)》([^<]*)</strong></a>([^<]*)</dt>')).exec(itemlist[i]);
                    if (!rsn) {
                    	continue;
                    }
                    var name = rsn[2];
                    var video = {pic:imageurl,id:itemid,title:name,album:true};
                    videos.push(video);
                }
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
		

}