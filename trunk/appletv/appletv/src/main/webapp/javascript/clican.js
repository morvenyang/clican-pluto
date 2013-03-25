var appletv = {
	logEnable : true,
	logSeverity : 'DEBUG',
	//browser,atv,native
	simulate : 'atv',
	//local server url
	serverurl : 'http://local.clican.org/appletv',
	//remote server url
	remoteserverurl : 'http://www.clican.org/appletv',
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
	getDeviceId: function(){
		if(appletv.simulate!='atv'){
			return '1234';
		}else{
			return atv.device.udid;
		}
	},
	
	// public method for encoding
	encode : function (input) {
	    var output = "";
	    var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
	    var i = 0;

	    input = appletv._utf8_encode(input);

	    while (i < input.length) {

	        chr1 = input.charCodeAt(i++);
	        chr2 = input.charCodeAt(i++);
	        chr3 = input.charCodeAt(i++);

	        enc1 = chr1 >> 2;
	        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	        enc4 = chr3 & 63;

	        if (isNaN(chr2)) {
	            enc3 = enc4 = 64;
	        } else if (isNaN(chr3)) {
	            enc4 = 64;
	        }

	        output = output +
	        appletv._keyStr.charAt(enc1) + appletv._keyStr.charAt(enc2) +
	        appletv._keyStr.charAt(enc3) + appletv._keyStr.charAt(enc4);

	    }

	    return output;
	},

	// public method for decoding
	decode : function (input) {
	    var output = "";
	    var chr1, chr2, chr3;
	    var enc1, enc2, enc3, enc4;
	    var i = 0;

	    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

	    while (i < input.length) {

	        enc1 = appletv._keyStr.indexOf(input.charAt(i++));
	        enc2 = appletv._keyStr.indexOf(input.charAt(i++));
	        enc3 = appletv._keyStr.indexOf(input.charAt(i++));
	        enc4 = appletv._keyStr.indexOf(input.charAt(i++));

	        chr1 = (enc1 << 2) | (enc2 >> 4);
	        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	        chr3 = ((enc3 & 3) << 6) | enc4;

	        output = output + String.fromCharCode(chr1);

	        if (enc3 != 64) {
	            output = output + String.fromCharCode(chr2);
	        }
	        if (enc4 != 64) {
	            output = output + String.fromCharCode(chr3);
	        }

	    }

	    output = appletv._utf8_decode(output);

	    return output;

	},

	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
	    string = string.replace(/\r\n/g,"\n");
	    var utftext = "";

	    for (var n = 0; n < string.length; n++) {

	        var c = string.charCodeAt(n);

	        if (c < 128) {
	            utftext += String.fromCharCode(c);
	        }
	        else if((c > 127) && (c < 2048)) {
	            utftext += String.fromCharCode((c >> 6) | 192);
	            utftext += String.fromCharCode((c & 63) | 128);
	        }
	        else {
	            utftext += String.fromCharCode((c >> 12) | 224);
	            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
	            utftext += String.fromCharCode((c & 63) | 128);
	        }

	    }

	    return utftext;
	},

	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
	    var string = "";
	    var i = 0;
	    var c = c1 = c2 = 0;

	    while ( i < utftext.length ) {

	        c = utftext.charCodeAt(i);

	        if (c < 128) {
	            string += String.fromCharCode(c);
	            i++;
	        }
	        else if((c > 191) && (c < 224)) {
	            c2 = utftext.charCodeAt(i+1);
	            string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
	            i += 2;
	        }
	        else {
	            c2 = utftext.charCodeAt(i+1);
	            c3 = utftext.charCodeAt(i+2);
	            string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
	            i += 3;
	        }

	    }
	    return string;
	},
	
	toGBK: function (I) {
		O = '';
		g = atv.localStorage['gbk'];
		var i = 0;
		while (i < I.length) {
			c = I[i];
			i++;
			h = c.charCodeAt(0);
			if (h < 0x80) {
				O = O + c;
			} else {
				l = I.charCodeAt(i);
				i++;
				if (h >= 0xa1 && h <= 0xa9 && l >= 0xa1 && l <= 0xfe) {
					O = O + g.charAt((h - 0xa1) * 94 + (l - 0xa1));
				} else if (h >= 0xb0 && h <= 0xf7 && l >= 0xa1 && l <= 0xfe) {
					O = O + g.charAt((h - 0xb0) * 94 + (l - 0xa1) + 846);
				} else if (h >= 0x81 && h <= 0xA0 && l >= 0x40 && l <= 0xFE) {
					O = O
							+ g.charAt((h - 0x81) * 190 + (l - 0x40)
									- (l > 0x7F ? 1 : 0) + 846 + 6768);
				} else if (h >= 0xAA && h <= 0xFE && l >= 0x40 && l <= 0xA0) {
					O = O
							+ g.charAt((h - 0xAA) * 96 + (l - 0x40)
									- (l > 0x7F ? 1 : 0) + 846 + 6768 + 6080);
				} else if (h >= 0xA8 && h <= 0xA9 && l >= 0x40 && l <= 0xA0) {
					O = O
							+ g
									.charAt((h - 0xA8) * 96 + (l - 0x40)
											- (l > 0x7F ? 1 : 0) + 846 + 6768
											+ 6080 + 8160);
				}
			}
		}
		return O;
	},
	
	base64Decode: function (I) {
		var k = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=", O = "", chr1, chr2, chr3 = "", enc1, enc2, enc3, enc4 = "", i = 0, I = I
				.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		do {
			enc1 = k.indexOf(I.charAt(i++));
			enc2 = k.indexOf(I.charAt(i++));
			enc3 = k.indexOf(I.charAt(i++));
			enc4 = k.indexOf(I.charAt(i++));
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
			O = O + String.fromCharCode(chr1);
			if (enc3 != 64) {
				O = O + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				O = O + String.fromCharCode(chr3);
			}
			chr1 = chr2 = chr3 = "";
			enc1 = enc2 = enc3 = enc4 = "";
		} while (i < I.length);
		return unescape(O);
	},
	
	getDeviceUdid : function() {
		if(this.simulate!='atv'){
			return '1234';
		}else{
			return atv.device.udid;
		}
	},

	logToServer : function(logText,severity) {
		if (this.logEnable) {
			if(severity==null){
				severity='DEBUG';
			}
			if(this.logSeverity=='DEBUG'){
				if(severity=='ERROR'||severity=='WARN'||severity=='INFO'||severity=='DEBUG'){
					this.makePostRequest(this.remoteserverurl + '/ctl/log.do', logText,
							function(data) {

							});
				}
			}else if(this.logSeverity=='INFO'){
				if(severity=='ERROR'||severity=='WARN'||severity=='INFO'){
					this.makePostRequest(this.remoteserverurl + '/ctl/log.do', logText,
							function(data) {

							});
				}
			}else if(this.logSeverity=='WARN'){
				if(severity=='ERROR'||severity=='WARN'){
					this.makePostRequest(this.remoteserverurl + '/ctl/log.do', logText,
							function(data) {

							});
				}
			}else if(this.logSeverity=='ERROR'){
				if(severity=='ERROR'){
					this.makePostRequest(this.remoteserverurl + '/ctl/log.do', logText,
							function(data) {

							});
				}
			}
			
		}
	},
	playMp3 : function(mp3url) {
		var plist = {
			type : "video-asset",
			"media-asset" : {
				"media-url" : mp3url,
				type : 'http-live-streaming',
				title : 'test',
				description : 'test',
				length : 100
			}
		};
		atv.loadAndSwapPlist(plist);
	},

	makeRequest : function(url, callback) {
		this.makeRequest(url, callback, null);
	},

	makeRequest : function(url, callback, headers) {
		if (!url) {
			throw "loadURL requires a url argument";
		}
		if(appletv.simulate=='native'){
			makeProxyRequest(url,callback,headers);
		}else{
			var xhr = new XMLHttpRequest();

			xhr.onreadystatechange = function() {
				try {
					if (xhr.readyState == 4) {
						if (xhr.status == 200) {
							if (xhr.responseText == null) {
								if(appletv.simulate=='atv'){
									gbkchar = atv.localStorage['gbk'];
								}
								if (!gbkchar) {
									appletv.makeRequest(appletv.serverurl+'/template/gbk.txt', function(gbkcontent){
										if(appletv.simulate=='atv'){
											atv.localStorage['gbk'] = gbkcontent;
										}
										callback(appletv.toGBK(appletv.base64Decode(xhr.responseDataAsBase64)));
									});
								}else{
									callback(appletv.toGBK(appletv.base64Decode(xhr.responseDataAsBase64)));
								}
							} else {
								callback(xhr.responseText);
							}
						} else {
							appletv.logToServer('xhr status:' + xhr.status
									+ ' for ' + url);
							callback(null);
						}
					}
				} catch (e) {
					appletv
							.logToServer('makeRequest caught exception while processing request for '
									+ url + '. Aborting. Exception: ' + e);
					xhr.abort();
					callback(null);
				}
			}

			xhr.open("GET", url, true);
			if(headers!=null){
				for ( var key in headers) {
					xhr.setRequestHeader(key, headers[key]);
				}
			}
			xhr.send();
			return xhr;
		}
	},

	makePostRequest : function(url, content, callback) {
		if (!url) {
			throw "loadURL requires a url argument";
		}
		if(appletv.simulate=='native'){
			makeProxyPostRequest(url,content,callback);
		}else{
			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function() {
				try {
					if (xhr.readyState == 4) {
						if (xhr.status == 200) {
							if (xhr.responseText == null) {
								gbkchar = atv.localStorage['gbk'];
								if (!gbkchar) {
									appletv.makeRequest(appletv.serverurl+'/template/gbk.txt', function(gbkcontent){
										atv.localStorage['gbk'] = gbkcontent;
										callback(appletv.toGBK(appletv.base64Decode(xhr.responseDataAsBase64)));
									});
								}else{
									callback(appletv.toGBK(appletv.base64Decode(xhr.responseDataAsBase64)));
								}
							}else{
								callback(xhr.responseText);
							}
						} else {
							appletv.logToServer('xhr status:' + xhr.status
									+ ' for ' + url);
							callback(null);
						}
					}
				} catch (e) {
					appletv
					.logToServer('makeRequest caught exception while processing request for '
							+ url + '. Aborting. Exception: ' + e);
					xhr.abort();
					callback(null);
				}
			}
			xhr.open("POST", url, true);
			if(content!=null){
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			}
			xhr.send(content);
			return xhr;
		}
		
	},
	
	shareToSinaWeibo : function(title, shareURL, imageURL) {
		var url = appletv.remoteserverurl + '/ctl/weibo/createStatus.xml?deviceId='
				+ atv.device.udid + '&title=' + encodeURIComponent(title)
				+ '&shareURL=' + encodeURIComponent(shareURL) + '&imageURL='
				+ encodeURIComponent(imageURL);
		atv.loadURL(url);
	},
	
	showOptionPage: function(title,desc,options){
		try{
			var data = {"title":title,"desc":desc,"options":options,"serverurl":appletv.serverurl};
			var xml = new EJS({
				url : appletv.serverurl
						+ '/template/option.ejs'
			}).render(data);
			appletv.loadAndSwapXML(xml);
		}catch(e){
			appletv.logToServer('Show option page error :'+e);
		}
		
	},
	
	playM3u8 : function(url,proxy){
		if(proxy==null||proxy.length==0){
			if(appletv.serverurl.indexOf('clican.org')<=0){
				proxy = appletv.serverurl;
			}
		}
		if(proxy!=null&&proxy.length>0) {
			var options = [];
			var encodeUrl = url.replace(new RegExp('&', 'g'),'&amp;');
			options.push({"title":"直接播放","script":"appletv.loadAndSwapXML(appletv.makePlayXml('"+encodeUrl+"'));"});
			url = proxy+"/noctl/proxy/play.m3u8?url="+encodeURIComponent(url);
			options.push({"title":"本地服务器代理下载播放","script":"appletv.loadXML(appletv.makePlayXml('"+url+"'));"});
			appletv.showOptionPage('播放源选择','',options);
		}else{
			appletv.loadAndSwapXML(appletv.makePlayXml(url));
		}
	},
	
	playMp4 : function(url,proxy){
		if(proxy==null||proxy.length==0){
			if(appletv.serverurl.indexOf('clican.org')<=0){
				proxy = appletv.serverurl;
			}
		}
		if(proxy!=null&&proxy.length>0) {
			var options = [];
			var encodeUrl = url.replace(new RegExp('&', 'g'),'&amp;');
			options.push({"title":"直接播放","script":"appletv.loadAndSwapXML(appletv.makePlayXml('"+encodeUrl+"'));"});
			url = proxy+"/noctl/proxy/play.mp4?url="+encodeURIComponent(url);
			options.push({"title":"本地服务器代理下载播放","script":"appletv.loadXML(appletv.makePlayXml('"+url+"'));"});
			appletv.showOptionPage('播放源选择','',options);
		}else{
			appletv.loadAndSwapXML(appletv.makePlayXml(url));
		}
	},
	
	makePlayXml : function(url) {
		var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"play\"><httpLiveStreamingVideoAsset><mediaURL><![CDATA[";
		xml += url;
		xml += "]]></mediaURL></httpLiveStreamingVideoAsset></videoPlayer></body></atv>";
		return xml
	},

	makeDialog : function(message, description) {
		if (!message) {
			message = "";
		}
		if (!description) {
			description = "";
		}

		var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
	        <atv> \
	        <body> \
	        <dialog id="com.sample.error-dialog"> \
	        <title><![CDATA['
				+ message
				+ ']]></title> \
	        <description><![CDATA['
				+ description
				+ ']]></description> \
	        </dialog> \
	        </body> \
	        </atv>';

		return atv.parseXML(errorXML);
	},

	loadXML : function(xml) {
		if (this.simulate!='atv') {
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/postxml.xml', xml, 
					function(result){
						window.open(appletv.remoteserverurl+'/ctl/showxml.xml');
					});
		} else {
			atv.loadXML(atv.parseXML(xml));
		}
	},
	
	loadAndSwapXML : function(xml) {
		if (this.simulate!='atv') {
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/postxml.xml', xml, 
					function(result){
						window.open(appletv.remoteserverurl+'/ctl/showxml.xml');
					});
		} else {
			atv.loadAndSwapXML(atv.parseXML(xml));
		}
	},

	loadURL : function(url) {
		if (this.simulate!='atv') {
			window.location.href = url;
		} else {
			atv.loadURL(url);
		}
	},
	showLoading :function(){
		if (this.simulate=='atv') {
			appletv.showDialog('加载中...','Loading...');
		}
	},
	showDialog : function(message, description) {
		if (this.simulate!='atv') {
			alert(message);
		} else {
			atv.loadXML(this.makeDialog(message, description));
		}

	},
	
	showSwapDialog : function(message, description) {
		if (this.simulate!='atv') {
			alert(message);
		} else {
			atv.loadAndSwapXML(this.makeDialog(message, description));
		}

	},
	
	setValue:function(key,value){
		if(this.simulate=='atv'){
			atv.localStorage[key] = value;
		}else{
			var payload = {"name":key,"value":value};
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/setValue.do', JSON.stringify(payload), function(result){
				
			});
		}
	},
	
	getValue:function(key,callback){
		if(this.simulate=='atv') {
			var value = atv.localStorage[key];
			appletv.logToServer(JSON.stringify(value));
			callback(value);
		}else{
			appletv.makeRequest(appletv.remoteserverurl+'/ctl/getValue.do?name='+key, function(result){
				if(result==null||result.length==0){
					callback(null);
				}else{
					callback(JSON.parse(result));
				}
				
			});
		}
	},
	
	setSessionValue:function(key,value){
		if(this.simulate=='atv'){
			atv.sessionStorage[key] = value;
		}
	},
	
	getSessionValue:function(key){
		if(this.simulate=='atv') {
			return atv.sessionStorage[key];
		}else{
			return null;
		}
	},

	showInputTextPage : function(label, instructions, callback, callbackName,initialText) {
		if (this.simulate!='atv') {
			window.location.href = this.remoteserverurl
					+ '/ctl/simulator/input.xml?callback=' + callbackName;
		} else {
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = instructions;
			textEntry.label = label;
			textEntry.defaultValue=initialText;
			textEntry.onSubmit = callback;
			textEntry.show();
		}
	},
	
	findall : function(restr, data) {
		var rss = new Array();
		var re = new RegExp(restr, "g");
		while (true) {
			rs = re.exec(data);
			if (rs == null)
				break;
			rs.shift();
			rss.push(rs);
		}
		;
		return rss;
	},
	
	find : function(restr, data) {
		var rss = new Array();
		var re = new RegExp(restr, "g");
		rs = re.exec(data);
		if(rs!=null){
			return rs[1];
		}else{
			return '';
		}
		
	},
	
	subIndexString : function(data,startstr) {
		start = data.indexOf(startstr);
		if(start<0){
			return '';
		}
		return data.substring(start+startstr.length);
	},
	
	substring : function(data,startstr,endstr) {
		start = data.indexOf(startstr);
		if(start<0){
			return '';
		}
		end = data.indexOf(endstr,start+startstr.length);
		if(end<0){
			return '';
		}
		return data.substring(start+startstr.length,end);
	},
	
	substringByTag : function(data,startstr,endstr,tagName) {
		start = data.indexOf(startstr);
		if(start<0){
			return '';
		}
		offset = start+startstr.length;
		while(true){
			end = data.indexOf(endstr,offset);
			if(end<0){
				return '';
			}
			temp =  data.substring(start+startstr.length,end);
			
			a = appletv.getCount(temp,'<'+tagName);
			b = appletv.getCount(temp,'</'+tagName);
			if(a==b){
				return temp;
			}else{
				offset = end+endstr.length;
			}
		}
		return '';
	},
	
	getSubValues : function(data,startstr,endstr) {
		var values=[];
		start = 0;
		while(start>=0){
			start = data.indexOf(startstr,start);
			if(start<0){
				break;
			}
			end = data.indexOf(endstr,start+startstr.length);
			if(end<0){
				break;
			}
			temp =  data.substring(start+startstr.length,end);
			values.push(temp);
			start = end+endstr.length;
		}
		return values
	},
	
	getSubValuesByTag : function(data,startstr,endstr,tagName) {
		var values=[];
		start = 0;
		while(start>=0){
			start = data.indexOf(startstr,start);
			if(start<0){
				break;
			}
			offset = start+startstr.length;
			while(true){
				end = data.indexOf(endstr,offset);
				if(end<0){
					break;
				}
				temp =  data.substring(start+startstr.length,end);
				
				a = appletv.getCount(temp,'<'+tagName);
				b = appletv.getCount(temp,'</'+tagName);
				if(a==b){
					values.push(temp);
					break;
				}else{
					offset = end+endstr.length;
				}
			}
			start = end+endstr.length;
		}
		return values
	},
	
	getCount: function(data,str){
		var count = 0;
		var offset = 0
		while(offset>=0){
			offset = data.indexOf(str,offset);
			if(offset>=0){
				count++;
				offset=offset+str.length;
			}
		}
		return count;
	},
	
	getTextInTag: function(data){
		var left = 0;
		var right = 0;
		var result = '';
		var charArray = data.split('');
		for(i=0;i<charArray.length;i++){
			if(charArray[i]=='<'){
				left++;
			}else if(charArray[i]=='>'){
				right++;
			}else{
				if(left==right){
					result = result + charArray[i];
				}
			}
		}
		return result;
	},
	
	loadFavoritePage:function(){
		appletv.getValue('clican.config.favorites', function(favorites){
			if(favorites==null){
				appletv.getConfig('clican.config.favorites',function(favorites){
					var videos = [];
					if(favorites!=null){
						for(i=0;i<favorites.length;i++){
							var video = {"title":favorites[i]['title'],"pic":favorites[i]['pic'],"script":appletv.decode(favorites[i]['script'])};
							videos.push(video);
						}
					}
					if(videos.length==0){
						appletv.showDialog('没有相关收藏', '');
					}else{
						var data = {'serverurl':appletv.serverurl,'videos':videos};
						var xml = new EJS({url: appletv.serverurl+'/template/favorite.ejs'}).render(data);
						appletv.loadAndSwapXML(xml);
					}
				});
			}else{
				var videos = [];
				if(favorites!=null){
					for(i=0;i<favorites.length;i++){
						var video = {"title":favorites[i]['title'],"pic":favorites[i]['pic'],"script":appletv.decode(favorites[i]['script'])};
						videos.push(video);
					}
				}
				if(videos.length==0){
					appletv.showDialog('没有相关收藏', '');
				}else{
					var data = {'serverurl':appletv.serverurl,'videos':videos};
					var xml = new EJS({url: appletv.serverurl+'/template/favorite.ejs'}).render(data);
					appletv.loadAndSwapXML(xml);
				}
			}
		});
		
	},
	
	addToFavorite: function(title,pic,script){
		var favorite = {"title":title,"pic":pic,"script":script};
		appletv.getValue('clican.config.favorites', function(favorites){
			if(favorites==null){
				appletv.getConfig('clican.config.favorites', function(content){
					favorites = content;
					if(favorites==null){
						favorites = [];
					}
					var found = false;
					for(i=0;i<favorites.length;i++){
						if(favorites[i].script==script){
							found = true;
							break;
						}
					}
					if(found){
						appletv.showDialog('已经收藏,无法重复收藏', '');
					}else{
						favorites.push(favorite);
						appletv.setValue('clican.config.favorites',favorites);
						appletv.saveConfig('clican.config.favorites', favorites, function(content){
							appletv.showDialog('收藏成功', '');
						});
					}
				});
			}else{
				var found = false;
				for(i=0;i<favorites.length;i++){
					if(favorites[i].script==script){
						found = true;
						break;
					}
				}
				if(found){
					appletv.showDialog('已经收藏,无法重复收藏', '');
				}else{
					favorites.push(favorite);
					appletv.setValue('clican.config.favorites',favorites);
					appletv.saveConfig('clican.config.favorites', favorites, function(content){
						appletv.showDialog('收藏成功', '');
					});
				}
			}
		});
	},
	
	delFromFavorite: function(index){
		appletv.getValue('clican.config.favorites', function(favorites){
			if(favorites==null){
				appletv.showDialog('删除失败', '');
			}
			try{
				var favorite = favorites[index];
				favorites.splice(index, 1);
				appletv.setValue('clican.config.favorites',favorites);
				appletv.saveConfig('clican.config.favorites',favorites,function(content){
					appletv.loadFavoritePage();
				});
			}catch(e){
				appletv.showDialog('删除失败', '');
			}
		});
	},
	
	saveConfig: function(key,value,callback){
		var config = {"deviceId":appletv.getDeviceId(),"key":key,"value":value};
		appletv.makePostRequest(appletv.remoteserverurl+'/ctl/config/saveConfig.do', JSON.stringify(config),callback);
	},
	
	getConfig: function(key,callback){
		appletv.makeRequest(appletv.remoteserverurl+'/ctl/config/getConfig.do?deviceId='+appletv.getDeviceId()+'&key='+key,function(content){
			if(content==null||content.length==0){
				callback(null);
			}else{
				callback(JSON.parse(content));
			}
		});
	},
	
	loadLocalServerIndexPage : function(){
		appletv.showLoading();
		appletv.makeRequest(appletv.remoteserverurl+'/ctl/localserver/retrive.do', function(innerIP){
			if(innerIP!=null&&innerIP.length>0){
				var url = 'http://'+innerIP+':8080/local.xml';
				appletv.loadURL(url);
			}else{
				appletv.showDialog('未侦测到本地服务器', '请去网站http://clican.org参考详细如何安装使用本地服务器.');
			}
		});
	},
};