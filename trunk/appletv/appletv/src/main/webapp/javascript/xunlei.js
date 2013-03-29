
var xunleiClient = {
	
	loadXunleiSession:function(callback){
		if(appletv.serverurl.indexOf('local.clican.org')==-1){
			appletv.makeRequest(appletv.serverurl+'/noctl/xunlei/getsession.do',function(result){
				if(result==null||result.length==0){
					appletv.showDialog('1登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
				}else{
					var xunleisession
					try{
						xunleisession = JSON.parse(result);
					}catch(e){
						appletv.showDialog('2登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
					}
					if(xunleisession!=null){
						callback(xunleisession);
					}
				}
			});
		}else{
			appletv.getValue('xunleiu',function(xunlei){
				if(xunlei==null){
					appletv.showDialog('未侦测到本地服务器,请去TT的迅雷登录界面登录迅雷VIP帐号','你也可以通过安装本地服务器以实现迅雷登录功能,具体请参考http://clican.org');
				}else{
					var xunleisession = {sessionid:xunlei['sessionid'],userid:xunlei['userid'],vip:xunlei['isvip']};
					callback(xunleisession);
				}
			});
		}
	},
	
	play:function(url,name){
		this.loadXunleiSession(function(xunleisession){
			var sessionid=xunleisession['sessionid'];
			var userid=xunleisession['userid'];
			var vip = xunleisession['vip'];
			if(sessionid==null||sessionid.length==0||userid==null||userid.length==0||vip==null||vip.length==0){
				appletv.showDialog('3登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
			}else{
				if(name==null||name.length==0){
					if(url.indexOf('ed2k')>=0){
						var startstr ='file|';
						var endstr = '|';
						name = appletv.substringByData(url, startstr, endstr);
					}else{
						name = 'Unknown';
					}
				}

				var xunleiurl = "http://i.vod.xunlei.com/req_get_method_vod?url="
						+ encodeURIComponent(url) + "&video_name=" +name+ "&platform=1&userid=" + userid
						+ "&vip="+vip+"&sessionid=" + sessionid
						+ "&cache=" + new Date().getTime()
						+ "&from=vlist&jsonp=xunleiClient.xunleicallback";
				appletv.logToServer('xunleiurl:'+xunleiurl);
				if(appletv.simulate=='browser'){
					appletv.makePostRequest(appletv.serverurl+"/noctl/xunlei/geturl.do",xunleiurl,function(result){
						eval(result);
					});
				}else{
					appletv.makeRequest(xunleiurl,function(result){
						eval(result);
					},{
						"Referer" : "http://61.147.76.6/iplay.html"
					});
				}
			}
		});
	},
	
	xunleicallback:function (res) {
		appletv.logToServer('xunleicallback is called');
		try {
			msg = res['resp']['vod_permit']['msg'];
			if (msg == 'overdue session') {
				appletv.showDialog('4登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
				return;
			}
			if (msg == 'too much share userid') {
				appletv.showDialog('您的帐号因登录IP过多已被限制为不能播放，请于1天后重试，建议您尽快修改密码','');
				return;
			}
		} catch (e) {
		}
		try {
			if (res['resp']["trans_wait"] != 0
					&& res['resp']["vodinfo_list"].length == 0) {
				appletv.showDialog('改视频还未转码无法播放','');
				return;
			}
		} catch (e) {
		}
		if (!res['resp']["vodinfo_list"]) {
			appletv.showDialog('错误，请重试','');
			return;
		}
		if (res['resp']["vodinfo_list"].length == 0) {
			appletv.showDialog('没有内容，请重试','');
			return;
		}
		var m3u8url = res['resp']["vodinfo_list"][0]['vod_url'];
		appletv.playM3u8(m3u8url,'');
	}
}