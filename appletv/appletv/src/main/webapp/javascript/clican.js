var appletv = {
	logEnable : true,
	simulate : false,
	serverurl : 'http://10.0.1.5/appletv',

	getDeviceUdid : function() {
		return atv.device.udid;
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
							appletv.logToServer("get response from base64 2");
							callback(xhr.responseDataAsBase64);
						} else {
							appletv.logToServer("get response from text 2");
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
							callback(xhr.responseDataAsBase64);
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

	showInputTextPage : function(label, instructions, callback, callbackName) {
		if (this.simulate) {
			window.location.href = this.serverurl
					+ '/ctl/simulator/input.xml?callback=' + callbackName;
		} else {
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = instructions;
			textEntry.label = label;
			textEntry.onSubmit = callback;
			textEntry.show();
		}
	}
};