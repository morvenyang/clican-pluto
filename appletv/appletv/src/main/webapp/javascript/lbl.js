var lblClient = {
	lblChannels : [ {
		label : "搜索",
		value : 'category/search'
	}, {
		label : "最新电影",
		value : 'category/movie'
	}, {
		label : "最新电视",
		value : 'category/television'
	}, {
		label : "最新动画",
		value : 'category/movie/zxdh'
	}, {
		label : "综艺娱乐",
		value : 'category/video'
	}, {
		label : "大陆电影",
		value : 'category/movie/dldy'
	}, {
		label : "港台电影",
		value : 'category/movie/gtdy'
	}, {
		label : "日韩电影",
		value : 'category/movie/rhdy'
	}, {
		label : "欧美电影",
		value : 'category/movie/omdy'
	}, {
		label : "大陆剧集",
		value : 'category/television/dljj'
	}, {
		label : "港台剧集",
		value : 'category/television/gtjj'
	}, {
		label : "欧美剧集",
		value : 'category/television/omjj'
	}, {
		label : "日韩剧集",
		value : 'category/television/rhjj'
	} ],

	lblChannelMap : {
		'category/search' : {
			label : "搜索",
			value : 'category/search'
		},
		'category/movie' : {
			label : "最新电影",
			value : 'category/movie'
		},
		'category/television' : {
			label : "最新电视",
			value : 'category/television'
		},
		'category/movie/zxdh' : {
			label : "最新动画",
			value : 'category/movie/zxdh'
		},
		'category/video' : {
			label : "综艺娱乐",
			value : 'category/video'
		},
		'category/movie/dldy' : {
			label : "大陆电影",
			value : 'category/movie/dldy'
		},
		'category/movie/gtdy' : {
			label : "港台电影",
			value : 'category/movie/gtdy'
		},
		'category/movie/rhdy' : {
			label : "日韩电影",
			value : 'category/movie/rhdy'
		},
		'category/movie/omdy' : {
			label : "欧美电影",
			value : 'category/movie/omdy'
		},
		'category/television/dljj' : {
			label : "大陆剧集",
			value : 'category/television/dljj'
		},
		'category/television/gtjj' : {
			label : "港台剧集",
			value : 'category/television/gtjj'
		},
		'category/television/omjj' : {
			label : "欧美剧集",
			value : 'category/television/omjj'
		},
		'category/television/rhjj' : {
			label : "日韩剧集",
			value : 'category/television/rhjj'
		}
	},

	loadChannelPage:function(){
		var data = {
				'channels' : lblClient.lblChannels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/lbl/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.lblChannelMap[channelId];
		var videos = [];
		var queryUrl;
		if(channelId=='category/search'){
			queryUrl = 'http://www.longbuluo.com/index.php?s='+encodeURIComponent(keyword)+'&submit=%E5%85%A8%E7%AB%99%E6%90%9C%E7%B4%A2'
		}else{
			queryUrl = 'http://www.longbuluo.com/' + channel['value']
			+ '/page/' + page;
		}

		var s1 = new Date();
		appletv.makeRequest(queryUrl, function(content) {
			if (content != null && content.length > 0) {
				var packs = appletv.getSubValues(content, '<h4>', '</h4>');
				for (i = 0; i < packs.length; i++) {
					var pack = packs[i];
					var title = appletv.substringByData(pack, 'title="', '"');
					var id = appletv.substringByData(pack, '<a href="', '"');
					var video = {
						"title" : title,
						"id" : id
					};
					videos.push(video);
				}
				lblClient.loadPics(videos, function(resultVideos){
					lblClient.generateIndexPage(keyword, page, channel,
							resultVideos);
					var s2 = new Date();
					appletv.logToServer('total:'
							+ (s2.getTime() - s1.getTime()))
				});
			} else {
				appletv.showDialog('加载失败', '');
			}
		});

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
			'channels' : lblClient.lblChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/lbl/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadPics : function(videos, callback) {
		var urls = [];
		for(var i=0;i<videos.length;i++){
			urls.push(videos[i]['id']);
		}

		appletv.makePostRequest(appletv.remoteserverurl+'/ctl/lbl/getImages.json',JSON.stringify(urls), function(jsonContent) {
			var images = JSON.parse(jsonContent);
			for(var i=0;i<images.length;i++){
				videos[i]['pic'] = images[i];
			}
			callback(videos);
		});
	},

	loadVideoPage : function(id) {
		appletv.showLoading();
		var url = id;
		appletv.makeRequest(url, function(htmlContent) {
			if(htmlContent==null||htmlContent.length==0){
				appletv.showDialog('无法加载相关内容','无法加载相关内容');
				return;
			}
			var actor = '-';
			var dctor = '-';
			var area = '-';
			var score = '-';
			var year = '-';
			var shareurl = url;
			var desc;
			var entry = appletv.substringByTag(htmlContent,
					'<div class="entry">', '</div>', 'div');

			var ps = appletv.getSubValues(entry, '<p>', '</p>');
			desc = appletv.getTextInTag(ps[0]);
			pic = appletv.substringByData(entry, 'src="', '"');
			title = appletv.substringByData(htmlContent, '<h2>', '</h2>');
			index1 = title.indexOf("《");
			index2 = title.indexOf("》");
			if (index1 >= 0 && index2 >= 0) {
				title = title.substring(index1 + 1, index2);
			}
			area = appletv.substringByData(ps[2], '制片国家/地区', '<br');
			if(area==null||area.length==0){
				area = appletv.substringByData(ps[2], '制片地区', '<br');
			}
			area=area.replace(':','').replace('：','').trim();
			year = appletv.substringByData(ps[2], '上映日期', '<br');
			if(year==null||year.length==0){
				year = appletv.substringByData(ps[2], '出品时间', '<br');
			}
			year = year.replace(':','').replace('：','').trim();
			actor = appletv.subIndexString(ps[2], '主演');
			if(actor.indexOf('<')>=0){
				actor=actor.substring(0,actor.indexOf('<'));
			}
			actor = actor.replace(':','').replace('：','').trim();
			dctor = appletv.substringByData(ps[2], '导演', '<br');
			dctor = dctor.replace(':','').replace('：','').trim();
			
			var items = [];
			var urls = appletv.getSubValues(htmlContent,'<a href="thunder://','</a>');
			for(i=0;i<urls.length;i++){
				url = '<a href="thunder://'+urls[i]+'</a>'
				var c = appletv.substringByData(url, 'href="', '"');
				var t = appletv.substringByData(url, '>','<').trim();
				if(c==null||c.length==0){
					continue;
				}
				var item = {
					'title' : t,
					'encodetitle' : encodeURIComponent(t),
					'id' : c
				};
				items.push(item);
			}
			
			urls = appletv.getSubValues(htmlContent,'<li>thunder://','</li>');
			for(i=0;i<urls.length;i++){
				url = 'thunder://'+urls[i];
				var c = url;
				var t = title + ' 第'+(i+1)+'集';
				if(c==null||c.length==0){
					continue;
				}
				var item = {
					'title' : t,
					'encodetitle' : encodeURIComponent(t),
					'id' : c
				};
				items.push(item);
			}
			
			urls = appletv.getSubValues(htmlContent,'<a href="http://kuai.xunlei.com','</a>');
			for(i=0;i<urls.length;i++){
				url = '<a href="http://kuai.xunlei.com'+urls[i]+'</a>'
				var c = appletv.substringByData(url, 'href="', '"');
				var t = appletv.substringByData(url, '>','<').trim();
				if(c==null||c.length==0){
					continue;
				}
				var item = {
					'title' : t,
					'encodetitle' : encodeURIComponent(t),
					'id' : c
				};
				items.push(item);
			}
			
			urls = appletv.getSubValues(htmlContent,'<a href="ed2k://','</a>');
			for(i=0;i<urls.length;i++){
				url = '<a href="ed2k://'+urls[i]+'</a>'
				var c = appletv.substringByData(url, 'href="', '"');
				var t = appletv.substringByData(url, '>','<').trim();
				if(c==null||c.length==0){
					continue;
				}
				var item = {
					'title' : t,
					'encodetitle' : encodeURIComponent(t),
					'id' : c
				};
				items.push(item);
			}
			
			var video = {
				'serverurl' : appletv.serverurl,
				script : appletv.encode("lblClient.loadVideoPage('" + id
						+ "');"),
				video : {
					'id' : id,
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
			appletv.setValue('clican.lbl.video', video);
			var xml = new EJS({
				url : appletv.serverurl + '/template/lbl/video.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},

	loadItemsPage : function() {
		appletv.showLoading();
		try {
			appletv.getValue('clican.lbl.video', function(video) {
				var xml = new EJS({
					url : appletv.serverurl + '/template/lbl/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		} catch (e) {
			appletv.logToServer('Error occured lbl.loadItemsPage ' + e);
		}

	},
	
	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', lblClient.loadKeywordsPage,
				'lblClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/lbl/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	play : function(id, title) {
		appletv.showLoading();
		var url = id;
		if (url.indexOf('http://kuai.xunlei.com') >= 0) {
			appletv.makeRequest(url, function(content) {
				var c2 = appletv.substringByTag(content, '<span class="c_2">',
						'</span>', 'span');
				var href = appletv.substringByData(c2, 'href="', '"');
				var encodeUrl = href.replace(new RegExp('&', 'g'),'&amp;');
				var options = [];
				options.push({"title":"迅雷云点播/迅雷云转码播放","script":"xunleiClient.play('"+encodeUrl+"','');"});
				if(appletv.serverurl.indexOf('clican.org') == -1){
					options.push({"title":"迅雷离线下载播放/iOS本地转码","script":"xunleiClient.offlinePlay('"+encodeUrl+"');"});
				}else{
					options.push({"title":"迅雷离线下载播放/iOS本地转码","script":"appletv.showDialog('该功能需要iOS本地服务器的支持','相关安装配置请参见http://clican.org');"});
				}
				appletv.showOptionPage('播放源选择','',options);
			});
		} else {
			var encodeUrl = url.replace(new RegExp('&', 'g'),'&amp;');
			var options = [];
			options.push({"title":"迅雷云点播/迅雷云转码播放","script":"xunleiClient.play('"+encodeUrl+"','');"});
			if(appletv.serverurl.indexOf('clican.org') == -1){
				options.push({"title":"迅雷离线下载播放/iOS本地转码MKV","script":"xunleiClient.offlinePlay('"+encodeUrl+"');"});
			}else{
				options.push({"title":"迅雷离线下载播放/iOS本地转码MKV","script":"appletv.showDialog('该功能需要iOS本地服务器的支持','相关安装配置请参见http://clican.org');"});
			}
			appletv.showOptionPage('播放源选择','',options);
		}
	},
}