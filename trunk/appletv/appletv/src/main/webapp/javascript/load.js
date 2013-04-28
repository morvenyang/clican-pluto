try {
	if(atv){
		var jsVersion = '1.0.1.e';
		var src = document
		.evaluateXPath("descendant::script", document.rootElement)[0]
		.getAttribute('src');
		var serverurl = src.substring(0, src.indexOf('/appletv'))+'/appletv';
		var localJsVersion = atv.localStorage['clican.js.version'];
		var urls = ['clican.js', 'ejs.js', 'fivesix.js', 'lbl.js', 'myphoto.js',
				'photoPreview.js', 'qq.js', 'smb.js', 'soku.js', 'taobao.js',
				'tu.js', 'tudou.js', 'view.js', 'weivideo.js', 'xunlei.js',
				'youku.js', 'yyets.js' ];
		if (localJsVersion == null || localJsVersion.length == 0
				|| localJsVersion != jsVersion) {
			for ( var i = 0; i < urls.length; i++) {
				var name = 'clican.' + urls[i];
				var url = serverurl + '/javascript/' + urls[i];
				var xhr = new XMLHttpRequest();
				xhr.open("GET", url, false);
				xhr.send();
				value = xhr.responseText;
				atv.localStorage[name] = value;
				eval(value);
			}
			atv.localStorage['clican.js.version'] = jsVersion;
		} else {
			for ( var i = 0; i < urls.length; i++) {
				var name = 'clican.' + urls[i];
				var value = atv.localStorage[name];
				if (value == null || value.length == 0) {
					var url = serverurl + '/javascript/' + urls[i];
					var xhr = new XMLHttpRequest();
					xhr.open("GET", url, false);
					xhr.send();
					value = xhr.responseText;
					atv.localStorage[name] = value;
				}
				eval(value);
			}
		}
	}
} catch (e) {
	var errorXML = '<?xml version="1.0" encoding="UTF-8"?> \
        <atv> \
        <body> \
        <dialog id="com.sample.error-dialog"> \
        <title><![CDATA['
			+ '动态加载JS失败'
			+ ']]></title> \
        <description><![CDATA['
			+ e
			+ ']]></description> \
        </dialog> \
        </body> \
        </atv>';
	atv.loadXML(atv.parseXML(errorXML));
}