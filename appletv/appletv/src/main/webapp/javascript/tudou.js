var tudouSearchApi='http://api.tudou.com/v5/wireless.search/3/${tudou.wirelessid}/json/?type=all&pageSize=30&ip=219.142.48.73&os=5.1&ver=2.7.0.12081617&ua=%5BiPad2%2C1%5D%5B5.1.1%5D'
var tudouClient = {
		
	tudouChannels:
		[
		 {label:"推荐",value:1000},
		 {label:"搜索",value:1001},
		 {label:"电视剧",value:30},
		 {label:"电影",value:22},
		 {label:"综艺",value:31},
		 {label:"搞笑",value:5},
		 {label:"热点",value:29},
		 {label:"动画",value:9},
		 {label:"原创",value:99},
		 {label:"娱乐",value:1},
		 {label:"女性",value:27},
		 {label:"体育",value:15},
		 {label:"汽车",value:26},
		 {label:"科技",value:21},
		 {label:"风尚",value:32},
		 {label:"乐活",value:3},
		 {label:"教育",value:25},
		 {label:"教育",value:24}
		],
		
		tudouChannelMap:
		{
			 "1000":{label:"推荐",value:1000},
			 "1001":{label:"搜索",value:1001},
			 "30":{label:"电视剧",value:30},
			 "22":{label:"电影",value:22},
			 "31":{label:"综艺",value:31},
			 "5":{label:"搞笑",value:5},
			 "29":{label:"热点",value:29},
			 "9":{label:"动画",value:9},
			 "99":{label:"原创",value:99},
			 "1":{label:"娱乐",value:1},
			 "27":{label:"女性",value:27},
			 "15":{label:"体育",value:15},
			 "26":{label:"汽车",value:26},
			 "21":{label:"科技",value:21},
			 "32":{label:"风尚",value:32},
			 "3":{label:"乐活",value:3},
			 "25":{label:"教育",value:25},
			 "4":{label:"教育",value:24}
		},
			
		loadIndexPage: function(keyword,page,channelId){
			//appletv.showDialog('加载中...','Loading...');
			var channel = this.tudouChannelMap[channelId];
			var videos = [];
			var queryUrl;
			if(channelId==1001){
				queryUrl = tudouSearchApi+'&pageNo='+page+'&kw='+encodeURIComponent(keyword);
				appletv.makeRequest(queryUrl,function(content){
					if(content!=null&&content.length>0){
						var wirelessSearchResult = JSON.parse(content)['wirelessSearchResult'];
						var albumArray = wirelessSearchResult['albums'];
						var items = wirelessSearchResult['items'];
						
						if (albumArray != null) {
							for (i = 0; i < albumArray.length; i++) {
								var video = {
										"title" : albumArray[i]['title'],
										"id" :  albumArray[i]['itemid'],
										"pic" : albumArray[i]['picurl'],
										"album":1
									};
								videos.push(video);
							}
						}

						if (itemArray != null) {
							for (int i = 0; i < itemArray.length; i++) {
								var video = {
										"title" : itemArray[i]['title'],
										"id" :  itemArray[i]['itemid'],
										"pic" : itemArray[i]['picurl'],
										"album":0
									};
								videos.push(video);
							}
						}
						
						tudouClient.generateIndexPage(keyword,page,channel,videos);
					} else {
						atv.loadXML(appletv.makeDialog('加载失败',''));
					}
				});
			}else{
				queryUrl="http://www.tudou.com/cate/ach"+channelId+"a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe-2pa"+page+".html";
				appletv.makeRequest(queryUrl,function(content){
					if(content!=null&&content.length>0){
						var packs = appletv.getSubValuesByTag(content, '<div class="pack pack_album">', '</div>','div');
						for(i=0;i<packs.length;i++){
							var pack = packs[i];
							var pic = appletv.substring(pack,'<img class="quic" src="','"');
							var title = appletv.substring(pack,'title="','"');
							var id = appletv.substring(pack,'<a href="','"');
							id = appletv.substring(id,'albumcover/','.html');
							var album = 0;
							if(channelId==30||channelId==9){
								album = 1;
							}
							var video = {
									"title" : title,
									"id" : id,
									"pic" : pic,
									"album":album
								};
							videos.push(video);
						}
						tudouClient.generateIndexPage(keyword,page,channel,videos);
					} else {
						atv.loadXML(appletv.makeDialog('加载失败',''));
					}
				});
			}
			
			
		},
		
		generateIndexPage: function(keyword,page,channel,videos){
			var begin= 1;
			var end = 1;
			if (page < 90) {
				begin = page;
				end = page + 9;
			} else {
				end = 99;
				begin = 90;
			}
			var data = {'channel':channel,'keyword':keyword,'begin':begin,'end':end,'channels':tudouClient.tudouChannels,'serverurl':appletv.serverurl,'videos':videos};
			var xml = new EJS({url: appletv.serverurl+'/template/tudou/index.ejs'}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage : function(code,channelId,isalbum) {
			atv.loadXML(appletv.makeDialog('加载中...','Loading...'));
			var url = 'http://www.tudou.com/albumcover/'+code+'.html';
			appletv.makeRequest(url, function(htmlContent) {
				if (htmlContent == null) {
					return;
				}
				var itemid = appletv.substring(htmlContent,'iid: ',',').trim();
				itemid = itemid.substring(1,itemid.length-1);
				appletv.logToServer('itemid:'+itemid);
				tudouClient.loadAlbumXml(itemid,channelId,1,isalbum);
			});
		},
		
		loadAlbumXml : function(itemid, channelId, hd, isalbum) {
			atv.loadXML(appletv.makeDialog('加载中...', 'Loading...'));
			appletv.makeRequest(
							'http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=&columnid='
									+ channelId
									+ '&itemid='
									+ itemid
									+ '&ishd='
									+ hd,
							function(data) {
								var album = JSON.parse(data);
								var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><head><script src=\"'
										+ appletv.serverurl
										+ '/javascript/clican.js\"/><script src=\"'
										+ appletv.serverurl
										+ '/javascript/tudou.js\"/></head><body>';
								xml += '<itemDetail id=\"itemdetail\">';
								xml += '<title><![CDATA[' + album['title']
										+ ']]></title>';
								xml += '<summary><![CDATA[' + album['description']
										+ ']]></summary>';
								xml += '<image style=\"moviePoster\">'
										+ album['picurl'] + '</image>';
								xml += '<table><columnDefinitions><columnDefinition width=\"50\"><title>其他信息</title></columnDefinition><columnDefinition width=\"50\"><title></title></columnDefinition></columnDefinitions><rows>';
								xml += '<row><label><![CDATA[导演:'
										+ album['directors']
										+ ']]></label><label><![CDATA[年代:'
										+ album['year'] + ']]></label></row>';
								xml += '<row><label><![CDATA[类型:'
										+ album['type_desc']
										+ ']]></label><label><![CDATA[地区:'
										+ album['area_desc'] + ']]></label></row>';
								xml += '<row><label><![CDATA[剧集:共'
										+ album['albumitems'].length
										+ '集]]></label><label><![CDATA[主演:'
										+ album['actors'] + ']]></label></row>';
								xml += '</rows></table><centerShelf><shelf id=\"album\"><sections><shelfSection><items>';
								if (isalbum == 1) {
									xml += '<actionButton id=\"album_1\" onSelect=\"appletv.loadAlbumListXml('
											+ itemid
											+ ','
											+ channelId
											+ ','
											+ hd
											+ ',2);\" onPlay=\"appletv.loadAlbumListXml('
											+ itemid
											+ ','
											+ channelId
											+ ','
											+ hd
											+ ',2);\"><title>标清</title></actionButton>';
									xml += '<actionButton id=\"album_2\" onSelect=\"appletv.loadAlbumListXml('
											+ itemid
											+ ','
											+ channelId
											+ ','
											+ hd
											+ ',3);\" onPlay=\"appletv.loadAlbumListXml('
											+ itemid
											+ ','
											+ channelId
											+ ','
											+ hd
											+ ',3);\"><title>高清</title></actionButton>';
									if (album['hd'] == 1) {
										xml += '<actionButton id=\"album_3\" onSelect=\"appletv.loadAlbumListXml('
												+ itemid
												+ ','
												+ channelId
												+ ','
												+ hd
												+ ',4);\" onPlay=\"appletv.loadAlbumListXml('
												+ itemid
												+ ','
												+ channelId
												+ ','
												+ hd
												+ ',4);\"><title>超清</title></actionButton>';
									}
								} else {
									xml += '<actionButton id=\"album_1\" onSelect=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?st=2&amp;itemid='
											+ itemid
											+ '\');\" onPlay=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?st=2&amp;itemid='
											+ itemid
											+ '\');\"><title>标清</title></actionButton>';
									xml += '<actionButton id=\"album_2\" onSelect=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?st=4&amp;itemid='
											+ itemid
											+ '\');\" onPlay=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?st=3&amp;itemid='
											+ itemid
											+ '\');\"><title>高清</title></actionButton>';
									if (album['hd'] == 1) {
										xml += '<actionButton id=\"album_3\" onSelect=\"atv.loadURL(\''
												+ appletv.serverurl
												+ '/ctl/tudou/play.xml?st=4&amp;itemid='
												+ itemid
												+ '\');\" onPlay=\"atv.loadURL(\''
												+ appletv.serverurl
												+ '/ctl/tudou/play.xml?st=4&amp;itemid='
												+ itemid
												+ '\');\"><title>超清</title></actionButton>';
									}
								}
								xml += '<actionButton id=\"album_4\" onSelect=\"appletv.shareToSinaWeibo(\''
										+ album['title']
										+ '\',\''
										+ album['emailshareurl']
										+ '\',\''
										+ album['picurl']
										+ '\');\" onPlay=\"appletv.shareToSinaWeibo(\''
										+ album['title']
										+ '\',\''
										+ album['emailshareurl']
										+ '\',\''
										+ album['picurl']
										+ '\');\"><title>分享</title></actionButton>';
								xml += '</items></shelfSection></sections></shelf></centerShelf></itemDetail></body></atv>';
								appletv.loadAndSwapXML(xml);
							});
		},
		

		loadAlbumListXml : function(itemid, channelId, hd, st) {
			atv.loadXML(appletv.makeDialog('加载中...', 'Loading...'));
			appletv.makeRequest(
							'http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=&columnid='
									+ channelId
									+ '&itemid='
									+ itemid
									+ '&ishd='
									+ hd,
							function(data) {
								var album = JSON.parse(data);
								var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><listScrollerSplit id=\"albumlist\"><header><simpleHeader horizontalAlignment=\"left\">';
								xml += '<title><![CDATA[' + album['title']
										+ ']]></title>';
								xml += '<image>' + album['picurl'] + '</image>';
								xml += '</simpleHeader></header><menu><sections><menuSection><items>';
								var items = album['albumitems'];
								var item;
								for ( var i = 0; i < items.length; i++) {
									item = items[i];
									xml += '<imageTextImageMenuItem id=\"albumItem_'
											+ i
											+ '\" onPlay=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?itemid='
											+ item['itemid']
											+ '&amp;st='
											+ st
											+ '\');" onSelect=\"atv.loadURL(\''
											+ appletv.serverurl
											+ '/ctl/tudou/play.xml?itemid='
											+ item['itemid']
											+ '&amp;st='
											+ st
											+ '\');\">';
									xml += '<leftImage>' + item['picurl']
											+ '</leftImage>';
									xml += '<label>第' + (i + 1) + '集</label>';
									xml += '<rightImage></rightImage>';
									xml += '<imageSeparatorText></imageSeparatorText>'
									xml += '</imageTextImageMenuItem>';
								}
								xml += '</items></menuSection></sections></menu></listScrollerSplit></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		
		loadSearchPage: function(){
	    	appletv.showInputTextPage('关键字','搜索',tudouClient.loadKeywordsPage,'tudouClient.loadKeywordsPage','');
	    },
	    
	    loadKeywordsPage: function(q){
	    	var queryUrl = 'http://tip.tudou.soku.com/hint?q='+q;
	    	appletv.makeRequest(queryUrl,function(result){
	    		appletv.logToServer(result);
	    		var keywords = JSON.parse(result);
	    		var data = {keywords:keywords,serverurl:appletv.serverurl};
	    		var xml = new EJS({url: appletv.serverurl+'/template/tudou/keywords.ejs'}).render(data);
				appletv.loadAndSwapXML(xml);
	    	});
	    },
	    
	    
}