var myPhotoClient = {
		loadMyPhotos : function() {
			
			var url = appletv.serverurl+'/noctl/photo/list.json';
			appletv.logToServer(url);
			appletv.makeRequest(url,
					function(jsonContent) {
						var itemList = JSON.parse(jsonContent);
						var photoDicts = [];
						for(var i=0;i<itemList.length;i++){
								var item = itemList[i];
								var id = 'p'+i;
								var photoDict = {"id":id,type:'photo',assets:[{width:1024,height:768,src:item}]};
								photoDicts.push(photoDict);
						}
						if(photoDicts.length==0){
							appletv.showDialog('没有相关照片','');
							return;
						}
						var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
						fullScreenMediaBrowser.onItemSelection = function(photoID) {
							
						};
						appletv.logToServer('pl='+photoDicts.length);
						fullScreenMediaBrowser.show(photoDicts, 0);
					});
		}
}