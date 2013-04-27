var weivideoClient = {
	weivideoChannelMap : {
		"search" : {
			label : "搜索",
			value : 'search'
		},
		"1" : {
			label : "电视剧",
			value : '1'
		},
		"4" : {
			label : "电影",
			value : '4'
		}
	},

	weivideoChannels : [ {
		label : "搜索",
		value : 'search'
	}, {
		label : "电视剧",
		value : '1'
	}, {
		label : "电影",
		value : '4'
	} ],

	loadChannelPage : function() {
		var channels = [];
		var originalChannels = weivideoClient.weivideoChannels;
		if (appletv.isTVAndMovieEnable()) {
			channels = originalChannels;
		} else {
			for ( var i = 0; i < originalChannels.length; i++) {
				var channel = originalChannels[i];
				if (channel.value != 'tv' && channel.value != 'movie') {
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
			var queryUrl = 'http://newvideopc.video.sina.com.cn/movie/fapi/data?uid=&q_category='
					+ channelId
					+ '&q_region='
					+ encodeURIComponent('全部')
					+ '&page=' + page + '&time=' + new Date().getTime();
			appletv.logToServer(queryUrl);
			appletv
					.makeRequest(
							queryUrl,
							function(jsonContent) {
								if (jsonContent != null
										&& jsonContent.length > 0) {
									var videoList = JSON.parse(jsonContent)['result']['data']['list'];
									for (i = 0; i < videoList.length; i++) {
										var v = videoList[i];
										var pic = v['pic_list'];
										var title = v['title'];
										var id = v['single_video_set_id'];
										var video = {
											"title" : title,
											"id" : id,
											"pic" : pic
										};
										videos.push(video);
									}
									weivideoClient.generateIndexPage(keyword,
											page, channel, videos);
								} else {
									appletv.showDialog('加载失败', '');
								}
							});
		}

	},

	generateIndexPage : function(keyword, page, channel, videos) {
		if (videos.length == 0) {
			appletv.showDialog('没有相关视频', '');
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
			'channels' : weivideoClient.weivideoChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/weivideo/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadVideoPage : function(id) {
		var url = 'http://video.weibo.com/detail/' + id;
		appletv.logToServer(url);
		appletv.makeRequest(url, function(htmlContent) {
			var title;
			var desc;
			var pic;
			var actor = '-';
			var dctor = '-';
			var area = '-';
			var score = '-';
			var year = '-';
			var shareurl = url;
			var script;
			var items = [];
			var videoContent = htmlContent;
			title = appletv.substringByData(videoContent, '<h2>', '</h2>');
			desc = appletv.substringByTag(videoContent,
					'<p id="mvSiInfoIntro"', '</p>', 'p');
			desc = appletv.subIndexString(desc, '>');
			script = appletv.encode("weivideoClient.loadVideoPage('" + id
					+ "');");
			pic = appletv.substringByTag(videoContent,
					'<div class="mv-center">', '</div>', 'div');
			pic = appletv.substringByData(pic, '<img src="', '"');
			var itemscontent = appletv.substringByTag(videoContent,
					'<div class="scs-ace-content clearfix">', '</div>', 'div');
			appletv.logToServer(itemscontent);
			var ids = appletv.getSubValues(itemscontent, 'href="', '"');
			for ( var i = 0; i < ids.length; i++) {
				var t = '第' + (i + 1) + '集';
				var c = appletv.substringByData(ids[i], 'weishipin/', '.htm');
				var item = {
					'title' : t,
					'id' : c
				};
				items.push(item);
			}
			var video = {
				'serverurl' : appletv.serverurl,
				script : script,
				video : {
					'id' : url,
					'actor' : actor,
					'area' : area,
					'dctor' : dctor,
					'pic' : pic,
					'score' : score,
					'title' : title,
					'year' : year,
					'desc' : desc,
					'shareurl' : shareurl
				},
				items : items
			};
			if (items.length > 1) {
				appletv.setValue('clican.weivideo.video', video);
			}
			var xml = new EJS({
				url : appletv.serverurl + '/template/weivideo/video.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},

	loadItemsPage : function() {
		appletv.showLoading();
		appletv.getValue('clican.weivideo.video', function(video) {
			var xml = new EJS({
				url : appletv.serverurl + '/template/weivideo/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},
}