try {
	if(atv){
		var jsVersion = '1.2.0.1';
		var src = document
		.evaluateXPath("descendant::script", document.rootElement)[0]
		.getAttribute('src');
		var localEnv = true;
		if(src.indexOf('local.clican.org')!=-1||src.indexOf('www.clican.org')!=-1){
			localEnv = false;
		}
		if(src.indexOf('local.clican.org')!=-1){
			src = src.replace('local.clican.org','www.clican.org');
		}
		var serverurl = src.substring(0, src.indexOf('/appletv'))+'/appletv';
		var localJsVersion = atv.localStorage['clican.js.version'];
		if(localEnv){
			localJsVersion = atv.localStorage['clican.local.js.version'];
		}else{
			localJsVersion = atv.localStorage['clican.remote.js.version'];
		}
		var urls = ['clican.js', 'ejs.js', 'fivesix.js', 'lbl.js', 'localcache.js', 'myphoto.js',
				'photoPreview.js', 'qq.js', 'smb.js', 'soku.js','subtitle.js', 'taobao.js',
				'tu.js', 'tudou.js', 'view.js', 'weivideo.js', 'xunlei.js',
				'youku.js', 'yyets.js' ];
		if (localJsVersion == null || localJsVersion.length == 0
				|| localJsVersion != jsVersion) {
			for ( var i = 0; i < urls.length; i++) {
				var url = serverurl + '/javascript/' + urls[i];
				var name = 'clican.'+serverurl + '/javascript/' + urls[i]+'?version='+jsVersion;
				var xhr = new XMLHttpRequest();
				xhr.open("GET", url, false);
				xhr.send();
				var value = xhr.responseText;
				atv.localStorage[name] = value;
				eval(value);
			}
			if(localEnv){
				atv.localStorage['clican.local.js.version'] = jsVersion;
			}else{
				atv.localStorage['clican.remote.js.version'] = jsVersion;
			}
		} else {
			for ( var i = 0; i < urls.length; i++) {
				var url = serverurl + '/javascript/' + urls[i];
				var name = 'clican.'+serverurl + '/javascript/' + urls[i]+'?version='+jsVersion;
				var value = atv.localStorage[name];
				if (value == null || value.length == 0) {
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