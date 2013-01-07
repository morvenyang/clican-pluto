var appletv = {
		loadData:function(itemid,channelId,hd) {
			atv.loadURL('http://10.0.1.5:9000/appletv/tudou/album.xml?itemid='+itemid+'&channelId='+channelId+'&hd='+hd);
		}
}