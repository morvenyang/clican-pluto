function __init() {
	CHANNELS = [ [ "直播", 0 ], [ "电影", "1" ], [ "电视剧", "2" ], [ "动漫", "16" ],
			[ "综艺", "7" ] ];
	return {
		"version" : 8,
		main : function(args) {
			items = new Array();
			for ( var i = 0; i < CHANNELS.length; i++) {
				items
						.push('<oneLineMenuItem id="menu_'
								+ i
								+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'sohu.page\',\'\',[\''
								+ CHANNELS[i][1]
								+ '\']);"><label>'
								+ CHANNELS[i][0]
								+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
			}
			;
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.sohu.main"><header><simpleHeader><title>搜狐视频</title></simpleHeader></header><menu><sections><menuSection><items>'
					+ items.join("\n")
					+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
		live : function(args) {
			url = args[0];
			name = args[1];
			atv.setTimeout(function() {
				atvu.realplay(url, name, name, -1);
			}, 100);
		},
		page : function(args) {
			c = args[0];
			if (c == '0') {
				url = baseURL + '/slive.txt';
				atvu
						.ajax(
								url,
								"GET",
								null,
								null,
								function(v, c) {
									ll = v.split("\n");
									shelfs = [];
									shelfs
											.push('<collectionDivider alignment="left" accessibilityLabel=""><title>(长按中间键加入收藏)</title></collectionDivider>');
									items = [];
									for ( var i = 0; i < ll.length; i++) {
										l = ll[i];
										c = l.split(" ");
										if (c.length != 4)
											continue;
										act = "atvu.loadAction('sohu.live','"
												+ xmlchar(c[1]) + "',['"
												+ xmlchar(c[3]) + "','"
												+ xmlchar(c[1]) + "'])";
										acth = "atvu.saveAction('sohu.live','"
												+ xmlchar(c[1]) + "',['"
												+ xmlchar(c[3]) + "','"
												+ xmlchar(c[1]) + "'])";
										item = '<actionButton id="shelf_page_'
												+ i + '" onSelect="' + act
												+ ';" onPlay="' + act
												+ ';" onHoldSelect="' + acth
												+ ';"><title>' + xmlchar(c[1])
												+ '</title></actionButton>';
										items.push(item);
									}
									;
									sstr = '<grid id="grid_' + shelfs.length
											+ '" columnCount="10"><items>'
											+ items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
									xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
											+ baseURL
											+ '/main.js" /></head><body><scroller id="com.atvttvv.sohu.livepage"><items>'
											+ shelfs.join('\n')
											+ '</items></scroller></body></atv>';
									atv.loadAndSwapXML(atv.parseXML(xml));
								});
				return;
			}
			;
			area = args[1] || '';
			cat = args[2] || '';
			year = args[3] || '';
			o = args[4] || '7';
			p = args[5] || '1';
			swap = args[6] || 0;
			shelfs = []
			shelfs
					.push('<shelf id="shelf_top"><sections><shelfSection><items><actionButton id="shelf_item_filter" onSelect="atvu.loadAction(\'sohu.filter\',\'\',[\''
							+ c
							+ '\',\''
							+ area
							+ '\',\''
							+ cat
							+ '\',\''
							+ year
							+ '\',\''
							+ o
							+ '\']);"><title>选择</title></actionButton></items></shelfSection></sections></shelf>');
			url = 'http://v.m.sohu.com/api/jsp.action?c=' + c + '&area=' + area
					+ '&cat=' + cat + '&year=' + year + '&o=' + o
					+ '&plat=1&sver=2.4.1&mtype=1&mq=0&tvType=-1&v=4&p=' + p
					+ '&s=36';
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(data, cookie) {
								v = JSON.parse(data);
								count = v['c'];
								if (!count) {
									logger.debug(data);
									atv.loadAndSwapXML(atvu.showMessage({
										title : '搜狐',
										message : '没有内容'
									}));
									return;
								}
								;
								items = [];
								for ( var i = 0; i < v['r'].length; i++) {
									r = v['r'][i];
									imgurl = r['wpic'];
									n = r['title'];
									if (r['tipNum'] == '1/1') {
										url31 = r['url31'] || '';
										highUrl = r['highUrl'] || '';
										url = r['url'] || '';
										lowUrl = r['lowUrl'] || '';
										nn = xmlchar(n.replace(/'/, ''));
										nd = xmlchar(r['desc'].replace(/'/, ''));
										length = parseInt(r['timeLength']);
										act = "atvu.loadAction('sohu.playchoice','"
												+ xmlchar(n)
												+ "',['"
												+ nn
												+ "','"
												+ nd
												+ "','"
												+ imgurl
												+ "',"
												+ length
												+ ",'"
												+ lowUrl
												+ "','"
												+ url
												+ "','"
												+ highUrl
												+ "','" + url31 + "'])";
										acth = "atvu.saveAction('sohu.playchoice','"
												+ xmlchar(n)
												+ "',['"
												+ nn
												+ "','"
												+ nd
												+ "','"
												+ imgurl
												+ "',"
												+ length
												+ ",'"
												+ lowUrl
												+ "','"
												+ url
												+ "','"
												+ highUrl
												+ "','" + url31 + "'])";
									} else {
										act = "atvu.loadAction('sohu.view','"
												+ xmlchar(n) + "',['"
												+ r['aid'] + "'])";
										acth = "atvu.saveAction('sohu.view','"
												+ xmlchar(n) + "',['"
												+ r['aid'] + "'])";
									}
									;
									item = '<sixteenByNinePoster id="shelf_item_'
											+ items.length
											+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
											+ act
											+ ';" onPlay="'
											+ act
											+ ';" onHoldSelect="'
											+ acth
											+ ';"><title>'
											+ xmlchar(n)
											+ '</title><image>'
											+ xmlchar(imgurl)
											+ '</image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
									items.push(item);
								}
								;
								shelfs
										.push('<collectionDivider alignment="left" accessibilityLabel=""><title>(长按中间键加入收藏)</title></collectionDivider>');
								shelfs.push('<grid id="shelf_' + shelfs.length
										+ '" columnCount="9"><items>'
										+ items.join("\n") + '</items></grid>');
								pages = Math.floor((count + 35) / 36);
								items = [];
								if (pages > 1) {
									grids = [];
									for ( var i = 1; i <= pages; i++) {
										if (i == parseInt(p))
											continue;
										act = "atvu.loadAction('sohu.page','',['"
												+ c
												+ "','"
												+ area
												+ "','"
												+ cat
												+ "','"
												+ year
												+ "','"
												+ o + "','" + i + "',1])";
										item = '<actionButton id="shelf_page_'
												+ i + '" onSelect="' + act
												+ ';" onPlay="' + act
												+ ';"><title>第' + i
												+ '页</title></actionButton>';
										items.push(item);
										if (items.length == 10) {
											gstr = '<grid id="pagegrid_'
													+ grids.length
													+ '" columnCount="10"><items>'
													+ items.join("\n")
													+ '</items></grid>';
											grids.push(gstr);
											items = [];
										}
									}
									;
									if (items.length > 0) {
										gstr = '<grid id="pagegrid_'
												+ grids.length
												+ '" columnCount="12"><items>'
												+ items.join("\n")
												+ '</items></grid>';
										grids.push(gstr);
									}
									;
									shelfs
											.push('<pagedGrid id="shelf_'
													+ shelfs.length + '">'
													+ grids.join("\n")
													+ '</pagedGrid>');
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.sohu.page"><items>'
										+ shelfs.join("\n")
										+ '</items></scroller></body></atv>';
								if (swap == 1)
									atv.unloadPage();
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		playchoice : function(args) {
			n = args[0];
			d = args[1];
			img = args[2];
			length = args[3];
			lu = args[4];
			url = args[5];
			hu = args[6];
			u3 = args[7];
			actions = [];
			if (u3 != '') {
				act = "atvu.realplay('" + u3 + "','" + xmlchar(n) + "','"
						+ xmlchar(d) + "'," + length + ")";
				astr = '<actionButton id="play" onSelect="'
						+ act
						+ ';"><title>原画</title><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage><badge>HD</badge></actionButton>';
				actions.push(astr);
			}
			;
			if (hu != '') {
				act = "atvu.realplay('" + hu + "','" + xmlchar(n) + "','"
						+ xmlchar(d) + "'," + length + ")";
				astr = '<actionButton id="play" onSelect="'
						+ act
						+ ';"><title>超清</title><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage><badge>HD</badge></actionButton>';
				actions.push(astr);
			}
			;
			if (url != '') {
				act = "atvu.realplay('" + url + "','" + xmlchar(n) + "','"
						+ xmlchar(d) + "'," + length + ")";
				astr = '<actionButton id="play" onSelect="'
						+ act
						+ ';"><title>普通</title><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage><badge>HD</badge></actionButton>';
				actions.push(astr);
			}
			;
			if (lu != '') {
				act = "atvu.realplay('" + lu + "','" + xmlchar(n) + "','"
						+ xmlchar(d) + "'," + length + ")";
				astr = '<actionButton id="play" onSelect="'
						+ act
						+ ';"><title>流畅</title><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage><badge>HD</badge></actionButton>';
				actions.push(astr);
			}
			;
			xml = '<atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><itemDetail id="com.atvttvv.sohu.playchoice"><title>'
					+ xmlchar(n)
					+ '</title><subtitle>'
					+ xmlchar(n)
					+ '</subtitle><rating>PG</rating><summary>'
					+ xmlchar(d)
					+ '</summary><image style="moviePoster">'
					+ xmlchar(img)
					+ '</image><defaultImage>resource://Poster.png</defaultImage><footnote>'
					+ xmlchar(n)
					+ '</footnote><centerShelf><shelf id="centerShelf" columnCount="4" center="true"><sections><shelfSection><items>'
					+ actions.join("\n")
					+ '</items></shelfSection></sections></shelf></centerShelf></itemDetail></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
		filter : function(args) {
			c = args[0];
			area = args[1] || '';
			cat = args[2] || '';
			year = args[3] || '';
			o = args[4] || '7';
			swap = args[5] || 0;
			url = 'http://v.m.sohu.com/api/listtype.action?plat=1&sver=5.1.1&c='
					+ c;
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(data, cookie) {
								var shelfs = new Array();
								var items;
								var si = 1, sii = 1;
								typelist = JSON.parse(data);
								if (typelist['areav']) {
									items = new Array();
									areav = typelist['areav'];
									for ( var i = 0; i < areav.length; i++) {
										ainfo = areav[i];
										acode = ainfo['code'];
										n = ainfo['name'];
										if (acode == area)
											sel = "选择";
										else
											sel = "";
										act = "atvu.loadAction('sohu.filter','',['"
												+ c
												+ "','"
												+ acode
												+ "','"
												+ cat
												+ "','"
												+ year
												+ "','"
												+ o + "',1])";
										item = '<actionButton id="shelf_item_'
												+ sii + '" onSelect="' + act
												+ ';" onPlay="' + act
												+ ';"><title>' + n
												+ '</title><subtitle>' + sel
												+ '</subtitle></actionButton>';
										items.push(item);
										sii++;
									}
									;
									sstr = '<grid id="shelf_' + si
											+ '" columnCount="' + items.length
											+ '"><items>' + items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
									si++;
								}
								;
								if (typelist['yearv']) {
									items = new Array();
									yearv = typelist['yearv'];
									for ( var i = 0; i < yearv.length; i++) {
										yinfo = yearv[i];
										ycode = yinfo['code'];
										n = yinfo['name'];
										if (ycode == year)
											sel = "选择";
										else
											sel = "";
										act = "atvu.loadAction('sohu.filter','',['"
												+ c
												+ "','"
												+ area
												+ "','"
												+ cat
												+ "','"
												+ ycode
												+ "','"
												+ o + "',1])";
										item = '<actionButton id="shelf_item_'
												+ sii + '" onSelect="' + act
												+ ';" onPlay="' + act
												+ ';"><title>' + n
												+ '</title><subtitle>' + sel
												+ '</subtitle></actionButton>';
										items.push(item);
										sii++;
									}
									;
									sstr = '<grid id="shelf_' + si
											+ '" columnCount="' + items.length
											+ '"><items>' + items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
									si++;
								}
								;
								if (typelist['catv']) {
									items = new Array();
									catv = typelist['catv'];
									for ( var i = 0; i < catv.length; i++) {
										cinfo = catv[i];
										ccode = cinfo['code'];
										n = cinfo['name'];
										if (ccode == cat)
											sel = "选择";
										else
											sel = "";
										act = "atvu.loadAction('sohu.filter','',['"
												+ c
												+ "','"
												+ area
												+ "','"
												+ ccode
												+ "','"
												+ year
												+ "','"
												+ o + "',1])";
										item = '<actionButton id="shelf_item_'
												+ sii + '" onSelect="' + act
												+ ';" onPlay="' + act
												+ ';"><title>' + n
												+ '</title><subtitle>' + sel
												+ '</subtitle></actionButton>';
										items.push(item);
										sii++;
									}
									;
									sstr = '<grid id="shelf_' + si
											+ '" columnCount="' + items.length
											+ '"><items>' + items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
									si++;
								}
								;
								act = "atvu.loadAction('sohu.page','',['" + c
										+ "','" + area + "','" + cat + "','"
										+ year + "','" + o + "','1',1])";
								sstr = '<grid id="shelf_'
										+ shelfs.length
										+ '" columnCount="1"><items><actionButton id="shelf_item_'
										+ (sii++)
										+ '" onSelect="'
										+ act
										+ ';" onPlay="'
										+ act
										+ ';"><title>确定</title></actionButton></items></grid>';
								shelfs.push(sstr);
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.sohu.filter"><items>'
										+ shelfs.join('\n')
										+ '</items></scroller></body></atv>';
								if (swap == 1)
									atv.unloadPage();
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		playview : function(args) {
			aid = args[0];
			i = args[1];
			usepl = args[2];
			url = "http://v.m.sohu.com/api/view.action?subjectId=" + aid
					+ "&device=ipad&ipadNot=1";
			atvu.ajax(url, "GET", null, null, function(data, cookie) {
				morev = JSON.parse(data);
				v = morev['details'][i];
				p = v['bigPic'];
				n = v['videoTitle'];
				d = v['des'];
				length = parseInt(v["timeLength"]);
				if (v['highUrl'] && v['highUrl'] != '')
					vurl = v['highUrl'];
				else
					vurl = v['url'];
				pl = [];
				for ( var j = i + 1; j < morev['details'].length; j++) {
					nv = morev['details'][j];
					nn = nv['videoTitle'];
					nd = nv['des'];
					nlength = parseInt(nv["timeLength"]);
					if (nv['highUrl'] && nv['highUrl'] != '')
						nvurl = nv['highUrl'];
					else
						nvurl = nv['url'];
					pl.push([ nn, nvurl, nd, nlength ]);
				}
				;
				if (usepl == 1)
					atvu.realplay(vurl, n, d, length, pl);
				else
					atvu.realplay(vurl, n, d, length);
			});
		},
		view : function(args) {
			aid = args[0];
			url = "http://v.m.sohu.com/api/view.action?subjectId=" + aid
					+ "&device=ipad&ipadNot=1";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(data, cookie) {
								morev = JSON.parse(data);
								shelfs = [];
								items = [];
								allnames = [];
								for ( var i = 0; i < morev['details'].length; i++) {
									allnames
											.push(morev['details'][i]['videoTitle']);
								}
								;
								namebase = atvu.reducename(allnames, 3);
								shelfs
										.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
												+ xmlchar(namebase)
												+ '(长按中间键连续播放)</title></collectionDivider>');
								for ( var i = 0; i < morev['details'].length; i++) {
									v = morev['details'][i];
									p = v['bigPic'];
									n = v['videoTitle'];
									nname = n;
									if (namebase.length > 0) {
										if (nname.substring(0, namebase.length) == namebase) {
											nname = nname
													.substring(namebase.length);
										}
									}
									;
									act = "atvu.loadAction('sohu.playview','"
											+ n + "',['" + aid + "'," + i
											+ "])";
									acth = "atvu.loadAction('sohu.playview','"
											+ n + "',['" + aid + "'," + i
											+ ",1])";
									item = '<sixteenByNinePoster id="shelf_item_'
											+ i
											+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
											+ act
											+ ';" onPlay="'
											+ act
											+ ';" onHoldSelect="'
											+ acth
											+ ';"><title>'
											+ xmlchar(nname)
											+ '</title><image>'
											+ xmlchar(p)
											+ '</image><defaultImage>resource://16x9.png</defaultImage></sixteenByNinePoster>';
									items.push(item);
								}
								;
								sstr = '<grid id="grid_' + shelfs.length
										+ '"><items>' + items.join('\n')
										+ '</items></grid>';
								shelfs.push(sstr);
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.sohu.view"><items>'
										+ shelfs.join('\n')
										+ '</items></scroller></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
	}
};