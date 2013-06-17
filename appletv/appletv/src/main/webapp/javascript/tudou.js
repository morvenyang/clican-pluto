var tudouSearchApi = 'http://api.tudou.com/v5/wireless.search/3/4f3cde4bc0a80001c0a80001000000000661570f01/json/?type=all&pageSize=30&ip=219.142.48.73&os=5.1&ver=2.7.0.12081617&ua=%5BiPad2%2C1%5D%5B5.1.1%5D'
var tudouClient = {

	tudouChannels : [ {
		label : "搜索",
		value : 1001
	}, {
		label : "电视剧",
		value : 30
	}, {
		label : "电影",
		value : 22
	}, {
		label : "综艺",
		value : 31
	}, {
		label : "搞笑",
		value : 5
	}, {
		label : "热点",
		value : 29
	}, {
		label : "动画",
		value : 9
	}, {
		label : "原创",
		value : 99
	}, {
		label : "娱乐",
		value : 1
	}, {
		label : "女性",
		value : 27
	}, {
		label : "体育",
		value : 15
	}, {
		label : "汽车",
		value : 26
	}, {
		label : "科技",
		value : 21
	}, {
		label : "风尚",
		value : 32
	}, {
		label : "乐活",
		value : 3
	}, {
		label : "教育",
		value : 25
	} ],

	tudouChannelMap : {
		"1001" : {
			label : "搜索",
			value : 1001
		},
		"30" : {
			label : "电视剧",
			value : 30
		},
		"22" : {
			label : "电影",
			value : 22
		},
		"31" : {
			label : "综艺",
			value : 31
		},
		"5" : {
			label : "搞笑",
			value : 5
		},
		"29" : {
			label : "热点",
			value : 29
		},
		"9" : {
			label : "动画",
			value : 9
		},
		"99" : {
			label : "原创",
			value : 99
		},
		"1" : {
			label : "娱乐",
			value : 1
		},
		"27" : {
			label : "女性",
			value : 27
		},
		"15" : {
			label : "体育",
			value : 15
		},
		"26" : {
			label : "汽车",
			value : 26
		},
		"21" : {
			label : "科技",
			value : 21
		},
		"32" : {
			label : "风尚",
			value : 32
		},
		"3" : {
			label : "乐活",
			value : 3
		},
		"25" : {
			label : "教育",
			value : 25
		}
	},
	
	loadChannelPage:function(){
		var channels = [];
		var originalChannels = tudouClient.tudouChannels;
		if(!appletv.isAppleApproveCheck()){
			channels = originalChannels;
		}else{
			for(var i=0;i<originalChannels.length;i++){
				var channel = originalChannels[i];
				if(channel.value!=22&&channel.value!=30){
					channels.push(channel);
				}
			}
		}
		var data = {
				'channels' : channels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/tudou/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadIndexPage : function(keyword, page, channelId, queryUrl) {
		appletv.showLoading();
		var channel = this.tudouChannelMap[channelId];
		var videos = [];
		if (channelId == 1001) {
			queryUrl = tudouSearchApi + '&pageNo=' + page + '&kw='
					+ encodeURIComponent(keyword);
			appletv
					.makeRequest(
							queryUrl,
							function(content) {
								if (content != null && content.length > 0) {
									var wirelessSearchResult = JSON
											.parse(content)['wirelessSearchResult'];
									var albumArray = wirelessSearchResult['albums'];
									var itemArray = wirelessSearchResult['items'];

									if (albumArray != null) {
										for (i = 0; i < albumArray.length; i++) {
											var video = {
												"title" : albumArray[i]['title'],
												"id" : albumArray[i]['itemid'],
												"pic" : albumArray[i]['picurl'],
												"album" : 1
											};
											videos.push(video);
										}
									}

									if (itemArray != null) {
										for (i = 0; i < itemArray.length; i++) {
											var video = {
												"title" : itemArray[i]['title'],
												"id" : itemArray[i]['itemid'],
												"pic" : itemArray[i]['picurl'],
												"album" : 0
											};
											videos.push(video);
										}
									}

									tudouClient.generateIndexPage(keyword,
											page, channel, videos);
								} else {
									appletv.showDialog('加载失败', '');
								}
							});
		} else {
			if(queryUrl==null){
				if(channelId==30||channelId==22|channelId==9||channelId==31){
					queryUrl = "http://www.tudou.com/cate/ach" + channelId
					+ "a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe-2pa"
					+ page + ".html";
				}else{
					queryUrl = "http://www.tudou.com/cate/ich" + channelId
					+ "a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe-2pa"
					+ page + ".html";
				}
			}else{
				queryUrl = 'http://'+appletv.substringByData(queryUrl,'http://','2pa')+'2pa'+page+'.html';
			}
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var packs = appletv.getSubValuesByTag(content,
							'<div class="pack', '</div>', 'div');
					for (i = 0; i < packs.length; i++) {
						var pack = packs[i];
						var pic = appletv.substringByData(pack,
								'<img', '>');
						pic = appletv.substringByData(pic,'src="','"');
						var title = appletv.substringByData(pack, 'title="', '"');
						var id = appletv.substringByData(pack, '<a href="', '"');
						var album = 0;
						if (channelId == 30 || channelId == 9) {
							album = 1;
						}
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic,
							"album" : album
						};
						videos.push(video);
					}
					tudouClient.generateIndexPage(keyword, page, channel,
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
			'channels' : tudouClient.tudouChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
			'url': url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/tudou/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	getCategory: function(content,channelId,url){
		var categoryFilterContent = appletv.substringByTag(content,'<div class="category-filter','</div>','div');
		var categoryFilters = appletv.getSubValues(categoryFilterContent,'<div class="category-item','</div>');
		
		var categoryNames = [];
		var categoryMap = {};
		var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId};
		for(i=0;i<categoryFilters.length;i++){
			var categoryName = appletv.substringByData(categoryFilters[i],'<h3>','</h3>');
			categoryNames.push(categoryName);
			var categoryValues = [];
			var categoryLis = appletv.getSubValues(categoryFilters[i],'<li','</li>');
			for(j=0;j<categoryLis.length;j++){
				var select = false;
				if(categoryLis[j].indexOf('class="current"')!=-1){
					select = true
				}
				var categoryLabel = appletv.substringByData(categoryLis[j],'html">','</a>');
				var categoryUrl = 'http://www.tudou.com/cate/'+appletv.substringByData(categoryLis[j],'href="','"');
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
			category = tudouClient.getCategory(content,channelId,url);
			var xml = new EJS({
				url : appletv.serverurl + '/template/tudou/category.ejs'
			}).render(category);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadVideoPage : function(url, channelId, isalbum) {
		appletv.showLoading();
		appletv.makeRequest(url, function(htmlContent) {
			if(htmlContent==null||htmlContent.length==0){
				appletv.showDialog('无法加载相关内容','无法加载相关内容');
				return;
			}
			var itemid = appletv.substringByData(htmlContent, 'iid: ', ',').trim();
			if(itemid.indexOf("'")!=-1){
				itemid = itemid.substring(1,itemid.length-1);
			}
			appletv.logToServer('itemid:' + itemid);
			if(channelId==null||channelId.length==0){
				channelId=appletv.substringByData(htmlContent, 'cid: ', ',').trim();
			}
			tudouClient.loadAlbumPage(itemid, channelId, isalbum,false);
		});
	},

	loadAlbumPage : function(itemid, channelId, isalbum,showloading) {
		if(showloading){
			appletv.showLoading();
		}
		appletv
				.makeRequest(
						'http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=&columnid='
								+ channelId + '&itemid=' + itemid + '&ishd=1',
						function(data) {
							var album = JSON.parse(data);
							var albumitems = album['albumitems'];
							var items = [];
							for ( var i = 0; i < albumitems.length; i++) {
								var t = albumitems[i]['title']+albumitems[i]['subtitle'];
								if(t==null||t.length==0){
									t = '第' + (i + 1) + '集';
								}
								var item = {
									'title' : t,
									'vcode' : albumitems[i]['vcode'],
									'itemid': albumitems[i]['itemid']
								};
								items.push(item);
							}
							var video = {
								'serverurl' : appletv.serverurl,
								album : isalbum,
								channelId : channelId,
								script : appletv.encode("tudouClient.loadAlbumPage('"+itemid+"',"+channelId+","+isalbum+",true);"),
								video : {
									'id' : itemid,
									'actor' : album['actors'],
									'area' : album['area_desc'],
									'dctor' : album['directors'],
									'pic' : album['picurl'],
									'score' : '-',
									'title' : album['title'],
									'year' : album['year'],
									'desc' : album['description'],
									'shareurl':album['emailshareurl']
								},
								items : items
							};
							if(items.length>1){
								appletv.setValue('clican.tudou.video',video);
							}
							var xml = new EJS({
								url : appletv.serverurl
										+ '/template/tudou/video.ejs'
							}).render(video);
							appletv.loadAndSwapXML(xml);
						});
	},

	loadItemsPage : function() {
		appletv.showLoading();
		try{
			appletv.getValue('clican.tudou.video',function(video){
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/tudou/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		}catch(e){
			appletv.logToServer('Error occured tudou.loadItemsPage ' + e);
		}
		
	},

	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', tudouClient.loadKeywordsPage,
				'tudouClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/tudou/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},

	play : function(vcode) {
		appletv.showLoading();
		var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
		appletv.playM3u8(url, '');
	},
	
	playByItemid: function(itemid){
		appletv.showLoading();
		var url = 'http://vr.tudou.com/v2proxy/v2.m3u8?st=4&it='+itemid;
		appletv.playM3u8(url, '');
	}

}