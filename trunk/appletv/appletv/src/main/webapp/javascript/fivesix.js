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
			value : "rd",
			url: "http://video.56.com/videolist-type-hot_dir-rd_k-_key-_v-_t-_c-2_charset-utf-8_page-1.html"
		},
		"yl" : {
			label : "娱乐",
			value : "yl",
			url: "http://video.56.com/videolist-type-hot_dir-yl_k-_key-_v-_t-_c-1_charset-utf-8_page-1.html"
		},
		"yc" : {
			label : "原创",
			value : "yc",
			url: "http://video.56.com/videolist-type-hot_dir-yc_k-_key-_v-_t-_c-3_charset-utf-8_page-1.html"
		},
		"zy" : {
			label : "综艺",
			value : "zy",
			url: "http://video.56.com/videolist-v-zyjm_k-_key-_type-_t-_charset-utf-8_page-1.html"
		},
		"yy" : {
			label : "音乐",
			value : "yy",
			url: "http://video.56.com/videolist-type-hot_dir-yy_k-_key-_v-_t-_c-41_charset-utf-8_page-1.html"
		},
		"gx" : {
			label : "搞笑",
			value : "gx",
			url: "http://video.56.com/videolist-type-hot_dir-gx_k-_key-_v-_t-_c-4_charset-utf-8_page-1.html"
		},
		"dm" : {
			label : "动漫",
			value : "dm",
			url: "http://video.56.com/videolist-type-hot_dir-dm_k-_key-_v-_t-_c-8_charset-utf-8_page-1.html"
		},
		"ty" : {
			label : "体育",
			value : "ty",
			url: "http://video.56.com/videolist-type-hot_dir-ty_k-_key-_v-_t-_c-14_charset-utf-8_page-1.html"
		},
		"yx" : {
			label : "游戏",
			value : "yx",
			url: "http://video.56.com/videolist-type-hot_dir-yx_k-_key-_v-_t-_c-26_charset-utf-8_page-1.html"
		},
		"nx" : {
			label : "女性",
			value : "nx",
			url: "http://video.56.com/videolist-type-hot_dir-nx_k-_key-_v-_t-_c-11_charset-utf-8_page-1.html"
		},
		"my" : {
			label : "母婴",
			value : "my",
			url: "http://video.56.com/videolist-type-hot_dir-my_k-_key-_v-_t-_c-34_charset-utf-8_page-1.html"
		},
		"qc" : {
			label : "汽车",
			value : "qc",
			url: "http://video.56.com/videolist-type-hot_dir-qc_k-_key-_v-_t-_c-28_charset-utf-8_page-1.html"
		},
		"kj" : {
			label : "科教",
			value : "kj",
			url: "http://video.56.com/videolist-type-hot_dir-kj_k-_key-_v-_t-_c-10_charset-utf-8_page-1.html"
		},
		"ly" : {
			label : "旅游",
			value : "ly",
			url: "http://video.56.com/videolist-type-hot_dir-ly_k-_key-_v-_t-_c-27_charset-utf-8_page-1.html"
		},
		"videolist-v-mm.html" : {
			label : "美女主播",
			value : "videolist-v-mm.html",
			url: "http://video.56.com/videolist-v-mm_k-_key-_type-_t-_charset-utf-8_page-1.html"
		},
		"videolist-v-pm.html" : {
			label : "微栏目",
			value : "videolist-v-pm.html",
			url: "http://video.56.com/videolist-v-pm_k-_key-_type-_t-_charset-utf-8_page-1.html"
		}
	},
	
	loadChannelPage:function(){
		var channels = [];
		var originalChannels = fivesixClient.fivesixChannels;
		if(!appletv.isAppleApproveCheck()){
			channels = originalChannels;
		}else{
			for(var i=0;i<originalChannels.length;i++){
				var channel = originalChannels[i];
				if(channel.value=='qc'||channel.value=='ly'||channel.value=='kj'){
					channels.push(channel);
				}
			}
		}
		var data = {
				'channels' : channels,
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
					start = start+5;
					queryUrl = queryUrl.substring(0,start)+page+'.html';
				}else{
					start = queryUrl.indexOf('.html');
					if(start>=0){
						queryUrl = queryUrl.substring(0,start)+'_page-'+page+'.html';
					}
				}
			}
			appletv.logToServer(queryUrl);
			appletv.makeRequest(queryUrl, function(content) {
				if (content != null && content.length > 0) {
					var videoContent = appletv.substringByTag(content,'<div class="content','</div>','div');
					var packs = appletv.getSubValuesByTag(videoContent,
							'<dt>', '</dt>', 'dt');
					for (i = 0; i < packs.length; i++) {
						var pack = packs[i];
						var pic = appletv.substringByData(pack,
								'<img', '>');
						pic = appletv.substringByData(pic,'src="','"');
						var title
						if(channelId=='dsj'||channelId=='dy'){
							title = appletv.substringByData(pack, '<a target="_blank" href="', '</a>');
							title = appletv.subIndexString(title,'>');
						}else if(channelId=='zy'){
							title = appletv.substringByData(pack,'alt="','"');
						}else{
							title = appletv.substringByData(pack,'title="','"');
						}
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
	
	getCategory: function(content,channelId,url){
		var channel = this.fivesixChannelMap[channelId];
		var categoryFilterContent = appletv.substringByTag(content,'<div class="tv_type">','</div>','div');
		var categoryFilters = appletv.getSubValuesByTag(categoryFilterContent,'<dl>','</dl>','dl');
		appletv.logToServer(categoryFilters[1]);
		var categoryNames = [];
		var categoryMap = {};
		var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId};
		for(i=0;i<categoryFilters.length;i++){
			var categoryName = appletv.substringByData(categoryFilters[i],'<dt>','</dt>');
			categoryNames.push(categoryName);
			var categoryValues = [];
			var categoryLis = appletv.getSubValues(categoryFilters[i],'<li','</li>');
			for(j=0;j<categoryLis.length;j++){
				var select = false;
				var categoryLabel;
				if(categoryLis[j].indexOf('class="active"')!=-1){
					select = true
				}else{
				}
				categoryLabel = appletv.substringByData(categoryLis[j],'<a','</a>');
				categoryLabel = appletv.subIndexString(categoryLabel,'>');
				var categoryUrl = appletv.substringByData(categoryLis[j],'href="','"');
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
			category = fivesixClient.getCategory(content,channelId,url);
			var xml = new EJS({
				url : appletv.serverurl + '/template/fivesix/category.ejs'
			}).render(category);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	loadVideoPage : function(url,pic,channelId) {
		appletv
				.makeRequest(url,
						function(htmlContent) {
							var title;
							var desc;
							var actor = '-';
							var dctor = '-';
							var area = '-';
							var score = '-';
							var year = '-';
							var shareurl = url;
							var script;
							var playall;
							var items = [];
							if(channelId=='dsj'||channelId=='dy'){
								var videoContent = appletv.substringByTag(htmlContent,
										'<div class="content">', '</div>', 'div');
								
								
								title = appletv.substringByData(videoContent,'title="','"');
								desc = appletv.substringByData(videoContent,'<dd class="h_2" id="videoinfo_l" style="display:none">','<a');
								dctor = appletv.substringByData(videoContent,'导演','</dd>');
								dctor = appletv.getSubValuesByTag(dctor,'<a','</a>','a');
								
								actor = appletv.substringByData(videoContent,'演员','</dd>');
								actor = appletv.getSubValuesByTag(actor,'<a','</a>','a');
								year = appletv.substringByData(videoContent,'上映','</dd>');
								year = appletv.subIndexString(year,'<dd>');
								area = appletv.substringByData(videoContent,'地区','</dd>');
								area = appletv.subIndexString(area,'<dd>');
								script = appletv.encode("youkuClient.loadVideoPage('"+url+"');");
								
								var itemscontent = appletv.substringByTag(htmlContent,'<div id="albumVideos">','</div>','div');
								var dls = appletv.getSubValues(itemscontent,'<dl','</dl>');
								for(var i=0;i<dls.length;i++){
									var dl = dls[i];
									var t = appletv.substringByData(dl,'title="','"');
									var c = appletv.substringByData(dl,'href="','"');
									c = appletv.substringByData(c,'v_','.html');
									var item = {
											'title' : t,
											'id' : c
										};
									items.push(item);
								}
								var mid = appletv.substringByData(url,'opera/','.html');
								playall = 'http://video.56.com/index.php?Controller=Opera&Action=GetOpera&mid='+mid+'&callback=fivesixClient.loadVideos';
								playall=playall.replace(new RegExp('&', 'g'),'&amp;');
								appletv.logToServer('playall:'+playall);
							}else{
								title = appletv.substringByData(htmlContent,'<h1 id="vh_title">','</h1>');
								desc = "";
								var c = appletv.substringByData(url,'v_','.html');
								var item = {
										'title' : title,
										'id' : c
									};
								items.push(item);
							}
							
							var video = {
									'serverurl' : appletv.serverurl,
									script : script,
									playall : playall,
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
										'shareurl':shareurl
									},
									items : items
								};
								if(items.length>1){
									appletv.setValue('clican.fivesix.video',video);
								}
								var xml = new EJS({
									url : appletv.serverurl
											+ '/template/fivesix/video.ejs'
								}).render(video);
								appletv.loadAndSwapXML(xml);
							
						});
	},
	
	loadItemsPage : function(url) {
		appletv.showLoading();
		appletv.logToServer('url:'+url);
		if(url==null||url.length==0){
			appletv.getValue('clican.fivesix.video',function(video){
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/fivesix/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		}else{
			appletv.makeRequest(url,function(content){
				appletv.logToServer(content);
				eval(content);
			});
		}
		
	},
	
	loadVideos:function(videos){
		var datas = videos['data'];
		var items = [];
		for(i=0;i<datas.length;i++){
			var data = datas[i];
			var item = {
					'title' : data['title'],
					'id' : data['vid']
			};
			items.push(item);
		}
		appletv.getValue('clican.fivesix.video',function(video){
			video['items'] = items;
			var xml = new EJS({
				url : appletv.serverurl
						+ '/template/fivesix/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	play : function(vcode) {
		appletv.showLoading();
		var url = 'http://m.56.com/view/id-'+vcode+'.html';
		appletv.makeRequest(url,function(content){
			var vid = appletv.substringByData(content,'vid = "','"').trim();
			var mediaUrl = 'http://vxml.56.com/mobile/'+vid+'/?src=3gapi';
			appletv.makeRequest(mediaUrl,function(jsonContent){
				var json = JSON.parse(jsonContent);
				var url = json['info']['rfiles'][0]['url'];
				appletv.playMp4(url, '');
			});
		});
		
	},
	
	playByVid : function(vid) {
		appletv.showLoading();
		var mediaUrl = 'http://vxml.56.com/mobile/'+vid+'/?src=3gapi';
		appletv.makeRequest(mediaUrl,function(jsonContent){
			var json = JSON.parse(jsonContent);
			var url = json['info']['rfiles'][0]['url'];
			appletv.playMp4(url, '');
		});
	},
}