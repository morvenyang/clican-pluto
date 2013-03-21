var lblClient = {
	lblChannels : [ {
		label : "最新电影",
		value : 'category/movie'
	}, {
		label : "最新电视",
		value : 'category/television'
	} ],

	lblChannelMap : {
		'category/movie' : {
			label : "最新电影",
			value : 'category/movie'
		},
		'category/television' : {
			label : "最新电视",
			value : 'category/television'
		}
	},

	
	loadIndexPage : function(keyword, page, channelId,queryUrl) {
		appletv.showLoading();
		var channel = this.lblChannelMap[channelId];
		var videos = [];
		if(queryUrl ==null||queryUrl.length==0){
			queryUrl = 'http://www.longbuluo.com/'+channel['value']+'/page/'+page;
		}
		var s1 = new Date();
		appletv.makeRequest(queryUrl, function(content) {
			if (content != null && content.length > 0) {
				var packs = appletv.getSubValues(content,
						'<h4>', '</h4>');
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
				lblClient.loadPics(videos, 0, function(videos){
					lblClient.generateIndexPage(keyword, page, channel, videos,queryUrl);
					var s2 = new Date();
					appletv.logToServer('total:'+(s2.getTime()-s1.getTime()))
				});
			} else {
				appletv.showDialog('加载失败','');
			}
		});

	},

	generateIndexPage : function(keyword, page, channel, videos,url) {
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
			'videos' : videos,
			'url' : url
		};
		var xml = new EJS({
			url : appletv.serverurl + '/template/lbl/index.ejs'
		}).render(data);
		appletv.loadAndSwapXML(xml);
	},
	
	loadPics : function(videos,index,callback){
		if(index==videos.length){
			callback(videos);
		}else{
			var url = videos[index]['id'];
			appletv.makeRequest(url,function(content){
				var entry = appletv.substringByTag(content,'<div class="entry">','</div>','div');
				videos[index]['pic'] = appletv.substring(entry,'src="','"');
				lblClient.loadPics(videos,index+1,callback);
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
			var entry = appletv.substringByTag(htmlContent,'<div class="entry">','</div>','div');
			
			var ps = appletv.getSubValues(entry,'<p>','</p>');
			desc = appletv.getTextInTag(ps[0]);
			pic = appletv.substring(ps[1],'src="','"');
			title = appletv.substring(ps[1],'title="','"');
			index1 = title.indexOf("《");
			index2 = title.indexOf("》");
			if(index1>=0&&index2>=0){
				title = title.substring(index1+1,index2);
			}
			area = appletv.substring(ps[2],'制片国家/地区: ','<br');
			
			year = appletv.substring(ps[2],'上映日期: ','<br');
			actor = appletv.subIndexString(ps[2],'主演: ');
			dctor = appletv.substring(ps[2],'导演: ','<br');
			var items = [];
			for(i=4;i<ps.length;i++){
				var c = appletv.substring(ps[i],'href="','"');
				var t = appletv.substring(ps[i],'target="_blank">','</a>');
				var item = {
						'title' : t,
						'id' : c
				};
				items.push(item);
			}
			
			var video = {
					'serverurl' : appletv.serverurl,
					script : appletv.encode("lblClient.loadVideoPage('"+id+"');"),
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
						'shareurl':shareurl
					},
					items : items
				};
			appletv.setValue('clican.lbl.video',video);
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/lbl/video.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
		});
	},
	
	loadItemsPage : function() {
		appletv.showLoading();
		try{
			appletv.getValue('clican.lbl.video',function(video){
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/lbl/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		}catch(e){
			appletv.logToServer('Error occured lbl.loadItemsPage ' + e);
		}
		
	},
	
	play : function(id,title) {
		var url = id;
		if(url.indexOf('http://kuai.xunlei.com')>=0){
			appletv.makeRequest(url,function(content){
				var c2 = appletv.substringByTag(content,'<span class="c_2">','</span>','span');
				var href = appletv.substring(c2,'href="','"');
				xunleiClient.play(href, title);
			});
		}else{
			xunleiClient.play(url, title);
		}
	},
}