
var xunleiClient = {
	
	loadXunleiSession:function(callback){
		if(appletv.serverurl.indexOf('clican.org')==-1){
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
	//云点播
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
	
	//云点播
	offlinePlay:function(url,name){
		this.loadXunleiSession(function(xunleisession){
			var gdriveid=xunleisession['gdriveid'];
			var userid=xunleisession['userid'];
			var lxsessionid=xunleisession['lxsessionid'];
			
			if(gdriveid==null||gdriveid.length==0||userid==null||userid.length==0||lxsessionid==null||lxsessionid.length==0){
				appletv.showDialog('迅雷离线下载登录过期请在本地服务器上重新登录','具体说明请参考http://clican.org');
			}else{
				var cookie = "userid="+userid+"; lx_sessionid="+lxsessionid+";lx_login="+userid+";gdriveid="+gdriveid+";"
				if(name==null||name.length==0){
					if(url.indexOf('ed2k')>=0){
						var startstr ='file|';
						var endstr = '|';
						name = appletv.substringByData(url, startstr, endstr);
					}else{
						name = 'Unknown';
					}
				}
				var random = new Date().getTime();
				var checkUrl = "http://dynamic.cloud.vip.xunlei.com/interface/task_check?callback=xunleiClient.queryCid&url="+encodeURIComponent(url)+"&interfrom=task&random="+random+"&tcache="+random;
				appletv.makeRequest(checkUrl,function(result){
					result = result.substring(0,result.length-1)+",'"+userid+",'"+url+"','"+cookie+"')";
					eval(result);
				},{
					"Cookie" : cookie
				});
			}
		});
	},
	//queryCid('B26972E0D11A61AAFFAB04D3F50FBF631D51BFA9', '641F83D84F7C02094A5E30634A8C3DC41E23B924', '0','1125892196800765', 'crossbow.inception.720p.mkv', 0, 0, 0,'1365305929759329104.26622072','movie')
	queryCid:function(p1,p2,p3,p4,fileName,p6,p7,p8,p9,p10,userid,url,cookie){
		if(fileName.indexOf('mkv')==-1&&fileName.indexOf('MKV')==-1){
			appletv.showDialog('迅雷离线下载播放只支持MKV格式的文件','');
			return;
		}
		var random = new Date().getTime();
		var commitUrl = "http://dynamic.cloud.vip.xunlei.com/interface/task_commit?callback=xunleiClient.retriveTask&uid="+userid+"&cid=&gcid=&size=&goldbean=0&silverbean=0&t="+encodeURIComponent(fileName)+"&url="+url+"&type=2&o_page=history&o_taskid=0&class_id=0&database=undefined&interfrom=task&time="+random+"&noCacheIE="+random;
		appletv.makeRequest(commitUrl,function(result){
			if(result.indexOf('xunleiClient.retriveTask')!=-1){
				result = result.substring(0,result.length-1)+",'"+userid+"','"+cookie+"')";
				eval(result);
			}else{
				appletv.showDialog('迅雷离线下载失败','');
			}
		},{
			"Cookie" : cookie
		});
	},
	
	retriveTask:function(result,id,userid,cookie){
		if(result==1){
			var queryDownloadUrl = "http://dynamic.cloud.vip.xunlei.com/user_task?userid="+userid;
			appletv.makeRequest(queryDownloadUrl,function(result){
				var startTag = '<input id="dl_url'+id+'" type="hidden" value="';
				var downloadUrl = appletv.substringByData(startTag,'"').trim();
				appletv.playMkv(downloadUrl);
			});
		}else{
			appletv.showDialog('迅雷离线下载失败','');
		}
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