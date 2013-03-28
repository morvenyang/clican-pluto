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
	
	loadChannelPage:function(){
		var data = {
				'channels' : youkuClient.youkuChannels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/youku/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadIndexPage : function(keyword, page, channelId,queryUrl) {
		appletv.showLoading();
		var channel = this.youkuChannelMap[channelId];
		var videos = [];
		if (channelId == 1001) {
		} else {
			if(queryUrl==null){
				if(channel.album){
					queryUrl = 'http://www.youku.com/v_olist/c_'+channelId+'_a__s__g__r__lg__im__st__mt__tg__d_1_et_0_fv_0_fl__fc__fe__o_7_p_'+page+'.html';
				}else{
					queryUrl = 'http://www.youku.com/v_showlist/t1c'+channelId+'g0d4p'+page+'.html'
				}
			}else{
				if(channel.album){
					var pIndex = queryUrl.lastIndexOf('p_');
					if(pIndex==-1){
						pIndex = queryUrl.indexOf('.html');
						queryUrl = queryUrl.substring(0,pIndex)+'p_'+page+'.html';
					}
					queryUrl = queryUrl.substring(0,pIndex)+'p_'+page+'.html';
				}else{
					var pIndex = queryUrl.lastIndexOf('p');
					if(pIndex==-1){
						pIndex = queryUrl.indexOf('.html');
					}
					queryUrl = queryUrl.substring(0,pIndex)+'p'+page+'.html';
				}
			}
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var itemscontent = appletv.substringByTag(content,'<div class="items">', '</div>', 'div');
					var items = appletv.getSubValuesByTag(itemscontent,
							'<ul class="p pv">', '</ul>', 'ul');
					for (i = 0; i < items.length; i++) {
						var item = items[i];
						var pic = appletv.substringByData(item,
								'<img src="', '"');
						var title = appletv.substringByData(item, 'title="', '"');
						var id = appletv.substringByData(item, '<a href="', '"');
						id = appletv.substringByData(id, 'id_', '.html');
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic,
							"album" : channel.album
						};
						videos.push(video);
					}
					youkuClient.generateIndexPage(keyword, page, channel,
							videos,queryUrl);
				} else {
					appletv.showDialog('加载失败', '');
				}
			});
		
		}

	},

	generateIndexPage : function(keyword, page, channel, videos,url) {
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
			'url' : url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/youku/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	getCategory: function(content,channelId,url){
		var channel = this.youkuChannelMap[channelId];
		var categoryFilterContent = appletv.substringByTag(content,'<div class="filter" id="filter">','</div>','div');
		var categoryFilters = appletv.getSubValuesByTag(categoryFilterContent,'<div class="item">','</div>','div');
		var categoryNames = [];
		var categoryMap = {};
		var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId};
		for(i=0;i<categoryFilters.length;i++){
			var categoryName = appletv.substringByData(categoryFilters[i],'<label>','</label>');
			categoryNames.push(categoryName);
			var categoryValues = [];
			var categoryLis = appletv.getSubValues(categoryFilters[i],'<li','</li>');
			for(j=0;j<categoryLis.length;j++){
				var select = false;
				var categoryLabel;
				if(categoryLis[j].indexOf('class="current"')!=-1){
					select = true
					categoryLabel = appletv.substringByData(categoryLis[j],'<span>','</span>');
				}else{
					categoryLabel = appletv.substringByData(categoryLis[j],'">','<');
				}
				var categoryUrl = 'http://www.youku.com'+appletv.substringByData(categoryLis[j],'href="','"');
				var categoryValue={"categoryLabel":categoryLabel,"categoryUrl":categoryUrl,"select":select};
				categoryValues.push(categoryValue);
			}
			categoryMap[categoryName] = categoryValues;
		}
		return category;
	},
	
	loadCategoryPage: function(url,channelId,loading){
		if(loading){
			appletv.showLoading();
		}
		appletv.makeRequest(url, function(content) {
			category = youkuClient.getCategory(content,channelId,url);
			var xml = new EJS({
				url : appletv.serverurl + '/template/youku/category.ejs'
			}).render(category);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadVideoPage : function(code, channelId, isalbum,pic) {
		appletv.showLoading();
		var url;
		if(isalbum){
			url = 'http://www.youku.com/show_page/id_'+code+'.html';
		}else{
			url = 'http://v.youku.com/v_show/id_'+code+'.html';
		}
		appletv.makeRequest(url, function(htmlContent) {
			if (htmlContent == null) {
				return;
			}
			var actor = '-';
			var dctor = '-';
			var area = '-';
			var score = '-';
			var year = '-';
			var shareurl = url;
			var desc;
			if(isalbum){
				pic = appletv.substringByData(htmlContent,'<li class="thumb">','</li>');
				pic = appletv.substringByData(pic,'src=\'','\'');
				title = appletv.substringByData(htmlContent,'<span class="name">','</span>');
				area = appletv.substringByData(htmlContent,'<span class="area">','</span>');
				area = appletv.getSubValues(area,'target="_blank">', '</a>');
				year = appletv.substringByData(htmlContent,'<span class="pub">','</span>');
				score = appletv.substringByData(htmlContent,'<em class="num">','</em>');
				if(channelId==97){
					//电视剧
					actor = appletv.substringByData(htmlContent,'<span class="actor">','</span>');
					actor = appletv.getSubValues(actor,'target="_blank">', '</a>');
					desc = appletv.substringByData(htmlContent,'<span class="short" id="show_info_short" style="display: inline;">','</span>');
				}else if(channelId==96){
					//电影
					actor = appletv.substringByData(htmlContent,'<span class="actor">','</span>');
					actor = appletv.getSubValues(actor,'target="_blank">', '</a>');
					dctor = appletv.substringByData(htmlContent,'<span class="director">','</span>');
					dctor = appletv.getSubValues(dctor,'target="_blank">', '</a>');
					desc = appletv.substringByData(htmlContent,'<span class="long" style="display:none;">','</span>');
				}
			}else{
				title =  appletv.substringByData(htmlContent,'<meta name="title" content="','"');
				desc =  appletv.substringByData(htmlContent,'<meta name="description" content="','"');
			}
			desc = appletv.getTextInTag(desc);
			var script = appletv.encode("youkuClient.loadVideoPage('"+code+"',"+channelId+","+isalbum+",'"+pic+"');");
			if(channelId==96){
				isalbum = false;
				code = appletv.substringByData(htmlContent,'id_','.html');
			}
			var items = [];
			if(isalbum){
				var itemscontent = appletv.substringByTag(htmlContent,'<div class="items"','</div>','div');
				var urls = appletv.getSubValues(itemscontent,'<a','</a>');
				for(i=0;i<urls.length;i++){
					url = urls[i];
					var t = appletv.substringByData(url,'title="','"');
					var c = appletv.substringByData(url,'id_','.html');
					var item = {
							'title' : t,
							'id' : c
						};
					items.push(item);
				}
			}else{
				var item = {
						'title' : title,
						'id' : code
					};
				items.push(item);
			}
			
			
			var video = {
					'serverurl' : appletv.serverurl,
					album : isalbum,
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
					appletv.setValue('clican.youku.video',video);
				}
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/youku/video.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
		});
	},
	
	loadItemsPage : function() {
		appletv.showLoading();
		appletv.getValue('clican.youku.video',function(video){
			var xml = new EJS({
				url : appletv.serverurl
						+ '/template/youku/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', youkuClient.loadKeywordsPage,
				'youkuClient.loadKeywordsPage', '');
	},

	loadKeywordsPage : function(q) {
		appletv.showLoading();
		var queryUrl = 'http://tip.tudou.soku.com/hint?q=' + q;
		appletv.makeRequest(queryUrl, function(result) {
			appletv.logToServer(result);
			var keywords = JSON.parse(result);
			var data = {
				keywords : keywords,
				serverurl : appletv.serverurl
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/youku/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	play : function(vcode) {
		appletv.showLoading();
		var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
		appletv.playM3u8(url, '');
	},

}