function __init() {
	return {
		"version" : 17,
		main : function(args) {
			atvu
					.ajax(
							"http://list.iqiyi.com/",
							"GET",
							null,
							null,
							function(v, c) {
								pos1 = v.indexOf('<ul class="submenulist">');
								pos2 = v.indexOf('</ul>', pos1);
								cha_data = v.substring(pos1, pos2);
								items = new Array();
								var re = new RegExp(
										"<li.*?<a\\s+href=\"(.*?)\".*?>(.*?)</a>",
										'g');
								var i = 0;
								while (1) {
									i++;
									rs = re.exec(cha_data);
									if (rs == null)
										break;
									if (rs[1] == '#')
										link = '1/------------2-1-1-1---';
									else
										link = rs[1].substring(26,
												rs[1].length - 5);
									items
											.push('<oneLineMenuItem id="menu_'
													+ i
													+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'qiyi.page\',\'\',[\''
													+ link
													+ '\',0])"><label>'
													+ xmlchar(rs[2])
													+ '</label><accessories><arrow /></accessories></oneLineMenuItem>');
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.qiyi.main"><header><simpleHeader><title>奇艺</title></simpleHeader></header><menu><sections><menuSection><items>'
										+ items.join("\n")
										+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		filter : function(args) {
			page = args[0];
			swap = args[1];
			url = "http://list.iqiyi.com/www/" + page + ".html";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(v, c) {
								var x = v.split('<label>按');
								var shelfs = new Array();
								ii = 0;
								for ( var i = 1; i < x.length; i++) {
									tlist = x[i];
									if (tlist.substring(0, 2) == '资费')
										continue;
									typelists = atvu
											.findall(
													'<li data-value=\".*?\"(.*?)><a\\s+href="http://list.iqiyi.com/www/(.*?).html" >(.*?)</a>',
													tlist);
									if (typelists.length == 0)
										typelists = atvu
												.findall(
														'<li(.*?)><a\\s+href="http://list.iqiyi.com/www/(.*?).html" >(.*?)</a>',
														tlist);
									items = new Array();
									for ( var j = 0; j < typelists.length; j++) {
										rs = typelists[j];
										if (rs[0].indexOf('current') >= 0)
											sel = '选择'
										else
											sel = '';
										item = '<actionButton id="shelf_item_'
												+ (ii++)
												+ '" onSelect="atvu.loadAction(\'qiyi.filter\',\'\',[\''
												+ rs[1]
												+ '\',1]);" onPlay="atvu.loadAction(\'qiyi.filter\',\'\',[\''
												+ rs[1] + '\',1]);"><title>'
												+ xmlchar(rs[2])
												+ '</title><subtitle>' + sel
												+ '</subtitle></actionButton>';
										items.push(item);
									}
									;
									if (items.length > 0) {
										shelfs
												.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
														+ tlist.substring(0, 2)
														+ '</title></collectionDivider>');
										if (items.length < 15)
											sstr = '<grid id="shelf_'
													+ shelfs.length
													+ '" columnCount="'
													+ items.length
													+ '"><items>'
													+ items.join('\n')
													+ '</items></grid>';
										else
											sstr = '<grid id="shelf_'
													+ shelfs.length
													+ '"><items>'
													+ items.join('\n')
													+ '</items></grid>';
										shelfs.push(sstr);
									}
								}
								;
								sstr = '<grid id="shelf_'
										+ shelfs.length
										+ '" columnCount="1"><items><actionButton id="shelf_item_'
										+ (ii++)
										+ '" onSelect="atvu.loadAction(\'qiyi.page\',\'\',[\''
										+ page
										+ '\',1]);" onPlay="atvu.loadAction(\'qiyi.page\',\'\',[\''
										+ page
										+ '\',1]);"><title>确定</title></actionButton></items></grid>';
								shelfs.push(sstr);
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.qiyi.page"><items>'
										+ shelfs.join('\n')
										+ '</items></scroller></body></atv>';
								if (swap == 1)
									atv.unloadPage();
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		page : function(args) {
			page = args[0];
			vv = page.split("-");
			vv[10] = '0';
			page = vv.join("-");
			swap = args[1];
			url = "http://list.iqiyi.com/www/" + page + ".html";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(v, c) {
								var re = new RegExp(
										"<!--列表部分_START-->([\\s\\S]+?)<!--列表部分_END-->");
								var match = re.exec(v);
								var i = 0;
								var shelfs = new Array();
								sstr = '<shelf id="shelf_0"><sections><shelfSection><items><actionButton id="shelf_item_0" onSelect="atvu.loadAction(\'qiyi.filter\',\'\',[\''
										+ page
										+ '\',0]);" onPlay="atvu.loadAction(\'qiyi.filter\',\'\',[\''
										+ page
										+ '\',0]);"><title>筛选</title></actionButton></items></shelfSection></sections></shelf>';
								shelfs.push(sstr);
								shelfs
										.push('<collectionDivider alignment="left" accessibilityLabel=""><title>(长按中间键加入收藏)</title></collectionDivider>');
								if (match != null) {
									re = new RegExp(
											"<li[^>]+>([\\s\\S]+?)</li>", "g");
									items = new Array();
									while (1) {
										i++;
										rs = re.exec(match[1]);
										if (rs == null)
											break;
										p_url = /class="imgBg1" href="(.+?)"/
												.exec(rs)[1];
										p_name = /alt="(.+?)"/.exec(rs)[1];
										p_thumb = /src="(.+?)"/.exec(rs)[1];
										link = p_url.substring(p_url
												.indexOf('.com/') + 5,
												p_url.length - 5);
										sit = p_url.substring(7, p_url
												.indexOf('.'));
										if (page.substring(0, 2) == '5/')
											act = "atvu.loadAction('qiyi.item','"
													+ xmlchar(p_name)
													+ "',['"
													+ link
													+ "','"
													+ sit
													+ "',1])";
										else
											act = "atvu.loadAction('qiyi.item','"
													+ xmlchar(p_name)
													+ "',['"
													+ link
													+ "','"
													+ sit
													+ "',0])";
										acth = "atvu.saveAction('qiyi.item','"
												+ xmlchar(p_name)
												+ "',['"
												+ link
												+ "','"
												+ sit
												+ "',0],'"
												+ xmlchar(p_thumb)
												+ "','"
												+ xmlchar("http://" + sit
														+ ".iqiyi.com/" + link
														+ ".html") + "')";
										item = '<sixteenByNinePoster id="shelf_item_'
												+ i
												+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
												+ act
												+ ';" onPlay="'
												+ act
												+ ';" onHoldSelect="'
												+ acth
												+ ';"><title>'
												+ xmlchar(p_name)
												+ '</title><image>'
												+ xmlchar(p_thumb)
												+ '</image><defaultImage>resource://16x9.png</defaultImage></sixteenByNinePoster>';
										items.push(item);
									}
									;
									sstr = '<grid id="grid_' + shelfs.length
											+ '"><items>' + items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
								}
								;
								re = new RegExp(
										"<a data-key=\".+?\" title=\".*?\" href=\"(.*?)\">(.+?)</a>",
										"g");
								items = new Array();
								i = 0;
								while (1) {
									i++;
									rs = re.exec(v);
									if (rs == null)
										break;
									link = rs[1]
											.substring(26, rs[1].length - 5);
									item = '<actionButton id="shelf_page_'
											+ i
											+ '" onSelect="atvu.loadAction(\'qiyi.page\',\'\',[\''
											+ link
											+ '\',1]);" onPlay="atvu.loadAction(\'qiyi.page\',\'\',[\''
											+ link + '\',1]);"><title>' + rs[2]
											+ '</title></actionButton>';
									if (rs[2] == '下一页')
										items.unshift(item);
									else
										items.push(item);
								}
								;
								if (items.length > 0) {
									sstr = '<grid id="grid_' + shelfs.length
											+ '" columnCount="10"><items>'
											+ items.join('\n')
											+ '</items></grid>';
									shelfs.push(sstr);
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><scroller id="com.atvttvv.qiyi.page"><items>'
										+ shelfs.join('\n')
										+ '</items></scroller></body></atv>';
								if (swap == 1)
									atv.unloadPage();
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		genp : function(args) {
			link = args[0];
			callback = args[1];
			url = "http://www.iqiyi.com/" + link + ".html";
			atvu.ajax(url, "GET", null, null, function(v, c) {
				if (v == null) {
					callback(null);
					return;
				}
				;
				var info = null;
				var re = new RegExp("var (info\\s*=[\\s\\S]*?)</script>", 'm');
				rs = re.exec(v);
				if (rs != null) {
					try {
						eval(rs[1])
					} catch (e) {
					}
					;
				}
				;
				if (info && info.pid && info.videoId) {
					if (info.tvId)
						url = 'http://cache.video.qiyi.com/m/' + info.tvId
								+ '/' + info.videoId + '/';
					else
						url = 'http://cache.video.qiyi.com/m/' + info.videoId
								+ '/';
					atvu.ajax(url, "GET", null, null, function(v, c) {
						if (v == null) {
							callback(null);
							return;
						}
						;
						eval(v);
						length = info["data"]["playLength"];
						try {
							la = length.split(':');
							if (la.length == 3)
								mlength = (parseInt(la[0], 10) * 60 + parseInt(
										la[1], 10))
										* 60 + parseInt(la[2], 10);
							else
								mlength = 0;
						} catch (e) {
							mlength = 0;
						}
						;
						vurl = ipadUrl['data']['url'];
						hd = atv.localStorage['hd'] || 0;
						vname = info['title'];
						vdesc = info["data"]["videoDesc"];
						if (hd == 0) {
							try {
								var urlarray = ipadUrl.data.mtl;
								for (i = 0; i < urlarray.length; i++) {
									if (urlarray[i].vd == 3) {
										vurl = urlarray[i].m3u;
										vname = vname + '(高清)';
									}
								}
							} catch (e) {
							}
						}
						;
						nexturl = ipadUrl['data']['nextVideoUrl'] || '';
						if (nexturl != '') {
							nextlink = nexturl.substring(nexturl
									.indexOf('.com/') + 5, nexturl.length - 5);
							pl = [ [ 'atvu', 'qiyi.genp', nextlink ] ];
							callback([ vurl, vname, vdesc, mlength, pl ]);
						} else
							callback([ vurl, vname, vdesc, mlength, null ]);
					});
				} else {
					callback(null);
				}
			});
		},
		checkfav : function(fname, favi, args, callback) {
			if (fname != 'item') {
				callback(favi, -1);
				return;
			}
			;
			link = args[0];
			sit = args[1];
			url = "http://" + sit + ".iqiyi.com/" + link + ".html";
			atvu.ajax(url, "GET", null, null, function(v, c) {
				rs = (new RegExp('"currentMaxEpisode":(\\d+)\\s*,')).exec(v);
				if (!rs) {
					callback(favi, 0);
					return;
				}
				;
				callback(favi, parseInt(rs[1]));
			});
		},
		item : function(args) {
			me = this;
			link = args[0];
			sit = args[1];
			p = args[2];
			url = "http://" + sit + ".iqiyi.com/" + link + ".html";
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(v, c) {
								rs1 = (new RegExp('data-player-tvid="(\\d+)"'))
										.exec(v);
								rs2 = (new RegExp(
										'data-player-videoid="([^"]*)"'))
										.exec(v);
								if (rs1 && rs2) {
									tvid = rs1[1];
									videoid = rs2[1];
									infourl = "http://cache.m.iqiyi.com/vj/"
											+ tvid + "/" + videoid + "/";
									atvu
											.ajax(
													infourl,
													"GET",
													null,
													null,
													function(infov, c) {
														eval(infov);
														vname = tvInfoJs.data.vn;
														vdesc = tvInfoJs.data.info;
														url = "http://cache.m.iqiyi.com/mt/"
																+ tvid + "/";
														atvu
																.ajax(
																		url,
																		"GET",
																		null,
																		null,
																		function(
																				v,
																				c) {
																			res = JSON
																					.parse(v);
																			code = res.code;
																			if (code == 'Q00202') {
																				atv
																						.loadAndSwapXML(atvu
																								.showMessage({
																									title : '爱奇艺',
																									message : '很抱歉，由于版权到期您所观看的视频已经下线',
																									buttons : [ {
																										label : '确定',
																										script : 'atv.unloadPage()'
																									} ]
																								}));
																				return;
																			}
																			;
																			if (code == 'Q00201') {
																				atv
																						.loadAndSwapXML(atvu
																								.showMessage({
																									title : '爱奇艺',
																									message : '对不起，此视频因版权方的要求，暂时只支持在PC上播放',
																									buttons : [ {
																										label : '确定',
																										script : 'atv.unloadPage()'
																									} ]
																								}));
																				return;
																			}
																			;
																			vurls = res.data.mtl;
																			hd = atv.localStorage['hd'] || 0;
																			vurl = '';
																			mlength = 0;
																			vd = '';
																			for ( var i = 0; i < vurls.length; i++) {
																				if (vurls[i].vd == 3
																						&& hd == 0) {
																					vurl = vurls[i].m3u;
																					break;
																					mlength = vurls[i].plt;
																					vd = vurls[i].vd;
																				}
																				;
																				if (vurl == '') {
																					vurl = vurls[i].m3u;
																					mlength = vurls[i].plt;
																					vd = vurls[i].vd;
																				}
																			}
																			;
																			if (vd == 1)
																				vname = vname
																						+ '(普通)';
																			if (vd == 2)
																				vname = vname
																						+ '(清晰)';
																			if (vd == 3)
																				vname = vname
																						+ '(高清)';
																			if (vd == 96)
																				vname = vname
																						+ '(流畅)';
																			atvu
																					.realplay(
																							vurl,
																							vname,
																							vdesc,
																							mlength);
																		});
													});
									return;
								}
								;
								var info = null;
								var re = new RegExp(
										"var (info\\s*=[\\s\\S]*?)</script>",
										'm');
								rs = re.exec(v);
								if (rs != null) {
									try {
										eval(rs[1])
									} catch (e) {
									}
									;
								}
								;
								if (info && info.pid && info.videoId) {
									if (info.tvId)
										url = 'http://cache.video.qiyi.com/m/'
												+ info.tvId + '/'
												+ info.videoId + '/';
									else
										url = 'http://cache.video.qiyi.com/m/'
												+ info.videoId + '/';
									atvu
											.ajax(
													url,
													"GET",
													null,
													null,
													function(v, c) {
														eval(v);
														length = info["data"]["playLength"];
														try {
															la = length
																	.split(':');
															if (la.length == 3)
																mlength = (parseInt(
																		la[0],
																		10) * 60 + parseInt(
																		la[1],
																		10))
																		* 60
																		+ parseInt(
																				la[2],
																				10);
															else
																mlength = 0;
														} catch (e) {
															mlength = 0;
														}
														;
														code = ipadUrl['code'];
														if (code == 'Q00202') {
															atv
																	.loadAndSwapXML(atvu
																			.showMessage({
																				title : '爱奇艺',
																				message : '很抱歉，由于版权到期您所观看>的视频已经下线',
																				buttons : [ {
																					label : '确定',
																					script : 'atv.unloadPage()'
																				} ]
																			}));
															return;
														}
														;
														if (code == 'Q00201') {
															atv
																	.loadAndSwapXML(atvu
																			.showMessage({
																				title : '爱奇艺',
																				message : '对不起，此视频因版权方的要求>，暂时只支持在PC上播放',
																				buttons : [ {
																					label : '确定',
																					script : 'atv.unloadPage()'
																				} ]
																			}));
															return;
														}
														;
														vurl = ipadUrl['data']['url'];
														hd = atv.localStorage['hd'] || 0;
														vname = info['title'];
														vdesc = info["data"]["videoDesc"]
																|| vname;
														if (hd == 0) {
															try {
																var urlarray = ipadUrl.data.mtl;
																for (i = 0; i < urlarray.length; i++) {
																	if (urlarray[i].vd == 3) {
																		vurl = urlarray[i].m3u;
																		vname = vname
																				+ '(高清)';
																	}
																}
															} catch (e) {
															}
														}
														;
														nexturl = ipadUrl['data']['nextVideoUrl']
																|| '';
														if (nexturl != '') {
															nextlink = nexturl
																	.substring(
																			nexturl
																					.indexOf('.com/') + 5,
																			nexturl.length - 5);
															pl = [ [
																	'atvu',
																	'qiyi.genp',
																	nextlink ] ];
															if (p == 1)
																atvu
																		.realplay(
																				vurl,
																				vname,
																				vdesc,
																				mlength,
																				pl);
															else
																atvu
																		.realplay(
																				vurl,
																				vname,
																				vdesc,
																				mlength);
														} else
															atvu.realplay(vurl,
																	vname,
																	vdesc,
																	mlength);
													});
								} else {
									pagelists = atvu
											.findall(
													'<li><a\\s+j-tab="j-album-(\\d+)" href="#"><span>(.*?)</span></a></li>',
													v);
									pos = v.indexOf('关联剧推荐');
									rellists = new Array();
									if (pos >= 0) {
										pos1 = v.indexOf("</ul>", pos);
										rellists = atvu
												.findall(
														'<a alt="([^"]*)" title="[^"]*" href="(http://www[^"]*)">',
														v.substring(pos, pos1));
									}
									;
									if (p == 0) {
										lists = atvu
												.findall(
														'<li><a href="([^"]*)" class="a_bar">\\s*<img[^>]*data-lazy="([\\s\\S]*?)" title="([\\s\\S]*?)" alt="[\\s\\S]*?" class=" ">',
														v);
										if (lists.length == 0)
											lists = atvu
													.findall(
															'<li><a href="([^"]*)" class="a_bar">[\\s\\S]*?<img[\\s\\S]*?src="([\\s\\S]*?)" title="([\\s\\S]*?)" alt="[\\s\\S]*?" class=" ">',
															v);
										if (lists.length == 0) {
											npos = v.indexOf("<!--listBox-->");
											if (npos >= 0) {
												npos1 = v.indexOf(
														'<!--listBox end-->',
														npos);
												lists = atvu
														.findall(
																'<li>\\s*<a href="(http://www[^"]*)" title="[^"]*">\\s*<img src="([\\s\\S]*?)" alt="[^"]*" />\\s*([\\s\\S]*?)</a>',
																v.substring(
																		npos,
																		npos1));
											} else
												lists = atvu
														.findall(
																'<li>\\s*<a href="(http://www[^"]*)" title="[^"]*">\\s*<img src="([\\s\\S]*?)" alt="[^"]*" />\\s*([\\s\\S]*?)</a>',
																v);
										}
									} else {
										rs = new RegExp(
												'<div id="j-album-'
														+ p
														+ '" style="display: none;">(.*?)</div>')
												.exec(v);
										if (rs == null) {
											lists = new Array();
										} else {
											nurl = "http://www.iqiyi.com"
													+ rs[1];
											atvu
													.ajax(
															nurl,
															"GET",
															null,
															null,
															function(v, c) {
																lists = atvu
																		.findall(
																				'<li><a href="([^"]*)" class="a_bar">\\s*<img[^>]*data-lazy="([\\s\\S]*?)" title="([\\s\\S]*?)" alt="[\\s\\S]*?" class=" ">',
																				v);
																if (lists.length == 0)
																	lists = atvu
																			.findall(
																					'<li><a href="([^"]*)" class="a_bar">[\\s\\S]*?<img[\\s\\S]*?src="([\\s\\S]*?)" title="([\\s\\S]*?)" alt="[\\s\\S]*?" class=" ">',
																					v);
																if (lists.length == 0) {
																	lists = atvu
																			.findall(
																					'<li>\\s*<a href="(http://www[^"]*)" title="[^"]*">\\s*<img src="([\\s\\S]*?)" alt="[^"]*" />\\s*([\\s\\S]*?)</a>',
																					v);
																}
																;
																if (lists.length > 0) {
																	var shelfs = new Array();
																	var items = new Array();
																	var i = 0;
																	allnames = new Array();
																	for ( var j = 0; j < lists.length; j++) {
																		allnames
																				.push(sh(lists[j][2]));
																	}
																	;
																	namebase = atvu
																			.reducename(
																					allnames,
																					3);
																	shelfs
																			.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
																					+ xmlchar(namebase)
																					+ '(长按中间键连续播放)</title></collectionDivider>');
																	for ( var j = 0; j < lists.length; j++) {
																		rs = lists[j];
																		nlink = rs[0]
																				.substring(
																						rs[0]
																								.indexOf('.com/') + 5,
																						rs[0].length - 5);
																		i++;
																		nname = sh(rs[2]);
																		nnname = nname;
																		if (namebase.length > 0) {
																			if (nname
																					.substring(
																							0,
																							namebase.length) == namebase) {
																				nname = nname
																						.substring(namebase.length);
																			}
																		}
																		;
																		item = '<sixteenByNinePoster id="shelf_item_'
																				+ i
																				+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.loadAction(\'qiyi.item\',\''
																				+ xmlchar(nnname)
																				+ '\',[\''
																				+ nlink
																				+ '\',\''
																				+ sit
																				+ '\',0]);" onPlay="atvu.loadAction(\'qiyi.item\',\''
																				+ xmlchar(nnname)
																				+ '\',[\''
																				+ nlink
																				+ '\',\''
																				+ sit
																				+ '\',0]);" onHoldSelect="atvu.loadAction(\'qiyi.item\',\''
																				+ xmlchar(nnname)
																				+ '\',[\''
																				+ nlink
																				+ '\',\''
																				+ sit
																				+ '\',1]);"><title>'
																				+ xmlchar(nname)
																				+ '</title><image>'
																				+ xmlchar(rs[1])
																				+ '</image><defaultImage>resource://16x9.png</defaultImage></sixteenByNinePoster>';
																		items
																				.push(item);
																	}
																	;
																	sstr = '<grid id="grid_'
																			+ shelfs.length
																			+ '"><items>'
																			+ items
																					.join('\n')
																			+ '</items></grid>';
																	shelfs
																			.push(sstr);
																	if (pagelists.length > 0) {
																		items = new Array();
																		for ( var j = 0; j < pagelists.length; j++) {
																			n = pagelists[j][1];
																			id = pagelists[j][0];
																			i++;
																			item = '<actionButton id="shelf_item_'
																					+ i
																					+ '" onSelect="atvu.loadAction(\'qiyi.item\',\'\',[\''
																					+ link
																					+ '\',\''
																					+ sit
																					+ '\','
																					+ id
																					+ ']);" onPlay="atvu.loadAction(\'qiyi.item\',\'\',[\''
																					+ link
																					+ '\',\''
																					+ sit
																					+ '\','
																					+ id
																					+ ']);"><title>'
																					+ n
																					+ '</title></actionButton>';
																			items
																					.push(item);
																		}
																		;
																		sstr = '<grid id="grid_'
																				+ shelfs.length
																				+ '"><items>'
																				+ items
																						.join('\n')
																				+ '</items></grid>';
																		shelfs
																				.push(sstr);
																	}
																	;
																	if (rellists.length > 0) {
																		items = new Array();
																		for ( var j = 0; j < rellists.length; j++) {
																			n = rellists[j][0];
																			p_url = rellists[j][1];
																			nlink = p_url
																					.substring(
																							p_url
																									.indexOf('.com/') + 5,
																							p_url.length - 5);
																			i++;
																			item = '<actionButton id="shelf_item_'
																					+ i
																					+ '" onSelect="atvu.loadAction(\'qiyi.item\',\''
																					+ xmlchar(n)
																					+ '\',[\''
																					+ nlink
																					+ '\',\''
																					+ sit
																					+ '\',0]);" onPlay="atvu.loadAction(\'qiyi.item\',\''
																					+ xmlchar(n)
																					+ '\',[\''
																					+ nlink
																					+ '\',\''
																					+ sit
																					+ '\',0]);"><title>'
																					+ xmlchar(n)
																					+ '</title></actionButton>';
																			items
																					.push(item);
																		}
																		;
																		sstr = '<grid id="grid_'
																				+ shelfs.length
																				+ '"><items>'
																				+ items
																						.join('\n')
																				+ '</items></grid>';
																		shelfs
																				.push(sstr);
																	}
																	;
																	xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
																			+ baseURL
																			+ '/main.js" /></head><body><scroller id="com.atvttvv.qiyi.item"><items>'
																			+ shelfs
																					.join('\n')
																			+ '</items></scroller></body></atv>';
																	atv
																			.unloadPage();
																	atv
																			.loadAndSwapXML(atv
																					.parseXML(xml));
																}
															});
											return;
										}
									}
									;
									if (lists.length > 0) {
										var shelfs = new Array();
										var items = new Array();
										var i = 0;
										allnames = new Array();
										for ( var j = 0; j < lists.length; j++) {
											allnames.push(sh(lists[j][2]));
										}
										;
										namebase = atvu.reducename(allnames, 3);
										shelfs
												.push('<collectionDivider alignment="left" accessibilityLabel=""><title>'
														+ xmlchar(namebase)
														+ '(长按中间键连续播放)</title></collectionDivider>');
										for ( var j = 0; j < lists.length; j++) {
											rs = lists[j];
											nlink = rs[0].substring(rs[0]
													.indexOf('.com/') + 5,
													rs[0].length - 5);
											i++;
											nname = sh(rs[2]);
											nnname = nname;
											if (namebase.length > 0) {
												if (nname.substring(0,
														namebase.length) == namebase) {
													nname = nname
															.substring(namebase.length);
												}
											}
											;
											item = '<sixteenByNinePoster id="shelf_item_'
													+ i
													+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="atvu.loadAction(\'qiyi.item\',\''
													+ xmlchar(nnname)
													+ '\',[\''
													+ nlink
													+ '\',\''
													+ sit
													+ '\',0]);" onPlay="atvu.loadAction(\'qiyi.item\',\''
													+ xmlchar(nnname)
													+ '\',[\''
													+ nlink
													+ '\',\''
													+ sit
													+ '\',0]);" onHoldSelect="atvu.loadAction(\'qiyi.item\',\''
													+ xmlchar(nnname)
													+ '\',[\''
													+ nlink
													+ '\',\''
													+ sit
													+ '\',1]);"><title>'
													+ xmlchar(nname)
													+ '</title><image>'
													+ xmlchar(rs[1])
													+ '</image><defaultImage>resource://16x9.png</defaultImage></sixteenByNinePoster>';
											items.push(item);
										}
										;
										sstr = '<grid id="grid_'
												+ shelfs.length + '"><items>'
												+ items.join('\n')
												+ '</items></grid>';
										shelfs.push(sstr);
										if (pagelists.length > 0) {
											items = new Array();
											for ( var j = 0; j < pagelists.length; j++) {
												n = pagelists[j][1];
												id = pagelists[j][0];
												i++;
												item = '<actionButton id="shelf_item_'
														+ i
														+ '" onSelect="atvu.loadAction(\'qiyi.item\',\'\',[\''
														+ link
														+ '\',\''
														+ sit
														+ '\','
														+ id
														+ ']);" onPlay="atvu.loadAction(\'qiyi.item\',\'\',[\''
														+ link
														+ '\',\''
														+ sit
														+ '\','
														+ id
														+ ']);"><title>'
														+ n
														+ '</title></actionButton>';
												items.push(item);
											}
											;
											sstr = '<grid id="grid_'
													+ shelfs.length
													+ '"><items>'
													+ items.join('\n')
													+ '</items></grid>';
											shelfs.push(sstr);
										}
										;
										if (rellists.length > 0) {
											items = new Array();
											for ( var j = 0; j < rellists.length; j++) {
												n = rellists[j][0];
												p_url = rellists[j][1];
												nlink = p_url.substring(p_url
														.indexOf('.com/') + 5,
														p_url.length - 5);
												i++;
												item = '<actionButton id="shelf_item_'
														+ i
														+ '" onSelect="atvu.loadAction(\'qiyi.item\',\''
														+ xmlchar(n)
														+ '\',[\''
														+ nlink
														+ '\',\''
														+ sit
														+ '\',0]);" onPlay="atvu.loadAction(\'qiyi.item\',\''
														+ xmlchar(n)
														+ '\',[\''
														+ nlink
														+ '\',\''
														+ sit
														+ '\',0]);"><title>'
														+ xmlchar(n)
														+ '</title></actionButton>';
												items.push(item);
											}
											;
											sstr = '<grid id="grid_'
													+ shelfs.length
													+ '"><items>'
													+ items.join('\n')
													+ '</items></grid>';
											shelfs.push(sstr);
										}
										;
										xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
												+ baseURL
												+ '/main.js" /></head><body><scroller id="com.atvttvv.qiyi.item"><items>'
												+ shelfs.join('\n')
												+ '</items></scroller></body></atv>';
										atv.loadAndSwapXML(atv.parseXML(xml));
									}
								}
							});
		},
		playurl : function(args) {
			vurl = args[0];
			listp = args[1];
			atvu.ajax(vurl, "GET", null, null, function(data, cookie) {
				re = /"navigation":"([^"]*)"/;
				rs = re.exec(data);
				listurl = '';/*
								 * if (rs){n=rs[1];re=new
								 * RegExp(/www.iqiyi.com\/(.*?).html/g);rs=n.match(re);for
								 * (var i=0;i<rs.length;i++)if
								 * (rs[i].indexOf("index")==-1)listurl=rs[i]; }
								 */
				if (listurl == '') {
					link = vurl.substring(vurl.indexOf('.com/') + 5,
							vurl.length - 5);
					sit = vurl.substring(7, vurl.indexOf('.'));
					atvu.loadAction('qiyi.item', '', [ link, sit, listp ], 0);
					return;
				}
				;
				vurl = listurl;
				link = vurl.substring(vurl.indexOf('.com/') + 5,
						vurl.length - 5);
				sit = vurl.substring(0, vurl.indexOf('.'));
				atvu.loadAction('qiyi.item', '', [ link, sit, 0 ], 0);
			});
		},
	}
};
