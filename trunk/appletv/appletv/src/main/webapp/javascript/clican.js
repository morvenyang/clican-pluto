var appletv = {
	logEnable : true,
	simulate : false,
	serverurl : 'http://10.0.1.5/appletv',

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

	logToServer : function(logText) {
		if (this.logEnable) {
			this.makePostRequest(this.serverurl + '/ctl/log.do', logText,
					function(data) {

					});
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

	makeRequest : function(url, callback, overrideMimeType) {
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
		var url = appletv.serverurl + '/ctl/weibo/createStatus.xml?deviceId='
				+ atv.device.udid + '&title=' + encodeURIComponent(title)
				+ '&shareURL=' + encodeURIComponent(shareURL) + '&imageURL='
				+ imageURL;
		atv.loadURL(url);
	},

	makePlayXml : function(url) {
		var xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"play\"><httpLiveStreamingVideoAsset><mediaURL><![CDATA[";
		xml += url;
		xml += "]]></mediaURL></httpLiveStreamingVideoAsset></videoPlayer></body></atv>";
		return atv.parseXML(xml);
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
			document.documentElement.innerHTML = '<html><head></head><body><textarea rows=\"100\" cols=\"150\">'
					+ xml + '</textarea></body></html>';
		} else {
			atv.loadXML(atv.parseXML(xml));
		}
	},
	
	loadAndSwapXML : function(xml) {
		if (this.simulate) {
			document.documentElement.innerHTML = '<html><head></head><body><textarea rows=\"100\" cols=\"150\">'
					+ xml + '</textarea></body></html>';
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
		}
	},
	
	getValue:function(key){
		if(!this.simulate) {
			return atv.localStorage[key];
		}else{
			return null;
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
			window.location.href = this.serverurl
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
	
};