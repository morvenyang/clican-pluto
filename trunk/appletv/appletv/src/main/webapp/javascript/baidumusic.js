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
		
		loadIndexPage : function(keyword, page, channelId, queryUrl) {
			appletv.showLoading();
			var channel = this.baidumusicChannelMap[channelId];
			var videos = [];
			if (channelId == 'search') {
				
			} else {
				if(queryUrl==null){
					queryUrl = 'http://music.baidu.com/album/all?order=time&style=all&start=0&size=10';
				}else{
					var s = queryUrl.indexOf('start=');
					var e = queryUrl.indexOf('&',s);
					queryUrl = queryUrl.substring(0,s+6)+page+queryUrl.substring(e);
				}
				appletv.logToServer(queryUrl);
				appletv.makeRequest(queryUrl, function(content) {
					if (content != null && content.length > 0) {
						var packs = appletv.getSubValuesByTag(content,
								'<li class="clearfix c6 bb">', '</li>', 'li');
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
					var c = appletv.substringByData(a,'href="','"');
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
		
		play : function(url) {
			appletv.showLoading();
		},
}