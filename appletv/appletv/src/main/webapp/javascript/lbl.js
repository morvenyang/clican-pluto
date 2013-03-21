var lblClient = {
	lblChannels : [ {
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

	loadIndexPage : function(keyword, page, channelId) {
		appletv.showLoading();
		var channel = this.lblChannelMap[channelId];
		var videos = [];
		var queryUrl = 'http://www.longbuluo.com/' + channel['value']
				+ '/page/' + page;

		var s1 = new Date();
		appletv.makeRequest(queryUrl, function(content) {
			if (content != null && content.length > 0) {
				var packs = appletv.getSubValues(content, '<h4>', '</h4>');
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
				lblClient.loadPics(videos, 0,
						function(videos) {
							lblClient.generateIndexPage(keyword, page, channel,
									videos);
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
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/lbl/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadPics : function(videos, index, callback) {
		if (index == videos.length) {
			callback(videos);
		} else {
			var url = videos[index]['id'];
			appletv.makeRequest(url, function(content) {
				var entry = appletv.substringByTag(content,
						'<div class="entry">', '</div>', 'div');
				videos[index]['pic'] = appletv.substring(entry, 'src="', '"');
				lblClient.loadPics(videos, index + 1, callback);
			});
		}
	},

	loadVideoPage : function(id) {
		appletv.showLoading();
		var url = id;
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
			var entry = appletv.substringByTag(htmlContent,
					'<div class="entry">', '</div>', 'div');

			var ps = appletv.getSubValues(entry, '<p>', '</p>');
			desc = appletv.getTextInTag(ps[0]);
			pic = appletv.substring(ps[1], 'src="', '"');
			title = appletv.substring(ps[1], 'title="', '"');
			index1 = title.indexOf("《");
			index2 = title.indexOf("》");
			if (index1 >= 0 && index2 >= 0) {
				title = title.substring(index1 + 1, index2);
			}
			area = appletv.substring(ps[2], '制片国家/地区', '<br');
			if(area==null||area.length==0){
				area = appletv.substring(ps[2], '制片地区', '<br');
			}
			area=area.replace(':','').replace('：','').trim();
			year = appletv.substring(ps[2], '上映日期', '<br');
			if(year==null||year.length==0){
				year = appletv.substring(ps[2], '出品时间', '<br');
			}
			year = year.replace(':','').replace('：','').trim();
			actor = appletv.subIndexString(ps[2], '主演');
			if(actor.indexOf('<')>=0){
				actor=actor.substring(0,actor.indexOf('<'));
			}
			actor = actor.replace(':','').replace('：','').trim();
			dctor = appletv.substring(ps[2], '导演', '<br');
			dctor = dctor.replace(':','').replace('：','').trim();
			
			var items = [];
			for (i = 4; i < ps.length; i++) {
				var c = appletv.substring(ps[i], 'href="', '"');
				var t = appletv.substring(ps[i], 'target="_blank">', '</a>');
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
			var ols = appletv.substring(entry,'<ol>','</ol>');
			if(ols!=null&&ols.length!=0){
				var hrefs = appletv.getSubValues(ols,'<li>','</li>');
				for (i = 0; i < hrefs.length; i++) {
					var c =  hrefs[i];
					var t = title +' 第'+(i+1)+'集';
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

	play : function(id, title) {
		appletv.showLoading();
		var url = id;
		if (url.indexOf('http://kuai.xunlei.com') >= 0) {
			appletv.makeRequest(url, function(content) {
				var c2 = appletv.substringByTag(content, '<span class="c_2">',
						'</span>', 'span');
				var href = appletv.substring(c2, 'href="', '"');
				xunleiClient.play(href, title);
			});
		} else {
			xunleiClient.play(url, title);
		}
	},
}