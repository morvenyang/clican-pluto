var tuClient ={
		tuChannelMap : {
			"1001" : {
				label : "搜索",
				value : 1001
			},
			"15" : {
				label : "电影",
				value : 15,
				album : true
			},
			"16" : {
				label : "电视",
				value : 16,
				album : true
			},
			"7" : {
				label : "动画片",
				value : 7,
				album : true
			},
			"8" : {
				label : "综艺片",
				value : 8,
				album : true
			}
		},
		tuChannels : [ {
			label : "搜索",
			value : 1001
		},  {
			label : "电影",
			value : 15,
			album : true
		}, {
			label : "电视",
			value : 16,
			album : true
		}, {
			label : "电影",
			value : 15,
			album : true
		}, {
			label : "动画片",
			value : 7,
			album : true
		}, {
			label : "综艺片",
			value : 8,
			album : true
		}],
		
		loadChannelPage:function(){
			var data = {
					'channels' : youkuClient.youkuChannels,
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
		
}