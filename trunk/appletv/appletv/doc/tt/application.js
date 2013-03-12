atv.onGenerateRequest = function(request) {
	var videoFormat = (atv.device.preferredVideoPreviewFormat) ? atv.device.preferredVideoPreviewFormat
			: atv.device.preferredVideoFormat;
	if (videoFormat != "HD") {
		request.url = request.url.replace('-hd.xml', '-sd.xml');
	} else {
		request.url = request.url.replace('-sd.xml', '-hd.xml');
	}
};
atv.config = {
	"doesJavaScriptLoadRoot" : true,
	"DEBUG_LEVEL" : 4,
	"ROOT_URL" : "http://serv.ottnt.com:8580/index.xml",
};
baseURL = 'http://trailers.apple.com:8000';
atv.sessionStorage['baseURL'] = baseURL;
function makeErrorDocument(message, description) {
	if (!message) {
		message = "";
	}
	;
	if (!description) {
		description = "";
	}
	;
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?>     <atv> <body>     <dialog id="errorDialog"> <title><![CDATA['
			+ message
			+ ']]></title> <description><![CDATA['
			+ description
			+ ']]></description>     </dialog> </body>     </atv>';
	return atv.parseXML(errorXML);
};
atv.player.currentAssetChanged = function() {
	lastTime = 0;
	lastSubS = -1;
	lastSubE = -1;
	if (atv.device.udid == 'DY3HJY89DRHN')
		logger.debug('atv.player.currentAssetChanged');
};
atv.player.willStartPlaying = function() {
	if (atv.device.udid == 'DY3HJY89DRHN')
		logger.debug('atv.player.willStartPlaying');
	lastTime = 0;
	lastSubS = -1;
	lastSubE = -1;
	baseURL = atv.sessionStorage['baseURL'];
	var d = new Date();
	atv.localStorage["asset"] = "test:" + d;
	var playlist = atv.player.asset['playlist'];
	if (playlist != null) {
		atv.player.loadMoreAssets = loadNextPlaylistArrayItem;
	} else
		atv.player.loadMoreAssets = null;
	var o = atv.player.asset['oword'];
	var sub = atv.player.asset['subtitle'];
	var rootV = new atv.View();
	rootV.subviews = [];
	atv.player.overlay = rootV;
	try {
		if (o != null) {
			var screenFrame = atv.device.screenFrame;
			var overlayHeight = screenFrame.height * 0.07;
			var overlay = new atv.View();
			overlay.frame = {
				x : screenFrame.x,
				y : screenFrame.y + screenFrame.height - overlayHeight,
				width : screenFrame.width,
				height : overlayHeight
			};
			overlay.backgroundColor = {
				red : 0.188,
				green : 0.188,
				blue : 0.188,
				alpha : 1
			};
			var messageAttributes = {
				pointSize : 22.0,
				color : {
					red : 1,
					blue : 1,
					green : 1
				}
			};
			var message = new atv.TextView();
			var topPadding = overlay.frame.height * 0.35;
			var horizontalPadding = overlay.frame.width * 0.05;
			message.frame = {
				x : horizontalPadding,
				y : 0,
				width : overlay.frame.width - (2 * horizontalPadding),
				height : overlay.frame.height - topPadding
			};
			message.attributedString = {
				string : o,
				attributes : messageAttributes
			};
			var numberOfSeconds = 0;
			overlay.subviews = [ message ];
			var rootV = new atv.View();
			rootV.subviews = [ overlay ]
			atv.player.overlay = rootV;
			atv.setTimeout(function() {
				atv.player.overlay.subviews = [];
			}, 5000);
		}
	} catch (e) {
		atv.loadXML(makeErrorDocument("d", e));
	}
};
function loadNextPlaylistArrayItem(callback) {
	if (atv.device.udid == 'DY3HJY89DRHN')
		logger.debug('loadNextPlaylistArrayItem');
	var d = new Date();
	try {
		asset = atv.player.asset;
		if (asset['playlist']) {
			b = [];
			for ( var i = 0; i < asset['playlist'].length; i++) {
				pitem = asset['playlist'][i];
				if (pitem[0] == 'atvu') {
					atvu.loadAction(pitem[1], '', [ pitem[2], function(ret) {
						if (ret == null)
							callback.success(null);
						url = ret[0];
						name = ret[1];
						desc = ret[2];
						mlength = ret[3];
						pl = ret[4];
						if (!name)
							name = '';
						if (pl)
							c = {
								playlist : pl,
								type : "video-asset",
								"media-asset" : {
									"media-url" : url,
									type : 'http-live-streaming',
									title : name,
									description : name,
									length : mlength
								}
							};
						else
							c = {
								type : "video-asset",
								"media-asset" : {
									"media-url" : url,
									type : 'http-live-streaming',
									title : name,
									description : desc,
									length : mlength
								}
							};
						logger.debug(JSON.stringify(c));
						callback.success([ c ]);
					} ], 1);
					return;
				} else {
					url = asset['playlist'][i][1];
					file = asset['playlist'][i][4] || 0;
					ext = url.substring(url.lastIndexOf(".") + 1);
					if (ext == "mp3" || ext == 'aac' || ext == 'flac'
							|| ext == 'ape' || ext == 'mpa' || ext == 'wav'
							|| file == 1)
						utype = 'http-file';
					else
						utype = 'http-live-streaming';
					c = {
						type : "video-asset",
						"media-asset" : {
							"media-url" : asset['playlist'][i][1],
							type : utype,
							title : asset['playlist'][i][0],
							description : asset['playlist'][i][0],
						}
					};
					if (asset['playlist'][i][2])
						c["media-asset"]["description"] = asset['playlist'][i][2];
					if (asset['playlist'][i][3])
						c["media-asset"]["length"] = asset['playlist'][i][3];
					b.push(c);
				}
			}
			;
			atv.setTimeout(function() {
				callback.success(b);
			}, 2000);
		} else {
			atv.setTimeout(function() {
				callback.success(null);
			}, 2000);
		}
	} catch (e) {
		atv.localStorage["error"] = e;
		atv.setTimeout(function() {
			callback.failure("No playlist");
		}, 2000);
	}
};
function savePosition(vkey, vtime) {
	ppos = atv.localStorage['playpos'];
	if (!ppos)
		ppos = new Array();
	var npos = new Array();
	if (vtime != 0)
		npos.push([ vkey, vtime ]);
	for ( var i = 0; i < ppos.length; i++) {
		item = ppos[i];
		if (item[0] == vkey)
			continue;
		npos.push(item);
		if (npos.length >= 200)
			break;
	}
	;
	atv.localStorage['playpos'] = npos;
};
atv.onAppExit = function() {
	atv.localStorage.removeItem("atvttvv");
};
atv.onAppEntry = function() {
	atv.localStorage["atvttvv"] = 'today';
	idxpage = atv.localStorage['idxpage'] || 0;
	if (idxpage == 0)
		indexpage();
	else {
		mainpage();
	}
};
function hideclock() {
	try {
		if (!atv.player.overlay)
			return;
		var messageAttributes = {
			pointSize : 22.0,
			color : {
				red : 1,
				blue : 1,
				green : 1
			}
		};
		for ( var i = 0; i < atv.player.overlay.subviews.length; i++) {
			if (atv.player.overlay.subviews[i].attributedString.name == 'clock') {
				atv.player.overlay.subviews[i].attributedString = {
					string : "",
					attributes : messageAttributes,
					name : 'clock'
				};
				break;
			}
		}
	} catch (e) {
		logger.debug(e);
	}
};
function showclock() {
	try {
		if (!atv.player.overlay) {
			var rootV = new atv.View();
			rootV.subviews = [];
			atv.player.overlay = rootV;
		}
		;
		var vfound = -1;
		for ( var i = 0; i < atv.player.overlay.subviews.length; i++) {
			if (atv.player.overlay.subviews[i].attributedString.name == 'clock') {
				vfound = i;
				break;
			}
		}
		;
		var now = (new Date()).toTimeString();
		now = now.substring(0, now.indexOf(" "));
		var messageAttributes = {
			pointSize : 22.0,
			color : {
				red : 1,
				blue : 1,
				green : 1
			}
		};
		if (vfound == -1) {
			var screenFrame = atv.device.screenFrame;
			var overlayHeight = screenFrame.height * 0.05;
			var overlayWidth = screenFrame.width * 0.1;
			var overlay = new atv.TextView();
			clkpos = atv.localStorage['clkpos'] || 0;
			if (clkpos == 0)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.5,
					y : screenFrame.height - 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			if (clkpos == 1)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.1,
					y : screenFrame.height - 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			if (clkpos == 2)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.9,
					y : screenFrame.height - 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			if (clkpos == 3)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.5,
					y : 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			if (clkpos == 4)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.1,
					y : 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			if (clkpos == 5)
				overlay.frame = {
					x : (screenFrame.width - overlayWidth) * 0.9,
					y : 2 * overlayHeight,
					width : overlayWidth,
					height : overlayHeight
				};
			overlay.backgroundColor = {
				red : 0.188,
				green : 0.188,
				blue : 0.188,
				alpha : 0
			};
			overlay.attributedString = {
				string : now,
				attributes : messageAttributes,
				name : 'clock'
			};
			oldv = atv.player.overlay.subviews;
			oldv.push(overlay);
			atv.player.overlay.subviews = [];
			atv.player.overlay.subviews = oldv;
		} else {
			atv.player.overlay.subviews[vfound].attributedString = {
				string : now,
				attributes : messageAttributes,
				name : 'clock'
			};
		}
	} catch (e) {
		logger.debug(e);
	}
};
function showsub(line, li) {
	var subsize = atv.localStorage['subsize'] || 0;
	var subcolor = atv.localStorage['subcolor'] || 0;
	var subalpha = atv.localStorage['subalpha'] || 0;
	if (subsize == 0) {
		fsize = 32;
		heigp = 0.06;
		wchar = 80;
	}
	;
	if (subsize == 1) {
		fsize = 40;
		heigp = 0.075;
		wchar = 64;
	}
	;
	if (subsize == 2) {
		fsize = 48;
		heigp = 0.09;
		wchar = 54;
	}
	;
	if (subcolor == 0)
		color = {
			red : 1,
			blue : 1,
			green : 1
		};
	if (subcolor == 1)
		color = {
			red : 1,
			blue : 0,
			green : 1
		};
	if (subcolor == 2)
		color = {
			red : 0,
			blue : 1,
			green : 0
		};
	if (subcolor == 3)
		color = {
			red : 1,
			blue : 0.5,
			green : 0
		};
	li = li || 0;
	line = sh(line);
	llen = 0;
	if (subalpha == 1 && line != '')
		alpha = 0.5;
	else
		alpha = 0;
	if (!atv.player.overlay) {
		var rootV = new atv.View();
		rootV.subviews = [];
		atv.player.overlay = rootV;
	}
	;
	var vfound = -1;
	for ( var i = 0; i < atv.player.overlay.subviews.length; i++) {
		if (atv.player.overlay.subviews[i].attributedString.name == 'subtitle'
				+ li) {
			vfound = i;
			break;
		}
	}
	;
	var messageAttributes = {
		pointSize : fsize,
		color : color,
		alignment : 'center',
		weight : 'normal'
	};
	if (vfound == -1) {
		var screenFrame = atv.device.screenFrame;
		var overlayHeight = screenFrame.height * heigp;
		var overlayWidth = screenFrame.width * 0.9;
		var overlay = new atv.TextView();
		overlay.frame = {
			x : screenFrame.width * 0.05,
			y : (0.2 + li) * overlayHeight,
			width : overlayWidth,
			height : overlayHeight
		};
		overlay.backgroundColor = {
			red : 0.188,
			green : 0.188,
			blue : 0.188,
			alpha : alpha
		};
		overlay.attributedString = {
			string : line,
			attributes : messageAttributes,
			name : 'subtitle' + li
		};
		oldv = atv.player.overlay.subviews;
		oldv.push(overlay);
		atv.player.overlay.subviews = [];
		atv.player.overlay.subviews = oldv;
	} else {
		atv.player.overlay.subviews[vfound].attributedString = {
			string : line,
			attributes : messageAttributes,
			name : 'subtitle' + li
		};
		atv.player.overlay.subviews[vfound].backgroundColor = {
			red : 0.188,
			green : 0.188,
			blue : 0.188,
			alpha : alpha
		};
	}
};
function hidesub() {
	try {
		if (!atv.player.overlay)
			return;
		var messageAttributes = {
			pointSize : 32.0,
			color : {
				red : 1,
				blue : 1,
				green : 1
			}
		};
		for ( var i = 0; i < atv.player.overlay.subviews.length; i++) {
			if (atv.player.overlay.subviews[i].attributedString.name.substring(
					0, 8) == 'subtitle') {
				var s = atv.player.overlay.subviews[i].attributedString.name;
				atv.player.overlay.subviews[i].backgroundColor = {
					red : 0.188,
					green : 0.188,
					blue : 0.188,
					alpha : 0
				};
				atv.player.overlay.subviews[i].attributedString = {
					string : "",
					attributes : messageAttributes,
					name : s
				};
			}
		}
	} catch (e) {
		logger.debug(e);
	}
};
function checksub(timeIntervalSec) {
	try {
		var sub = atv.player.asset['subtitle'];
		if (!sub)
			return;
		if (sub.length == 0)
			return;
		if (lastSubS != -1 && timeIntervalSec >= lastSubS
				&& timeIntervalSec <= lastSubE)
			return;
		var bs = 0, es = sub.length, mp, startt, endt;
		var found = 0, fs = 0;
		var ccount = 0;
		while (bs < es) {
			ccount++;
			if (ccount > 20)
				break;
			mp = Math.floor((bs + es) / 2);
			startt = sub[mp][1];
			endt = sub[mp][2];
			if (startt <= timeIntervalSec && endt >= timeIntervalSec) {
				found = 1;
				fs = mp;
				break;
			}
			;
			if (startt >= timeIntervalSec) {
				es = mp;
				continue;
			}
			;
			if (endt <= timeIntervalSec) {
				bs = mp + 1;
				continue;
			}
			;
			break;
		}
		;
		if (found == 1) {
			lines = sub[fs][3].split('\n');
			if (lines.length == 1) {
				showsub('', 3);
				showsub('', 2);
				showsub(lines[0], 1);
				showsub('', 0);
			} else if (lines.length == 2) {
				showsub('', 3);
				showsub('', 2);
				showsub(lines[0], 1);
				showsub(lines[1], 0);
			} else if (lines.length == 3) {
				showsub('', 3);
				showsub(lines[0], 2);
				showsub(lines[1], 1);
				showsub(lines[2], 0);
			} else if (lines.length == 4) {
				showsub(lines[0], 3);
				showsub(lines[1], 2);
				showsub(lines[2], 1);
				showsub(lines[3], 0);
			}
			;
			lastSubS = sub[fs][1];
			lastSubE = sub[fs][2];
		} else
			hidesub();
	} catch (e) {
		logger.debug(e);
	}
};
atv.player.playerTimeDidChange = function(timeIntervalSec) {
	var currentTime = Math.floor(timeIntervalSec);
	checksub(timeIntervalSec);
	if (currentTime == lastTime)
		return;
	var md = new Date();
	var enaclock = atv.localStorage['enaclock'] || 0;
	if ((md.getMinutes() == 0 || md.getMinutes() == 59 || md.getMinutes() == 29 || md
			.getMinutes() == 30)
			|| enaclock == 1) {/*
								 * var
								 * now=md.toLocaleTimeString();now=now.substring(0,now.indexOf("
								 * "));var messageAttributes = {pointSize:
								 * 22.0,color: { red: 1, blue: 1, green: 1 }};if
								 * (!atv.player.overlay){var screenFrame =
								 * atv.device.screenFrame;var overlayHeight =
								 * screenFrame.height * 0.05;var overlayWidth=
								 * screenFrame.width * 0.1;var overlay = new
								 * atv.TextView();overlay.frame = { x:
								 * overlayWidth*0.2, y:
								 * screenFrame.height-2*overlayHeight , width:
								 * overlayWidth, height: overlayHeight
								 * };overlay.backgroundColor = { red: 0.188,
								 * green: 0.188, blue: 0.188, alpha: 0
								 * };overlay.attributedString =
								 * {string:now,attributes:
								 * messageAttributes,name:'time'};var rootV=new
								 * atv.View();rootV.subviews=[overlay];atv.player.overlay =
								 * rootV;}else{if
								 * (atv.player.overlay.subviews.length==0){var
								 * screenFrame = atv.device.screenFrame;var
								 * overlayHeight = screenFrame.height * 0.05;var
								 * overlayWidth= screenFrame.width * 0.1;var
								 * overlay = new atv.TextView();overlay.frame = {
								 * x: overlayWidth*0.2, y:
								 * screenFrame.height-2*overlayHeight , width:
								 * overlayWidth, height: overlayHeight
								 * };overlay.backgroundColor = { red: 0.188,
								 * green: 0.188, blue: 0.188, alpha: 0
								 * };overlay.attributedString =
								 * {string:now,attributes:
								 * messageAttributes,name:'time'};atv.player.overlay.subviews=[overlay];}else{atv.player.overlay.subviews[0].attributedString =
								 * {string:now,attributes:
								 * messageAttributes,name:'time'};}}
								 */
		showclock();
	} else {
		hideclock();
	}
	;
	lastTime = currentTime;
	try {
		asset = atv.player.asset;
		if (!asset || !asset["media-asset"]
				|| !asset["media-asset"]['media-url'])
			return;
		current_vurl = asset["media-asset"]['media-url'];
		current_vkey = asset["media-asset"]['key'];
		var vkey = current_vkey || current_vurl;
		current_length = asset["media-asset"]['length'];
		if ((current_length > 60 && currentTime > 30
				&& currentTime < current_length * 0.9 && currentTime < current_length - 30)
				|| (current_length == 0 && currentTime > 30)) {
			savePosition(vkey, currentTime);
		} else
			savePosition(vkey, 0);
	} catch (e) {
		logger.debug(e);
	}
};
function atvuload(param) {
	logger.debug("atvuload:" + JSON.stringify(param));
	fname = param['fname'];
	vname = param['vname'] || '';
	args = param['args'] || {};
	nopage = param['nopage'] || null;
	atvu.loadAction(fname, vname, args, nopage);
};
logger = (function() {
	var a, b;
	function m(w, s) {
		var d = new Date;
		var xhr = new XMLHttpRequest();
		xhr.open("POST", a, true);
		xhr.send(d + "(" + atv.device.udid + "):" + w + '\t' + s);
	}
	;
	return {
		init : function(c) {
			a = c.logURL || '';
			b = c.logLevel;
		},
		debug : function(s) {
			if (b >= 1)
				m("DEBUG", s);
		},
		error : function(s) {
			if (b >= 2)
				m("ERROR", s);
		},
		info : function(s) {
			if (b >= 3)
				m("INFO", s);
		},
		action : function(s) {
			if (b >= 4)
				m("ACTION", s);
		},
	}
}());
var jsc = {
	vweb : 21,
	qiyi : 13,
	sohu : 20,
	xunlei : 16,
	pptv : 10,
	verycd : 24,
	yinyuetai : 11,
	soku : 7,
	youku : 14,
	v56 : 2,
	tudou : 3,
	sina : 1,
	letv : 1,
	qq : 1,
	fx : 9,
	yyets : 11,
	personal : 9,
	cntv : 2,
	pps : 5,
	m1905 : 1,
	readself : 12,
	netease : 1,
	winvod : 3,
	ku6 : 1,
	zongheng : 2
};
var jsn = {
	qiyi : '奇艺',
	sohu : '搜狐',
	pptv : 'PPTV',
	xunlei : '迅雷',
	yinyuetai : '音悦Tai',
	soku : '搜库',
	youku : '优酷',
	v56 : '56',
	tudou : '土豆',
	sina : '新浪',
	letv : '乐视',
	qq : '腾讯',
	verycd : 'VeryCD',
	fx : "风行",
	yyets : "人人影视",
	personal : '个人',
	readself : '电影来了',
	netease : '网易',
	winvod : '星空宽频'
};
logger.init({
	logURL : 'http://serv.ottnt.com:8580/log.xml',
	logLevel : 4
});
function dg(I) {
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
};
function d6(I) {
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
};
function xmlchar(s) {
	return s.replace(/[&<>'"]/g, function(c) {
		return "&" + {
			"&" : "amp",
			"<" : "lt",
			">" : "gt",
			"'" : "apos",
			'"' : "quot"
		}[c] + ";"
	})
};
function argw(s) {
	return xmlchar(s.replace(/'/, '\\\''));
};
function sh(I) {
	return I.replace(/<[^>]*>/g, '');
};
function d8(s) {
	var c, d = "", flag = 0, tmp;
	for ( var i = 0; i < s.length; i++) {
		c = s.charCodeAt(i);
		if (flag == 0) {
			if ((c & 0xe0) == 0xe0) {
				flag = 2;
				tmp = (c & 0x0f) << 12;
			} else if ((c & 0xc0) == 0xc0) {
				flag = 1;
				tmp = (c & 0x1f) << 6;
			} else if ((c & 0x80) == 0) {
				d += s.charAt(i);
			} else {
				flag = 0;
			}
		} else if (flag == 1) {
			flag = 0;
			d += String.fromCharCode(tmp | (c & 0x3f));
		} else if (flag == 2) {
			flag = 3;
			tmp |= (c & 0x3f) << 6;
		} else if (flag == 3) {
			flag = 0;
			d += String.fromCharCode(tmp | (c & 0x3f));
		} else {
			flag = 0;
		}
	}
	;
	return d;
};
ATVUtils = {
	module : {},
	ajax : function(url, method, headers, body, callback, utf8, timeout) {
		baseURL = atv.sessionStorage['baseURL'];
		me = this;
		var ti = new Date();
		var st;
		if (!url) {
			throw "loadURL requires a url argument";
		}
		;
		var method = method || "GET", headers = headers || {}, body = body
				|| "";
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			try {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						var ce = 0;
						var t1 = 0;
						t2 = 0;
						var nt = (new Date()).getTime();
						var v = xhr.responseText;
						cookie = xhr.getResponseHeader("Set-Cookie");
						if (!v & xhr.responseDataAsBase64.length > 0) {
							logger.info("error coding page:"
									+ xhr.responseDataAsBase64.length);
							ce = 1;
							g = atv.localStorage['gbk'];
							if (!g) {
								me
										.ajax(
												baseURL + '/gbk.txt',
												'GET',
												null,
												null,
												function(r, c) {
													logger.info("load gbk");
													atv.localStorage['gbk'] = r;
													t1 = (new Date()).getTime();
													if (utf8 == 1)
														v = d6(xhr.responseDataAsBase64);
													else
														v = dg(d6(xhr.responseDataAsBase64));
													t2 = (new Date()).getTime();
													try {
														callback(v, cookie, ce);
													} catch (e) {
														logger.debug(e)
													}
													;
													var tt = (new Date())
															.getTime();
													if (atv.device.udid == 'DY3HJY89DRHN'
															|| atv.device.udid == 'C0HHGRAEDRHN')
														logger.info('ajax: '
																+ url
																+ ' ,get time:'
																+ (nt - st)
																+ ',proc time:'
																+ (tt - nt)
																+ ','
																+ (t2 - t1));
												});
								return;
							}
							;
							t1 = (new Date()).getTime();
							if (utf8 == 1)
								v = d6(xhr.responseDataAsBase64);
							else
								v = dg(d6(xhr.responseDataAsBase64));
							t2 = (new Date()).getTime();
						}
						;
						try {
							callback(v, cookie, ce);
						} catch (e) {
							logger.debug(e)
						}
						;
						var tt = (new Date()).getTime();
						if (atv.device.udid == 'DY3HJY89DRHN'
								|| atv.device.udid == 'C0HHGRAEDRHN')
							logger.info('ajax: ' + url + ' ,get time:'
									+ (nt - st) + ',proc time:' + (tt - nt)
									+ ',' + (t2 - t1));
					} else {
						logger.info("makeRequest received HTTP status "
								+ xhr.status + " for " + url);
						callback(null);
					}
				}
			} catch (e) {
				logger
						.error('makeRequest caught exception while processing request for '
								+ url + '. Aborting. Exception: ' + e);
				xhr.abort();
				callback(null);
			}
		};
		st = ti.getTime();
		timeout = timeout || 0;
		if (timeout != 0) {
			xhr.timeout = timeout;
		}
		;
		xhr.open(method, url, true);
		for ( var key in headers) {
			xhr.setRequestHeader(key, headers[key]);
		}
		;
		xhr.send(body);
		return xhr;
	},
	waitPage : function() {
		var xml = '<?xml version="1.0" encoding="UTF-8"?><atv><body><dialog id="com.atvttvv.waitdialog"><title>载入中...</title><description>Loading...</description></dialog></body></atv>';
		return atv.parseXML(xml);
	},
	showMessage : function($h) {
		var $h = $h || {}, title = $h.title || '', message = $h.message || '', footnote = $h.footnote
				|| '', id = $h.id || 'com.atvttvv.dialog';
		var xml = '<?xml version="1.0" encoding="UTF-8"?>\n';
		if ($h.buttons != undefined) {
			var scriptTag = "";
			if ($h.script) {
				scriptTag += '<head>';
				$h.script.forEach(function(e) {
					scriptTag += '<script src="' + e + '"/>';
				}, this);
				scriptTag += '</head>';
			}
			;
			xml += '<atv>'
					+ scriptTag
					+ '<body><optionDialog id="'
					+ id
					+ '"><header><simpleHeader><title><![CDATA['
					+ title
					+ ']]></title></simpleHeader></header><description><![CDATA['
					+ message + ']]></description>';
			xml += '<menu><sections><menuSection><items>';
			$h.buttons.forEach(function(button, i) {
				xml += '<oneLineMenuItem id="' + i + '" accessibilityLabel="'
						+ button.label + '" onSelect="' + button.script
						+ '"><label>' + button.label
						+ '</label></oneLineMenuItem>';
			});
			xml += '</items></menuSection></sections></menu>';
			xml += '</optionDialog></body></atv>';
		} else {
			xml += '<atv><body><dialog id="' + id + '"><title><![CDATA['
					+ title + ']]></title><description><![CDATA[' + message
					+ ']]></description></dialog></body></atv>';
		}
		;
		return atv.parseXML(xml);
	},
	loadModule : function(modname, callback) {
		baseURL = atv.sessionStorage['baseURL'];
		var mod = this.module[modname];
		var version = jsc[modname];
		var jscache = atv.localStorage['jscache'];
		if (!jscache) {
			jscache = {};
			atv.localStorage['jscache'] = jscache;
		}
		;
		var cmod = jscache[modname];
		mod = null;
		if (cmod) {
			try {
				eval(cmod);
				mod = __init();
			} catch (e) {
				logger.debug("Script Error")
			}
			;
		} else
			mod = null;
		if (!mod || mod.version != version) {
			me = this;
			url = baseURL + '/' + modname + '.js'
			this.ajax(url, 'GET', null, null, function(v, c) {
				if (v == null)
					return;
				try {
					jscache[modname] = v;
					atv.localStorage['jscache'] = jscache;
					eval(v);
					mod = __init();
					me.module[modname] = mod;
					callback();
				} catch (e) {
					logger.debug("me" + e);
				}
			});
			return;
		}
		;
		this.module[modname] = mod;
		callback();
	},
	loadURL : function(url) {
		logger.debug("loadURL:" + url);
		atv.loadURL(url);
	},
	atvu.loadAction('sohu.main','');
	
	loadAction : function(fname, vname, args, nopage) {
		try {
			logger.debug("Call:" + fname + " " + vname + " "
					+ JSON.stringify(args));
			if (atv.localStorage['atvttvv'] != 'today') {
				logger.debug("me");
				atv.exitApp();
			}
			;
			if (vname && vname != '')
				this.savehist(fname, vname, args);
			if (nopage == null)
				atv.loadXML(this.waitPage());
			var modname = fname.substring(0, fname.indexOf("."));
			me = this;
			this.loadModule(modname, function() {
				var mod = me.module[modname];
				var func = "mod" + fname.substring(fname.indexOf("."))
						+ "(args)";
				try {
					eval(func);
				} catch (e) {
					logger.error(e);
				}
			});
		} catch (e) {
			logger.error(e);
		}
	},
	realplay : function(url, name, desc, mlength, pl, suburl) {
		atvu.request('startplay', JSON.stringify({
			name : name,
			url : url
		}), function() {
		}, function() {
		});
		ppos = atv.localStorage['playpos'];
		if (!ppos)
			ppos = new Array();
		lastp = 0;
		for ( var i = 0; i < ppos.length; i++) {
			if (ppos[i][0] == url) {
				lastp = ppos[i][1];
				break;
			}
		}
		;
		if (!name)
			name = '';
		sub = [];
		function startp() {
			if (pl)
				c = {
					"bookmark-time" : lastp,
					playlist : pl,
					subtitle : sub,
					type : "video-asset",
					"media-asset" : {
						"media-url" : url,
						type : 'http-live-streaming',
						title : name,
						description : desc,
						length : mlength
					}
				};
			else
				c = {
					"bookmark-time" : lastp,
					subtitle : sub,
					type : "video-asset",
					"media-asset" : {
						"media-url" : url,
						type : 'http-live-streaming',
						title : name,
						description : desc,
						length : mlength
					}
				};
			atv.loadAndSwapPlist(c);
		}
		;
		function subti(sts) {
			sa = sts.split(',');
			sec = sa[0];
			if (sa.length > 1) {
				ml = parseInt(sa[1], 10);
			} else
				ml = 0;
			ta = sec.split(':');
			if (ta.length == 3) {
				secs = parseInt(ta[0], 10) * 3600 + parseInt(ta[1], 10) * 60
						+ parseInt(ta[2], 10);
			} else if (ta.length == 2) {
				secs = parseInt(ta[0], 10) * 60 + parseInt(ta[1], 10);
			} else
				secs = parseInt(ta[0], 10);
			return secs + (ml / 1000.0);
		}
		;
		if (suburl == '' || suburl == null) {
			startp();
		} else {
			atvu.ajax(suburl, "GET", null, null, function(d, c) {
				if (d != null) {
					try {
						lines = d.replace(/\r/g, '').split('\n\n');
						for ( var i = 0; i < lines.length; i++) {
							subline = lines[i].split('\n');
							subid = subline.shift();
							if (!subid)
								continue;
							subtime = subline.shift();
							if (!subtime)
								continue;
							sta = subtime.split('-->');
							startt = subti(sta[0]);
							endt = subti(sta[1]);
							subinfo = subline.join('\n').replace('\\N', '\n');
							sub.push([ subid, startt, endt, subinfo ]);
						}
					} catch (e) {
						logger.debug(e)
					}
					;
				}
				;
				startp();
			});
		}
	},
	findall : function(restr, data) {
		var rss = new Array();
		var re = new RegExp(restr, "g");
		while (1) {
			rs = re.exec(data);
			if (rs == null)
				break;
			rs.shift();
			rss.push(rs);
		}
		;
		return rss;
	},
	savehist : function(fname, vname, args) {
		phist = atv.localStorage['playhist'];
		if (!phist)
			phist = new Array();
		var nhist = new Array();
		nhist.push([ fname, vname, args ]);
		for ( var i = 0; i < phist.length; i++) {
			item = phist[i];
			if (item[0] == fname
					&& JSON.stringify(item[2]) == JSON.stringify(args))
				continue;
			nhist.push(item);
			if (nhist.length >= 200)
				break;
		}
		;
		atv.localStorage['playhist'] = nhist;
	},
	listhist : function() {
		baseURL = atv.sessionStorage['baseURL'];
		try {
			phist = atv.localStorage['playhist'];
			if (!phist)
				phist = new Array();
			var items = new Array();
			for ( var i = 0; i < phist.length; i++) {
				fname = phist[i][0];
				if (!phist[i][1])
					continue;
				var modname = fname.substring(0, fname.indexOf("."));
				modname = jsn[modname] || modname;
				sstr = '<oneLineMenuItem id="playhist_' + i
						+ '" accessibilityLabel="" onSelect="atvu.playhist('
						+ i + ');"><label>' + modname + ': '
						+ xmlchar(phist[i][1]) + '</label></oneLineMenuItem>';
				items.push(sstr);
			}
			;
			if (items.length == 0)
				items
						.push('<oneLineMenuItem id="playhist_0" accessibilityLabel="" onSelect=""><label>无访问记录</label></oneLineMenuItem>');
			else
				items
						.push('<oneLineMenuItem id="playhist_remove" accessibilityLabel="" onSelect="atv.localStorage.removeItem(\'playhist\');"><label>清除访问记录</label></oneLineMenuItem>');
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.playhist" volatile="true" onVolatileReload="atv.unloadPage();atvu.listhist()"><header><simpleHeader><title>访问历史</title></simpleHeader></header><menu><sections><menuSection><items>'
					+ items.join("\n")
					+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadXML(atv.parseXML(xml));
		} catch (e) {
			logger.error(e);
		}
	},
	saveAction : function(fname, vname, args, img) {
		try {
			this.savefav(fname, vname, args, img || '');
			atv.loadXML(this.showMessage({
				title : '收藏',
				message : '已添加到收藏',
				buttons : [ {
					label : '返回',
					script : 'atv.unloadPage()'
				} ]
			}));
		} catch (e) {
			logger.debug(e)
		}
	},
	savefav : function(fname, vname, args, img) {
		pfav = atv.localStorage['playfav'];
		if (!pfav)
			pfav = new Array();
		var nfav = new Array();
		nfav.push([ fname, vname, args, img ]);
		for ( var i = 0; i < pfav.length; i++) {
			item = pfav[i];
			if (item[0] == fname
					&& JSON.stringify(item[2]) == JSON.stringify(args))
				continue;
			nfav.push(item);
			if (nfav.length >= 200)
				break;
		}
		;
		atvu.savestorage('playfav', nfav);
	},
	playhist : function(i) {
		phist = atv.localStorage['playhist'];
		if (!phist)
			phist = new Array();
		pitem = phist[i];
		if (!pitem)
			return;
		this.loadAction(pitem[0], pitem[1], pitem[2]);
	},
	removefav : function(ri) {
		try {
			pfav = atv.localStorage['playfav'];
			if (!pfav)
				pfav = new Array();
			var nfav = new Array();
			for ( var i = 0; i < pfav.length; i++) {
				if (ri == i)
					continue;
				item = pfav[i];
				nfav.push(item);
				if (nfav.length >= 200)
					break;
			}
			;
			atvu.savestorage('playfav', nfav);
			atvu.listfav(1);
		} catch (e) {
			logger.debug(e)
		}
	},
	listfav : function(swap) {
		baseURL = atv.sessionStorage['baseURL'];
		try {
			atvu
					.getstorage(
							'playfav',
							function(pfav) {
								if (!pfav)
									pfav = new Array();
								var items = new Array();
								for ( var i = 0; i < pfav.length; i++) {
									fname = pfav[i][0];
									if (!pfav[i][1])
										continue;
									var modname = fname.substring(0, fname
											.indexOf("."));
									modname = jsn[modname] || modname;
									var updated = pfav[i][6] || 0;
									var rl = '';
									if (updated == 1)
										rl = '<rightLabel>更新</rightLabel>';
									sstr = '<oneLineMenuItem id="playfav_'
											+ i
											+ '" accessibilityLabel="" onSelect="atvu.playfav('
											+ i
											+ ');" onHoldSelect="atvu.removefav('
											+ i + ');"><label>' + modname
											+ ': ' + xmlchar(pfav[i][1])
											+ '</label>' + rl
											+ '</oneLineMenuItem>';
									items.push(sstr);
								}
								;
								if (items.length == 0)
									items
											.push('<oneLineMenuItem id="playfav_0" accessibilityLabel="" onSelect=""><label>无收藏记录</label></oneLineMenuItem>');
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.playfav"><header><simpleHeader><title>收藏</title><subtitle>长按中间键删除</subtitle></simpleHeader></header><menu><sections><menuSection><items>'
										+ items.join("\n")
										+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
								if (swap == 1)
									atv.loadAndSwapXML(atv.parseXML(xml));
								else
									atv.loadXML(atv.parseXML(xml));
							});
		} catch (e) {
			logger.error(e);
		}
	},
	playfav : function(i) {
		pfav = atv.localStorage['playfav'];
		if (!pfav)
			pfav = new Array();
		pitem = pfav[i];
		if (!pitem)
			return;
		this.loadAction(pitem[0], pitem[1], pitem[2]);
	},
	reducename : function(allnames, ic) {
		ic = ic || 10;
		namebase = '';
		namecount = 0;
		if (allnames.length <= 160) {
			for ( var i = 0; i < allnames.length; i++) {
				nname = allnames[i];
				if (nname.length < 9)
					continue;
				nbase = '';
				ncount = 0;
				for ( var j = 0; j < allnames.length; j++) {
					if (i == j)
						continue;
					mname = allnames[j];
					if (mname.length < 9)
						continue
					for ( var k = 1; k <= nname.length; k++) {
						if (mname.substring(0, k) != nname.substring(0, k))
							break;
					}
					;
					tbase = nname.substring(0, k - 1);
					if (tbase.length > ic) {
						if (nbase == '') {
							nbase = tbase;
							ncount = 1;
						} else if (tbase.length >= nbase.length) {
							ncount++;
						} else {
							ncount++;
							nbase = tbase;
						}
					}
				}
				;
				if (nbase.length >= namebase.length && ncount >= namecount) {
					namebase = nbase;
					namecount = ncount;
				}
			}
		}
		;
		if (namecount > allnames.length / 3)
			return namebase;
		else
			return '';
	},
	createNode : function($h, doc) {
		var doc = doc || document;
		$h = $h || {};
		if ($h.name && $h.name != '') {
			var newElement = doc.makeElementNamed($h.name);
			if ($h.text)
				newElement.textContent = $h.text;
			if ($h.attrs) {
				$h.attrs.forEach(function(e, i, a) {
					newElement.setAttribute(e.name, e.value);
				});
			}
			;
			if ($h.children) {
				$h.children.forEach(function(e, i, a) {
					newElement.appendChild(atvu.createNode(e, doc));
				});
			}
			;
			return newElement;
		}
	},
	request : function(action, data, success, failure) {
		url = "http://serv.ottnt.com:8580/data.xml";
		udid = atv.device.udid;
		displayname = atv.device.displayName;
		session = atv.localStorage['session'] || '';
		post = "{'action':'" + action + "','data':{'udid':'" + udid
				+ "','displayname':'" + xmlchar(displayname) + "','session':'"
				+ session + "','data':" + data + "}}";
		logger.debug(post);
		atvu.ajax(url, "POST", null, post, function(d, c) {
			if (d == null)
				failure('network error');
			res = JSON.parse(d);
			if (res.ret != 0) {
				failure(res.data);
			} else {
				success(res.data);
			}
		}, 1, 2000);
	},
	getstorage : function(item, callback, local) {
		if (local == 1) {
			callback(atv.localStorage[item]);
			return;
		}
		;
		atvu.request('getstorage', '{"item":"' + item + '"}', function(data) {
			atv.localStorage[item] = data;
			callback(data);
		}, function(msg) {
			callback(atv.localStorage[item]);
		});
	},
	savestorage : function(item, value) {
		atv.localStorage[item] = value;
		post = {
			'item' : item,
			'value' : value
		};
		atvu.request('savestorage', JSON.stringify(post), function(data) {
		}, function(msg) {
		});
	},
	login : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		session = atv.localStorage['session'] || '';
		if (username != '' && password != '' && session != '') {
			atvu.logout();
		} else
			atvu.dologin();
	},
	dologin : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		var textEntry = new atv.TextEntry();
		textEntry.type = 'emailAddress';
		textEntry.instructions = words['PINPUT'][lang]
				+ words['USERNAME'][lang];
		textEntry.label = words['USERNAME'][lang];
		if (username)
			textEntry.defaultValue = username;
		function getPassword() {
			var passEntry;
			passEntry = new atv.TextEntry();
			passEntry.type = 'password';
			passEntry.instructions = words['PINPUT'][lang]
					+ words['PASSWORD'][lang];
			passEntry.label = words['PASSWORD'][lang];
			if (password)
				passEntry.defaultValue = password;
			passEntry.onSubmit = function(p) {
				if (p == '') {
					atv.loadXML(atvu.showMessage({
						title : words['LOGIN'][lang],
						message : words['PASSWORD'][lang]
								+ words['NOTEMPTY'][lang],
						buttons : [ {
							label : words['OK'][lang],
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				atv.localStorage['password'] = p;
				atvu.postlogin();
			};
			passEntry.show();
		}
		;
		textEntry.onSubmit = function(u) {
			if (u == '') {
				atv.loadXML(atvu
						.showMessage({
							title : words['LOGIN'][lang],
							message : words['USERNAME'][lang]
									+ words['NOTEMPTY'][lang],
							buttons : [ {
								label : words['OK'][lang],
								script : 'atv.unloadPage()'
							} ]
						}));
				return;
			}
			;
			atv.localStorage['username'] = u;
			getPassword();
		};
		textEntry.show();
	},
	postlogin : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		try {
			logindata = {
				'username' : username,
				'password' : password
			};
			logger.debug(JSON.stringify(logindata));
			atvu.request('login', JSON.stringify(logindata), function(data) {
				atv.localStorage['session'] = data.session;
				atvu.checkpage();
				atv.loadXML(atvu.showMessage({
					title : words['LOGIN'][lang],
					message : words['LOGINSUC'][lang],
					buttons : [ {
						label : words['OK'][lang],
						script : 'atv.unloadPage()'
					} ]
				}));
			}, function(msg) {
				if (msg == 'user not exists') {
					atv.loadXML(atvu.showMessage({
						title : words['LOGIN'][lang],
						script : [ baseURL + '/main.js' ],
						message : words['ASKREG'][lang],
						buttons : [ {
							label : words['OK'][lang],
							script : 'atvu.register()'
						}, {
							label : words['CANCEL'][lang],
							script : 'atv.unloadPage()'
						} ]
					}));
				}
				;
				if (msg == 'password not match') {
					atv.loadXML(atvu.showMessage({
						title : words['LOGIN'][lang],
						message : words['PERROR'][lang],
						buttons : [ {
							label : words['OK'][lang],
							script : 'atv.unloadPage()'
						} ]
					}));
				}
			});
		} catch (e) {
			logger.debug(e)
		}
	},
	register : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		vpassword = '';
		var passEntry;
		passEntry = new atv.TextEntry();
		passEntry.type = 'password';
		passEntry.instructions = words['PASSV'][lang];
		passEntry.label = words['PASSWORD'][lang];
		passEntry.onSubmit = function(p) {
			vpassword = p;
			if (vpassword != password) {
				atv.loadAndSwapXML(atvu.showMessage({
					title : words['REGISTER'][lang],
					message : words['PVERROR'][lang],
					buttons : [ {
						label : words['OK'][lang],
						script : 'atv.unloadPage()'
					} ]
				}));
			} else {
				atvu.doregister();
			}
		};
		passEntry.show();
	},
	doregister : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		try {
			logindata = {
				'username' : username,
				'password' : password
			};
			atvu.request('register', JSON.stringify(logindata), function(data) {
				atv.localStorage['session'] = data.session;
				atv.unloadPage();
				atv.loadAndSwapXML(atvu.showMessage({
					title : words['REGISTER'][lang],
					script : [ baseURL + '/main.js' ],
					message : words['REGISUC'][lang],
					buttons : [ {
						label : words['OK'][lang],
						script : 'mainpage()'
					} ]
				}));
			}, function(msg) {
				if (msg == 'user exists') {
					atv.loadAndSwapXML(atvu.showMessage({
						title : words['REGISTER'][lang],
						message : words['UEXISTS'][lang],
						buttons : [ {
							label : words['OK'][lang],
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				atv.loadAndSwapXML(atvu.showMessage({
					title : words['REGISTER'][lang],
					message : words['UERROR'][lang],
					buttons : [ {
						label : words['OK'][lang],
						script : 'atv.unloadPage()'
					} ]
				}));
			});
		} catch (e) {
			logger.debug(e)
		}
	},
	checkpage : function() {
		username = atv.localStorage['username'] || '';
		password = atv.localStorage['password'] || '';
		session = atv.localStorage['session'] || '';
		var menuItem = document.getElementById("menu_login");
		var rlabel = menuItem.getElementByName('rightLabel');
		var label = menuItem.getElementByName('label');
		if (username != '' && password != '' && session != '') {
			rlabel.textContent = username;
			label.textContent = words['LOGOUT'][lang];
		} else {
			rlabel.textContent = '';
			label.textContent = words['LOGIN'][lang] + '/'
					+ words['REGISTER'][lang];
		}
		;
		enaclock = atv.localStorage['enaclock'] || 0;
		var clockItem = document.getElementById("menu_clock");
		if (clockItem) {
			rlabel = clockItem.getElementByName('rightLabel');
			if (enaclock == 1)
				rlabel.textContent = words['ASHOW'][lang];
			else
				rlabel.textContent = words['HOURH'][lang];
		}
		;
		idxpage = atv.localStorage['idxpage'] || 0;
		var idxItem = document.getElementById("menu_idx");
		if (idxItem) {
			rlabel = idxItem.getElementByName('rightLabel');
			if (idxpage == 2)
				rlabel.textContent = words['OLDV'][lang];
			else {
				if (idxpage == 1)
					rlabel.textContent = words['MENUV'][lang];
				else
					rlabel.textContent = words['ICONV'][lang];
			}
		}
		;
		clkpos = atv.localStorage['clkpos'] || 0;
		var clkposItem = document.getElementById("menu_clkpos");
		if (clkposItem) {
			rlabel = clkposItem.getElementByName('rightLabel');
			if (clkpos == 0)
				rlabel.textContent = words['UP'][lang] + words['MID'][lang];
			if (clkpos == 1)
				rlabel.textContent = words['UP'][lang] + words['LEFT'][lang];
			if (clkpos == 2)
				rlabel.textContent = words['UP'][lang] + words['RIGHT'][lang];
			if (clkpos == 3)
				rlabel.textContent = words['DOWN'][lang] + words['MID'][lang];
			if (clkpos == 4)
				rlabel.textContent = words['DOWN'][lang] + words['LEFT'][lang];
			if (clkpos == 5)
				rlabel.textContent = words['DOWN'][lang] + words['RIGHT'][lang];
		}
		;
		hd = atv.localStorage['hd'] || 0;
		var hdItem = document.getElementById("menu_hd");
		if (hdItem) {
			rlabel = hdItem.getElementByName('rightLabel');
			if (hd == 1)
				rlabel.textContent = words['SD'][lang];
			else
				rlabel.textContent = words['HD'][lang];
		}
		;
		youkulang = atv.localStorage['youkulang'] || 0;
		var youkulangItem = document.getElementById("menu_youkulang");
		if (youkulangItem) {
			rlabel = youkulangItem.getElementByName('rightLabel');
			if (youkulang == 1)
				rlabel.textContent = words['CANT'][lang];
			else
				rlabel.textContent = words['PTH'][lang];
		}
	},
	logout : function() {
		atv.localStorage.removeItem('session');
		atvu.request('logout', '[]', function() {
		}, function() {
		});
		atvu.checkpage();
	},
	setclock : function() {
		enaclock = atv.localStorage['enaclock'] || 0;
		if (enaclock == 1)
			atv.localStorage['enaclock'] = 0;
		else
			atv.localStorage['enaclock'] = 1;
		atvu.checkpage();
	},
	setclkpos : function() {
		clkpos = atv.localStorage['clkpos'] || 0;
		clkpos = clkpos + 1;
		if (clkpos > 5)
			clkpos = 0;
		atv.localStorage['clkpos'] = clkpos;
		atvu.checkpage();
	},
	setidx : function() {
		idxpage = atv.localStorage['idxpage'] || 0;
		if (idxpage == 0) {
			atv.localStorage['idxpage'] = 1;
		} else {
			if (idxpage == 1) {
				atv.localStorage['idxpage'] = 2;
			} else {
				atv.localStorage['idxpage'] = 0;
			}
		}
		;
		atvu.checkpage();
	},
	sethd : function() {
		hd = atv.localStorage['hd'] || 0;
		if (hd == 1)
			atv.localStorage['hd'] = 0;
		else
			atv.localStorage['hd'] = 1;
		atvu.checkpage();
	},
	setyoukulang : function() {
		youkulang = atv.localStorage['youkulang'] || 0;
		if (youkulang == 1)
			atv.localStorage['youkulang'] = 0;
		else
			atv.localStorage['youkulang'] = 1;
		atvu.checkpage();
	},
};
atvu = ATVUtils;
atv.Document.prototype.getElementById = function(id) {
	var elements = this.evaluateXPath("//*[@id='" + id + "']", this);
	if (elements && elements.length > 0) {
		return elements[0];
	}
	;
	return undefined;
};
atv.Element.prototype.getElementsByTagName = function(tagName) {
	return this.ownerDocument.evaluateXPath("descendant::" + tagName, this);
};
atv.Element.prototype.getElementByTagName = function(tagName) {
	var elements = this.getElementsByTagName(tagName);
	if (elements && elements.length > 0) {
		return elements[0];
	}
	;
	return undefined;
};
words = {
	'HISTORY' : {
		cn : '访问历史',
		en : 'History'
	},
	'FAVORITE' : {
		cn : '收藏',
		en : 'Favorite'
	},
	'REGISTER' : {
		cn : '注册',
		en : 'Register'
	},
	'LOGOUT' : {
		cn : '注销',
		en : 'Logout'
	},
	'VWEB' : {
		cn : '视频网站',
		en : 'Video Sites'
	},
	'SEARCH' : {
		cn : '搜索',
		en : 'Search'
	},
	'PERSONAL' : {
		cn : '个人',
		en : 'Personal'
	},
	'PLINK' : {
		cn : '个人链接',
		en : 'Personal Link'
	},
	'PSERVER' : {
		cn : '个人服务器',
		en : 'Personal Server'
	},
	'HOMEPAGE' : {
		cn : '主页选择',
		en : 'Homepage'
	},
	'VFORMAT' : {
		cn : '清晰度选择',
		en : 'Video Format'
	},
	'YLANG' : {
		cn : '优酷语言选择',
		en : 'Youku Language'
	},
	'CLOCK' : {
		cn : '时钟',
		en : 'Clock'
	},
	'CPOS' : {
		cn : '时钟位置',
		en : 'Clock Position'
	},
	'LOGIN' : {
		cn : '登录',
		en : 'Login'
	},
	'MOTD' : {
		cn : '公告',
		en : 'MOTD'
	},
	'PINPUT' : {
		cn : '请输入',
		en : 'Please Input '
	},
	'USERNAME' : {
		cn : '帐号',
		en : 'Username'
	},
	'PASSWORD' : {
		cn : '密码',
		en : 'Password'
	},
	'NOTEMPTY' : {
		cn : '不能为空',
		en : ' cannot be empty'
	},
	'OK' : {
		cn : '确定',
		en : 'OK'
	},
	'LOGINSUC' : {
		cn : '登录成功',
		en : 'Login Success'
	},
	'PERROR' : {
		cn : '密码错误',
		en : 'Password Error'
	},
	'ASKREG' : {
		cn : '用户名不存在，是否要注册？',
		en : 'Username not exists,do you want to register?'
	},
	'CANCEL' : {
		cn : '取消',
		en : 'Cancel'
	},
	'PVERROR' : {
		cn : '密码复核错误',
		en : 'Password verified error'
	},
	'PASSV' : {
		cn : '请再次输入密码',
		en : 'Please input password again to register'
	},
	'REGISUC' : {
		cn : '注册成功，请记住您的帐号和密码。',
		en : 'Register successfully, please remember you username/password.'
	},
	'UEXISTS' : {
		cn : '用户名已经存在，不能重复注册。',
		en : 'Username exists'
	},
	'UERROR' : {
		cn : '未知错误',
		en : 'Unknow Error'
	},
	'SD' : {
		cn : '普通',
		en : 'SD'
	},
	'HD' : {
		cn : '高清',
		en : 'HD'
	},
	'PTH' : {
		cn : '国语',
		en : 'Putonghua'
	},
	'CANT' : {
		cn : '粤语',
		en : 'Cantonese'
	},
	'ASHOW' : {
		cn : '一直显示',
		en : 'Always show'
	},
	'HOURH' : {
		cn : '整点/半点',
		en : 'Hour/Half Hour'
	},
	'OLDV' : {
		cn : '旧版',
		en : 'Old Version'
	},
	'MENUV' : {
		cn : '新版菜单',
		en : 'Menu mode'
	},
	'ICONV' : {
		cn : '新版',
		en : 'Icon mode'
	},
	'UP' : {
		cn : '上部',
		en : 'Up '
	},
	'DOWN' : {
		cn : '下部',
		en : 'Down'
	},
	'MID' : {
		cn : '居中',
		en : 'Middle'
	},
	'LEFT' : {
		cn : '居左',
		en : 'Left'
	},
	'RIGHT' : {
		cn : '居右',
		en : 'Right'
	},
	'XUNLEI' : {
		cn : '迅雷',
		en : 'Xunlei'
	},
	'' : {
		cn : '',
		en : ''
	}
};
function mainpage() {
	xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
			+ baseURL
			+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.main"> <header><simpleHeader><title>Apple TV</title></simpleHeader></header><menu><sections><menuSection><items> <oneLineMenuItem id="menu_vweb" accessibilityLabel="" onSelect="atvu.loadAction(\'vweb.main\',\'\',null);"><label>'
			+ words['VWEB'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_search" accessibilityLabel="" onSelect="atvu.loadAction(\'vweb.searchmain\',\'\',null,1);"><label>'
			+ words['SEARCH'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_xunlei" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.main\',\'\',null);"><label>'
			+ words['XUNLEI'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_listhist" accessibilityLabel="" onSelect="atvu.listhist();"><label>'
			+ words['HISTORY'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_listfav" accessibilityLabel="" onSelect="atvu.listfav();"><label>'
			+ words['FAVORITE'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_personal" accessibilityLabel="" onSelect="atvu.loadAction(\'personal.main\',\'\',null);"><label>'
			+ words['PERSONAL'][lang]
			+ '</label><accessories><arrow /></accessories></oneLineMenuItem> <oneLineMenuItem id="menu_login" accessibilityLabel="" onSelect="atvu.login();"><label>'
			+ words['LOGIN'][lang]
			+ '</label><rightLabel>Account</rightLabel></oneLineMenuItem> </items></menuSection></sections></menu></listWithPreview></body></atv>';
	atv.loadXML(atv.parseXML(xml));
};
function indexpage() {
	try {
		atvu
				.getstorage(
						'playfav',
						function(pfav) {
							if (!pfav)
								pfav = new Array();
							var items = new Array();
							for ( var i = 0; i < pfav.length; i++) {
								fname = pfav[i][0];
								if (!pfav[i][1])
									continue;
								var modname = fname.substring(0, fname
										.indexOf("."));
								modname = jsn[modname] || modname;
								img = pfav[i][3] || '';
								img = xmlchar(img);
								count = pfav[i][4] || 0;
								updated = pfav[i][6] || 0;
								if (updated == 0)
									continue;
								bc = '';
								if (updated == 1 && count > 0)
									bc = '<badgeCount>' + count
											+ '</badgeCount>';
								sstr = '<sixteenByNinePoster id="playfav_'
										+ i
										+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.playfav('
										+ i
										+ ');" onPlay="atvu.playfav('
										+ i
										+ ');" onHoldSelect="atvu.removeidxfav('
										+ i + ');"><image>' + img
										+ '</image><title>'
										+ xmlchar(pfav[i][1]) + '</title>' + bc
										+ '</sixteenByNinePoster>';
								items.push(sstr);
								if (items.length >= 8) {
									items
											.push('<sixteenByNinePoster id="playfav_more" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.listfav();"><title>更多</title></sixteenByNinePoster>');
									break;
								}
							}
							;
							if (items.length < 8) {
								for ( var i = 0; i < pfav.length; i++) {
									fname = pfav[i][0];
									if (!pfav[i][1])
										continue;
									var modname = fname.substring(0, fname
											.indexOf("."));
									modname = jsn[modname] || modname;
									img = pfav[i][3] || '';
									img = xmlchar(img);
									count = pfav[i][4] || 0;
									updated = pfav[i][6] || 0;
									if (updated != 0)
										continue;
									bc = '';
									if (updated == 1 && count > 0)
										bc = '<badgeCount>' + count
												+ '</badgeCount>';
									sstr = '<sixteenByNinePoster id="playfav_'
											+ i
											+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.playfav('
											+ i
											+ ');" onPlay="atvu.playfav('
											+ i
											+ ');" onHoldSelect="atvu.removeidxfav('
											+ i + ');"><image>' + img
											+ '</image><title>'
											+ xmlchar(pfav[i][1]) + '</title>'
											+ bc + '</sixteenByNinePoster>';
									items.push(sstr);
									if (items.length >= 8) {
										items
												.push('<sixteenByNinePoster id="playfav_more" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.listfav();"><title>更多</title></sixteenByNinePoster>');
										break;
									}
								}
							}
							;
							if (items.length == 0) {
								favstr = '';
								wordstr = '（欢迎访问我们的论坛 www.ottnt.com ）';
							} else {
								favstr = '<collectionDivider alignment="left" accessibilityLabel=""><title>我的收藏（欢迎访问我们的论坛 www.ottnt.com ）</title></collectionDivider><shelf id="shelf_fav"><sections><shelfSection><items id="fav_item">'
										+ items.join("\n")
										+ '</items></shelfSection></sections></shelf>';
								wordstr = '';
							}
							;
							var pserver = atv.localStorage['pserver'] || [];
							psitems = [];
							for ( var i = 0; i < pserver.length; i++) {
								url = pserver[i].url;
								name = pserver[i].name;
								psitems
										.push('<moviePoster id="pserver_'
												+ i
												+ '" accessibilityLabel="" featured="true" alwaysShowTitles="true" onSelect="atvu.loadAction(\'personal.loadserver\',\'\',[\''
												+ xmlchar(url)
												+ '\',\''
												+ xmlchar(name)
												+ '\'])"><title><![CDATA['
												+ name
												+ ']]></title><image>http://trailers.apple.com:8000/blank-v2.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster>');
							}
							;
							var psstr = '';
							if (psitems.length > 0) {
								psstr = '<collectionDivider alignment="left" accessibilityLabel=""><title>个人服务器</title></collectionDivider><shelf id="shelf_pserver" columnCount="5"><sections><shelfSection><items id="pserver_item">'
										+ psitems.join("\n")
										+ '</items></shelfSection></sections></shelf>';
							}
							;
							page = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
									+ baseURL
									+ '/main.js" /></head><body> <scroller id="com.atvttvv.index" volatile="true" onVolatileReload="reloadfav()"><items> <grid id="grid_1" columnCount="5"><items> <!--<shelf id="shelf_1" columnCount="5"><sections><shelfSection><items>--> <moviePoster id="shelf_item_1" accessibilityLabel="sohu" featured="true" onSelect="atvu.loadAction(\'sohu.main\',\'\',[]);" onPlay="atvu.loadAction(\'sohu.main\',\'\',[]);"><image>http://trailers.apple.com:8000/sohu-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_2" accessibilityLabel="youku" featured="true" onSelect="atvu.loadAction(\'youku.main\',\'\',[]);" onPlay="atvu.loadAction(\'youku.main\',\'\',[]);"><image>http://trailers.apple.com:8000/youku-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_3" accessibilityLabel="qiyi" featured="true" onSelect="atvu.loadAction(\'qiyi.main\',\'\',[]);" onPlay="atvu.loadAction(\'qiyi.main\',\'\',[]);"><image>http://trailers.apple.com:8000/iqiyi-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_4" accessibilityLabel="pptv" featured="true" onSelect="atvu.loadAction(\'pptv.main\',\'\',[]);" onPlay="atvu.loadAction(\'pptv.main\',\'\',[]);"><image>http://trailers.apple.com:8000/pptv-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_5" accessibilityLabel="fx" featured="true" onSelect="atvu.loadAction(\'fx.main\',\'\',[]);" onPlay="atvu.loadAction(\'fx.main\',\'\',[]);"><image>http://trailers.apple.com:8000/fx-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <!--</items></shelfSection></sections></shelf><shelf id="shelf_2" columnCount="5"><sections><shelfSection><items>--> <moviePoster id="shelf_item_6" accessibilityLabel="verycd" featured="true" onSelect="atvu.loadAction(\'verycd.main\',\'\',[]);" onPlay="atvu.loadAction(\'verycd.main\',\'\',[]);"><image>http://trailers.apple.com:8000/verycd-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_7" accessibilityLabel="readself" featured="true" onSelect="atvu.loadAction(\'readself.main\',\'\',[]);" onPlay="atvu.loadAction(\'readself.main\',\'\',[]);"><image>http://trailers.apple.com:8000/readself-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_8" accessibilityLabel="yyets" featured="true" onSelect="atvu.loadAction(\'yyets.list\',\'\',[]);" onPlay="atvu.loadAction(\'yyets.list\',\'\',[]);"><image>http://trailers.apple.com:8000/yyets-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_9" accessibilityLabel="yinyuetai" featured="true" onSelect="atvu.loadAction(\'yinyuetai.main\',\'\',[]);" onPlay="atvu.loadAction(\'yinyuetai.main\',\'\',[]);"><image>http://trailers.apple.com:8000/yinyuetai-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_10" accessibilityLabel="xunlei" featured="true" onSelect="atvu.loadAction(\'xunlei.main\',\'\',[]);" onPlay="atvu.loadAction(\'xunlei.main\',\'\',[]);"><image>http://trailers.apple.com:8000/xunlei-v2.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_11" accessibilityLabel="search" featured="true" onSelect="atvu.loadAction(\'vweb.searchmain\',\'\',[],1);" onPlay="atvu.loadAction(\'vweb.searchmain\',\'\',[],1);"><image>http://trailers.apple.com:8000/search-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_12" accessibilityLabel="personal" featured="true" onSelect="atvu.loadAction(\'personal.newlink\',\'\',[]);" onPlay="atvu.loadAction(\'personal.newlink\',\'\',[]);"><image>http://trailers.apple.com:8000/user-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <!--</items></shelfSection></sections></shelf><shelf id="shelf_3" columnCount="5"><sections><shelfSection><items>--> <moviePoster id="shelf_item_13" accessibilityLabel="main" featured="true" onSelect="mainpage();" onPlay="mainpage();"><image>http://trailers.apple.com:8000/menu-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_15" accessibilityLabel="setting" featured="true" alwaysShowTitles="true" onSelect="atvu.loadAction(\'personal.main\',\'\',[]);" onPlay="atvu.loadAction(\'personal.main\',\'\',[]);"><image>http://trailers.apple.com:8000/setting-v2.png?2012120101</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <!--</items></shelfSection></sections></shelf>--> </items></grid>  '
									+ favstr
									+ psstr
									+ '<collectionDivider alignment="left" accessibilityLabel=""><title>友情链接'
									+ wordstr
									+ '</title></collectionDivider> <grid id="grid_2" columnCount="5"><items> <moviePoster id="shelf_item_15" accessibilityLabel="missde" featured="true" onSelect="atvu.loadURL(\'http://4.appletv3.sinaapp.com/index.php\');" onPlay="atvu.loadURL(\'http://4.appletv3.sinaapp.com/index.php\');"><image>http://trailers.apple.com:8000/missde-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_16" accessibilityLabel="lsl" featured="true" onSelect="atvu.loadURL(\'http://218.204.102.10/appletv/youku/youku.php\');" onPlay="atvu.loadURL(\'http://218.204.102.10/appletv/youku/youku.php\');"><image>http://trailers.apple.com:8000/lslyouku-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> <moviePoster id="shelf_item_17" accessibilityLabel="jgk" featured="true" onSelect="atvu.loadURL(\'http://atv.jianguoke.com/\');" onPlay="atvu.loadURL(\'http://atv.jianguoke.com\');"><image>http://trailers.apple.com:8000/jianguoke-v1.png</image><defaultImage>resource://16x9.png</defaultImage></moviePoster> </items></grid> </items></scroller></body></atv>';
							atv.loadXML(atv.parseXML(page));
						}, 1);
	} catch (e) {
		logger.error(e);
	}
};
function motd(always) {
	try {
		var today = (new Date()).getDate();
		var mday = atv.localStorage['motd'];
		il = [];
		il
				.push({
					"type" : "photo",
					"id" : "photo_0",
					"assets" : [ {
						"width" : 640,
						"height" : 480,
						"src" : 'http://img02.taobaocdn.com/imgextra/i2/54178405/T222q5XjpXXXXXXXXX_!!54178405.jpg'
					} ]
				});
		xml = '<?xml version="1.0" encoding="UTF-8"?><atv><body><shelfList id="test"><centerShelf><shelf id="shelf"><sections><shelfSection><items><goldenPoster id="p1"><image><![CDATA[http://img02.taobaocdn.com/imgextra/i2/54178405/T222q5XjpXXXXXXXXX_!!54178405.jpg]]></image></goldenPoster></items></shelfSection></sections></shelf></centerShelf><menu><sections><menuSection><items><oneLineMenuItem id="m1" onSelect="atv.unloadPage();"><label>OK</label></oneLineMenuItem></items></menuSection></sections></menu></shelfList></body></atv>';
		if (mday == today && always != 1)
			return;
		atv.localStorage['motd'] = today;
		atvu.ajax(baseURL + '/motd.txt', "GET", null, null, function(v, c) {
			if (v == null)
				return;
			atv.loadXML(atvu.showMessage({
				title : "MOTD",
				message : v,
				buttons : [ {
					label : '返回',
					script : 'atv.unloadPage()'
				} ]
			}));
		});
	} catch (e) {
		logger.debug(e);
	}
};
try {
	lang = atv.device.language;
	if (lang != 'zh_CN' && lang != 'zh_TW')
		lang = 'en';
	else
		lang = 'cn';
} catch (e) {
	logger.debug(e);
};