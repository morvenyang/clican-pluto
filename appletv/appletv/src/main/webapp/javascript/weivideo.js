var weivideoClient{
	weivideoChannelMap : {
		"search" : {
			label : "搜索",
			value : 'search'
		},
		"tv" : {
			label : "电视剧",
			value : 'tv'
		},
		"movie" : {
			label : "电影",
			value : 'movie'
		}
	},

	weivideoChannels : [ {
		label : "搜索",
		value : 'search'
	}, {
		label : "电视剧",
		value : 'tv'
	}, {
		label : "电影",
		value : 'movie'
	}],
	
	loadChannelPage:function(){
		var channels = [];
		var originalChannels = weivideoClient.weivideoChannels;
		if(appletv.isTVAndMovieEnable()){
			channels = originalChannels;
		}else{
			for(var i=0;i<originalChannels.length;i++){
				var channel = originalChannels[i];
				if(channel.value!='tv'&&channel.value!='movie'){
					channels.push(channel);
				}
			}
		}
		var data = {
				'channels' : channels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/weivideo/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.weivideoChannelMap[channelId];
		var videos = [];
		if (channelId == 1001) {
			
		} else {
			var queryUrl = 'http://newvideopc.video.sina.com.cn/movie/fapi/data?uid=&q_category=1&q_region='+encodeURIComponent('全部')+'&page=1&time='+new Date().getTime();
			appletv.makeRequest(queryUrl, function(jsonContent) {
				if (jsonContent != null && jsonContent.length > 0) {
					var videoList = JSON.parse(jsonContent)['result']['data']['list'];
					for (i = 0; i < videoList.length; i++) {
						var v = videoList[i];
						var pic = v['pic_play_page'];
						var title = v['title'];
						var id = v['single_video_set_id'];
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic
						};
						videos.push(video);
					}
					weivideoClient.generateIndexPage(keyword, page, channel,
							videos);
				} else {
					appletv.showDialog('加载失败', '');
				}
			});
		
		}

	},
	
	generateIndexPage : function(keyword, page, channel, videos) {
		if(videos.length==0){
			appletv.showDialog('没有相关视频','');
			return;
		}
		var begin = 1;
		var end = 1;
		if (page < 92) {
			begin = page;
			end = page + 7;
		} else {
			end = 99;
			begin = 92;
		}
		var data = {
			'page' : page,
			'channel' : channel,
			'keyword' : keyword,
			'begin' : begin,
			'end' : end,
			'channels' : youkuClient.youkuChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/weivideo/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
}