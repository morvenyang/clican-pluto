var smbClient = {
		loadIndexPage:function(smbUrl){
			var url = appletv.serverurl+'/noctl/smb/resource';
			if(smbUrl!=null&&smbUrl.length>0){
				url = smbUrl;
			}
			appletv.makeRequest(url,function(result){
				appletv.logToServer(result);
				var json = JSON.parse(result);
				var title = json['title'];
				if(title!=null){
					appletv.showDialog(json['title'],json['description']);
				}else{
					appletv.logToServer(json['items'].length);
					var data = {
							'serverurl': appletv.serverurl,
							'items' : json['items'],
							'smbPath' : json['smbPath']
						};
					var xml = new EJS({
						url : appletv.serverurl + '/template/smb/index.ejs'
					}).render(data);
					appletv.logToServer(xml);
					appletv.loadAndSwapXML(xml);
				}
			});
		},
		
}