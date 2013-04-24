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
logger.init({
	logURL : 'http://serv.ottnt.com:8580/log.xml',
	logLevel : 4
});
try {
	var src = document
			.evaluateXPath("descendant::script", document.rootElement)[0]
			.getAttribute('src');
	baseURL = src.substring(0, src.lastIndexOf('/'));
	atv.sessionStorage['baseURL'] = baseURL;
} catch (e) {
	logger.debug(e);
};
var jsc = {
	main : 19,
	vweb : 23,
	qiyi : 16,
	sohu : 30,
	xunlei : 23,
	pptv : 20,
	verycd : 27,
	yinyuetai : 13,
	soku : 11,
	youku : 20,
	v56 : 3,
	tudou : 3,
	sina : 2,
	letv : 1,
	qq : 1,
	fx : 9,
	yyets : 15,
	personal : 13,
	cntv : 2,
	pps : 7,
	m1905 : 1,
	readself : 20,
	netease : 1,
	winvod : 3,
	ku6 : 1,
	zongheng : 2
};
var jscache = atv.localStorage['jscache'] || {};
maincode = jscache['main'] || '';
mainversion = 0;
if (maincode != '') {
	try {
		eval(maincode);
	} catch (e) {
	}
};
try {
	if (mainversion != jsc.main) {
		url = baseURL + '/mains.js';
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function() {
			try {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						maincode = xhr.responseText;
						try {
							eval(maincode);
							jscache['main'] = maincode;
							atv.localStorage['jscache'] = jscache;
						} catch (e) {
							logger.debug("Script Error:" + e);
						}
					}
				}
			} catch (e) {
				logger.debug(e);
			}
		};
		xhr.open("GET", url, false);
		xhr.send();
	}
} catch (e) {
	logger.debug(e);
};