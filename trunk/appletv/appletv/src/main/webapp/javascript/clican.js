var appletv = {
		loadAlbumXml:function(itemid,channelId,hd,serverurl) {
			this.makeRequest('http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=columnid='+channelId+'&itemid='+itemid+'&ishd='+hd,function(data){
				var album = JSON.parse(data);
				var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><head><script src=\"'+serverurl+'javascript/clican.js\"/></head><body>';
				xml+='<itemDetail id=\"itemdetail\">';
				xml+='<title>'+album['title']+'</title>';
				xml+='<summary>'+album['description']+'</summary>';
				xml+='<image style=\"moviePoster\">'+album['picurl']+'</image>';
				xml+='<table><columnDefinitions><columnDefinition width=\"50\"><title>其他信息</title></columnDefinition><columnDefinition width=\"50\"><title></title></columnDefinition></columnDefinitions><rows>';
				xml+='<row><label><![CDATA[导演:'+album['directors']+']]></label><label><![CDATA[年代:'+album['year']+']]></label></row>';
				xml+='<row><label><![CDATA[类型:'+album['type_desc']+']]></label><label><![CDATA[地区:'+album['area_desc']+']]></label></row>';
				xml+='<row><label><![CDATA[剧集:共'+album['size']+'集]]></label><label><![CDATA[主演:'+album['actors']+']]></label></row>';
				xml+='</rows></table><centerShelf><shelf id=\"album\"><sections><shelfSection><items>';
				xml+='<actionButton id=\"album_1\" onSelect=\"appletv.loadAlbumListXml('+itemid+','+channelid+','+hd+',2,\''+serverurl+'\');\" onPlay=\"atv.loadURL(\''+serverurl+'/tudou/albumlist.xml?st=2\');\"><title>标清</title></actionButton>';
				xml+='<actionButton id=\"album_2\" onSelect=\"atv.loadURL(\''+serverurl+'/tudou/albumlist.xml?st=3\');\" onPlay=\"atv.loadURL(\''+serverurl+'/tudou/albumlist.xml?st=3\');\"><title>高清</title></actionButton>';
				if(album['hd']==1){
					xml+='<actionButton id=\"album_3\" onSelect=\"atv.loadURL(\''+serverurl+'/tudou/albumlist.xml?st=4\');\" onPlay=\"atv.loadURL(\''+serverurl+'/tudou/albumlist.xml?st=4\');\"><title>超清</title></actionButton>';
				}
				xml+='</items></shelfSection></sections></shelf></centerShelf></itemDetail></body></atv>';
				atv.loadAndSwapXML(atv.parseXML(xml));
			});
		},
		
		loadAlbumListXml:function(itemid,channelId,hd,st,serverurl) {
			this.makeRequest('http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=columnid='+channelId+'&itemid='+itemid+'&ishd='+hd,function(data){
				var album = JSON.parse(data);
				var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><listScrollerSplit id=\"albumlist\"><header><simpleHeader horizontalAlignment=\"left\">';
				xml+='<title>'+album['title']+'</title>';
				xml+='<image>'+album['picurl']+'</image>';
				xml+='<itemDetail id=\"itemdetail\">';
				xml+='</simpleHeader></header><menu><sections><menuSection><items>';
				var items = album['albumitems'];
				for ( var i = 0; i < items.length; i++) {
					item = items[i];
					xml+='<imageTextImageMenuItem id=\"albumItem_'+i+'\" onPlay=\"atv.loadURL(\''+serverurl+'/tudou/play.xml?itemid='+item['itemid']+'&st='+st+'\');" onSelect=\"atv.loadURL(\''+serverurl+'/tudou/play.xml?itemid='+item['itemid']+'&st='+st+'\');\">';
					xml+='<leftImage>'+item['pciurl']+'</leftImage>';
					xml+='<label>第'+i+'集</label>;
					xml+='<rightImage></rightImage>';
					xml+='</imageTextImageMenuItem>';
				}
				xml+='</items></menuSection></sections></menu></listScrollerSplit></body></atv>';
				atv.loadAndSwapXML(atv.parseXML(xml));
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