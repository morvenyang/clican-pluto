var baidumusicClient = {
		
		baidumusicChannels : [ {
			label : "搜索",
			value : 'search'
		},{
			label : "专辑",
			value : 'album'
		}],
		
		baidumusicChannelMap : {
			"search" : {
				label : "搜索",
				value : "search"
			},
			"album" : {
				label : "专辑",
				value : "album"
			}
		},
		
		loadChannelPage:function(){
			var data = {
					'channels' : baidumusicClient.baidumusicChannels,
					'serverurl' : appletv.serverurl
				};
			var templateEJS = new EJS({
				url : appletv.serverurl + '/template/baidumusic/channel.ejs'
			});	
			var xml = templateEJS.render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadIndexPage : function(keyword, page, channelId, queryUrl) {
			appletv.showLoading();
			var channel = this.baidumusicChannelMap[channelId];
			var videos = [];
			if (channelId == 'search') {
				queryUrl = 'http://music.baidu.com/search?key='+encodeURIComponent(keyword);
				baidumusicClient.loadVideoPageByUrl(queryUrl);
			} else {
				if(queryUrl==null){
					queryUrl = 'http://music.baidu.com/album/all?order=hot&style=all&start=0&size=30';
				}else{
					var s = queryUrl.indexOf('start=');
					if(s==-1){
						queryUrl = queryUrl+'&start=0&size=10';
					}else{
						var e = queryUrl.indexOf('&',s);
						queryUrl = queryUrl.substring(0,s+6)+(page-1)*10+queryUrl.substring(e);
					}
				}
				appletv.logToServer(queryUrl);
				appletv.makeRequest(queryUrl, function(content) {
					if (content != null && content.length > 0) {
						var packs = appletv.getSubValuesByTag(content,
								'<li class="clearfix c6 bb', '</li>', 'li');
						for (i = 0; i < packs.length; i++) {
							var pack = packs[i];
							var pic = appletv.substringByData(pack,
									'<img', '>');
							pic = appletv.substringByData(pic,'src="','"');
							var detail = appletv.substringByData(pack,'<h4>','</h4>');
							var title = appletv.substringByData(detail,'title="','"');
							var id = 'http://music.baidu.com'+appletv.substringByData(detail, 'href="','"');
							var video = {
								"title" : title,
								"id" : id,
								"pic" : pic
							};
							videos.push(video);
						}
						baidumusicClient.generateIndexPage(keyword, page, channel,
								videos,queryUrl);
					} else {
						appletv.showDialog('加载失败', '');
					}
				});
			}
			
		},

		generateIndexPage : function(keyword, page, channel, videos,url) {
			if(videos.length==0){
				appletv.showDialog('没有相关音乐专辑','');
				return;
			}
			if(url!=null){
				url = url.replace(new RegExp('&', 'g'), '&amp;');
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
				'channels' : baidumusicClient.baidumusicChannels,
				'serverurl' : appletv.serverurl,
				'videos' : videos,
				'url': url
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/baidumusic/index.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		getCategory: function(content,url){
			var categoryFilterContent = appletv.substringByTag(content,'<div class="select">','</div>','div');
			var categoryFilters = appletv.getSubValuesByTag(categoryFilterContent,'<div class="by-','</div>','div');
			appletv.logToServer(categoryFilters);
			var categoryNames = [];
			var categoryMap = {};
			url = url.replace(new RegExp('&', 'g'), '&amp;');
			var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl};
			for(i=0;i<categoryFilters.length;i++){
				var categoryName = appletv.substringByData(categoryFilters[i],'<h4>','</h4>');
				categoryNames.push(categoryName);
				var categoryValues = [];
				var categoryLis = appletv.getSubValues(categoryFilters[i],'<li','</li>');
				for(j=0;j<categoryLis.length;j++){
					var select = false;
					var categoryLabel;
					if(categoryLis[j].indexOf('class="type-selected"')!=-1){
						select = true
					}
					categoryLabel = appletv.substringByData(categoryLis[j],'<a','</a>');
					categoryLabel = appletv.subIndexString(categoryLabel,'>');
					
					var categoryUrl = 'http://music.baidu.com'+appletv.substringByData(categoryLis[j],'href="','"');
					categoryUrl = categoryUrl.replace(new RegExp('&', 'g'), '&amp;');
					var categoryValue={"categoryLabel":categoryLabel,"categoryUrl":categoryUrl,"select":select};
					categoryValues.push(categoryValue);
				}
				categoryMap[categoryName] = categoryValues;
			}
			return category;
		},
		
		loadCategoryPage: function(url,loading){
			if(loading){
				appletv.showLoading();
			}
			appletv.logToServer('load baidumusic category page');
			appletv.makeRequest(url, function(content) {
				category = baidumusicClient.getCategory(content,url);
				var xml = new EJS({
					url : appletv.serverurl + '/template/baidumusic/category.ejs'
				}).render(category);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		loadVideoPageByUrl : function(url) {
			appletv.showLoading();
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
				pic = appletv.substringByData(htmlContent,'<span class="cover">','</span>');
				pic = appletv.substringByData(pic,'src="','"');
				title =  appletv.substringByData(htmlContent,'<h2 class="album-name">','</h2>');
				desc = '';
				var script = appletv.encode("baidumusicClient.loadVideoPageByUrl('"+url+"');");
				var items = [];
				var itemscontents = appletv.getSubValuesByTag(htmlContent,'<div class="song-item">','</div>','div');
				for(i=0;i<itemscontents.length;i++){
					var a =  appletv.substringByData(itemscontents[i],'<a','</a>');
					var t = appletv.substringByData(a,'title="','"');
					var c = appletv.substringByData(a,'href="/song/','"');
					var item = {
							'title' : t,
							'id' : c
						};
					items.push(item);
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
							'shareurl':shareurl
						},
						items : items
					};
				appletv.setValue('clican.baidumusic.video',video);
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/baidumusic/video.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
		
		loadItemsPage : function() {
			appletv.showLoading();
			appletv.getValue('clican.baidumusic.video',function(video){
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/baidumusic/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		loadSearchPage : function() {
			appletv.showInputTextPage('关键字', '搜索', baidumusicClient.loadKeywordsPage,
					'baidumusicClient.loadKeywordsPage', '');
		},

		loadKeywordsPage : function(q) {
			appletv.showLoading();
			var queryUrl = 'http://tip.tudou.soku.com/hint?q=' + q;
			appletv.logToServer(queryUrl);
			appletv.makeRequest(queryUrl, function(result) {
				appletv.logToServer(result);
				var keywords = JSON.parse(result);
				var data = {
					keywords : keywords,
					serverurl : appletv.serverurl
				};
				var xml = new EJS({
					url : appletv.serverurl + '/template/baidumusic/keywords.ejs'
				}).render(data);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		play : function(vcode) {
			appletv.showLoading();
			var url = 'http://music.baidu.com/song/'+vcode+'/download?__o=%2Fsong%2F'+vcode;
			appletv.makeRequest(url,function(htmlContent){
				if(htmlContent.indexOf('Sorry, our current licence does not allow playback in your current territory')!=-1){
					appletv.showDialog('抱歉我们所有的版权无法在当前国家/地区被播放该音乐','Sorry, our current licence does not allow playback in your current territory');
					return;
				}
				var contents = appletv.getSubValues(htmlContent,'<a data-btndata','>');
				for(i=0;i<contents.length;i++){
					var content = contents[i];
					var href = appletv.substringByData(content,'href="','"');
					if(href!=null&&href.length>0&&href.indexOf('.mp3')!=-1){
						appletv.playMp3('http://music.baidu.com'+href);
						return;
					}
				}
				appletv.showDialog('没有相关音乐','没有相关音乐');
			});
			
		},
}