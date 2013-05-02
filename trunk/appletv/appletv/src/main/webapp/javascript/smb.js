var smbClient = {
		loadIndexPage:function(smbUrl){
			appletv.showLoading();
			var url = appletv.serverurl+'/noctl/smb/resource';
			if(smbUrl!=null&&smbUrl.length>0){
				url = smbUrl;
			}
			
			appletv.makeRequest(url,function(result){
				if(result==null||result.length==0||result=='null'){
					appletv.showDialog('没有相关内容','');
					return;
				}
				var json = JSON.parse(result);
				var title = json['title'];
				if(title!=null){
					appletv.showDialog(json['title'],json['description']);
				}else{
					var data = {
							'serverurl': appletv.serverurl,
							'items' : json['items'],
							'smbPath' : json['smbPath']
						};
					var xml = new EJS({
						url : appletv.serverurl + '/template/smb/index.ejs'
					}).render(data);
					appletv.loadAndSwapXML(xml);
				}
			});
		},
		
		play: function(url){
			appletv.showLoading();
			var index = url.indexOf('url=');
			if(index>0){
				var smbPath = url.substring(index+4);
				var subTitleDownloadUrl = appletv.serverurl+'/noctl/subtitle/download.txt?url='+smbPath;
				var subTitles = [];
				subTitles.push(
						{
							"title" : "无字幕",
							"url" : ""
						});
				subTitles.push(
						{
							"title" : "射手网字幕",
							"url" : subTitleDownloadUrl
						});
				subTitleClient.playWithSubTitles(url,subTitles);
			}else{
				appletv.showDialog('无法播放','解析filePath出错');
			}
			
		},
}