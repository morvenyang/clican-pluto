var appletv = {
		loadData:function(itemid,channelId,hd) {
			atv.loadURL('http://10.0.1.5:9000/tudou/album.xml?itemid='+itemid+'&amp;channelId='+channelId+'&amp;hd='+hd);
		}
}