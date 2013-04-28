var sokuClient = {
		
		sokuChannels : [ {
			label : "搜索",
			value : 'search'
		},{
			label : "电影",
			value : 'movie'
		},{
			label : "电视剧",
			value : 'tv'
		}],
		
		sokuChannelMap : {
			"search" : {
				label : "搜索",
				value : "search"
			},
			"movie" : {
				label : "电影",
				value : "movie",
				url: "http://www.soku.com/channel/movielist_0_0_0_1_1.html"
			},
			"tv" : {
				label : "电视剧",
				value : "tv",
				url: "http://www.soku.com/channel/teleplaylist_0_0_0_1_1.html"
			}
		},
		
		loadChannelPage:function(){
			var data = {
					'channels' : sokuClient.sokuChannels,
					'serverurl' : appletv.serverurl
				};
			var templateEJS = new EJS({
				url : appletv.serverurl + '/template/soku/channel.ejs'
			});	
			var xml = templateEJS.render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadIndexPage : function(keyword, page, channelId, queryUrl) {
			appletv.showLoading();
			var channel = this.sokuChannelMap[channelId];
			var videos = [];
			if (channelId == 'search') {
				var url1 = "http://api.3g.youku.com/layout/phone2/ios/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
				var url2 = "http://api.3g.youku.com/videos/search/"+encodeURIComponent(keyword)+"?pg="+page+"&pid=69b81504767483cf&pz=30";
				var videos = [];
				
				appletv.makeRequest(url1, function(jsonContent1) {
					appletv.logToServer('s1');
					var results1 = JSON.parse(jsonContent1)['results'];
					appletv.logToServer('s2');
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
					appletv.logToServer('s3');
					appletv.makeRequest(url2,function(jsonContent2){
						appletv.logToServer('s4');
						var results2 = JSON.parse(jsonContent2)['results'];
						appletv.logToServer('s5');
						for(var i=0;i<results2.length;i++){
							var result2 = results2[i];
							var video = {
									"title" : result2['title'],
									"id" : result2['videoid'],
									"pic" : result2['img'],
									"site" : result2['default_site'],
									"album": false
								};
							videos.push(video);
						}
						appletv.logToServer('s6');
						sokuClient.generateIndexPage(keyword, page, channel,
								videos,queryUrl);
					});
				});
			} else {
				if(queryUrl==null){
					queryUrl = channel['url'].replace('_1.html','_'+page+'.html');
				}else{
					var start = queryUrl.indexOf('_1.html');
					if(start>=0){
						start = start+1;
						queryUrl = queryUrl.substring(0,start)+page+'.html';
					}
				}
				appletv.logToServer(queryUrl);
				appletv.makeRequest(queryUrl, function(content) {
					appletv.logToServer('soku callback');
					if (content != null && content.length > 0) {
						var videoContent = appletv.substringByTag(content,'<div class="items"','</div>','div');
						var packs = appletv.getSubValuesByTag(videoContent,
								'<div class="item"', '</div>', 'div');
						for (i = 0; i < packs.length; i++) {
							var pack = packs[i];
							var pic = appletv.substringByData(pack,
									'<img', '>');
							pic = appletv.substringByData(pic,'original="','"');
							var title = appletv.substringByData(pack,'title="','"');
							var id = 'http://www.soku.com'+appletv.substringByData(pack, 'href="','"');
							var video = {
								"title" : title,
								"id" : id,
								"pic" : pic
							};
							videos.push(video);
						}
						sokuClient.generateIndexPage(keyword, page, channel,
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
				'channels' : sokuClient.sokuChannels,
				'serverurl' : appletv.serverurl,
				'videos' : videos,
				'url': url
			};
			if(channel['value']=='search'){
				var xml = new EJS({
					url : appletv.serverurl + '/template/soku/search.ejs'
				}).render(data);
				appletv.loadAndSwapXML(xml);
			}else{
				var xml = new EJS({
					url : appletv.serverurl + '/template/soku/index.ejs'
				}).render(data);
				appletv.loadAndSwapXML(xml);
			}
			
		},
		
		getCategory: function(content,channelId,url){
			var channel = this.sokuChannelMap[channelId];
			var categoryFilterContent = appletv.substringByTag(content,'<div class="filter">','</div>','div');
			var categoryFilters = appletv.getSubValuesByTag(categoryFilterContent,'<div class="item"','</div>','div');
			appletv.logToServer(categoryFilters[1]);
			var categoryNames = [];
			var categoryMap = {};
			var category = {"categoryMap":categoryMap,"categoryNames":categoryNames,"url":url,"serverurl":appletv.serverurl,"channelId":channelId};
			for(i=0;i<categoryFilters.length;i++){
				var categoryName = appletv.substringByData(categoryFilters[i],'<label>','</label>');
				categoryNames.push(categoryName);
				var categoryValues = [];
				var categoryLis = appletv.getSubValues(categoryFilters[i],'<li','</li>');
				for(j=0;j<categoryLis.length;j++){
					var select = false;
					var categoryLabel;
					if(categoryLis[j].indexOf('class="current"')!=-1){
						select = true
					}
					categoryLabel = appletv.substringByData(categoryLis[j],'<a','</a>');
					categoryLabel = appletv.subIndexString(categoryLabel,'>');
					var categoryUrl = 'http://www.soku.com'+appletv.substringByData(categoryLis[j],'href="','"');
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
				category = sokuClient.getCategory(content,channelId,url);
				var xml = new EJS({
					url : appletv.serverurl + '/template/soku/category.ejs'
				}).render(category);
				appletv.loadAndSwapXML(xml);
			});
		},
		
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
		loadVideoPageByUrl: function(id){
			appletv.showLoading();
			var url = id;
			appletv.makeRequest(url, function(htmlContent) {
				var detail = appletv.substringByTag(htmlContent,'<div class="detailinfo">','</div>','div');
				var title = appletv.substringByData(detail,'<h1>','</h1>');
				var desc = appletv.substringByData(htmlContent,'<div class="intro">','</div>');
				desc = appletv.substringByData(desc,'label>','<span').trim();
				var actor = appletv.substringByData(detail,'主演:','</span>');
				actor = appletv.getTextInTag(actor);
				var dctor = appletv.substringByData(detail,'导演:','</span>');
				dctor = appletv.getTextInTag(dctor);
				var area = appletv.substringByData(detail,'地区:','</span>');
				area = appletv.getTextInTag(area);
				var score =appletv.substringByData(detail,'<em class="num">','</em>');
				var year = appletv.substringByData(detail,'上映时间:','</span>');
				year = appletv.getTextInTag(year);
				var script = appletv.encode("sokuClient.loadVideoPageByUrl('"+id+"');");
				var pic = appletv.substringByData(detail,'<img','>');
				pic = appletv.substringByData(pic,'src="','"').trim();
				var size = 1;
				var sites = [];
				var siteContent = appletv.substringByTag(htmlContent,'<div class="source"','</div>','div');
				var siteSources = appletv.getSubValues(siteContent,'<li','</li>');
				for(var i=0;i<siteSources.length;i++){
					var stitle = appletv.substringByData(siteSources[i],'title="','"');
					var sid = appletv.substringByData(siteSources[i],'id="','"');
					var site = {"title":stitle,"id":sid};
					sites.push(site);
				}
				for(var i=0;i<sites.length;i++){
					var site = sites[i];
					appletv.logToServer('site='+site['id']);
					var si = appletv.substringByTag(htmlContent,'<div class=\'linkpanels '+site['id']+'\'','</div>','div');;
					var siteItems = appletv.getSubValues(si,'<li','</li>');
					var items = [];
					for(var j=0;j<siteItems.length;j++){
						var vurl = appletv.substringByData(si,'href="','"');
						var vtitle = '第'+(j+1)+'集';
						var v = {"url":vurl,"title":vtitle};
						items.push(v);
					}
					site['items']=items;
					size = items.length;
				}
				var video = {
						'serverurl' : appletv.serverurl,
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
							'size' : size
						},
						sites : sites
					};
					appletv.setValue('clican.soku.video',video);
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/soku/video.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
		
		loadItemsPageBySite : function (site){
			appletv.showLoading();
			appletv.getValue('clican.soku.video',function(video){
				var items = video['sites'][0]['items'];
				for(var i =0;i<video['sites'].length;i++){
					var s = video['sites'][i];
					appletv.logToServer(s['id']);
					if(s['id']==site){
						appletv.logToServer('change items to ' +site);
						items = s['items'];
						break;
					}
				}
				var v = {'video':video['video'],'items':items,'serverurl':appletv.serverurl};
				var xml = new EJS({
					url : appletv.serverurl
							+ '/template/soku/videoItems.ejs'
				}).render(v);
				appletv.loadAndSwapXML(xml);
			});
		
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
				var result = JSON.parse(jsonContent);
				var detail = result['detail'];
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
				var size = '1';
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
					size = detail['episode_total'];
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
							'size' : size,
							'vcode' : vcode,
						},
						sites : sites
					};
					appletv.logToServer('render video page');
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/soku/videoSearch.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
		
		loadItemsPage : function(id,site) {
			appletv.showLoading();
			var url =  "http://api.3g.youku.com/layout/phone2/ios/searchdetail?pid=69b81504767483cf&id="+id+"&site="+site;
			appletv.makeRequest(url, function(jsonContent) {
				appletv.logToServer(jsonContent);
				var result = JSON.parse(jsonContent);
				var detail = result['detail'];
				var title = detail['title'];
				var pic =  detail['img'];
				var items = [];
				
				var series = result['series']['data'];
				for(var i=0;i<series.length;i++){
					var serie = series[i];
					var title = serie['title'];
					if(title==null||title.trim().length==0){
						title = '第'+(i+1)+'集';
					}
					var item = {"title":title,"vcode":serie['videoid'],"url":serie['url']};
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
							+ '/template/soku/videoItemsSearch.ejs'
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
					var vid = appletv.substringByData(content,'vid:"','"');
					if(vid!=null&&vid.length!=0){
						qqClient.playVideo(vid);
					}else{
						var s = url.lastIndexOf('/');
						var e = url.indexOf('.html');
						vid = url.substring(s+1,e);
						qqClient.playVideo(id);
					}
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
					var tvId = appletv.substringByData(content, 'data-player-tvid="', '"');
					if(tvId==null||tvId.length==0){
						tvId = appletv.substringByData(content, '"tvId":"', '"');
					}
					var videoId = appletv.substringByData(content, 'data-player-videoid="', '"');
					if(videoId==null||videoId.length==0){
						videoId = appletv.substringByData(content, '"videoId":"', '"');
					}
					var jsUrl;
					if(tvId==null||tvId.length==0){
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
							appletv.playM3u8(m3u8url,'');
						}
					});
				});
			}else if(url.indexOf('cntv.cn')!=-1){
				appletv.showLoading();
				appletv.makeRequest(url,function(content){
					var id = appletv.substringByData(content, '"videoCenterId","', '"');
					appletv.logToServer('id for cntv:'+id);
					if(id==null||id.length==0){
						appletv.showDialog('无法从cntv.cn找到相关资源','');
					}else{
						var m3u8url = 'http://asp.cntv.lxdns.com/hls/'+id+'/main.m3u8';
						appletv.playM3u8(m3u8url,'');
					}
				});
			}else if(url.indexOf('tudou.com')!=-1){
				tudouClient.loadVideoPage(url, '', false);
			}else if(url.indexOf('youku.com')!=-1){
				var s = url.indexOf('id_');
				var e = url.indexOf('.html');
				var vcode = url.substring(s+3,e);
				var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
				appletv.playM3u8(url, '');
			}else if(url.indexOf('funshion.com')!=-1){
				var vid = appletv.substringByData(url,'play/','/');
				var jsonUrl = 'http://jsonfe.funshion.com/playinfo/?mid='+vid+'&cli=iphone&ver=1.2.3.2';
				appletv.makeRequest(jsonUrl,function(jsonContent){
					appletv.logToServer(jsonContent);
					var json = JSON.parse(jsonContent);
					var m3u8Url;
					try{
						m3u8Url = json['data']['mpurls']['dvd']['url'];
					}catch(e){
						
					}
					if(m3u8Url==null||m3u8Url.length==0){
						m3u8Url = json['data']['purl'];
					}
					appletv.playM3u8(m3u8Url, '');
				});
			}else if(url.indexOf('m1905.com')!=-1){
				var vid = appletv.substringByData(url,'play/','.shtml');
				var xmlUrl = 'http://mapps.m1905.com/index.php/Pad/video?id='+vid+'&cid=2';
				appletv.logToServer(xmlUrl);
				appletv.makeRequest(xmlUrl,function(xmlContent){
					var m3u8Url = appletv.substringByData(xmlContent,'<item','</item>');
					if(m3u8Url==null||m3u8Url.length==0){
						appletv.showDialog('无法播放',url);
						return;
					}
					m3u8Url = appletv.substringByData(m3u8Url,'url="','"');
					appletv.playM3u8(m3u8Url, '');
					},{
						"Cookie":"PHPSESSID=as1vljvgb2iv9vkld02kvi1ln2",
						"key":"0F80D95E9423828649D35EC7745DC524",
						"did":"01d0721c86d8ff7f84174ac08e715dfd560871c0"
					});
			}else{
				appletv.showDialog('无法播放',url);
			}
		},
		
		play : function(vcode){
			appletv.showLoading();
			var url = 'http://v.youku.com/player/getRealM3U8/vid/' + vcode + '/type/hd2/video.m3u8';
			appletv.playM3u8(url, '');
		},
}