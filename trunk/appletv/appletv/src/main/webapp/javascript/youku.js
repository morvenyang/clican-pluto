var youkuClient = {
	youkuChannelMap : {
		"1001" : {
			label : "搜索",
			value : 1001
		},
		"97" : {
			label : "电视剧",
			value : 97,
			album : true
		},
		"96" : {
			label : "电影",
			value : 96,
			album : true
		},
		"85" : {
			label : "综艺",
			value : 85,
			album : true
		},
		"100" : {
			label : "动漫",
			value : 100,
			album : true
		},
		"95" : {
			label : "音乐",
			value : 95,
			album : true
		},
		"87" : {
			label : "教育",
			value : 87,
			album : true
		},
		"84" : {
			label : "纪录片",
			value : 84,
			album : true
		},
		"91" : {
			label : "资讯",
			value : 91,
			album : false
		},
		"86" : {
			label : "娱乐",
			value : 86,
			album : false
		},
		"92" : {
			label : "原创",
			value : 92,
			album : false
		},
		"98" : {
			label : "体育",
			value : 98,
			album : false
		},
		"104" : {
			label : "汽车",
			value : 104,
			album : false
		},
		"105" : {
			label : "科技",
			value : 105,
			album : false
		},
		"99" : {
			label : "游戏",
			value : 99,
			album : false
		},
		"103" : {
			label : "生活",
			value : 103,
			album : false
		},
		"89" : {
			label : "时尚",
			value : 89,
			album : false
		},
		"88" : {
			label : "旅游",
			value : 88,
			album : false
		},
		"90" : {
			label : "母婴",
			value : 90,
			album : false
		},
		"94" : {
			label : "搞笑",
			value : 94,
			album : false
		},
		"102" : {
			label : "广告",
			value : 102,
			album : false
		}
	},

	youkuChannels : [ {
		label : "搜索",
		value : 1001
	}, {
		label : "电视剧",
		value : 97,
		album : true
	}, {
		label : "电影",
		value : 96,
		album : true
	}, {
		label : "综艺",
		value : 85,
		album : true
	}, {
		label : "动漫",
		value : 100,
		album : true
	}, {
		label : "音乐",
		value : 95,
		album : true
	}, {
		label : "教育",
		value : 87,
		album : true
	}, {
		label : "纪录片",
		value : 84,
		album : true
	}, {
		label : "资讯",
		value : 91,
		album : false
	}, {
		label : "娱乐",
		value : 86,
		album : false
	}, {
		label : "原创",
		value : 92,
		album : false
	}, {
		label : "体育",
		value : 98,
		album : false
	}, {
		label : "汽车",
		value : 104,
		album : false
	}, {
		label : "科技",
		value : 105,
		album : false
	}, {
		label : "游戏",
		value : 99,
		album : false
	}, {
		label : "生活",
		value : 103,
		album : false
	}, {
		label : "时尚",
		value : 89,
		album : false
	}, {
		label : "旅游",
		value : 88,
		album : false
	}, {
		label : "母婴",
		value : 90,
		album : false
	}, {
		label : "搞笑",
		value : 94,
		album : false
	}, {
		label : "广告",
		value : 102,
		album : false
	} ],
	
	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.youkuChannelMap[channelId];
		var videos = [];
		var queryUrl;
		if (channelId == 1001) {
			
		} else {
			if(channel.album){
				queryUrl = 'http://www.youku.com/v_olist/c_'+channelId+'_a__s__g__r__lg__im__st__mt__tg__d_1_et_0_fv_0_fl__fc__fe__o_7_p_'+page+'.html';
			}else{
				queryUrl = 'http://www.youku.com/v_showlist/t1c'+channelId+'g0d4p'+page+'.html'
			}
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var itemscontent = appletv.substringByTag(content,'<div class="items">', '</div>', 'div');
					var items = appletv.getSubValuesByTag(itemscontent,
							'<ul class="p pv">', '</ul>', 'ul');
					for (i = 0; i < items.length; i++) {
						var item = items[i];
						var pic = appletv.substring(item,
								'<img src="', '"');
						var title = appletv.substring(item, 'title="', '"');
						var id = appletv.substring(item, '<a href="', '"');
						id = appletv.substring(id, 'id_', '.html');
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic,
							"album" : channel.album
						};
						videos.push(video);
					}
					youkuClient.generateIndexPage(keyword, page, channel,
							videos);
				} else {
					atv.loadXML(appletv.makeDialog('加载失败', ''));
				}
			});
		
		}

	},

	generateIndexPage : function(keyword, page, channel, videos) {
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
			'channels' : youkuClient.youkuChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/youku/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
}