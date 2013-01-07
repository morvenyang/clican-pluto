var appletv = {
		loadData:function(itemid,channelId,hd) {
			this.makeRequest('http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=columnid='+channelId+'&itemid='+itemid+'&ishd='+hd,function(data){
				atv.loadURL('http://10.0.1.5:9000/appletv/tudou/album.xml?data='+encodeURI(data));
			});
		},
		
		makeRequest: function(url, callback) {
	        if ( !url ) {
	            throw "loadURL requires a url argument";
	        }

	        var xhr = new XMLHttpRequest();
	        xhr.onreadystatechange = function() {
	            try {
	                if (xhr.readyState == 4 ) {
	                    if ( xhr.status == 200) {
	                        callback(xhr.responseText);
	                    } else {
	                        console.log("makeRequest received HTTP status " + xhr.status + " for " + url);
	                        callback(null);
	                    }
	                }
	            } catch (e) {
	                console.error('makeRequest caught exception while processing request for ' + url + '. Aborting. Exception: ' + e);
	                xhr.abort();
	                callback(null);
	            }
	        }
	        xhr.open("GET", url, true);
	        xhr.send();
	        return xhr;
	    }
}