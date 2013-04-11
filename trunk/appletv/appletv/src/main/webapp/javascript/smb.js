var smbClient = {
		
		function:loadIndexPage(smbUrl){
			var url = appletv.serverurl+'/notcl/smb/resource';
			if(smbUrl!=null&&smbUrl.length>0){
				url = smbUrl;
			}
			appletv.makeRequest(url,function(result){
				var json = JSON.parse(result);
				var title = json['title'];
				if(title!=null){
					appletv.showDialog(json['title'],json['description']);
				}else{
					for(var i=0;i<json.length;i++){
						var item = json[i];
						
					}
				}
			});
		},
		
}