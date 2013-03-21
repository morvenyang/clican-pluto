var lblClient = {
	lblChannels : [ {
		label : "最新电影",
		value : 'category/movie'
	}, {
		label : "最新电视",
		value : 'category/television'
	} ],

	lblChannelMap : {
		'category/movie' : {
			label : "最新电影",
			value : 'category/movie'
		},
		'category/television' : {
			label : "最新电视",
			value : 'category/television'
		}
	},

	
	loadIndexPage : function(keyword, page, channelId,queryUrl) {
		appletv.showLoading();
		var channel = this.lblChannelMap[channelId];
		var videos = [];
		if(queryUrl ==null||queryUrl.length==0){
			queryUrl = 'http://www.longbuluo.com/'+channel['value']+'/page/'+page;
		}
		var s1 = new Date();
		appletv.makeRequest(queryUrl, function(content) {
			if (content != null && content.length > 0) {
				var packs = appletv.getSubValues(content,
						'<h4>', '</h4>');
				for (i = 0; i < packs.length; i++) {
					var pack = packs[i];
					var title = appletv.substring(pack, 'title="', '"');
					var id = appletv.substring(pack, '<a href="', '"');
					var video = {
						"title" : title,
						"id" : id,
					};
					videos.push(video);
				}
				lblClient.loadPics(videos, 0, function(videos){
					lblClient.generateIndexPage(keyword, page, channel, videos,queryUrl);
					var s2 = new Date();
					appletv.logToServer('total:'+(s2.getTime()-s1.getTime()))
				});
			} else {
				appletv.showDialog('加载失败','');
			}
		});

	},

	generateIndexPage : function(keyword, page, channel, videos,url) {
		var begin = 1;
		var end = 1;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		var data = {
			'channel' : channel,
			'keyword' : keyword,
			'begin' : begin,
			'end' : end,
			'channels' : lblClient.lblChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
			'url' : url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/lbl/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadPics : function(videos,index,callback){
		if(index==videos.length){
			callback(videos);
		}else{
			var url = videos[index]['id'];
			appletv.makeRequest(url,function(content){
				var entry = appletv.substringByTag(content,'<div class="entry">','</div>','div');
				videos[index]['pic'] = appletv.substring(entry,'src="','"');
				lblClient.loadPics(videos,index+1,callback);
			});
		}
	},
}