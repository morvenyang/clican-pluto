var weiboClient = {
	getToken : function(callback) {
		appletv.getValue('clican.weibo.accessToken',function(token){
			if(token==null||token.length==0){
				callback(token);
			}else{
				var url = appletv.remoteserverurl + '/ctl/weibo/getToken.do?deviceId='
				+ appletv.getDeviceId();
				appletv.makeRequest(
						url,
						function(jsonContent) {
							var accessToken = JSON.parse(jsonContent)['accessToken'];
							if (accessToken == null || accessToken.length == 0) {
								if (appletv.simulate == 'native') {
									var loginUrl = "https://api.weibo.com/oauth2/authorize?scope=follow_app_official_microblog&client_id=2865121764&redirect_uri=http://clican.org/appletv/ctl/weibo/oaAuthCallback.do&response_type=code&state="
											+ appletv.getDeviceId();
									appletv.loadHTML(loginUrl);
								} else {
									var title = "请通过以下URL和你的DeviceID进行新浪微博授权, 然后返回重试";
									description = "URL:http://clican.org\nDeviceID:"
											+ appletv.getDeviceId()
									appletv.showDialog(title, description);
								}
							} else {
								appletv.setValue('clican.weibo.accessToken',accessToken);
								callback(accessToken);
							}
						});
			}
		});
		
	},
	
	share : function(feature, title, shareURL, imageURL) {
		appletv.showLoading();
		weiboClient.getToken(token){
			if(token==null||token.length==0){
				return;
			}
			var status = ""
			if(feature==3){
				status = "我正在Apple TV3上观看在线视频（"+title+"）@Clican 了解更多 >>>";
				if(shareURL!=null&&shareURL.length>0){
					status = status + shareURL;
				}
			}
			var url = 'https://api.weibo.com/2/statuses/upload_url_text.json?access_token='+token+'&status='+encodeURIComponent(status);
			if(imageURL!=null&&imageURL.length>0){
				url = url + '&url='+imageURL
			}
			appletv.makeRequest(url,function(jsonContent){
				
			});
		}
		
	},
}