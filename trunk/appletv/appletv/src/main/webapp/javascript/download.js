var downloadClient = {
	downloadMp4 : function(url) {
		native_downloadMp4(url);
		appletv.showDialog('已添加到下载队列', '');
	},

	downloadM3u8 : function(url) {
		native_downloadM3u8(url);
		appletv.showDialog('已添加到下载队列', '');
	}
}