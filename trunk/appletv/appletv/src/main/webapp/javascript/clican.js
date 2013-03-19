var appletv = {
	logEnable : true,
	logSeverity : 'DEBUG',
	simulate : false,
	//local server url
	serverurl : 'http://local.clican.org/appletv',
	//remote server url
	remoteserverurl : 'http://www.clican.org/appletv',
	getDeviceId: function(){
		if(appletv.simulate){
			return '1234';
		}else{
			return atv.device.udid;
		}
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
		if(this.simulate){
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
		var xhr = new XMLHttpRequest();

		xhr.onreadystatechange = function() {
			try {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						if (xhr.responseText == null) {
							if(!appletv.simulate){
								gbkchar = atv.localStorage['gbk'];
							}
							if (!gbkchar) {
								appletv.makeRequest(appletv.serverurl+'/template/gbk.txt', function(gbkcontent){
									if(!appletv.simulate){
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
	},

	makePostRequest : function(url, content, callback) {
		if (!url) {
			throw "loadURL requires a url argument";
		}

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
	},
	
	shareToSinaWeibo : function(title, shareURL, imageURL) {
		var url = appletv.remoteserverurl + '/ctl/weibo/createStatus.xml?deviceId='
				+ atv.device.udid + '&title=' + encodeURIComponent(title)
				+ '&shareURL=' + encodeURIComponent(shareURL) + '&imageURL='
				+ imageURL;
		atv.loadURL(url);
	},

	playM3u8 : function(url,proxy){
		if(proxy==null||proxy.length==0){
			if(appletv.serverurl.indexOf('local')==-1){
				proxy = appletv.serverurl;
			}
		}
		if(proxy!=null&&proxy.length>0) {
			appletv.logToServer('play by proxy');
			url = proxy+"/noctl/proxy/play.m3u8?url="+encodeURIComponent(url);
			appletv.loadXML(appletv.makePlayXml(url));
		}else{
			appletv.logToServer('play by original url');
			appletv.loadXML(appletv.makePlayXml(url));
		}
	},
	
	playMp4 : function(url,proxy){
		if(proxy==null||proxy.length==0){
			if(appletv.serverurl.indexOf('local')==-1){
				proxy = appletv.serverurl;
			}
		}
		if(proxy!=null&&proxy.length>0) {
			appletv.logToServer('play by proxy');
			url = proxy+"/noctl/proxy/play.mp4?url="+encodeURIComponent(url);
			appletv.loadXML(appletv.makePlayXml(url));
		}else{
			appletv.logToServer('play by original url');
			appletv.loadXML(appletv.makePlayXml(url));
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
		if (this.simulate) {
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/postxml.xml', xml, 
					function(result){
						window.open(appletv.remoteserverurl+'/ctl/showxml.xml');
					});
		} else {
			atv.loadXML(atv.parseXML(xml));
		}
	},
	
	loadAndSwapXML : function(xml) {
		if (this.simulate) {
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/postxml.xml', xml, 
					function(result){
						window.open(appletv.remoteserverurl+'/ctl/showxml.xml');
					});
		} else {
			atv.loadAndSwapXML(atv.parseXML(xml));
		}
	},

	loadURL : function(url) {
		if (this.simulate) {
			window.location.href = url;
		} else {
			atv.loadURL(url);
		}
	},
	showLoading :function(){
		if (!this.simulate) {
			appletv.showDialog('加载中...','Loading...');
		}
	},
	showDialog : function(message, description) {
		if (this.simulate) {
			alert(message);
		} else {
			atv.loadXML(this.makeDialog(message, description));
		}

	},
	
	showSwapDialog : function(message, description) {
		if (this.simulate) {
			alert(message);
		} else {
			atv.loadAndSwapXML(this.makeDialog(message, description));
		}

	},
	
	setValue:function(key,value){
		if(!this.simulate){
			atv.localStorage[key] = value;
		}else{
			var payload = {"name":key,"value":value};
			appletv.makePostRequest(appletv.remoteserverurl+'/ctl/setValue.do', JSON.stringify(payload), function(result){
				
			});
		}
	},
	
	getValue:function(key,callback){
		if(!this.simulate) {
			var value = atv.localStorage[key];
			appletv.logToServer(JSON.stringify(value));
			callback(value);
		}else{
			appletv.makeRequest(appletv.remoteserverurl+'/ctl/getValue.do?name='+key, function(result){
				callback(JSON.parse(result));
			});
		}
	},
	
	setSessionValue:function(key,value){
		if(!this.simulate){
			atv.sessionStorage[key] = value;
		}
	},
	
	getSessionValue:function(key){
		if(!this.simulate) {
			return atv.sessionStorage[key];
		}else{
			return null;
		}
	},

	showInputTextPage : function(label, instructions, callback, callbackName,initialText) {
		if (this.simulate) {
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
	
};