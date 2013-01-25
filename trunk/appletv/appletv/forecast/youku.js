function __init() {
	return {
		"version" : 17,
		main : function(args) {
			items = [];
			shelfs = [];
			act = "atvu.loadAction('youku.page','',['http://www.youku.com/v_olist/c_96.html','电影'])";
			item = '<actionButton id="youku_item_movie" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>电影</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.page','',['http://www.youku.com/v_olist/c_97.html','电视剧'])";
			item = '<actionButton id="youku_item_tv" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>电视剧</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.page','',['http://www.youku.com/v_olist/c_85.html','综艺'])";
			item = '<actionButton id="youku_item_zy" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>综艺</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.page','',['http://www.youku.com/v_olist/c_95.html','音乐'])";
			item = '<actionButton id="youku_item_music" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>音乐</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.page','',['http://www.youku.com/v_olist/c_100.html','动漫'])";
			item = '<actionButton id="youku_item_comic" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>动漫</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.list','',[])";
			item = '<actionButton id="youku_item_list" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>全部</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('youku.login','',[])";
			item = '<actionButton id="youku_item_login" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>登录</title></actionButton>';
			items.push(item);
			act = "atvu.loadAction('soku.searchmain','',[])";
			item = '<actionButton id="youku_item_search" onSelect="' + act
					+ ';" onPlay="' + act
					+ ';"><title>搜索</title></actionButton>';
			items.push(item);
			shelfs.push('<grid id="shelf_' + shelfs.length
					+ '" columnCount="8"><items>' + items.join("\n")
					+ '</items></grid>');
			url = "http://www.youku.com/";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null)
									return;
								lis = atvu
										.findall(
												'<li id="[^"]*" tabIdx="([^"]+)"[^>]*>\\s*<a[^>]*>([^<]+)</a>',
												d);
								for ( var i = 0; i < lis.length; i++) {
									idx = lis[i][0];
									name = lis[i][1];
									if (name == '最新电视剧' || name == '大陆剧'
											|| name == '韩剧' || name == '港台剧'
											|| name == '美剧' || name == '最新电影'
											|| name == '热播大片' || name == '华语'
											|| name == '欧美' || name == '韩国') {
										items = [];
										pos = d
												.indexOf('<div id="' + idx
														+ '"');
										pos1 = d.indexOf('<div id="tab',
												pos + 10);
										div = d.substring(pos, pos1);
										itms = div.split('<ul class="p">');
										if (itms.length == 1)
											continue;
										shelfs
												.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
														+ xmlchar(name)
														+ '(长按中间键加入收藏)</title></collectionDivider>');
										for ( var j = 1; j < itms.length; j++) {
											rs = (new RegExp(
													'<li class="p_status">(.*)</li>'))
													.exec(itms[j]);
											if (rs)
												status = sh(rs[1]);
											else
												status = '';
											if (itms[j]
													.indexOf('<li class="p_ischarge">') >= 0)
												status = status + "(付费)";
											rs = (new RegExp(
													'<li class="p_ishd"><span class="[^"]*" title="([^"]+)">'))
													.exec(itms[j]);
											if (rs) {
												if (status == '')
													status = rs[1];
												else
													status = status + ","
															+ rs[1];
											}
											;
											rs = (new RegExp(
													'<li class="p_thumb"><img _src="([^"]+)"'))
													.exec(itms[j]);
											if (rs)
												imgurl = rs[1];
											else
												imgurl = '';
											rs = (new RegExp(
													'<li class="p_title"><a title="([^"]+)" href="([^"]+)"'))
													.exec(itms[j]);
											itemname = rs[1];
											itemurl = rs[2];
											rs = /&lt;(.*)&gt;/.exec(itemname);
											if (rs)
												itemname = rs[1];
											act = "atvu.loadAction('youku.viewurl','"
													+ xmlchar(itemname)
													+ "',['"
													+ xmlchar(itemurl)
													+ "','"
													+ xmlchar(itemname)
													+ "'])";
											acth = "atvu.saveAction('youku.viewurl','"
													+ xmlchar(itemname)
													+ "',['"
													+ xmlchar(itemurl)
													+ "','"
													+ xmlchar(itemname)
													+ "'],'"
													+ xmlchar(imgurl)
													+ "','"
													+ xmlchar(itemurl)
													+ "')";
											item = '<sixteenByNinePoster id="shelf_item_'
													+ shelfs.length
													+ '_'
													+ items.length
													+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
													+ act
													+ ';" onPlay="'
													+ act
													+ ';" onHoldSelect="'
													+ acth
													+ ';"><title>'
													+ xmlchar(itemname)
													+ '</title><subtitle>'
													+ xmlchar(status)
													+ '</subtitle><image>'
													+ xmlchar(imgurl)
													+ '</image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
											items.push(item);
										}
										;
										shelfs
												.push('<shelf id="shelf_'
														+ shelfs.length
														+ '"><sections><shelfSection><items>'
														+ items.join("\n")
														+ '</items></shelfSection></sections></shelf>');
									}
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.youku.main"><items>'
										+ shelfs.join("\n")
										+ '</items></scroller></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		captcha : function(arg) {
			imgurl = "http://www.youku.com/user/captcha/?" + Math.random();
			nurl = "http://serv.ottnt.com:8580/captcha.html?" + Math.random();
			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						try {
							img = xhr.responseDataAsBase64;
							atvu
									.ajax(
											"http://serv.ottnt.com:8580/captcha.html",
											"POST",
											null,
											img,
											function(d, c) {
												xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
														+ baseURL
														+ '/main.js" /></head><body><shelfList id="com.atvttvv.youku.captcha"><header><simpleHeader><title>查看验证码</title></simpleHeader></header><centerShelf><shelf id="shelf"><sections><shelfSection><items><moviePoster id="captcha" onSelect="atvu.loadAction(\'youku.inputcaptcha\',\'\',[],1);"><image>'
														+ nurl
														+ '</image></moviePoster></items></shelfSection></sections></shelf></centerShelf><menu><sections><menuSection><items><oneLineMenuItem id="btn_input" onSelect="atvu.loadAction(\'youku.inputcaptcha\',\'\',[],1);"><label>输入</label></oneLineMenuItem><oneLineMenuItem id="btn_reload" onSelect="atvu.loadAction(\'youku.captcha\',\'\',[],1);"><label>刷新</label></oneLineMenuItem></items></menuSection></sections></menu></shelfList></body></atv>';
												atv.loadAndSwapXML(atv
														.parseXML(xml));
											});
						} catch (e) {
							logger.debug(e)
						}
					}
				}
			};
			xhr.open("GET", imgurl, true);
			xhr.send();
		},
		inputcaptcha : function(arg) {
			username = atv.localStorage['youkuuser'] || '';
			password = atv.localStorage['youkupass'] || '';
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = "请输入验证码";
			textEntry.label = '验证码';
			textEntry.onSubmit = function(cc) {
				atvu.ajax('http://www.youku.com/user_login/', 'GET', {
					'Referer' : 'http://i.youku.com/u/home/'
				}, null, function(d, c) {
					url = "http://www.youku.com/user_loginSubmit/";
					postdata = "user_name_login=" + username + "&passwd_login="
							+ password + "&captcha=" + cc
							+ "&forever=on&callback=login_submit_callback";
					atvu.ajax(url, "POST", {
						'Origin' : 'http://www.youku.com',
						'Referer' : 'http://www.youku.com/user_login/'
					}, postdata, function(d, c) {
						rs = (new RegExp(
								"parent.login_submit_callback\\(([^,]+),"))
								.exec(d);
						if (rs) {
							ret = parseInt(rs[1]);
							if (ret == 1) {
								atv.loadAndSwapXML(atvu.showMessage({
									title : '优酷',
									message : '登录成功',
									buttons : [ {
										label : '确定',
										script : 'atv.unloadPage()'
									} ]
								}));
							} else {
								if (ret == -98) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '优酷',
										message : '登录失败，验证码错误',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								} else {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '优酷',
										message : '登录失败，帐号密码错误',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								}
							}
						}
					});
				});
			};
			textEntry.show();
		},
		login : function(arg) {
			username = atv.localStorage['youkuuser'] || '';
			password = atv.localStorage['youkupass'] || '';
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = "请输入优酷帐号";
			textEntry.label = '帐号';
			if (username != '')
				textEntry.defaultValue = username;
			textEntry.onSubmit = function(u) {
				atv.localStorage['youkuuser'] = u;
				var passEntry;
				passEntry = new atv.TextEntry();
				passEntry.type = 'password';
				passEntry.instructions = '请输入密码';
				passEntry.label = '密码';
				if (password != '')
					passEntry.defaultValue = password;
				passEntry.onSubmit = function(p) {
					atv.localStorage['youkupass'] = p;
					atvu.loadAction('youku.captcha', '', [], 1);
				};
				passEntry.show();
			};
			textEntry.show();
		},
		list : function(args) {
			url = "http://www.youku.com/v/";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null)
									return;
								pos1 = d.indexOf('<ul class="tree">');
								pos2 = d.indexOf('</ul>', pos1);
								cha_data = d.substring(pos1, pos2);
								items = [];
								chas = atvu
										.findall(
												'<li><a href="([^"]+)" charset="[^"]*" title="[^"]*">([^<]+)</a></li>',
												cha_data);
								for ( var i = 0; i < chas.length; i++) {
									name = chas[i][1];
									link = "http://www.youku.com" + chas[i][0];
									items
											.push('<oneLineMenuItem id="menu_'
													+ i
													+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.page\',\'\',[\''
													+ xmlchar(link)
													+ '\',\''
													+ xmlchar(name)
													+ '\',0])"><label>'
													+ xmlchar(name)
													+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.youku.list"><header><simpleHeader><title>优酷</title></simpleHeader></header><menu><sections><menuSection><items>'
										+ items.join("\n")
										+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		select : function(args) {
			url = args[0];
			cname = args[1];
			typen = args[2];
			atvu.ajax(url, "GET", null, null, function(d, c) {
				if (d == null)
					return;
				items = [];
				pos = d.indexOf('<div class="filter" id="filter">');
				pos1 = d.indexOf('<!--filter end-->', pos);
				filter = d.substring(pos, pos1);
				filteritems = filter.split('<div class="item"');
				msg = {
					title : cname,
					message : typen,
					script : [ baseURL + '/main.js' ],
					buttons : []
				};
				for ( var i = 1; i < filteritems.length; i++) {
					rs = (new RegExp('<label>(.*)</label>'))
							.exec(filteritems[i]);
					typename = rs[1];
					if (typename != typen)
						continue;
					lists = atvu.findall('href="([^"]*)"[^>]*>([^<]*)</a>',
							filteritems[i]);
					for ( var j = 0; j < lists.length; j++) {
						link = "http://www.youku.com" + lists[j][0];
						tn = lists[j][1];
						msg.buttons.push({
							label : xmlchar(tn),
							script : "atvu.loadAction('youku.page','',['"
									+ link + "','" + cname + "',1])"
						});
					}
				}
				;
				atv.loadAndSwapXML(atvu.showMessage(msg));
			});
		},
		page : function(args) {
			url = args[0];
			cname = args[1];
			swap = args[2] || 0;
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null)
									return;
								items = [];
								shelfs = [];
								pos = d
										.indexOf('<div class="filter" id="filter">');
								pos1 = d.indexOf('<!--filter end-->', pos);
								filter = d.substring(pos, pos1);
								filteritems = filter.split('<div class="item"');
								for ( var i = 1; i < filteritems.length; i++) {
									rs = (new RegExp('<label>(.*)</label>'))
											.exec(filteritems[i]);
									typename = rs[1];
									rs = (new RegExp(
											'<li class="current"><span>(.*)</span></li>'))
											.exec(filteritems[i]);
									current = rs[1];
									act = "atvu.loadAction('youku.select','',['"
											+ url
											+ "','"
											+ cname
											+ "','"
											+ typename + "'])";
									item = '<actionButton id="youku_select_'
											+ items.length + '" onSelect="'
											+ act + ';" onPlay="' + act
											+ ';"><title>' + typename
											+ '</title><subtitle>' + current
											+ '</subtitle></actionButton>';
									items.push(item);
								}
								;
								shelfs.push('<grid id="shelf_' + shelfs.length
										+ '" columnCount="8"><items>'
										+ items.join("\n") + '</items></grid>');
								pos = d.indexOf('<div class="result">');
								pos1 = d.indexOf('<!--vdata_list end-->', pos);
								if (pos1 == -1)
									pos1 = d.indexOf('<!-- vdata_list end -->',
											pos);
								movies = d.substring(pos, pos1).split(
										'<ul class="');
								items = [];
								for ( var i = 1; i < movies.length; i++) {
									rs = /<li class="._link"><a href="([^"]+)" title="([^"]+)"/
											.exec(movies[i]);
									name = rs[2];
									url = rs[1];
									rs = /<li class="._thumb"><img src="([^"]+)"/
											.exec(movies[i]);
									imgurl = rs[1];
									rs = (new RegExp(
											'<li class="p_status"><span class="status">([^<]*)</span>'))
											.exec(movies[i]);
									if (rs)
										status = rs[1];
									else
										status = '';
									rs = (new RegExp(
											'<li class="p_ishd">\\s*<span class="[^"]*" title="([^"]+)"></span>'))
											.exec(movies[i]);
									if (rs)
										status = status + '(' + rs[1] + ')';
									if (movies[i]
											.indexOf('<li class="p_ischarge">') >= 0) {
										if (status == '')
											status = '付费';
										else
											status = status + ',付费';
									}
									;
									act = "atvu.loadAction('youku.viewurl','"
											+ xmlchar(name) + "',['" + url
											+ "','" + name + "'])";
									acth = "atvu.saveAction('youku.viewurl','"
											+ xmlchar(name) + "',['" + url
											+ "','" + name + "'],'"
											+ xmlchar(imgurl) + "','"
											+ xmlchar(url) + "')";
									item = '<moviePoster id="shelf_item_'
											+ shelfs.length
											+ '_'
											+ items.length
											+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
											+ act
											+ ';" onPlay="'
											+ act
											+ ';" onHoldSelect="'
											+ acth
											+ ';"><title>'
											+ xmlchar(name)
											+ '</title><subtitle>'
											+ xmlchar(status)
											+ '</subtitle><image>'
											+ xmlchar(imgurl)
											+ '</image><defaultImage>resource://Poster.png</defaultImage></moviePoster>';
									items.push(item);
								}
								;
								shelfs
										.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
												+ xmlchar(cname)
												+ '(长按中间键加入收藏)</title></collectionDivider>');
								shelfs.push('<grid id="shelf_' + shelfs.length
										+ '" columnCount="7"><items>'
										+ items.join("\n") + '</items></grid>');
								pos = d.indexOf('<div class="qPager">');
								items = [];
								if (pos >= 0) {
									pos = d.indexOf('<ul class="turn">');
									if (pos >= 0) {
										pos1 = d.indexOf('</ul>', pos);
										pturn = d.substring(pos, pos1);
										turns = atvu
												.findall(
														'<a href="(/[^"]+)"[^>]*>(.*?)</a>',
														pturn);
										for ( var i = 0; i < turns.length; i++) {
											url = 'http://www.youku.com'
													+ turns[i][0];
											act = "atvu.loadAction('youku.page','',['"
													+ url
													+ "','"
													+ cname
													+ "',1])";
											pname = sh(turns[i][1]);
											item = '<actionButton id="youku_page_'
													+ items.length
													+ '" onSelect="'
													+ act
													+ ';" onPlay="'
													+ act
													+ ';"><title>'
													+ pname
													+ '</title></actionButton>';
											items.push(item);
										}
									}
									;
									pos = d.indexOf('<ul class="pages">');
									if (pos >= 0) {
										pos1 = d.indexOf('</ul>', pos);
										ppages = d.substring(pos, pos1).split(
												'<li');
										for ( var i = 1; i < ppages.length; i++) {
											rs = (new RegExp(
													'<span class="current">(\\d+)</span>'))
													.exec(ppages[i]);
											if (rs) {
												item = '<actionButton id="youku_page_'
														+ items.length
														+ '"><title>'
														+ rs[1]
														+ '</title><image>resource://Play.png</image></actionButton>';
												items.push(item);
											}
											;
											rs = (new RegExp(
													'<a href="(/[^"]+)"[^>]*>(\\d+)</a></li>'))
													.exec(ppages[i]);
											if (rs) {
												url = 'http://www.youku.com'
														+ rs[1];
												pname = sh(rs[2]);
												act = "atvu.loadAction('youku.page','',['"
														+ url
														+ "','"
														+ cname
														+ "',1])";
												item = '<actionButton id="youku_page_'
														+ items.length
														+ '" onSelect="'
														+ act
														+ ';" onPlay="'
														+ act
														+ ';"><title>'
														+ pname
														+ '</title></actionButton>';
												items.push(item);
											}
										}
									}
								}
								;
								if (items.length > 0)
									shelfs.push('<grid id="shelf_'
											+ shelfs.length
											+ '" columnCount="7"><items>'
											+ items.join("\n")
											+ '</items></grid>');
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.youku.page"><items>'
										+ shelfs.join("\n")
										+ '</items></scroller></body></atv>';
								if (swap == 1)
									atv.unloadPage();
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		testpage : function(args) {
			pname = args[0];
			url = "http://" + pname + ".youku.com";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null)
									return;
								items = [];
								shelfs = [];
								pos = d.indexOf("new PosterZY(");
								if (pos >= 0) {
									pos1 = d.indexOf("</script>", pos);
									poster = d.substring(pos + 13, pos1);
									pos = poster.lastIndexOf(");");
									poster = poster.substring(0, pos).replace(
											/\n/g, '');
									eval("posterdata=" + poster);
									for ( var i = 0; i < posterdata.data.length; i++) {
										imgurl = posterdata.data[i].img;
										name = posterdata.data[i].title;
										url = posterdata.data[i].url;
										status = posterdata.data[i].update;
										act = "atvu.loadAction('youku.viewurl','"
												+ xmlchar(name)
												+ "',['"
												+ xmlchar(url) + "'])";
										acth = "atvu.saveAction('youku.viewurl','"
												+ xmlchar(name)
												+ "',['"
												+ xmlchar(url)
												+ "'],'"
												+ xmlchar(imgurl)
												+ "','"
												+ xmlchar(url) + "')";
										item = '<sixteenByNinePoster id="shelf_item_'
												+ shelfs.length
												+ '_'
												+ items.length
												+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
												+ act
												+ ';" onPlay="'
												+ act
												+ ';" onHoldSelect="'
												+ acth
												+ ';"><title>'
												+ xmlchar(name)
												+ '</title><subtitle>'
												+ xmlchar(status)
												+ '</subtitle><image>'
												+ xmlchar(imgurl)
												+ '</image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
										items.push(item);
									}
								} else {
									pos = d.indexOf("new PosterOnePic2(");
									pos1 = d.indexOf("</script>", pos);
									poster = d.substring(pos + 18, pos1);
									pos = poster.lastIndexOf(");");
									poster = poster.substring(0, pos).replace(
											/\n/g, '');
									eval("posterdata=" + poster);
									for ( var i = 0; i < posterdata.data.length; i++) {
										imgurl = posterdata.data[i].thumb;
										name = posterdata.data[i].title;
										url = posterdata.data[i].url;
										act = "atvu.loadAction('youku.viewurl','"
												+ xmlchar(name)
												+ "',['"
												+ xmlchar(url) + "'])";
										acth = "atvu.saveAction('youku.viewurl','"
												+ xmlchar(name)
												+ "',['"
												+ xmlchar(url)
												+ "'],'"
												+ xmlchar(imgurl)
												+ "','"
												+ xmlchar(url) + "')";
										item = '<sixteenByNinePoster id="shelf_item_'
												+ shelfs.length
												+ '_'
												+ items.length
												+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
												+ act
												+ ';" onPlay="'
												+ act
												+ ';" onHoldSelect="'
												+ acth
												+ ';"><title>'
												+ xmlchar(name)
												+ '</title><image>'
												+ xmlchar(imgurl)
												+ '</image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
										items.push(item);
									}
								}
								;
								shelfs
										.push('<collectionDivider alignment="left" accessibilityLabel=""><title>热播(长按中间键加入收藏)</title></collectionDivider>');
								shelfs
										.push('<shelf id="shelf_'
												+ shelfs.length
												+ '"><sections><shelfSection><items>'
												+ items.join("\n")
												+ '</items></shelfSection></sections></shelf>');
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.youku.page"><items>'
										+ shelfs.join("\n")
										+ '</items></scroller></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		viewurl : function(args) {
			url = args[0];
			name = args[1];
			if (url.indexOf('http://v.youku.com/v_show/') >= 0) {
				atvu
						.ajax(
								url,
								"GET",
								null,
								null,
								function(d, c) {
									if (d.indexOf("剧集列表") >= 0
											&& d
													.indexOf('<h3 class="title">节目信息</h3></div>') >= 0) {
										rs = (new RegExp(
												'<div class="extend"><a href="([^"]+)" target="_blank">详情</a></div>'))
												.exec(d);
										if (rs)
											atvu.loadAction('youku.viewurl',
													'', [ rs[1], name ], 1);
										else
											atvu.loadAction('youku.playurl',
													'', [ url, name ], 1);
									} else {
										atvu.loadAction('youku.playurl', '', [
												url, name ], 1);
									}
								});
				return;
			}
			;
			if (url.indexOf('cps.youku.com/redirect.html') >= 0) {
				atvu.ajax(url, "GET", null, null, function(d, c) {
					rs = (new RegExp("var videoId2= '(.*?)';")).exec(d);
					newurl = "http://v.youku.com/v_show/id_" + rs[1] + ".html";
					atvu.loadAction('youku.playurl', '', [ newurl, name ], 1);
				});
				return;
			}
			;
			if (url.indexOf('www.youku.com/show_page/') >= 0) {
				id = (new RegExp('www.youku.com/show_page/(.*?).html'))
						.exec(url)[1];
				atvu
						.ajax(
								url,
								"GET",
								null,
								null,
								function(d, c) {
									if (d == null)
										return;
									var rs = atvu
											.findall(
													'<li data="(reload_[^"]*)"([^>]*)>\\s*<a[^>]*>([^<]*)</a>',
													d);
									if (rs.length > 0) {
										items = [];
										pos = d.indexOf('<div id="episode">');
										pos1 = d.indexOf('</div>', pos);
										lists = atvu
												.findall(
														'<li><a href="([^"]*)"[^>]*>([^<]*)</a></li>',
														d.substring(pos, pos1));
										if (lists.length > 0) {
											for ( var i = 0; i < lists.length; i++) {
												link = lists[i][0];
												n = lists[i][1];
												items
														.push('<oneLineMenuItem id="menu_'
																+ items.length
																+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\',1])"><label>'
																+ xmlchar(n)
																+ '</label></oneLineMenuItem>');
											}
										} else {
											posx = d.indexOf(
													'<div name="episode_hide"',
													pos);
											if (posx >= 0)
												pos1 = d
														.indexOf('</div>', posx);
											lists = atvu
													.findall(
															'<li class="ititle[^"]*"><label>(.*?)</label>\\s*<a[^>]+title="([^"]+)" href="([^"]+)"',
															d.substring(pos,
																	pos1));
											if (lists.length > 0) {
												for ( var i = 0; i < lists.length; i++) {
													link = lists[i][2];
													n = lists[i][0] + ' '
															+ lists[i][1];
													items
															.push('<oneLineMenuItem id="menu_'
																	+ items.length
																	+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																	+ xmlchar(link)
																	+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																	+ xmlchar(link)
																	+ '\',1])"><label>'
																	+ xmlchar(n)
																	+ '</label></oneLineMenuItem>');
												}
											}
										}
										;
										for ( var i = 0; i < rs.length; i++) {
											reload = rs[i][0];
											n = rs[i][2];
											c = rs[i][1];
											link = "http://www.youku.com/show_episode/"
													+ id
													+ ".html?dt=json&divid="
													+ reload
													+ "&__rt=1&__ro="
													+ reload;
											if (c.indexOf('current') == -1) {
												items
														.push('<oneLineMenuItem id="menu_'
																+ items.length
																+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.viewlist\',\'\',[\''
																+ xmlchar(link)
																+ '\',\''
																+ xmlchar(name)
																+ '\',\''
																+ xmlchar(n)
																+ '\'])"><label>'
																+ xmlchar(n)
																+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
											}
										}
										;
										xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
												+ baseURL
												+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.youku.viewurl"><header><simpleHeader><title>'
												+ xmlchar(name)
												+ '</title></simpleHeader></header><menu><sections><menuSection><items>'
												+ items.join("\n")
												+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
										atv.loadAndSwapXML(atv.parseXML(xml));
										return;
									}
									;
									pos = d.indexOf('<div id="episode">');
									if (pos >= 0) {
										pos1 = d.indexOf('</div>', pos);
										lists = atvu
												.findall(
														'<li><a href="([^"]*)"[^>]*>([^<]*)</a></li>',
														d.substring(pos, pos1));
										items = [];
										if (lists.length > 0) {
											for ( var i = 0; i < lists.length; i++) {
												link = lists[i][0];
												n = lists[i][1];
												items
														.push('<oneLineMenuItem id="menu_'
																+ items.length
																+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\',1])"><label>'
																+ xmlchar(n)
																+ '</label></oneLineMenuItem>');
											}
											;
											xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
													+ baseURL
													+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.youku.viewurl"><header><simpleHeader><title>'
													+ xmlchar(name)
													+ '</title></simpleHeader></header><menu><sections><menuSection><items>'
													+ items.join("\n")
													+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
											atv.loadAndSwapXML(atv
													.parseXML(xml));
											return;
										}
										;
										posx = d
												.indexOf(
														'<div name="episode_hide"',
														pos);
										if (posx >= 0)
											pos1 = d.indexOf('</div>', posx);
										lists = atvu
												.findall(
														'<li class="ititle[^"]*"><label>(.*?)</label>\\s*<a[^>]+title="([^"]+)" href="([^"]+)"',
														d.substring(pos, pos1));
										if (lists.length > 0) {
											for ( var i = 0; i < lists.length; i++) {
												link = lists[i][2];
												n = lists[i][0] + ' '
														+ lists[i][1];
												items
														.push('<oneLineMenuItem id="menu_'
																+ items.length
																+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
																+ xmlchar(link)
																+ '\',1])"><label>'
																+ xmlchar(n)
																+ '</label></oneLineMenuItem>');
											}
											;
											xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
													+ baseURL
													+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.youku.viewurl"><header><simpleHeader><title>'
													+ xmlchar(name)
													+ '</title></simpleHeader></header><menu><sections><menuSection><items>'
													+ items.join("\n")
													+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
											atv.loadAndSwapXML(atv
													.parseXML(xml));
											return;
										}
									}
									;
									rs = (new RegExp(
											'<a[^>]*href="([^"]*)"[^>]*><em>播放正片</em></a>'))
											.exec(d);
									if (rs) {
										atvu.loadAction('youku.playurl', name,
												[ rs[1] ], 1);
										return;
									}
									;
									rs = (new RegExp(
											'<a[^>]*href="([^"]*)"[^>]*><em>播放</em></a>'))
											.exec(d);
									if (rs) {
										atvu.loadAction('youku.playurl', name,
												[ rs[1] ], 1);
										return;
									}
									;
									rs = (new RegExp(
											'<a[^>]*href="([^"]*)"[^>]*><em>免费试看</em></a>'))
											.exec(d);
									if (rs) {
										atvu.loadAction('youku.playurl', name,
												[ rs[1] ], 1);
										return;
									}
								});
			}
		},
		viewlist : function(args) {
			url = args[0];
			name = args[1];
			n = args[2];
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null)
									return;
								items = [];
								lists = atvu
										.findall(
												'<li><a href="([^"]*)"[^>]*>([^<]*)</a>',
												d);
								if (lists.length > 0) {
									for ( var i = 0; i < lists.length; i++) {
										link = lists[i][0];
										n = lists[i][1];
										items
												.push('<oneLineMenuItem id="menu_'
														+ i
														+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
														+ xmlchar(link)
														+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
														+ xmlchar(link)
														+ '\',1])"><label>'
														+ xmlchar(n)
														+ '</label></oneLineMenuItem>');
									}
								} else {
									lists = atvu
											.findall(
													'<li class="ititle[^"]*"><label>(.*?)</label>\\s*<a[^>]+title="([^"]+)" href="([^"]+)"',
													d);
									if (lists.length > 0) {
										for ( var i = 0; i < lists.length; i++) {
											link = lists[i][2];
											n = lists[i][0] + ' ' + lists[i][1];
											items
													.push('<oneLineMenuItem id="menu_'
															+ items.length
															+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
															+ xmlchar(link)
															+ '\'])" onHoldSelect="atvu.loadAction(\'youku.playurl\',\'\',[\''
															+ xmlchar(link)
															+ '\',1])"><label>'
															+ xmlchar(n)
															+ '</label></oneLineMenuItem>');
										}
									}
								}
								;
								if (items.length == 0)
									items
											.push('<oneLineMenuItem id="menu_0" accessibilityLabel=""><label>暂无内容</label></oneLineMenuItem>');
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.youku.viewlist"><header><simpleHeader><title>'
										+ xmlchar(name)
										+ '</title></simpleHeader></header><menu><sections><menuSection><items>'
										+ items.join("\n")
										+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		playurl : function(args) {
			vurl = args[0];
			listp = args[1];
			asklang = args[2] || 0;
			try {
				encvid = /id_(.*?).html/.exec(vurl)[1];
			} catch (e) {
				encvid = '';
			}
			;
			atvu
					.ajax(
							vurl,
							"GET",
							null,
							null,
							function(data, cookie, error) {
								if (data == null) {
									if (cookie != '') {
										atv.loadAndSwapXML(atvu.showMessage({
											title : '优酷',
											message : '页面解析错误：' + e,
											buttons : [ {
												label : '确定',
												script : 'atv.unloadPage()'
											} ]
										}));
										return;
									}
									;
									if (error == 404) {
										atv.loadAndSwapXML(atvu.showMessage({
											title : '优酷',
											message : '页面没有找到，该视频或许已下架',
											buttons : [ {
												label : '确定',
												script : 'atv.unloadPage()'
											} ]
										}));
										return;
									}
									;
									atv.loadAndSwapXML(atvu.showMessage({
										title : '优酷',
										message : '页面读取错误，网络故障',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
									return;
								}
								;
								vid = /var videoId = '(\d+)';/.exec(data)[1];
								if (encvid == '') {
									rs = /var videoId2= '(.*?)';/.exec(data);
									if (rs)
										encvid = rs[1];
								}
								;
								url = "http://v.youku.com/player/getPlaylist/VideoIDS/"
										+ vid + "?__rt=1&__ro=";
								var d = new Date();
								var ts = parseInt(d.getTime() / 1000);
								atvu
										.ajax(
												url,
												"GET",
												null,
												null,
												function(v, c) {
													res = JSON.parse(v);
													try {
														youkulang = atv.localStorage['youkulang'] || 0;
														as = res['data'][0]['dvd']['audiolang'];
														if (as.length > 1
																&& asklang == 0) {
															var langask = 0;
															for ( var i = 0; i < as.length; i++) {
																if (as[i]['lang'] != '国语'
																		&& as[i]['lang'] != '粤语')
																	langask = 1;
															}
															;
															if (youkulang == 2
																	|| langask == 1) {
																msg = {
																	title : '优酷',
																	script : [ baseURL
																			+ '/main.js' ],
																	message : '请选择语言',
																	buttons : []
																};
																for ( var i = 0; i < as.length; i++) {
																	act = "atvu.loadAction('youku.playurl','',['http://v.youku.com/v_show/id_"
																			+ as[i]['vid']
																			+ ".html"
																			+ "',"
																			+ listp
																			+ ",1],1)";
																	msg.buttons
																			.push({
																				label : xmlchar(as[i]['lang']),
																				script : act
																			});
																}
																;
																atv
																		.loadAndSwapXML(atvu
																				.showMessage(msg));
																return;
															}
															;
															if (youkulang == 1)
																lang = '粤语';
															else
																lang = '国语';
															for ( var i = 0; i < as.length; i++) {
																if (as[i]['lang'] == lang
																		&& as[i]['vid'] != encvid) {
																	nvid = as[i]['vid'];
																	nvurl = "http://v.youku.com/v_show/id_"
																			+ nvid
																			+ ".html";
																	atvu
																			.loadAction(
																					'youku.playurl',
																					'',
																					[
																							nvurl,
																							listp ],
																					1);
																	return;
																}
															}
														}
													} catch (e) {
													}
													;
													ty = "mp4";
													hd = atv.localStorage['hd'] || 0;
													if (hd == 0) {
														try {
															st = res['data'][0]['streamtypes'];
															for ( var i = 0; i < st.length; i++) {
																if (st[i] == 'hd2')
																	ty = 'hd2';
															}
														} catch (e) {
														}
													}
													;
													nvurl = "http://v.youku.com/player/getM3U8/vid/"
															+ vid
															+ "/type/"
															+ ty
															+ "/ts/"
															+ ts
															+ "/v.m3u8";
													playlist = [];
													if (listp == 1) {
														list = res['data'][0]['list'];
														if (list) {
															st = 0;
															for ( var i = 0; i < list.length; i++) {
																if (st == 1) {
																	playlist
																			.push([
																					list[i]["title"],
																					"http://v.youku.com/player/getM3U8/vid/"
																							+ list[i]["vid"]
																							+ "/type/"
																							+ ty
																							+ "/ts/"
																							+ ts
																							+ "/v.m3u8",
																					list[i]["title"] ]);
																}
																;
																if (list[i]["vid"] == vid) {
																	st = 1;
																}
															}
														}
													}
													;
													length = parseInt(res['data'][0]['seconds']);
													if (isNaN(length))
														length = 0;
													atvu
															.realplay(
																	nvurl,
																	res['data'][0]['title'],
																	res['data'][0]['title'],
																	length,
																	playlist,
																	null,
																	0,
																	'youku.'
																			+ encvid);
												});
							});
		},
	}
};