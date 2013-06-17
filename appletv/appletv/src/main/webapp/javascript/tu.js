var tuClient ={
		tuChannelMap : {
			"1001" : {
				label : "搜索",
				value : 1001
			},
			"15" : {
				label : "电影",
				value : 15
			},
			"16" : {
				label : "电视",
				value : 16
			},
			"7" : {
				label : "动画片",
				value : 7
			},
			"8" : {
				label : "综艺片",
				value : 8
			}
		},
		tuChannels : [ {
			label : "搜索",
			value : 1001
		},  {
			label : "电影",
			value : 15
		}, {
			label : "电视",
			value : 16
		}, {
			label : "动画片",
			value : 7
		}, {
			label : "综艺片",
			value : 8
		}],
		
		loadChannelPage:function(){
			var channels = [];
			var originalChannels = tuClient.tuChannels;
			if(!appletv.isAppleApproveCheck()){
				channels = originalChannels;
			}else{
				for(var i=0;i<originalChannels.length;i++){
					var channel = originalChannels[i];
					if(channel.value!=16&&channel.value!=15){
						channels.push(channel);
					}
				}
			}
			var data = {
					'channels' : channels,
					'serverurl' : appletv.serverurl
				};
			var templateEJS = new EJS({
				url : appletv.serverurl + '/template/tu/channel.ejs'
			});	
			var xml = templateEJS.render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadIndexPage : function(keyword, page, channelId) {
			appletv.showLoading();
			var channel = this.tuChannelMap[channelId];
			var videos = [];
			if (channelId == 1001) {
				var encodeUrl = appletv.remoteserverurl+'/ctl/util/encode.do?content='+encodeURIComponent(keyword)+'&encoding=GBK';
				appletv.makeRequest(encodeUrl,function(result){
					queryUrl = 'http://www.2tu.cc/search.asp?searchword='+result;
					appletv.logToServer(queryUrl);
					appletv.makeRequest(queryUrl,function(content){
						if (content != null && content.length > 0) {
							var itemscontent = appletv.substringByData(content,'<ul class="piclist">', '</ul>');
							var items = appletv.getSubValuesByTag(itemscontent,
									'<li>', '</li>', 'li');
							appletv.logToServer('items size='+items.length);
							for (i = 0; i < items.length; i++) {
								var item = items[i];
								var pic = appletv.substringByData(item,
										'<img src="', '"');
								var title = appletv.substringByData(item, 'alt="', '"');
								var id = 'http://www.2tu.cc'+appletv.substringByData(item, '<a href="', '"');
								var video = {
									"title" : title,
									"id" : id,
									"pic" : pic
								};
								videos.push(video);
							}
							tuClient.generateIndexPage(keyword, page, channel,videos);
						} else {
							appletv.showDialog('加载失败', '');
						}
					});
				});
			} else {
				var queryUrl;
				if(page==1){
					queryUrl = 'http://www.2tu.cc/GvodHtml/'+channelId+'.html';
				}else{
					queryUrl = 'http://www.2tu.cc/GvodHtml/'+channelId+'_'+page+'.html';
				}
				appletv.makeRequest(queryUrl, function(content) {
					if (content != null && content.length > 0) {
						var itemscontent = appletv.substringByData(content,'<ul class="piclist">', '</ul>');
						var items = appletv.getSubValuesByTag(itemscontent,
								'<li>', '</li>', 'li');
						for (i = 0; i < items.length; i++) {
							var item = items[i];
							var pic = appletv.substringByData(item,
									'<img src="', '"');
							var title = appletv.substringByData(item, 'alt="', '"');
							var id = 'http://www.2tu.cc'+appletv.substringByData(item, '<a href="', '"');
							var video = {
								"title" : title,
								"id" : id,
								"pic" : pic
							};
							videos.push(video);
						}
						tuClient.generateIndexPage(keyword, page, channel,videos);
					} else {
						appletv.showDialog('加载失败', '');
					}
				});
			
			}

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
				'channels' : tuClient.tuChannels,
				'serverurl' : appletv.serverurl,
				'videos' : videos
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/tu/index.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage : function(url) {
			appletv.showLoading();
			appletv
					.makeRequest(url,
							function(htmlContent) {
								if(htmlContent==null||htmlContent.length==0){
									appletv.showDialog('无法加载相关内容','无法加载相关内容');
									return;
								}
								var playInfo = appletv.substringByTag(htmlContent,'<div id="playinfo">','</div>','div');
								var title = appletv.substringByData(playInfo,'alt="','"');
								var desc = appletv.substringByTag(htmlContent,'<div class="about_t">','</div>','div');
								desc = appletv.getTextInTag(desc);
								var pic = appletv.substringByData(playInfo,'src="','"');;
								var actor = '-';
								var dctor = '-';
								var area = '-';
								var score = '-';
								var year = '-';
								var shareurl = url;
								var script;
								var playall;
								var items = [];
						
								actor = appletv.substringByData(playInfo,'<p>主演：','</p>');
								actor = appletv.getTextInTag(actor);
								year = appletv.substringByData(playInfo,'上映年代：','</b>');
								area = appletv.substringByData(playInfo,'地区：','</b>');
								script = appletv.encode("tuClient.loadVideoPage('"+url+"');");
								var ftpScript= appletv.substringByData(htmlContent,'<script>var GvodUrls = "','";</script>');
								var ftps = ftpScript.split('###');
								for(var i=0;i<ftps.length;i++){
									var ftp = ftps[i];
									index = ftp.lastIndexOf('/');
									var t = ftp.substring(index+1);
									var c = ftp;
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
									if(items.length>1){
										appletv.setValue('clican.tu.video',video);
									}
									var xml = new EJS({
										url : appletv.serverurl
												+ '/template/tu/video.ejs'
									}).render(video);
									appletv.loadAndSwapXML(xml);
								
							});
		},
		
		loadItemsPage : function() {
			appletv.showLoading();
			appletv.getValue('clican.tu.video',function(video){
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/tu/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		loadSearchPage : function() {
			appletv.showInputTextPage('关键字', '搜索', tuClient.loadKeywordsPage,
					'tuClient.loadKeywordsPage', '');
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
					url : appletv.serverurl + '/template/tu/keywords.ejs'
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