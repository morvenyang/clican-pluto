var localcacheClient = {
	loadIndexPage : function() {
		appletv.showLoading();
		var url = 'http://16.165.14.189:8080/appletv/noctl/local/resource';
		appletv.makeRequest(url, function(result) {
			if (result == null || result.length == 0 || result == 'null') {
				appletv.showDialog('没有相关内容', '');
				return;
			}
			var json = JSON.parse(result);
			var data = {
				'serverurl' : appletv.serverurl,
				'items' : json
			};
			var xml = new EJS({
				url : appletv.serverurl + '/template/localcache/index.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);

		});
	},

	play : function(url) {
		appletv.showLoading();
		appletv.loadAndSwapXML(appletv.makePlayXml(url));
	},
	
	downloadMp4 : function(url) {
		native_downloadMp4(url);
		appletv.showDialog('已添加到下载队列', '');
	},

	downloadM3u8 : function(url) {
		native_downloadM3u8(url);
		appletv.showDialog('已添加到下载队列', '');
	}
}