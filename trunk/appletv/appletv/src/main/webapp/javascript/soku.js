var sokuClient = {
		loadVideoPage : function(href) {
			appletv.showLoading();
			var url = href;
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
				var script = appletv.encode("sokuClient.loadVideoPage('"+href+"');");
				var detail = appletv.substringByTag(htmlContent,'<div class="detail">','</div>','div');
				
				var title = appletv.substringByData(detail,'title="','"');
				var pic = appletv.substringByData(detail,'src="','"');
				
				var items = [];
				if(isalbum){
					var itemscontent = appletv.substringByTag(htmlContent,'<div class="items"','</div>','div');
					var urls = appletv.getSubValues(itemscontent,'<a','</a>');
					for(i=0;i<urls.length;i++){
						url = urls[i];
						var t = appletv.substringByData(url,'title="','"');
						var c = appletv.substringByData(url,'id_','.html');
						var item = {
								'title' : t,
								'id' : c
							};
						items.push(item);
					}
				}else{
					var item = {
							'title' : title,
							'id' : code
						};
					items.push(item);
				}
				
				
				var video = {
						'serverurl' : appletv.serverurl,
						album : isalbum,
						channelId : channelId,
						script : script,
						video : {
							'id' : code,
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
					if(isalbum){
						appletv.setValue('clican.youku.video',video);
					}
					var xml = new EJS({
						url : appletv.serverurl
								+ '/template/youku/video.ejs'
					}).render(video);
					appletv.loadAndSwapXML(xml);
			});
		},
}