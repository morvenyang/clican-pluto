var yyetsSearchApi = "http://ziyuan.kehuduan.rryingshi.com:20066/resources";
var yyetsVideoApi = "http://ziyuan.kehuduan.rryingshi.com:20066/resources/";
var yyetsClient = {
	yyetsChannels : [ {
		label : "电影",
		value : "movie"
	}, {
		label : "电视剧",
		value : "tv"
	}, {
		label : "公开课",
		value : "openclass"
	}, {
		label : "搜索",
		value : "search"
	} ],

	yyetsChannelMap : {
		"tv" : {
			label : "电视剧",
			value : "tv"
		},
		"movie" : {
			label : "电影",
			value : "movie"
		},
		"openclass" : {
			label : "公开课",
			value : "openclass"
		},
		"search" : {
			label : "搜索",
			value : "search"
		}
	},

	loadChannelPage:function(){
		var channels = [];
		var originalChannels = yyetsClient.yyetsChannels;
		if(!appletv.isAppleApproveCheck()){
			channels = originalChannels;
		}else{
			for(var i=0;i<originalChannels.length;i++){
				var channel = originalChannels[i];
				if(channel.value=='openclass'){
					channels.push(channel);
				}
			}
		}
		var data = {
				'channels' : channels,
				'serverurl' : appletv.serverurl
			};
		var templateEJS = new EJS({
			url : appletv.serverurl + '/template/yyets/channel.ejs'
		});	
		var xml = templateEJS.render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadIndexPage : function(keyword, page, channelId, url) {
		appletv.showLoading();
		if (channelId == 'search') {
			url = 'http://ziyuan.kehuduan.rryingshi.com:20066/search/resource/'
					+ encodeURIComponent(keyword);
		} else {
			if(url==null){
				url = yyetsSearchApi + "?c=" + channelId + "&a=all&k=all&page=" + page
				+ "&s=views";
			}
		}

		var channel = this.yyetsChannelMap[channelId];
		appletv
				.makeRequest(
						url,
						function(htmlContent) {
							if (htmlContent == null) {
								return;
							}
							var videos = [];
							if (channelId == 'search') {
								var idAndTitles = appletv
										.findall(
												'<h2><a href="/resources/(\\d*)">([^<]*)</a>',
												htmlContent);
								for ( var i = 0; i < idAndTitles.length; i++) {
									id = idAndTitles[i][0];
									title = idAndTitles[i][1];
									var video = {
										"title" : title,
										"id" : id,
										"pic" : ''
									};
									videos.push(video);
								}
							} else {
								var imgs = appletv
										.findall(
												'<div class="img"><a href="/resources/\\d*" class="resources-list-resource-url"><img src="([^"]*)"',
												htmlContent);
								var idAndTitles = appletv
										.findall(
												'<h2 class="fl"><a href="/resources/(\\d*)" class="resources-list-resource-url">([^<]*)</a>',
												htmlContent);
								for ( var i = 0; i < imgs.length; i++) {
									pic = imgs[i][0];
									id = idAndTitles[i][0];
									title = idAndTitles[i][1];
									var video = {
										"title" : title,
										"id" : id,
										"pic" : pic
									};
									videos.push(video);
								}
							}

							yyetsClient.generateIndexPage(keyword, page,
									channel, videos,url);
						});
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
		url = url.replace(new RegExp('&', 'g'), '&amp;');
		var data = {
			'page' : page,
			'channel' : channel,
			'keyword' : keyword,
			'begin' : begin,
			'end' : end,
			'channels' : yyetsClient.yyetsChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos,
			'url' : url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/yyets/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	getCategory: function(url,channelId){
		var categoryNames = ["排序","地区","类型"];
		var areas = ["全部","大陆","美国","香港","韩国","日本","英国","台湾","俄罗斯","其他","加拿大","印度","台湾","大陆","德国","意大利","新加坡","法国","泰国","澳大利亚","西班牙","越南"];
		var types = ["全部","传记","体育","剧情","励志","历史","冒险","动画","悬疑","惊悚","歌舞","偶像","爱情","罪案","真人秀","纪录","青春","科幻","魔幻","丧尸","动作","幻想","战争","喜剧","恐怖","血腥","西部","制造","暴力","医务","古装","灾难","史诗","谍战","幽默","生活","讽刺","律政","枪战","社会人文","综艺","科教","心理学","法律","物理","美术","计算机","金融","音乐","童话"];
		var categoryMap = {};
		var categorySortValues = [];
		var categoryAreaValues = [];
		var categoryTypeValues = [];
		var csort = appletv.subIndexString(url,'s=');
		var carea = appletv.substringByData(url,'a=','&');
		var ctype = appletv.substringByData(url,'k=','&');
		//sort
		categorySortValues.push({"categoryLabel":"更新日期","categoryUrl":url.replace(csort,'updated_at'),"select":csort=='updated_at'});
		categorySortValues.push({"categoryLabel":"发布日期","categoryUrl":url.replace(csort,'publish_date'),"select":csort=='publish_date'});
		categorySortValues.push({"categoryLabel":"排行","categoryUrl":url.replace(csort,'score'),"select":csort=='score'});
		categorySortValues.push({"categoryLabel":"点击率","categoryUrl":url.replace(csort,'views'),"select":csort='views'});
		categoryMap["排序"] = categorySortValues;
		//area
		for(var i=0;i<areas.length;i++){
			var area = areas[i];
			if(area=='全部'){
				categoryAreaValues.push({"categoryLabel":"全部","categoryUrl":url.replace(carea,'all'),"select":carea=='all'});
			}else{
				categoryAreaValues.push({"categoryLabel":area,"categoryUrl":url.replace(carea,encodeURIComponent(area)),"select":carea==encodeURIComponent(area)});
			}
		}
		categoryMap["地区"] = categoryAreaValues;
		//type
		for(var i=0;i<types.length;i++){
			var type = types[i];
			if(type=='全部'){
				categoryTypeValues.push({"categoryLabel":"全部","categoryUrl":url.replace(ctype,'all'),"select":ctype=='all'});
			}else{
				categoryTypeValues.push({"categoryLabel":type,"categoryUrl":url.replace(ctype,encodeURIComponent(type)),"select":ctype==encodeURIComponent(type)});
			}
		}
		categoryMap["类型"] = categoryTypeValues;
		var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId};
		return category;
	},
	
	loadCategoryPage: function(url,channelId,loading){
		if(loading){
			appletv.showLoading();
		}
		url = url.replace(new RegExp('&', 'g'), '&amp;');
		var category = yyetsClient.getCategory(url,channelId);
		var xml = new EJS({
			url : appletv.serverurl + '/template/yyets/category.ejs'
		}).render(category);
		appletv.loadAndSwapXML(xml);
		
	},

	loadVideoPage : function(id) {
		appletv.showLoading();
		try{
			var url = yyetsVideoApi + id;
			var s1 = new Date();
			appletv.makeRequest(url, function(htmlContent) {
				var s2 = new Date();
				if (htmlContent == null) {
					return;
				}
				var video;
				var title = appletv.substringByData(htmlContent, '<h2>', '</h2>');
				var pic = appletv.substringByData(htmlContent, '<img src="', '"');
				var score = appletv.substringByData(htmlContent, '<font class="point">',
						'</font>');
				var year = appletv.substringByData(htmlContent, '<li><span>年代：</span>',
						'</li>');
				var type = appletv.substringByData(htmlContent, '<li><span>类型：</span>',
						'</li>');
				var dctor = appletv.substringByData(htmlContent, '<li><span>导演：</span>',
						'</li>');
				var actor = appletv.substringByData(htmlContent, '<li><span>主演：</span>',
						'</li>');
				var desc = appletv.substringByData(htmlContent, '<div class="f5">',
						'</div>');
				var items = [];
				var formatcontent = appletv.substringByData(htmlContent,
						'<div class="desc_tit">', '</div>');
				var resourceitems = appletv.getSubValues(htmlContent,
						'<li class="resource-item"', '</li>');
				var seasoncontent = appletv.substringByData(htmlContent,
						'<div class="season_tit">', '</div>');

				var formats = appletv.getSubValues(formatcontent, 'data-value="',
						'"');

				index = formats.indexOf("all");
				var seasonformatmap = {};
				k = 0;
				if (index >= 0) {
					formats.splice(index, 1);
				}
				var seasons = appletv.getSubValues(seasoncontent, 'data-value="',
						'"');

				index = seasons.indexOf("all");
				if (index >= 0) {
					seasons.splice(index, 1);
				}
				for (i = 0; i < seasons.length; i++) {
					seasonformatmap[seasons[i]] = {};
					for (j = 0; j < formats.length; j++) {
						seasonformatmap[seasons[i]][formats[j]] = [];
					}
				}
				for (i = 0; i < resourceitems.length; i++) {
					resourceitem = resourceitems[i];
					dataformat = appletv.substringByData(resourceitem, 'data-format="',
							'"');
					dataseason = appletv.substringByData(resourceitem, 'data-season="',
							'"');
					datafile = 'ed2k'
							+ appletv.substringByData(resourceitem, 'href="ed2k', '"');
					if (datafile == 'ed2k') {
						datatitle = '资源URL无法点播';
					} else {
						datatitle = appletv.substringByData(datafile, "file|", "|");
					}

					data = {
						"url" : datafile,
						"title" : decodeURIComponent(datatitle)
					};
					seasonformatmap[dataseason][dataformat].push(data);
				}
				var yyetsVideo = {
					'seasonformatmap' : seasonformatmap,
					'formats' : formats,
					'title' : title,
					'pic' : pic
				};
				appletv.setValue('clican.yyets.video', yyetsVideo);
				var video = {
					'serverurl' : appletv.serverurl,
					script : appletv.encode("yyetsClient.loadVideoPage("+id+");"),
					video : {
						'id' : id,
						actor : actor,
						area : '',
						type : type,
						dctor : dctor,
						pic : pic,
						score : score,
						title : title,
						year : year,
						desc : desc
					},
					'seasons' : seasons
				};
				var s3 = new Date();
				var xml = new EJS({
					url : appletv.serverurl + '/template/yyets/video.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
				var s4 = new Date();
				var t1 = s1.getTime();
				var t2 = s2.getTime();
				var t3 = s3.getTime();
				var t4 = s4.getTime();
				appletv.logToServer((t2-t1)+','+(t3-t2)+','+(t4-t3));
			});
		}catch(e){
			appletv.logToServer('Error occured yyets.loadVideoPage ' + e);
		}
		
	},

	listVideosInSeason : function(season, format) {
		appletv.showLoading();
		appletv.getValue('clican.yyets.video', function(yyetsVideo) {
			try{
				var yvc = yyetsVideo;
				var formats = yvc['formats'];
				var newformats = [];

				var seasonformatmap = yvc['seasonformatmap'];
				var title = yvc['title'];
				var pic = yvc['pic'];
				var formatmap = seasonformatmap[season];

				for (i = 0; i < formats.length; i++) {
					tempformat = formats[i];
					if (formatmap[tempformat].length > 0) {
						newformats.push(tempformat);
					}
				}

				var items;
				var currentIndex = 0;
				if (format == null || format.length == 0) {
					format = newformats[0];
				}else{
					for(i=0;i<newformats.length;i++){
						if(newformats[i]==format){
							currentIndex = i;
							break;
						}
					}
				}
				items = formatmap[format];
				var video = {
					'serverurl' : appletv.serverurl,
					"title" : title,
					"pic" : pic,
					"items" : items,
					"formats" : newformats,
					"season" : season,
					"currentIndex" : currentIndex
				};
				var xml = new EJS({
					url : appletv.serverurl + '/template/yyets/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			}catch(e){
				appletv.logToServer('Error occured yyets.listVideosInSeason ' + e);
			}
			
		});
	},

	loadSearchPage : function() {
		appletv.showInputTextPage('关键字', '搜索', yyetsClient.loadKeywordsPage,
				'yyetsClient.loadKeywordsPage', '');
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
				url : appletv.serverurl + '/template/yyets/keywords.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		});
	},
	
	play:function(url){
		appletv.showLoading();
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
}