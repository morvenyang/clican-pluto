var sokuClient = {
		
		loadSearchPage : function() {
			appletv.showInputTextPage('关键字', '搜索', sokuClient.loadKeywordsPage,
					'sokuClient.loadKeywordsPage', '');
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
					url : appletv.serverurl + '/template/soku/keywords.ejs'
				}).render(data);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		loadIndexPage:function(keyword,page){
			var url1 = "http://api.3g.youku.com/layout/phone2/ios/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
			var url2 = "http://api.3g.youku.com/videos/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
			var videos = [];
			
			appletv.makeRequest(url1, function(jsonContent1) {
				var results1 = JSON.parse(jsonContent1)['results'];
				appletv.logToServer('r1:'+jsonContent1);
				for(var i=0;i<results1.length;i++){
					var result1 = results1[i];
					var video = {
							"title" : result1['showname'],
							"id" : result1['showid'],
							"pic" : result1['show_vthumburl'],
							"site" : result1['default_site'],
							"album": true
						};
					videos.push(video);
				}
				appletv.makeRequest(url2,function(jsonContent2){
					var results2 = JSON.parse(jsonContent2)['results'];
					appletv.logToServer('r2:'+jsonContent2);
					for(var i=0;i<results2.length;i++){
						var result2 = results2[i];
						var video = {
								"title" : result2['showname'],
								"id" : result2['showid'],
								"pic" : result2['img'],
								"site" : result1['default_site'],
								"album": false
							};
						videos.push(video);
					}
					sokuClient.generateIndexPage(keyword, page, videos);
				});
			});
		},
		
		generateIndexPage : function(keyword, page, videos) {
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
				'keyword' : keyword,
				'begin' : begin,
				'end' : end,
				'serverurl' : appletv.serverurl,
				'videos' : videos,
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/soku/index.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage : function(id,album,site) {
			appletv.showLoading();
			var url;
			if(album){
				url = "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id="+id+"&site="+site;
			}else{
				url = "http://api.3g.youku.com/layout/phone2_1/detail?pid=69b81504767483cf&id="+id+"&site="+site;
			}

			appletv.makeRequest(url, function(jsonContent) {
				detail = JSON.parse(jsonContent);
				var title;
				var detail;
				var actor;
				var dctor;
				var area;
				var score;
				var year;
				var desc;
				var script;
				var pic;
				var vcode;
				var sites = [];
				if(album){
					title = detail['title'];
					actor = JSON.stringify(detail['performer']);
					dctor = JSON.stringify(detail['director']);
					area = JSON.stringify(detail['area']);
					score = detail['reputation'];
					year = detail['showdate'];
					desc = detail['desc'];
					pic =  detail['img'];
					var siteItems = detail['site_items'];
					for(var i=0;i<siteItems.length;i++){
						var site = {"title":siteItems[i]['title'],"id":siteItems[i]["id"]};
						sites.push(site);
					}
				}else{
					title = detail['title'];
					desc = '-';
					pic =  detail['img'];
					vcode = detail['videoid'];
				}
				
				script = appletv.encode("sokuClient.loadVideoPage('"+id+"',"+album+",'');");
				var video = {
						'serverurl' : appletv.serverurl,
						album : album,
						id: id,
						script : script,
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
							'vcode' : vcode,
						},
						sites : sites
					};
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/soku/video.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
		
		loadItemsPage : function(id,site) {
			appletv.showLoading();
			var url =  "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id="+id+"&site="+site;
			appletv.makeRequest(url, function(jsonContent) {
				detail = JSON.parse(jsonContent);
				var title = detail['title'];
				var pic =  detail['img'];
				var items = [];
				
				var series = detail['series']['data'];
				for(var i=0;i<series.length;i++){
					var serie = series[i];
					var item = {"title":serie['title'],"vcode":serie['videoid'],"url":serie['url']};
					items.push(item);
				}
				var video = {
						'serverurl' : appletv.serverurl,
						video : {
							'id' : id,
							'pic' : pic,
							'title' : title,
						},
						items: items
					};
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/souku/videoItems.ejs'
				}).render(video);
				appletv.loadAndSwapXML(xml);
			});
		},
		
		openUrl : function(url){
			if(url.indexOf('56.com')!=-1){
				appletv.showLoading();
				appletv.makeRequest(url,function(content){
					var id = appletv.substringByData(content,'"id":"','"');
					var jsonUrl = 'http://vxml.56.com/mobile/'+id+'/?src=3gapi';
					appletv.makeRequest(jsonUrl,function(jsonContent){
						var rfiles = JSON.parse(jsonContent)['info']['rfiles'];
						if(rfiles.length>0){
							var url = rfiles[0]['url'];
							appletv.playMp4(url,'');
						}else{
							appletv.showDialog('无法从56.com找到相关资源','');
						}
					});
				});
			}else if(url.indexOf('qq.com')!=-1){
				appletv.makeRequest(url,function(content){
					var s = url.lastIndexOf('/');
					var e = url.indexOf('.html');
					var id = url.substring(s+1,e);
					qqClient.playVideo(id);
				});
			}else if(url.indexOf('sohu.com')!=-1){
				appletv.showLoading();
				appletv.makeRequest(url,function(content){
					var id = appletv.substringByData(content,'vid="','"');
					var jsonUrl = 'http://api.tv.sohu.com/video/playinfo/'+id+'.json?api_key=695fe827ffeb7d74260a813025970bd5'
					appletv.makeRequest(jsonUrl,function(jsonContent){
						var m3u8Url = JSON.parse(jsonContent)['data']['url_high'];
						appletv.playM3u8(m3u8Url,'');
					});
				});
			}else if(url.indexOf('letv.com')!=-1){
				appletv.showLoading();
				appletv.makeRequest(url,function(content){
					var id = appletv.substringByData(content,'mmsid:',',').trim();
					var jsonUrl = 'http://app.m.letv.com/android/mindex.php?mod=minfo&ctl=videofile&act=index&mmsid='+id+'&videoformat=ios&pcode=010210000&version=3.3'
					appletv.makeRequest(jsonUrl,function(jsonContent){
						var infos = JSON.parse(jsonContent)['body']['videofile']['infos'];
						var info = infos['mp4_1300'];
						if(info==null){
							info = infos['mp4_1000'];
						}
						if(info==null){
							info = infos['mp4_350'];
						}
						if(info==null){
							appletv.showDialog('无法从letv.com找到相关资源','');
						}else{
							var mainUrl = info['mainUrl'];
							appletv.makeRequest(mainUrl,function(mediaJson){
								var location = JSON.parse(mediaJson)['location'];
								if(location==null){
									appletv.showDialog('无法从letv.com找到相关资源','');
								}else if(location.indexOf('mp4')!=-1){
									appletv.playMp4(location,'');
								}else{
									appletv.playM3u8(location,'');
								}
							});
						}
						appletv.playM3u8(m3u8Url,'');
					});
				});
			}else if(url.indexOf('iqiyi.com')!=-1){
				appletv.showLoading();
				appletv.makeRequest(url,function(content){
					var tvId = appletv.substringByData(content, '"tvId":"', '"');
					var videoId = appletv.substringByData(content, '"videoId":"', '"');
					var jsUrl;
					if(tvid==null||tvid.length==0){
						jsUrl = 'http://cache.video.qiyi.com/m/'+videoId+'/';
					}else{
						jsUrl = 'http://cache.video.qiyi.com/m/'+tvId+'/'+videoId+'/';
					}
					appletv.makeRequest(jsUrl,function(jsContent){
						jsonContent = jsContent.replace('var ipadUrl=','');
						var detail = JSON.parse(jsonContent);
						var m3u8url = detail['data']['mtl'][0]['m3u'];
						if(m3u8url==null||m3u8url.length==0){
							appletv.showDialog('无法从iqiyi.com找到相关资源','');
						}else{
							appletv.playm3u8(m3u8url,'');
						}
					});
				});
			}else{
				appletv.showDialog('无法播放','');
			}
		},
		
		play : function(vcode){
			appletv.showLoading();
			var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
			appletv.playM3u8(url, '');
		},
}