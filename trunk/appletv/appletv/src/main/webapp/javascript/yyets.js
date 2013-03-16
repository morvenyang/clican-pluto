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
		label : "纪录片",
		value : "documentary"
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
		"documentary" : {
			label : "纪录片",
			value : "documentary"
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

	loadIndexPage : function(keyword, page, channelId) {
		var url = yyetsSearchApi + "?c=" + channelId + "&page=" + page+"&s=views";
		appletv.logToServer(url);
		var channel = this.yyetsChannelMap[channelId];
		appletv
				.makeRequest(
						url,
						function(htmlContent) {
							if (htmlContent == null) {
								return;
							}
							var videos = [];
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
							;
							yyetsClient.generateIndexPage(keyword, page,
									channel, videos);
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
			'channels' : yyetsClient.yyetsChannels,
			'serverurl' : appletv.serverurl,
			'videos' : videos
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/yyets/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},

	loadVideoPage : function(id) {
		// atv.loadXML(appletv.makeDialog('加载中...','Loading...'));
		var url = yyetsVideoApi + id;
		appletv.logToServer(url);
		appletv.makeRequest(url, function(htmlContent) {
			if (htmlContent == null) {
				return;
			}
			var video;
			var title = appletv.substring(htmlContent, '<h2>', '</h2>');
			var pic = appletv.substring(htmlContent, '<img src="', '"');
			var score = appletv.substring(htmlContent, '<font class="point">',
					'</font>');
			var year = appletv.substring(htmlContent, '<li><span>年代：</span>',
					'</li>');
			var type = appletv.substring(htmlContent, '<li><span>类型：</span>',
					'</li>');
			var dctor = appletv.substring(htmlContent, '<li><span>导演：</span>',
					'</li>');
			var actor = appletv.substring(htmlContent, '<li><span>主演：</span>',
					'</li>');
			var desc = appletv.substring(htmlContent, '<div class="f5">',
					'</div>');
			var items = [];
			var formatcontent = appletv.substring(htmlContent,
					'<div class="desc_tit">', '</div>');
			var resourceitems = appletv.getSubValues(htmlContent,
					'<li class="resource-item"', '</li>');
			var seasoncontent = appletv.substring(htmlContent,
					'<div class="season_tit">', '</div>');

			var formats = appletv.getSubValues(formatcontent, 'data-value="',
					'"');

			index = formats.indexOf("all");
			var formatseasonmap = {};
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
			for (i = 0; i < formats.length; i++) {
				formatseasonmap[formats[i]] = {};
				for (j = 0; j < seasons.length; j++) {
					formatseasonmap[formats[i]][seasons[j]] = [];
				}
			}
			for (i = 0; i < resourceitems.length; i++) {
				resourceitem = resourceitems[i];
				dataformat = appletv.substring(resourceitem, 'data-format="',
						'"');
				dataseason = appletv.substring(resourceitem, 'data-season="',
						'"');
				datafile = 'ed2k'
						+ appletv.substring(resourceitem, 'href="ed2k', '"');
				if (datafile == 'ed2k') {
					datatitle = '资源URL无法点播';
				} else {
					datatitle = appletv.substring(datafile, "file|", "|");
				}
				
				data = {
					"url" : datafile,
					"title" : decodeURIComponent(datatitle)
				};
				formatseasonmap[dataformat][dataseason].push(data);
			}
			var yyetsVideoCache = {
				'formatseasonmap' : formatseasonmap,
				'seasons' : seasons,
				'title' : title,
				'pic' : pic
			};
			appletv.setValue('yyetsVideo', yyetsVideoCache);
			var video = {
				'serverurl' : appletv.serverurl,
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
				'formats' : formats
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/yyets/video.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},

	listVideosInFormat : function(format, season) {
		appletv.getValue('yyetsVideo', function(yyetsVideoCache) {
			var yvc = yyetsVideoCache;
			var seasons = yvc['seasons'];
			var newseasons = [];

			var formatseasonmap = yvc['formatseasonmap'];
			var title = yvc['title'];
			var pic = yvc['pic'];
			var seasonmap = formatseasonmap[format];

			for (i = 0; i < seasons.length; i++) {
				tempseason = seasons[i];
				if (seasonmap[tempseason].length > 0) {
					newseasons.push(tempseason);
				}
			}

			var items;
			var currentIndex = 0;
			if (season == null || season.length == 0) {
				season = newseasons[0];
				items = seasonmap[season];
			}
			var video = {
				'serverurl' : appletv.serverurl,
				"title" : title,
				"pic" : pic,
				"items" : items,
				"seasons" : newseasons,
				"format" : format,
				"currentIndex" : currentIndex
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/yyets/videoItems.ejs'
			}).render(video);
			appletv.loadAndSwapXML(xml);
		});
	},
}