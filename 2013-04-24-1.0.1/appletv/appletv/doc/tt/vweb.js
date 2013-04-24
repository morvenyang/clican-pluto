function __init() {
	var sites = [ [ 'qiyi', '奇艺' ], [ 'sohu', '搜狐' ], [ 'pptv', 'PPTV' ],
			[ 'yinyuetai', '音悦Tai' ], [ 'fx', '风行' ], [ 'youku', '优酷' ],
			[ 'verycd', '电驴大全' ], [ 'readself', '电影来了' ] ];
	var ssites = [ [ 'verycd', '电驴大全' ], [ 'soku', '搜库' ], [ 'yyets', '人人影视' ],
			[ 'readself', '电影来了' ], [ 'yinyuetai', '音悦Tai' ],
			[ 'pps', [ 'PPS' ] ] ];
	return {
		"version" : 22,
		main : function(args) {
			if (atv.device.udid == 'DY3HJY89DRHN'
					|| atv.device.udid == 'C0HHGRAEDRHN') {
				sites = [ [ 'qiyi', '奇艺' ], [ 'sohu', '搜狐' ],
						[ 'pptv', 'PPTV' ], [ 'yinyuetai', '音悦Tai' ],
						[ 'fx', '风行' ], [ 'youku', '优酷' ],
						[ 'verycd', '电驴大全' ], [ 'readself', '电影来了' ],
						[ 'pps', 'PPS' ], [ 'winvod', '星空宽频' ] ];
			}
			;
			items = new Array();
			for ( var i = 0; i < sites.length; i++) {
				items
						.push('<oneLineMenuItem id="menu_'
								+ sites[i][0]
								+ '" accessibilityLabel="" onSelect="atvu.loadAction(\''
								+ sites[i][0]
								+ '.main\',\'\',null)"><label>'
								+ sites[i][1]
								+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
			}
			;
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.vweb.main"><header><simpleHeader><title>视频网站</title></simpleHeader></header><menu><sections><menuSection><items>'
					+ items.join("\n")
					+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
		search : function(args) {
			items = new Array();
			for ( var i = 0; i < ssites.length; i++) {
				items
						.push('<oneLineMenuItem id="menu_'
								+ ssites[i][0]
								+ '" accessibilityLabel="" onSelect="atvu.loadAction(\''
								+ ssites[i][0]
								+ '.searchmain\',\'\',null)"><label>'
								+ ssites[i][1]
								+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
			}
			;
			items
					.push('<oneLineMenuItem id="menu_search" accessibilityLabel="" onSelect="atvu.loadAction(\'vweb.searchmain\',\'\',null)"><label>TEST</label><accessories><arrow /></accessories></oneLineMenuItem>');
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.vweb.main"><header><simpleHeader><title>视频网站</title></simpleHeader></header><menu><sections><menuSection><items>'
					+ items.join("\n")
					+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
		searchmain : function(args) {
			xml = '<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd"><plist version="1.0"><dict><key>merchant</key><string>ottnt</string><key>identifier</key><string>com.atvttvv.vweb.search</string><key>page-type</key><dict><key>template-name</key><string>search</string><key>template-parameters</key><dict><key>header</key><dict><key>type</key><string>simple-header</string><key>title</key><string>搜索</string><key>subtitle</key><string></string></dict></dict></dict><key>url</key><string>http://trailers.apple.com:8000/search/searchs.xml?q=</string></dict></plist>';
			atv.loadPlist(xml);
		},
		searchword : function(args) {
			word = args[0];
			items = new Array();
			for ( var i = 0; i < ssites.length; i++) {
				items
						.push('<oneLineMenuItem id="menu_'
								+ ssites[i][0]
								+ '" accessibilityLabel="" onSelect="atvu.loadAction(\''
								+ ssites[i][0]
								+ '.search\',\'\',[\''
								+ xmlchar(word)
								+ '\'])"><label>'
								+ ssites[i][1]
								+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
			}
			;
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.vweb.main"><header><simpleHeader><title>选择搜索引擎</title><subtitle>搜索：'
					+ xmlchar(word)
					+ '</subtitle></simpleHeader></header><menu><sections><menuSection><items>'
					+ items.join("")
					+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
	}
};