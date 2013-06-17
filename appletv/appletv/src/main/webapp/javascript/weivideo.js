var weivideoClient = {
	weivideoChannelMap : {
		"1001" : {
			label : "搜索",
			value : '1001',
			path : ''
		},
		"1" : {
			label : "电视剧",
			value : '1',
			path: 'tv'
		},
		"4" : {
			label : "电影",
			value : '4',
			path : 'movie'
		},
		"2" : {
			label : "动漫",
			value : '2',
			path: 'cartoon'
		},
		"3" : {
			label : "综艺",
			value : '3',
			path: 'show'
		},
		"12" : {
			label : "体育",
			value : '12',
			path: 'other/12'
		},
		"11" : {
			label : "娱乐",
			value : '11',
			path: 'other/11'
		},
		"15" : {
			label : "搞笑",
			value : '15',
			path: 'other/15'
		}
	},

	weivideoChannels : [ {
		label : "搜索",
		value : '1001',
		path: ''
	}, {
		label : "电视剧",
		value : '1',
		path: 'tv'
	}, {
		label : "电影",
		value : '4',
		path: 'movie'
	}, {
		label : "动漫",
		value : '2',
		path: 'cartoon'
	}, {
		label : "综艺",
		value : '3',
		path: 'show'
	}, {
		label : "体育",
		value : '12',
		path: 'other/12'
	}, {
		label : "娱乐",
		value : '11',
		path: 'other/12'
	}, {
		label : "搞笑",
		value : '15',
		path: 'other/15'
	} ],

	loadChannelPage : function() {
		var channels = [];
		var originalChannels = weivideoClient.weivideoChannels;
		if (!appletv.isAppleApproveCheck()) {
			channels = originalChannels;
		} else {
			for ( var i = 0; i < originalChannels.length; i++) {
				var channel = originalChannels[i];
				if (channel.value != '1' && channel.value != '4') {
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

	loadIndexPage : function(keyword, page, channelId,queryUrl) {
		appletv.showLoading();
		var channel = this.weivideoChannelMap[channelId];
		var videos = [];
		if (channelId == 1001) {
			queryUrl = 'http://video.weibo.com/s?q='+encodeURIComponent(keyword);
			appletv.makeRequest(queryUrl,function(htmlContent){
				if(htmlContent!=null&&htmlContent.length>0){
					var videoList = appletv.getSubValues(htmlContent,'<div class="sc-sv-item">','</a>');
					for (i = 0; i < videoList.length; i++) {
						var v = videoList[i];
						var pic = appletv.substringByData(v, 'src="', '"');
						var title = appletv.substringByData(v, 'alt="', '"');
						var id = appletv.substringByData(v, 'href="', '"');
						id = id.substring(id.lastIndexOf('/')+1);
						var video = {
							"title" : title,
							"id" : id,
							"pic" : pic
						};
						videos.push(video);
					}
					weivideoClient.generateIndexPage(keyword,
							page, channel, videos);
				}else{
					appletv.showDialog('加载失败', '');
				}
			});
		} else {
			if(queryUrl==null){
				if(channelId=='10'){
					queryUrl = 'http://newvideopc.video.sina.com.cn/movie/fapi/other_data?uid=&q_category='
						+ channelId
						+ '&page=' + page + '&time=' + new Date().getTime();
				}else{
					queryUrl = 'http://newvideopc.video.sina.com.cn/movie/fapi/data?uid=&q_category='
						+ channelId
						+ '&page=' + page + '&time=' + new Date().getTime();
				}
				
			} else {
				queryUrl = queryUrl+'&page=' + page + '&time=' + new Date().getTime();
			}
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
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/weivideo/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	getCategory: function(content,channelId,url,region,tag,year){
		var channel = this.weivideoChannelMap[channelId];
		var categoryFilters = appletv.getSubValuesByTag(content,'<dl>','</dl>','dl');
		var categoryNames = [];
		var categoryMap = {};
		var submitUrl = 'http://newvideopc.video.sina.com.cn/movie/fapi/data?uid=&amp;q_category='
			+ channelId+'&amp;q_tag='+encodeURIComponent(tag)+'&amp;q_region='+encodeURIComponent(region)+'&amp;q_year='+encodeURIComponent(year)
		var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId,"submitUrl":submitUrl};
		for(i=0;i<categoryFilters.length;i++){
			var categoryName = appletv.substringByData(categoryFilters[i],'<dt>','</dt>');
			if(categoryName.indexOf('类型')!=-1||categoryName.indexOf('地区')!=-1||categoryName.indexOf('时间')!=-1){
				categoryNames.push(categoryName);
				var categoryValues = [];
				var categoryLis = appletv.getSubValues(categoryFilters[i],'<a','</a>');
				for(j=0;j<categoryLis.length;j++){
					var select = false;
					var categoryLabel;
					if(categoryLis[j].indexOf('class="cur"')!=-1){
						select = true
					}
					categoryLabel = appletv.subIndexString(categoryLis[j],'>');
					
					var categoryValue=null;
					if(categoryName.indexOf('类型')!=-1){
						categoryValue = {"categoryLabel":categoryLabel,"region":region,"year":year,"tag":categoryLabel,"select":select};
					}else if(categoryName.indexOf('地区')!=-1){
						categoryValue = {"categoryLabel":categoryLabel,"region":categoryLabel,"year":year,"tag":tag,"select":select};
					}else if(categoryName.indexOf('时间'!=-1)){
						categoryValue = {"categoryLabel":categoryLabel,"region":region,"year":categoryLabel,"tag":tag,"select":select};
					}
					categoryValues.push(categoryValue);
				}
				categoryMap[categoryName] = categoryValues;
			}
		}
		return category;
	},
	
	loadCategoryPage: function(channelId,region,tag,year,loading){
		var channel = this.weivideoChannelMap[channelId];
		var url = 'http://newvideopc.video.sina.com.cn/movie/f/'+channel['path'];
		if(loading){
			appletv.showLoading();
		}
		if(region==null||region.length==0){
			region='全部';
		}
		if(tag==null||tag.length==0){
			tag='全部';
		}
		if(year==null||year.length==0){
			year='全部';
		}
		appletv.makeRequest(url+'?q_tag='+encodeURIComponent(tag)+'&q_region='+encodeURIComponent(region)+'&q_year='+encodeURIComponent(year), function(content) {
			category = weivideoClient.getCategory(content,channelId,url,region,tag,year);
			var xml = new EJS({
				url : appletv.serverurl + '/template/weivideo/category.ejs'
			}).render(category);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadVideoPage : function(id) {
		appletv.showLoading();
		var url = 'http://video.weibo.com/detail/' + id;
		appletv.logToServer(url);
		appletv.makeRequest(url, function(htmlContent) {
			if(htmlContent==null||htmlContent.length==0){
				appletv.showDialog('无法加载相关内容','无法加载相关内容');
				return;
			}
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
			score = appletv.substringByData(videoContent, '<em class="mv-sii-num">', '</em>');
			score = appletv.getTextInTag(score);
			var itemscontent = appletv.substringByTag(videoContent,
					'<div class="scs-ace-content clearfix">', '</div>', 'div');
			if(itemscontent==null||itemscontent.length==0){
				var t = title;
				var c = appletv.substringByData(videoContent, 'href="/v/weishipin/', '.htm');
				var item = {
					'title' : t,
					'id' : c
				};
				items.push(item);
			}else{
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
	
	loadSearchPage : function() {
		appletv.logToServer('loadSearchPage');
		appletv.showInputTextPage('关键字', '搜索', weivideoClient.loadKeywordsPage,
				'weivideoClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/weivideo/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},
	play: function(vid){
		if (appletv.simulate == 'atv') {
			appletv.showDialog('跳转中...', '跳转到第三方视频网站');
		}
		var url = 'http://newvideopc.video.sina.com.cn/m/videosource.json?vid='+vid;
		appletv.logToServer(url);
		appletv.makeRequest(url,function(jsonContent){
			appletv.logToServer(jsonContent);
			var data = JSON.parse(jsonContent)['result']['data'];
			if(data.length==0){
				url = 'http://video.weibo.com/v/weishipin/'+vid+'.htm';
				appletv.makeRequest(url,function(htmlContent){
					var playUrl = appletv.substringByData(htmlContent,'name="iframe1" src="','"');
					appletv.logToServer(playUrl);
					sokuClient.openUrl(playUrl);
				});
			}else{
				var playUrl = data[0]['play_page_url'];
				appletv.logToServer(playUrl);
				sokuClient.openUrl(playUrl);
			}
		});
	}
}