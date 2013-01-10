var appletv = {
		logEnable:true,
		
		logToServer:function(logText,serverurl){
			if(this.logEnable){
				this.makePostRequest(serverurl+'/tudou/log.do',logText,function(data){
					
				});
			}
		},
		loadAlbumXml:function(itemid,channelId,hd,serverurl) {
			this.makeRequest('http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=&columnid='+channelId+'&itemid='+itemid+'&ishd='+hd,function(data){
				var album = JSON.parse(data);
				var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><head><script src=\"'+serverurl+'/javascript/clican.js\"/></head><body>';
				xml+='<itemDetail id=\"itemdetail\">';
				xml+='<title>'+album['title']+'</title>';
				xml+='<summary>'+album['description']+'</summary>';
				xml+='<image style=\"moviePoster\">'+album['picurl']+'</image>';
				xml+='<table><columnDefinitions><columnDefinition width=\"50\"><title>其他信息</title></columnDefinition><columnDefinition width=\"50\"><title></title></columnDefinition></columnDefinitions><rows>';
				xml+='<row><label><![CDATA[导演:'+album['directors']+']]></label><label><![CDATA[年代:'+album['year']+']]></label></row>';
				xml+='<row><label><![CDATA[类型:'+album['type_desc']+']]></label><label><![CDATA[地区:'+album['area_desc']+']]></label></row>';
				xml+='<row><label><![CDATA[剧集:共'+album['albumitems'].length+'集]]></label><label><![CDATA[主演:'+album['actors']+']]></label></row>';
				xml+='</rows></table><centerShelf><shelf id=\"album\"><sections><shelfSection><items>';
				xml+='<actionButton id=\"album_1\" onSelect=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',2,\''+serverurl+'\');\" onPlay=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',2,\''+serverurl+'\');\"><title>标清</title></actionButton>';
				xml+='<actionButton id=\"album_2\" onSelect=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',3,\''+serverurl+'\');\" onPlay=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',3,\''+serverurl+'\');\"><title>高清</title></actionButton>';
				if(album['hd']==1){
					xml+='<actionButton id=\"album_3\" onSelect=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',4,\''+serverurl+'\');\" onPlay=\"appletv.loadAlbumListXml('+itemid+','+channelId+','+hd+',4,\''+serverurl+'\');\"><title>超清</title></actionButton>';
				}
				xml+='</items></shelfSection></sections></shelf></centerShelf></itemDetail></body></atv>';
				atv.loadXML(atv.parseXML(xml));
			});
		},
		loadTest:function(){
			
		},
		
		loadAlbumListXml:function(itemid,channelId,hd,st,serverurl) {
			this.makeRequest('http://minterface.tudou.com/iteminfo?sessionid=GTR7J672EMAAA&origin=&columnid='+channelId+'&itemid='+itemid+'&ishd='+hd,function(data){
				var album = JSON.parse(data);
				var xml = '<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><listScrollerSplit id=\"albumlist\"><header><simpleHeader horizontalAlignment=\"left\">';
				xml+='<title>'+album['title']+'</title>';
				xml+='<image>'+album['picurl']+'</image>';
				xml+='</simpleHeader></header><menu><sections><menuSection><items>';
				var items = album['albumitems'];
				var item;
				for ( var i = 0; i < items.length; i++) {
					item = items[i];
					xml+='<imageTextImageMenuItem id=\"albumItem_'+i+'\" onPlay=\"atv.loadURL(\''+serverurl+'/tudou/play.xml?itemid='+item['itemid']+'&amp;st='+st+'\');" onSelect=\"atv.loadURL(\''+serverurl+'/tudou/play.xml?itemid='+item['itemid']+'&amp;st='+st+'\');\">';
					xml+='<leftImage>'+item['picurl']+'</leftImage>';
					xml+='<label>第'+(i+1)+'集</label>';
					xml+='<rightImage></rightImage>';
					xml+='<imageSeparatorText></imageSeparatorText>'
					xml+='</imageTextImageMenuItem>';
				}
				xml+='</items></menuSection></sections></menu></listScrollerSplit></body></atv>';
				atv.loadXML(atv.parseXML(xml));
			});
		},
		
		makeRequest: function(url, callback) {
	        if ( !url ) {
	            throw "loadURL requires a url argument";
	        }
	        atv.loadXML(this.makeDialog('加载中...','Loading...'));
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
	    },
	    
	    makePostRequest: function(url,content, callback) {
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
	        xhr.open("POST", url, true);
	        xhr.send(content);
	        return xhr;
	    },
	    
	    playQQVideo: function(playDescUrl,serverurl){
	    	this.makeRequest(playDescUrl,function(data){
	    		var urlIndexStart = data.indexOf("\"url\":\"");
	    		urlIndexStart += 7;
	    		var urlIndexEnd = data.indexOf("\"",urlIndexStart);
	    		var url = data.substring(urlIndexStart,urlIndexEnd);
	    		atv.loadXML(appletv.makePlayXml(url));
	    	});
	    },
	    
	    makePlayXml: function(url){
	    	var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"play\"><httpLiveStreamingVideoAsset><mediaURL>";
	    	xml+=url;
	    	xml+="</mediaURL></httpLiveStreamingVideoAsset></videoPlayer></body></atv>";
	    	return atv.parseXML(errorXML);
	    },
	    
	    makeDialog: function(message, description) {
	        if ( !message ) {
	            message = "";
	        }
	        if ( !description ) {
	            description = "";
	        }

	        var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
	        <atv> \
	        <body> \
	        <dialog id="com.sample.error-dialog"> \
	        <title><![CDATA[' + message + ']]></title> \
	        <description><![CDATA[' + description + ']]></description> \
	        </dialog> \
	        </body> \
	        </atv>';

	        return atv.parseXML(errorXML);
	    }
};