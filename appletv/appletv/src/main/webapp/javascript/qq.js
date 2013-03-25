var qqSearchApi = "http://ncgi.video.qq.com/tvideo/fcgi-bin/srh_ipad?num=30&tabid=0&plat=4&pver=2&sort=0&filter=18&otype=json&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN";
var qqChannelApi = "http://sns.video.qq.com/fcgi-bin/dlib/dataout?sort=2&iarea=-1&itype=-1&iyear=-1&iedition=-1&pagesize=30&itrailer=-1&otype=json&version=20000&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN&timeout=0";
var qqPlayApi = "http://vv.video.qq.com/geturl?otype=json&callback=a&qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN";
var qqClient = {
	qqChannels : [ {
		label : "推荐",
		value : 3,
		platform : 5
	}, {
		label : "搜索",
		value : 1001,
		platform : 5
	}, {
		label : "电视剧",
		value : 15,
		platform : 5
	}, {
		label : "电影",
		value : 14,
		platform : 5
	}, {
		label : "动漫",
		value : 16,
		platform : 5
	}, {
		label : "综艺",
		value : 17,
		platform : 5
	}, {
		label : "新闻",
		value : 22,
		platform : 5
	}, {
		label : "电视直播",
		value : 24,
		platform : 7
	}, {
		label : "美剧",
		value : 48,
		platform : 7
	}, {
		label : "娱乐",
		value : 21,
		platform : 5
	}, {
		label : "微讲堂",
		value : 37,
		platform : 5
	}, {
		label : "体育",
		value : 20,
		platform : 5
	}, {
		label : "纪录片",
		value : 19,
		platform : 5
	} ],

	qqChannelMap : {
		"3" : {
			label : "推荐",
			value : 3,
			platform : 5
		},
		"1001" : {
			label : "搜索",
			value : 1001,
			platform : 5
		},
		"15" : {
			label : "电视剧",
			value : 15,
			platform : 5
		},
		"14" : {
			label : "电影",
			value : 14,
			platform : 5
		},
		"16" : {
			label : "动漫",
			value : 16,
			platform : 5
		},
		"17" : {
			label : "综艺",
			value : 17,
			platform : 5
		},
		"22" : {
			label : "新闻",
			value : 22,
			platform : 5
		},
		"24" : {
			label : "电视直播",
			value : 24,
			platform : 7
		},
		"48" : {
			label : "美剧",
			value : 48,
			platform : 7
		},
		"21" : {
			label : "娱乐",
			value : 21,
			platform : 5
		},
		"37" : {
			label : "微讲堂",
			value : 37,
			platform : 5
		},
		"20" : {
			label : "体育",
			value : 20,
			platform : 5
		},
		"19" : {
			label : "纪录片",
			value : 19,
			platform : 5
		}
	},
	
	loadChannelPage:function(){
		appletv.logToServer('loadChannelPage 1');
		var data = {
				'channels' : qqClient.qqChannels,
				'serverurl' : appletv.serverurl
			};
		appletv.logToServer('loadChannelPage 2');
		var xml = new EJS({
			url : appletv.serverurl + '/template/qq/channel.ejs'
		}).render(data);
		appletv.logToServer('loadChannelPage 3');
		appletv.loadAndSwapXML(xml);
	},

	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.qqChannelMap[channelId];
		var videos = [];
		var queryUrl;
		if (channelId == 1001) {
			queryUrl = qqSearchApi + "&comment=1&cur=" + page + "&query="
					+ keyword;
		} else {
			queryUrl = qqChannelApi + "&page=" + page + "&auto_id=" + channelId
					+ "&platform=" + channel['platform'];
		}
		appletv.makeRequest(queryUrl, function(content) {
			if (content != null && content.length > 0) {
				var jsonContent = content.substring("QZOutputJson=".length,
						content.length - 1);
				var result = JSON.parse(jsonContent);
				if (channelId == 3) {
					// 推荐
					var datas = result['data'];
					var content;
					var video;
					for ( var i = 0; i < datas.length; i++) {
						var contents = datas[i]['contents'];
						for ( var j = 0; j < contents.length; j++) {
							content = contents[j];
							if (content['id_type'] != null
									&& content['id_type'] == 't') {
								continue;
							}
							video = {
								pic : content['v_pic'],
								id : content['id'],
								title : content['title'],
								album : true
							};

							videos.push(video);
						}
					}
				} else if (channelId == 1001) {
					var contents = result['list'];
					for ( var j = 0; j < contents.length; j++) {
						content = contents[j];
						video = {
							pic : content['AU'],
							id : content['ID'],
							title : content['TI'],
							album : true
						};
						videos.push(video);
					}
					var queryUrl2 = qqSearchApi + "&comment=0&cur=" + page
							+ "&query=" + keyword;
					appletv.makeRequest(queryUrl2, function(content2) {
						if (content2 != null && content2.length > 0) {
							var jsonContent2 = content2
									.substring("QZOutputJson=".length,
											content2.length - 1);
							var result2 = JSON.parse(jsonContent2);
							var contents2 = result2['list'];
							for ( var j = 0; j < contents2.length; j++) {
								content2 = contents2[j];
								var bn = content2['BN'];
								if (bn != null && bn != '0') {
									bn = '第' + bn + '集';
								} else {
									bn = '';
								}
								video = {
									pic : content2['AU'],
									id : content2['ID'],
									title : content2['TI'],
									subTitle : bn,
									album : false
								};
								videos.push(video);
							}
						}
						qqClient.generateIndexPage(keyword, page, channel,
								videos);
					});
					return;
				} else {
					var contents;
					if (result['video'] != null) {
						contents = result['video'];
					} else {
						contents = result['cover'];
					}
					for ( var j = 0; j < contents.length; j++) {
						content = contents[j];
						if (content['id_type'] != null
								&& content['id_type'] == 't') {
							continue;
						}
						var tempPic = content['c_pic'];
						if (tempPic == null) {
							tempPic = content['c_pic_url'];
						}
						video = {
							pic : tempPic,
							id : content['c_cover_id'],
							title : content['c_title'],
							album : true
						};
						videos.push(video);
					}
				}

			} else {
				appletv.showDialog('加载失败', '');
			}
			qqClient.generateIndexPage(keyword, page, channel, videos);
		});

	},

	generateIndexPage : function(keyword, page, channel, videos) {
		var begin = 0;
		var end = 0;
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
			'channels' : qqClient.qqChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/qq/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadAlbumPage : function(id) {
		appletv.showLoading();
		var url = 'http://live.qq.com/json/ipad/cover/'
				+ id.substring(0, 1)
				+ '/'
				+ id
				+ '.json?qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN';

		appletv.makeRequest(url, function(content) {
			var result = JSON.parse(content);
			var contents = result['videos'];
			var items = [];
			for ( var j = 0; j < contents.length; j++) {
				var item = {
					vid : contents[j]['vid'],
					title : contents[j]['tt']
				};
				items.push(item);
			}
			var video = {
				'serverurl' : appletv.serverurl,
				script : appletv.encode("qqClient.loadAlbumPage('"+id+"');"),
				video : {
					'id' : id,
					actor : JSON.stringify(result['actor']),
					area : result['area'],
					dctor : JSON.stringify(result['dctor']),
					pic : result['pic'],
					score : result['score'],
					title : result['tt'],
					year : result['year'],
					desc : result['desc']
				},
				'items' : items
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/qq/video.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},

	loadItemsPage : function(id) {
		appletv.showLoading();
		var url = 'http://live.qq.com/json/ipad/cover/'
				+ id.substring(0, 1)
				+ '/'
				+ id
				+ '.json?qq=&appver=2.0.0.2208&sysver=ios5.1.1&device=iPad&lang=zh_CN';

		appletv.makeRequest(url, function(content) {
			var result = JSON.parse(content);
			var contents = result['videos'];
			var items = [];
			for ( var j = 0; j < contents.length; j++) {
				var item = {
					vid : contents[j]['vid'],
					title : contents[j]['tt']
				};
				items.push(item);
			}
			var video = {
				'serverurl' : appletv.serverurl,
				video : {
					'id' : id,
					actor : JSON.stringify(result['actor']),
					area : result['area'],
					dctor : JSON.stringify(result['dctor']),
					pic : result['pic'],
					score : result['score'],
					title : result['tt'],
					year : result['year'],
					desc : result['desc']
				},
				'items' : items
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/qq/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},

	playVideo : function(id) {
		appletv.showLoading();
		var playDescUrl = qqPlayApi + '&vid=' + id;
		appletv.makeRequest(playDescUrl, function(data) {
			if (data.indexOf('ip is not authorized') != -1) {
				appletv.showDialog('你的IP地址受限', '无法播放视频');
			} else {
				var urlIndexStart = data.indexOf("\"url\":\"");
				if (urlIndexStart == -1) {
					appletv.logToServer('未知错误,QQ服务器返回数据如下:' + data);
					appletv.showDialog('未知错误,QQ服务器返回数据如下:', data);
				} else {
					urlIndexStart += 7;
					var urlIndexEnd = data.indexOf("\"", urlIndexStart);
					var url = data.substring(urlIndexStart, urlIndexEnd);
					appletv.playMp4(url, '');
				}
			}
		});
	},

	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', tudouClient.loadKeywordsPage,
				'qqClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/qq/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},

};