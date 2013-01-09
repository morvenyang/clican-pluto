function __init() {
	return {
		version:1,
		searchword: function(params) {
			var keyword = params[0];
			var serverurl = params[1];
			atv.loadURL(serverurl+'/tudou/index.xml?channelId=1001&keyword='+encodeURIComponent(keyword));
	    }
	}
}