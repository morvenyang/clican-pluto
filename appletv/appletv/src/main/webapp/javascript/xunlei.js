
var xunleiClient = {
	
	loadXunleiSession:function(callback){
		appletv.makeRequest(appletv.serverurl+'/noctl/xunlei/getsession.do',function(result){
			appletv.logToServer(result);
			if(result==null||result.length==0){
				appletv.showDialog('登录过期请在本地服务器上重新登录','');
			}else{
				var xunleisession
				try{
					xunleisession = JSON.parse(result);
				}catch(e){
					appletv.logToServer('parse xunlei session error');
				}
				if(xunleisession!=null){
					callback(xunleisession);
				}
			}
		});
	},
	
	play:function(ed2kurl){
		this.loadXunleiSession(function(xunleisession){
			var sessionid=xunleisession['sessionid'];
			var userid=xunleisession['userid'];
			var vip = xunleisession['vip'];
			if(sessionid==null||sessionid.length==0||userid==null||userid.length==0||vip==null||vip.length==0){
				appletv.showDialog('登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
			}else{
				var name = appletv.substring(ed2kurl, "file|", "|");
				var xunleiurl = "http://i.vod.xunlei.com/req_get_method_vod?url="
						+ encodeURIComponent(ed2kurl) + "&video_name=" +name+ "&platform=1&userid=" + userid
						+ "&vip="+vip+"&sessionid=" + sessionid
						+ "&cache=" + new Date().getTime()
						+ "&from=vlist&jsonp=xunleiClient.xunleicallback";
				if(appletv.simulate){
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
				appletv.showDialog('登录过期请在本地服务器上重新登录','');
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
		appletv.playM3u8(m3u8url,appletv.serverurl);
	}
}