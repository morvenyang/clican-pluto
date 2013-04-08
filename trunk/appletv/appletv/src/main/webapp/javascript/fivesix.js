var fivesixClient = {
	fivesixChannels : [ {
		label : "搜索",
		value : "search"
	}, {
		label : "电影",
		value : "dy"
	}, {
		label : "电视剧",
		value : "dsj"
	}, {
		label : "热点",
		value : "rd"
	}, {
		label : "娱乐",
		value : "yl"
	}, {
		label : "原创",
		value : "yc"
	}, {
		label : "综艺",
		value : "zy"
	}, {
		label : "音乐",
		value : "yy"
	}, {
		label : "搞笑",
		value : "gx"
	}, {
		label : "动漫",
		value : "dm"
	}, {
		label : "体育",
		value : "ty"
	}, {
		label : "游戏",
		value : "yx"
	}, {
		label : "女性",
		value : "nx"
	}, {
		label : "母婴",
		value : "my"
	}, {
		label : "汽车",
		value : "qc"
	}, {
		label : "科教",
		value : "kj"
	}, {
		label : "旅游",
		value : "ly"
	}, {
		label : "美女主播",
		value : "videolist-v-mm.html"
	}, {
		label : "微栏目",
		value : "videolist-v-pm.html"
	} ],

	fivesixChannelMap : {
		"search" : {
			label : "搜索",
			value : "search"
		},
		"dy" : {
			label : "电影",
			value : "dy",
			url: "http://video.56.com/tv-v-movie_sort-n_tid-_zid-_yid-_page-1.html"
		},
		"dsj" : {
			label : "电视剧",
			value : "dsj",
			url: "http://video.56.com/tv-v-tv_sort-n_tid-_zid-_yid-_page-1.html"
		},
		"rd" : {
			label : "热点",
			value : "rd"
		},
		"yl" : {
			label : "娱乐",
			value : "yl"
		},
		"yc" : {
			label : "原创",
			value : "yc"
		},
		"zy" : {
			label : "综艺",
			value : "zy"
		},
		"yy" : {
			label : "音乐",
			value : "yy"
		},
		"gx" : {
			label : "搞笑",
			value : "gx"
		},
		"dm" : {
			label : "动漫",
			value : "dm"
		},
		"ty" : {
			label : "体育",
			value : "ty"
		},
		"yx" : {
			label : "游戏",
			value : "yx"
		},
		"nx" : {
			label : "女性",
			value : "nx"
		},
		"my" : {
			label : "母婴",
			value : "my"
		},
		"qc" : {
			label : "汽车",
			value : "qc"
		},
		"kj" : {
			label : "科教",
			value : "kj"
		},
		"ly" : {
			label : "旅游",
			value : "ly"
		},
		"videolist-v-mm.html" : {
			label : "美女主播",
			value : "videolist-v-mm.html"
		},
		"videolist-v-pm.html" : {
			label : "微栏目",
			value : "videolist-v-pm.html"
		}
	},
	
	loadChannelPage:function(){
		var data = {
				'channels' : fivesixClient.fivesixChannels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/fivesix/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadIndexPage : function(keyword, page, channelId, queryUrl) {
		appletv.showLoading();
		var channel = this.fivesixChannelMap[channelId];
		var videos = [];
		if (channelId == 'search') {
			
		} else {
			if(queryUrl==null){
				queryUrl = channel['url'].replace('page-1','page-'+page);
			}else{
				var start = queryUrl.indexOf('page-');
				if(start>=0){
					start = start+1;
					queryUrl = queryUrl.substring(0,start)+page+'.html';
				}
			}
			appletv.logToServer(queryUrl);
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var videoContent = appletv.substringByTag(content,'<div class="content tv" id="content">','</div>','div');
					var packs = appletv.getSubValuesByTag(videoContent,
							'<dt>', '</dt>', 'dt');
					for (i = 0; i < packs.length; i++) {
						var pack = packs[i];
						var pic = appletv.substringByData(pack,
								'<img', '>');
						pic = appletv.substringByData(pic,'src="','"');
						var title = appletv.substringByData(pack, '<a target="_blank" href="', '</a>');
						title = appletv.subIndexString(title,'>');
						var id = appletv.substringByData(pack, 'href="','"');
						var album = false;
						if (channelId == 'dsj' || channelId == 'dy') {
							album = true;
						}
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic,
							"album" : album
						};
						videos.push(video);
					}
					fivesixClient.generateIndexPage(keyword, page, channel,
							videos,queryUrl);
				} else {
					appletv.showDialog('加载失败', '');
				}
			});
		}
	},

	generateIndexPage : function(keyword, page, channel, videos,url) {
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
			'channels' : fivesixClient.fivesixChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
			'url': url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/fivesix/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadVideoPage : function(url) {
		appletv.showLoading();
		appletv
				.makeRequest(url,
						function(htmlContent) {
							var content = appletv.getSubValuesByTag(htmlContent,
									'<div class="content">', '</div>', 'div')
							var actor = '-';
							var dctor = '-';
							var area = '-';
							var score = '-';
							var year = '-';
							var shareurl = url;
							
							var title = appletv.substringByData(content,'title="','"');
							var desc = appletv.substringByData(content,'<dd class="h_2" id="videoinfo_l" style="display:none">','<a');
							
							dctor = appletv.substringByData(content,'导演','</dd>');
							dctor = appletv.getSubValuesByTag(dctor,'<a','</a>','a');
							
							actor = appletv.substringByData(content,'演员','</dd>');
							actor = appletv.getSubValuesByTag(dctor,'<a','</a>','a');
							year = appletv.substringByData(content,'上映','</dd>');
							year = appletv.subIndexString(year,'<dd>');
							area = appletv.substringByData(content,'地区','</dd>');
							area = appletv.subIndexString(year,'<dd>');
							var script = appletv.encode("youkuClient.loadVideoPage('"+url+"');");
							
							var items = [];
							var itemscontent = appletv.substringByTag(htmlContent,'<div id="albumVideos">','</div>','div');
							var items = appletv.getSubValues(itemscontent,'<dl','</dl>');
							for(i=0;i<items.length;i++){
								var item = items[i];
								var t = appletv.substringByData(items,'title="','"');
								var c = appletv.substringByData(items,'href="','"');
								c = appletv.substringByData(c,'v_','.html');
								var item = {
										'title' : t,
										'id' : c
									};
								items.push(item);
							}
							
							
							var video = {
									'serverurl' : appletv.serverurl,
									channelId : channelId,
									script : script,
									video : {
										'id' : code,
										'actor' : actor,
										'area' : area,
										'dctor' : dctor,
										'pic' : pic,
										'score' : score,
										'title' : title,
										'year' : year,
										'desc' : desc,
										'shareurl':shareurl
									},
									items : items
								};
								if(isalbum){
									appletv.setValue('clican.fivesix.video',video);
								}
								var xml = new EJS({
									url : appletv.serverurl
											+ '/template/fivesix/video.ejs'
								}).render(video);
								appletv.loadAndSwapXML(xml);
						});
	},
}