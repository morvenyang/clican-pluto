function __init() {
	var username, password, sessionid, hd, isvip, userid;
	var domain = "http://vod.xunlei.com/";
	function playlink(url, name) {
		if (!name)
			name = '';
		xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
				+ baseURL
				+ '/main.js" /></head><body><videoPlayer id="com.sample.video-player"><httpFileVideoAsset id="teaser6453"><mediaURL><![CDATA['
				+ url
				+ ']]></mediaURL><title>'
				+ xmlchar(name)
				+ '</title><description>'
				+ xmlchar(name)
				+ '</description></httpFileVideoAsset></videoPlayer></body></atv>'
		atv.loadAndSwapXML(atv.parseXML(xml));
	}
	;
	function loadxl() {
		if (atv.localStorage['xunleiu']) {
			xunleiu = atv.localStorage['xunleiu'];
		} else {
			xunleiu = {};
			xunleiu.hd = 0
			xunleiu.password = "";
			xunleiu.username = "";
			xunleiu.sessionid = "";
			atv.localStorage['xunleiu'] = xunleiu;
		}
		;
		username = xunleiu.username;
		password = xunleiu.password;
		sessionid = xunleiu.sessionid;
		hd = xunleiu.hd;
		isvip = xunleiu.isvip;
		userid = xunleiu.userid;
	}
	;
	function XL_CLOUD_FX_INSTANCEqueryBack(res) {
		loadxl();
		d = new Date();
		logger.debug(JSON.stringify(res));
		try {
			msg = res['resp']['vod_permit']['msg'];
			if (msg == 'overdue session') {
				atv.loadAndSwapXML(atvu.showMessage({
					title : '登录',
					script : [ baseURL + '/main.js' ],
					message : '登录已过期，请返回重新登录',
					buttons : [ {
						label : '确定',
						script : 'atvu.loadAction(\'xunlei.login\',\'\',[],1)'
					} ]
				}));
				return;
			}
			;
			if (msg == 'too much share userid') {
				atv.loadAndSwapXML(atvu.showMessage({
					title : '登录',
					script : [ baseURL + '/main.js' ],
					message : '您的帐号因登录IP过多已被限制为不能播放，请于1天后重试，建议您尽快修改密码',
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					}, {
						label : '重新登录',
						script : 'atvu.loadAction(\'xunlei.login\',\'\',[],1)'
					} ]
				}));
				return;
			}
		} catch (e) {
		}
		;
		try {
			if (res['resp']["trans_wait"] != 0
					&& res['resp']["vodinfo_list"].length == 0) {
				atv.loadAndSwapXML(atvu.showMessage({
					title : '迅雷',
					message : '正在转码中，请等待',
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
				return;
			}
		} catch (e) {
		}
		;
		if (!res['resp']["vodinfo_list"]) {
			atv.loadAndSwapXML(atvu.showMessage({
				title : '迅雷',
				message : '错误，请重试',
				buttons : [ {
					label : '确定',
					script : 'atv.unloadPage()'
				} ]
			}));
			return;
		}
		;
		if (res['resp']["vodinfo_list"].length == 0) {
			atv.loadAndSwapXML(atvu.showMessage({
				title : '迅雷',
				message : '没有内容，请重试',
				buttons : [ {
					label : '确定',
					script : 'atv.unloadPage()'
				} ]
			}));
			return;
		}
		;
		name = '';
		gcid = '';
		cid = '';
		try {
			name = res['resp']['src_info']['file_name'];
			gcid = res['resp']['src_info']['gcid'];
			cid = res['resp']['src_info']['cid'];
		} catch (e) {
		}
		;
		mlength = 0;
		try {
			mlength = Math.floor(res['resp']["duration"] / 1000000);
		} catch (e) {
		}
		;
		suburl = '';
		function doplay() {
			if (suburl == '')
				suburl = srturl;
			vkey = '';
			try {
				vkey = 'xunlei.' + res['resp']["url_hash"];
			} catch (e) {
			}
			;
			if (hd == 3 && res['resp']["vodinfo_list"].length > 1) {
				msg = {
					title : '迅雷播放',
					script : [ baseURL + '/main.js' ],
					message : '请选择清晰度',
					buttons : []
				};
				for ( var i = 0; i < res['resp']["vodinfo_list"].length; i++) {
					if (i == 0)
						sname = '流畅';
					if (i == 1)
						sname = '高清';
					if (i == 2)
						sname = '超清';
					nname = decodeURIComponent(name) + '（' + sname + '）';
					act = 'atvu.realplay(\''
							+ xmlchar(res['resp']["vodinfo_list"][i]["vod_url"])
							+ '\',\'' + xmlchar(nname) + '\',\''
							+ xmlchar(nname) + '\',' + mlength + ',null,\''
							+ xmlchar(suburl) + '\',0,\'' + vkey + '\')';
					msg.buttons.push({
						label : sname,
						script : act
					});
				}
				;
				atv.loadAndSwapXML(atvu.showMessage(msg));
				return;
			}
			;
			if (res['resp']["vodinfo_list"].length > 2 && hd == 2) {
				li = res['resp']["vodinfo_list"][2];
				name = name + '（超清）';
			} else {
				if (res['resp']["vodinfo_list"].length > 1 && hd >= 1) {
					li = res['resp']["vodinfo_list"][1];
					name = name + '（高清）';
				} else {
					li = res['resp']["vodinfo_list"][0];
					name = name + '（流畅）';
				}
			}
			;
			vurl = li["vod_url"];
			atvu.realplay(vurl, name, name, mlength, null, suburl, 0, vkey);
		}
		;
		if (gcid != '') {
			surl = "http://i.vod.xunlei.com/subtitle/list?gcid=" + gcid
					+ "&cid=" + cid + "&userid=" + userid + "&t=" + d.getTime();
			atvu
					.ajax(
							surl,
							"GET",
							null,
							null,
							function(d, c) {
								try {
									if (d != null) {
										subr = JSON.parse(d);
										if (subr.sublist.length > 0) {
											msg = {
												title : '迅雷播放',
												script : [ baseURL + '/main.js' ],
												message : '请选择字幕',
												buttons : []
											};
											atv.sessionStorage['xunleivodlist'] = res['resp'];
											for ( var i = 0; i < subr.sublist.length; i++) {
												act = 'atvu.loadAction(\'xunlei.doplay\',\'\',[\''
														+ xmlchar(name)
														+ '\','
														+ mlength
														+ ',\''
														+ xmlchar(subr.sublist[i].surl)
														+ '\'],1)';
												msg.buttons
														.push({
															label : xmlchar(subr.sublist[i].sname),
															script : act
														});
											}
											;
											act = 'atvu.loadAction(\'xunlei.doplay\',\'\',[\''
													+ xmlchar(name)
													+ '\','
													+ mlength + ',\'\'],1)';
											msg.buttons.push({
												label : '无字幕',
												script : act
											});
											atv.loadAndSwapXML(atvu
													.showMessage(msg));
											return;
										}
									}
								} catch (e) {
									logger.debug(e);
								}
								;
								doplay();
							});
		} else
			doplay();
	}
	;
	function setResolution() {
		var menuItem = document.getElementById("ResItem");
		var label = menuItem.getElementByName('rightLabel');
		if (hd == 0)
			label.textContent = "流畅";
		else if (hd == 1)
			label.textContent = "高清";
		else if (hd == 2)
			label.textContent = "超清";
		else
			label.textContent = "询问";
	}
	;
	function checksession() {
		if (username && password && sessionid) {
			url = "http://login.xunlei.com/sessionid/";
			logger.debug(sessionid);
			postdata = "sessionid=" + sessionid;
			atvu.ajax(url, "POST", null, postdata, function(d, cookies) {
				isvip = getCookie(cookies, 'isvip');
				active = getCookie(cookies, 'active');
				if (isvip)
					atv.sessionStorage.vip = isvip;
				if (active != 1)
					atv.loadXML(atvu.showMessage({
						title : '登录',
						message : '登录已过期，请返回重新登录',
						buttons : [ {
							label : '确定',
							script : 'atv.unloadPage()'
						} ]
					}));
			});
		}
	}
	;
	function checkpage() {
		var menuItem = document.getElementById("LoginItem");
		var label = menuItem.getElementByName('rightLabel');
		if (username && password)
			label.textContent = username;
		else
			label.textContent = '';
	}
	;
	function getCookie(cookie, ab) {
		return (cookie.match(new RegExp("(^" + ab + "| " + ab + ")=([^;]*)")) == null) ? ""
				: decodeURIComponent(RegExp.$2);
	}
	;
	function getPassword() {
		var textEntry;
		textEntry = new atv.TextEntry();
		textEntry.type = 'password';
		textEntry.instructions = '请输入密码';
		textEntry.label = '密码';
		if (password)
			textEntry.defaultValue = password;
		textEntry.onSubmit = function(password) {
			xunleiu = atv.localStorage['xunleiu'];
			xunleiu.password = password;
			atv.localStorage['xunleiu'] = xunleiu;
			dologin();
		};
		textEntry.show();
	}
	;
	function dologin() {
		loadxl();
		url = domain + "home.html";
		atvu
				.ajax(
						url,
						"GET",
						null,
						null,
						function(d, c) {
							if (d == null) {
								atv.loadXML(atvu.showMessage({
									title : '登录',
									message : '代码错误，请重试，若再次失败请检查网络',
									buttons : [ {
										label : '确定',
										script : 'atv.unloadPage()'
									} ]
								}));
								return;
							}
							;
							jsurl = d.match(new RegExp(
									"src=\"(js/Login.js.*?)\">")) == null ? ""
									: domain + RegExp.$1;
							try {
								if (jsurl == "") {
									pos = d.indexOf("var modConfig = {");
									if (pos >= 0) {
										pos1 = d.indexOf("};");
										var modvar = d.substring(pos, pos1 + 2);
										eval(modvar);
										jsurl = modConfig.base
												+ modConfig.alias.login
												+ ".js?v=" + modConfig.AppVer;
									}
								}
							} catch (e) {
								logger.debug(e);
							}
							;
							if (jsurl == '') {
								atv.loadXML(atvu.showMessage({
									title : '登录',
									message : '代码错误，请重试，若再次失败请检查网络',
									buttons : [ {
										label : '确定',
										script : 'atv.unloadPage()'
									} ]
								}));
								return;
							}
							;
							atvu
									.ajax(
											jsurl,
											"GET",
											null,
											null,
											function(jst, c) {
												if (jst == null) {
													atv
															.loadXML(atvu
																	.showMessage({
																		title : '登录',
																		message : '代码错误，请重试，若再次失败请检查网络',
																		buttons : [ {
																			label : '确定',
																			script : 'atv.unloadPage()'
																		} ]
																	}));
													return;
												}
												;
												pos = jst
														.indexOf("init:function");
												if (pos > 0) {
													var Login;
													var jQuery = {
														extend : function() {
														},
														fn : {
															extend : function() {
															},
														},
													};
													var a = function(u) {
														return jQuery;
													};
													var define = function(ua,
															ub, c) {
														try {
															Login = c(a);
														} catch (e) {
															logger
																	.debug("Login:"
																			+ e);
														}
													};
													loginjs = jst.substring(0,
															pos)
															+ 'crp:function(ax){return P(ax)},'
															+ jst
																	.substring(pos);
													atv.localStorage['xunleiloginjs'] = loginjs;
													eval(loginjs);
													P = Login.crp;
												} else {
													atv
															.loadXML(atvu
																	.showMessage({
																		title : '登录',
																		message : '代码错误，请重试，若再次失败请检查网络',
																		buttons : [ {
																			label : '确定',
																			script : 'atv.unloadPage()'
																		} ]
																	}));
													return;
												}
												;
												url = "http://login.xunlei.com/check?u="
														+ username
														+ "&t="
														+ escape(new Date());
												atvu
														.ajax(
																url,
																"GET",
																null,
																null,
																function(d,
																		cookies) {
																	if (d == null) {
																		atv
																				.loadXML(atvu
																						.showMessage({
																							title : '登录',
																							message : '代码错误，请重试，若再次失败请检查网络',
																							buttons : [ {
																								label : '确定',
																								script : 'atv.unloadPage()'
																							} ]
																						}));
																		return;
																	}
																	;
																	check_result = getCookie(
																			cookies,
																			'check_result');
																	if (check_result
																			.indexOf(":") >= 0) {
																		verify_code = check_result
																				.split(":")[1];
																		ad = P(password);
																		ad = P(ad);
																		ad = P(ad
																				+ verify_code);
																		realLogin(
																				username,
																				ad,
																				verify_code,
																				0);
																	} else {
																		atv.sessionStorage["xunleiUsername"] = username;
																		atv.sessionStorage["xunleiPassword"] = password;
																		atvu
																				.loadAction(
																						'xunlei.capt',
																						'',
																						[],
																						1);
																	}
																});
											});
						});
	}
	;
	function realLogin(u, p, v, swap) {
		v = encodeURIComponent(v.toUpperCase());
		url = "http://login.xunlei.com/sec2login/"
		postData = "u=" + u + "&login_enable=1&login_hour=720&p=" + p
				+ "&verifycode=" + v;
		if (swap == 1) {
			loadXML = atv.loadAndSwapXML;
		} else
			loadXML = atv.loadXML;
		atvu.ajax(url, "POST", null, postData, function(d, cookies) {
			if (d == null) {
				loadXML(atvu.showMessage({
					title : '登录',
					message : '代码错误，请重试，若再次失败请检查网络',
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
				return;
			}
			;
			try {
				result = getCookie(cookies, 'blogresult');
				if (result == '1') {
					loadXML(atvu.showMessage({
						title : '登录',
						message : '登录失败,验证码错误',
						buttons : [ {
							label : '确定',
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				if (result == '2') {
					loadXML(atvu.showMessage({
						title : '登录',
						message : '登录失败,请检查用户名密码',
						buttons : [ {
							label : '确定',
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				if (result == '10') {
					loadXML(atvu.showMessage({
						title : '登录',
						message : 'SESSION过期，请重新登录',
						buttons : [ {
							label : '确定',
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				xunleiu = atv.localStorage['xunleiu'];
				xunleiu['sessionid'] = getCookie(cookies, 'lsessionid');
				if (!xunleiu['sessionid']) {
					loadXML(atvu.showMessage({
						title : '登录',
						message : '登录失败,请检查用户名密码',
						buttons : [ {
							label : '确定',
							script : 'atv.unloadPage()'
						} ]
					}));
					return;
				}
				;
				xunleiu['isvip'] = getCookie(cookies, 'isvip');
				xunleiu['userid'] = getCookie(cookies, 'userid');
				atv.localStorage['xunleiu'] = xunleiu;
				atv.sessionStorage.setItem('vip', xunleiu['isvip']);
				replace = 0;
				try {
					checkpage();
				} catch (e) {
					replace = 1
				}
				;
				if (!xunleiu['isvip'] || xunleiu['isvip'] == '0') {
					if (replace == 1) {
						if (swap == 1)
							atv.unloadPage();
						atv.loadAndSwapXML(atvu.showMessage({
							title : '登录',
							message : '你不是vip',
							buttons : [ {
								label : '确定',
								script : 'atv.unloadPage()'
							} ]
						}));
					} else
						loadXML(atvu.showMessage({
							title : '登录',
							message : '你不是vip',
							buttons : [ {
								label : '确定',
								script : 'atv.unloadPage()'
							} ]
						}));
				} else {
					if (replace == 1)
						atv.loadAndSwapXML(atvu.showMessage({
							title : '登录',
							message : '登录成功,VIP级别为：' + xunleiu['isvip'],
							buttons : [ {
								label : '确定',
								script : 'atv.unloadPage()'
							} ]
						}));
					else
						loadXML(atvu.showMessage({
							title : '登录',
							message : '登录成功,VIP级别为：' + xunleiu['isvip'],
							buttons : [ {
								label : '确定',
								script : 'atv.unloadPage()'
							} ]
						}));
				}
			} catch (e) {
				loadXML(atvu.showMessage({
					title : '登录',
					message : '代码错误:' + e,
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
			}
		});
	}
	;
	function displayFolder(li, page) {
		var hash = li['url'].substring(5);
		var d = new Date();
		var url = "http://i.vod.xunlei.com/req_subBT/info_hash/" + hash
				+ "/req_num/2/req_offset/0?&t=" + d.getTime();
		atv.sessionStorage.setItem('xunleifolder', li);
		atvu.ajax(url, "GET", null, null, function(d, c) {
			if (d == null) {
				atv.loadXML(atvu.showMessage({
					title : '迅雷',
					message : '代码错误，请重试，若再次失败请检查网络',
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
				return;
			}
			;
			try {
				res = JSON.parse(d);
				if (res['resp']['record_num'] == 1)
					playOne(li, res);
				else
					atvu.loadAction('xunlei.listFolder', '', [ page ], 1);
			} catch (e) {
				atv.loadXML(atvu.showMessage({
					title : '迅雷',
					message : '代码错误:' + e,
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
			}
		});
	}
	;
	function playOne(li, res) {
		loadxl();
		d = new Date();
		var bturl = li['url'] + "/" + res["resp"]["subfile_list"][0]["index"];
		var url = "http://i.vod.xunlei.com/req_get_method_vod?url="
				+ encodeURIComponent(bturl) + "&video_name=" + li['file_name']
				+ "&platform=1&userid=" + userid + "&vip=" + isvip
				+ "&sessionid=" + sessionid + "&cache=" + d.getTime()
				+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
		var refer = "http://61.147.76.6/iplay.html?uvs=" + userid + "_" + isvip
				+ "_" + sessionid + "&from=vlist&url="
				+ encodeURIComponent(bturl) + "&folder=" + li['file_name']
				+ "&list=all&p=1&onefilebt=1";
		srturl = '';
		atvu.ajax(url, "GET", {
			"Referer" : refer
		}, null, function(d, c) {
			eval(d);
		});
	}
	;
	return {
		"version" : 22,
		main : function(args) {
			xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
					+ baseURL
					+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.xunlei.main"><header><simpleHeader><title>迅雷</title></simpleHeader></header><menu><sections><menuSection><items><oneLineMenuItem id="ListItem" accessibilityLabel="List" onSelect="atvu.loadAction(\'xunlei.listURL\',\'\',[1]);"><label>播放特权</label></oneLineMenuItem><oneLineMenuItem id="XLPanItem" accessibilityLabel="List" onSelect="atvu.loadAction(\'xunlei.xlpan\',\'\',[1]);"><label>方舟</label></oneLineMenuItem><oneLineMenuItem id="YYETSItem" accessibilityLabel="YYETS" onSelect="atvu.loadAction(\'yyets.list\',\'\',[]);"><label>人人影视</label></oneLineMenuItem><oneLineMenuItem id="LoginItem" accessibilityLabel="Login" onSelect="atvu.loadAction(\'xunlei.login\',\'\',[],1);"><label>登录</label><rightLabel>Account</rightLabel></oneLineMenuItem><oneLineMenuItem id="ResItem" accessibilityLabel="Resolution" onSelect="atvu.loadAction(\'xunlei.switchResolution\',\'\',[],1);"><label>优先选择画质为</label><rightLabel>Resolution</rightLabel></oneLineMenuItem><oneLineMenuItem id="ListItem" accessibilityLabel="List" onSelect="atvu.loadAction(\'xunlei.clear\',\'\',[],1);"><label>清除数据</label></oneLineMenuItem></items></menuSection></sections></menu></listWithPreview></body></atv>';
			atv.loadAndSwapXML(atv.parseXML(xml));
		},
		capt : function(arg) {
			var imgurl = "http://verify.xunlei.com/image?cachetime="
					+ (new Date()).getTime();
			var nurl = "http://serv.ottnt.com:8580/captcha.html?"
					+ Math.random() + "=" + Math.random();
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
														+ '/main.js" /></head><body><shelfList id="com.atvttvv.xunlei.capt"><header><simpleHeader><title>查看验证码</title></simpleHeader></header><centerShelf><shelf id="shelf"><sections><shelfSection><items><moviePoster id="capt" onSelect="atvu.loadAction(\'xunlei.inputcapt\',\'\',[],1);"><image>'
														+ nurl
														+ '</image></moviePoster></items></shelfSection></sections></shelf></centerShelf><menu><sections><menuSection><items><oneLineMenuItem id="btn_input" onSelect="atvu.loadAction(\'xunlei.inputcapt\',\'\',[],1);"><label>输入</label></oneLineMenuItem><oneLineMenuItem id="btn_reload" onSelect="atvu.loadAction(\'xunlei.capt\',\'\',[],1);"><label>刷新</label></oneLineMenuItem></items></menuSection></sections></menu></shelfList></body></atv>';
												atv.loadXML(atv.parseXML(xml));
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
		inputcapt : function(arg) {
			var Login;
			var jQuery = {
				extend : function() {
				},
				fn : {
					extend : function() {
					},
				},
			};
			var a = function(u) {
				return jQuery;
			};
			var define = function(ua, ub, c) {
				try {
					Login = c(a);
				} catch (e) {
					logger.debug("Login:" + e);
				}
			};
			var loginjs = atv.localStorage['xunleiloginjs'];
			eval(loginjs);
			P = Login.crp;
			var username = atv.sessionStorage["xunleiUsername"];
			var password = atv.sessionStorage["xunleiPassword"];
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = "请输入验证码";
			textEntry.label = '验证码';
			textEntry.onSubmit = function(verify_code) {
				ad = P(password);
				ad = P(ad);
				ad = P(ad + encodeURIComponent(verify_code.toUpperCase()));
				realLogin(username, ad, verify_code, 1);
			};
			textEntry.show();
		},
		onload : function(args) {
			loadxl();
			id = args[0];
			if (id == "com.atvttvv.xunlei.main") {
				setResolution();
				if (!atv.sessionStorage['vip'])
					checksession();
				checkpage();
			}
		},
		switchResolution : function(args) {
			loadxl();
			if (hd == 0)
				hd = 1;
			else if (hd == 1)
				hd = 2;
			else if (hd == 2)
				hd = 3;
			else
				hd = 0;
			xunleiu = atv.localStorage['xunleiu'];
			xunleiu.hd = hd;
			atv.localStorage['xunleiu'] = xunleiu;
			setResolution();
		},
		login : function(args) {
			loadxl();
			var textEntry = new atv.TextEntry();
			textEntry.type = 'emailAddress';
			textEntry.instructions = "请输入迅雷帐号";
			textEntry.label = '帐号';
			if (username)
				textEntry.defaultValue = username;
			textEntry.onSubmit = function(username) {
				xunleiu = atv.localStorage['xunleiu'];
				xunleiu.username = username;
				atv.localStorage['xunleiu'] = xunleiu;
				getPassword();
			};
			textEntry.show();
		},
		doplay : function(args) {
			loadxl();
			name = args[0];
			mlength = args[1]
			suburl = args[2];
			res = atv.sessionStorage['xunleivodlist'];
			logger.debug(JSON.stringify(res));
			vkey = '';
			try {
				vkey = 'xunlei.' + res["url_hash"];
			} catch (e) {
			}
			;
			res = res["vodinfo_list"];
			if (hd == 3 && res.length > 1) {
				msg = {
					title : '迅雷播放',
					script : [ baseURL + '/main.js' ],
					message : '请选择清晰度',
					buttons : []
				};
				for ( var i = 0; i < res.length; i++) {
					if (i == 0)
						sname = '流畅';
					if (i == 1)
						sname = '高清';
					if (i == 2)
						sname = '超清';
					nname = decodeURIComponent(name) + '（' + sname + '）';
					act = 'atvu.realplay(\'' + xmlchar(res[i]["vod_url"])
							+ '\',\'' + xmlchar(nname) + '\',\''
							+ xmlchar(nname) + '\',' + mlength + ',null,\''
							+ xmlchar(suburl) + '\',0,\'' + vkey + '\')';
					msg.buttons.push({
						label : sname,
						script : act
					});
				}
				;
				atv.loadAndSwapXML(atvu.showMessage(msg));
				return;
			}
			;
			if (res.length > 2 && hd == 2) {
				li = res[2];
				name = name + '（超清）';
			} else {
				if (res.length > 1 && hd >= 1) {
					li = res[1];
					name = name + '（高清）';
				} else {
					li = res[0];
					name = name + '（流畅）';
				}
			}
			;
			vurl = li["vod_url"];
			atvu.realplay(vurl, name, name, mlength, null, suburl, 0, vkey);
		},
		listURL : function(args) {
			loadxl();
			if (!sessionid) {
				atv.loadAndSwapXML(atvu.showMessage({
					title : '迅雷',
					message : '请先登录讯雷会员',
					buttons : [ {
						label : '确定',
						script : 'atv.unloadPage()'
					} ]
				}));
				return;
			}
			;
			page = args[0];
			var off = (page - 1) * 30;
			var d = new Date();
			url = "http://i.vod.xunlei.com/req_history_play_list/req_num/30/req_offset/"
					+ off + "?type=all&order=create&t=" + d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '代码错误，请重试，若再次失败请检查网络',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
									return;
								}
								;
								try {
									ret = JSON.parse(d);
									li = ret['resp'];
									var total = li["record_num"];
									var tp;
									if ((total / 30) > parseInt((total / 30)))
										tp = parseInt((total / 30)) + 1;
									else
										tp = parseInt((total / 30));
									var items = new Array();
									var sii = 1;
									atv.sessionStorage
											.setItem('xunleilist', li);
									for (i = 0; i < li["history_play_list"].length; i++) {
										pi = li["history_play_list"][i];
										f1 = decodeURIComponent(pi['file_name']);
										filename = decodeURIComponent(pi['file_name']);
										act = "atvu.loadAction('xunlei.playURL','',["
												+ i + "]);";
										item = '<sixteenByNinePoster id="grid_item_'
												+ sii
												+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
												+ act
												+ '" onPlay="'
												+ act
												+ '"><title><![CDATA['
												+ filename
												+ ']]></title><image></image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
										items.push(item);
										sii++;
									}
									;
									sstr = '<grid id="grid_1"><items>'
											+ items.join("\n")
											+ '</items></grid>';
									if (tp > 1) {
										var items = new Array();
										for ( var p = 1; p <= tp; p++) {
											if (p == page)
												continue;
											item = '<actionButton id="grid_item_'
													+ sii
													+ '" accessibilityLabel=""  onSelect="atvu.loadAction(\'xunlei.listURL\',\'\',['
													+ p
													+ ']);"><title>第'
													+ p
													+ '页</title></actionButton>';
											items.push(item);
											sii++;
										}
										;
										pstr = '<grid id="grid_2"><items>'
												+ items.join("\n")
												+ '</items></grid>';
									} else
										pstr = '';
									xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
											+ baseURL
											+ '/main.js" /></head><body><scroller id="com.atvttvv.xunlei.list"><items>'
											+ sstr
											+ pstr
											+ '</items></scroller></body></atv>';
									atv.loadAndSwapXML(atv.parseXML(xml));
								} catch (e) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '数据错误:' + e,
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								}
							});
		},
		playURL : function(args) {
			loadxl();
			i = args[0];
			srturl = args[1] || '';
			li = atv.sessionStorage['xunleilist']["history_play_list"][i];
			if (!li.url)
				li.url = li.src_url;
			if (li['url'].substring(0, 5) == "bt://" && !li['gcid']) {
				displayFolder(li, 1);
				return;
			}
			;
			d = new Date();
			if (li['url'].substring(0, 5) == "ed2k:" && !li['gcid'])
				url = "http://i.vod.xunlei.com/req_get_method_vod?url="
						+ encodeURIComponent(li['url']) + "&video_name="
						+ li['file_name'] + "&platform=1&userid=" + userid
						+ "&vip=" + isvip + "&sessionid=" + sessionid
						+ "&cache=" + d.getTime()
						+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
			else
				url = "http://i.vod.xunlei.com/req_get_method_vod?url="
						+ encodeURIComponent(li['url']) + "&video_name="
						+ li['file_name'] + "&platform=1&userid=" + userid
						+ "&vip=" + isvip + "&sessionid=" + sessionid
						+ "&gcid=" + li['gcid'] + "&cid=" + li['cid']
						+ "&filesize=" + li['file_size'] + "&cache="
						+ d.getTime()
						+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
			refer = "http://61.147.76.6/iplay.html?uvs=" + userid + "_" + isvip
					+ "_" + sessionid + "&from=vlist&url="
					+ encodeURIComponent(li['url']) + "&filesize="
					+ li['file_size'] + "&gcid=" + li['gcid'] + "&cid="
					+ li['cid'] + "&filename=" + li['file_name']
					+ "&list=all&p=1";
			atvu.ajax(url, "GET", {
				"Referer" : refer
			}, null, function(d, c) {
				eval(d);
			});
		},
		listFolder : function(args) {
			page = args[0];
			li = atv.sessionStorage['xunleifolder'];
			var off = (page - 1) * 30;
			var hash = li['url'].substring(5);
			var d = new Date();
			var url = "http://i.vod.xunlei.com/req_subBT/info_hash/" + hash
					+ "/req_num/30/req_offset/" + off + "?&t=" + d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '代码错误，请重试，若再次失败请检查网络',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
									return;
								}
								;
								try {
									res = JSON.parse(d);
									var total = res['resp']['record_num'];
									var tp;
									if ((total / 30) > parseInt((total / 30)))
										tp = parseInt((total / 30)) + 1;
									else
										tp = parseInt((total / 30));
									var items = new Array();
									var sii = 1;
									atv.sessionStorage.setItem(
											'xunleifolderlist', res);
									for (i = 0; i < res['resp']["subfile_list"].length; i++) {
										pi = res['resp']["subfile_list"][i];
										filename = decodeURIComponent(pi['name']);
										act = "atvu.loadAction('xunlei.playFolderItem','',["
												+ i + "]);";
										item = '<sixteenByNinePoster id="grid_item_'
												+ sii
												+ '" accessibilityLabel="" alwaysShowTitles="true" onSelect="'
												+ act
												+ '" onPlay="'
												+ act
												+ '"><title><![CDATA['
												+ filename
												+ ']]></title><image></image><defaultImage>resource://16X9.png</defaultImage></sixteenByNinePoster>';
										items.push(item);
										sii++;
									}
									;
									sstr = '<grid id="grid_1"><items>'
											+ items.join("\n")
											+ '</items></grid>';
									if (tp > 1) {
										var items = new Array();
										for ( var p = 1; p <= tp; p++) {
											if (p == page)
												continue;
											act = "atvu.loadAction('xunlei.listFolder','',["
													+ p + "]);";
											item = '<actionButton id="grid_item_'
													+ sii
													+ '" accessibilityLabel=""  onSelect="'
													+ act
													+ '"><title>第'
													+ p
													+ '页</title></actionButton>';
											items.push(item);
											sii++;
										}
										;
										pstr = '<grid id="grid_2"><items>'
												+ items.join("\n")
												+ '</items></grid>';
									} else
										pstr = '';
									xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
											+ baseURL
											+ '/main.js" /></head><body><scroller id="com.atvttvv.xunlei.list"><items>'
											+ sstr
											+ pstr
											+ '</items></scroller></body></atv>';
									atv.loadAndSwapXML(atv.parseXML(xml));
								} catch (e) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '数据错误:' + e,
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								}
							});
		},
		playFolderItem : function(args) {
			loadxl();
			i = args[0];
			srturl = '';
			li = atv.sessionStorage['xunleifolder'];
			res = atv.sessionStorage['xunleifolderlist'];
			d = new Date();
			var bturl = li['url'] + "/"
					+ res["resp"]["subfile_list"][i]["index"];
			var url = "http://i.vod.xunlei.com/req_get_method_vod?url="
					+ encodeURIComponent(bturl) + "&video_name="
					+ li['file_name'] + "&platform=1&userid=" + userid
					+ "&vip=" + isvip + "&sessionid=" + sessionid + "&cache="
					+ d.getTime()
					+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
			var refer = "http://61.147.76.6/iplay.html?uvs=" + userid + "_"
					+ isvip + "_" + sessionid + "&from=vlist&url="
					+ encodeURIComponent(bturl) + "&folder=" + li['file_name']
					+ "&list=all&p=1&onefilebt=1";
			atvu.ajax(url, "GET", {
				"Referer" : refer
			}, null, function(d, c) {
				eval(d);
			});
		},
		clear : function(args) {
			atv.localStorage.setItem('xunleiu', {});
			atv.exitApp();
		},
		xlpan : function(args) {
			loadxl();
			var page = args[0];
			var uid = args[1] || userid;
			var d = new Date();
			var url = "http://svr.f.xunlei.com/styleBox/getUserFolderStyleBoxs?callback=jQuery17109081863281317055_"
					+ d.getTime()
					+ "&ownerUserId="
					+ uid
					+ "&needVrd=1&_="
					+ d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '代码错误，请重试，若再次失败请检查网络',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
									return;
								}
								;
								try {
									pos = d.indexOf("(");
									res = d.substring(pos + 1, d.length - 1);
									rs = JSON.parse(res);
									rs = rs['data'];
									nodes = rs['nodes'];
									items = new Array();
									if (uid == userid) {
										sstr = '<oneLineMenuItem id="xlpfollow" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpfollow\',\'\',[]);"><label>关注的人</label><accessories><arrow /></accessories></oneLineMenuItem>';
										items.push(sstr);
										sstr = '<oneLineMenuItem id="xlphist" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlphist\',\'\',[]);"><label>最近访问</label><accessories><arrow /></accessories></oneLineMenuItem>';
										items.push(sstr);
									} else {
										sstr = '<oneLineMenuItem id="xlpfollow" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpfollow\',\'\',['
												+ uid
												+ ']);"><label>他关注的人</label><accessories><arrow /></accessories></oneLineMenuItem>';
										items.push(sstr);
									}
									;
									try {
										for ( var i = 0; i < nodes.length; i++) {
											nname = nodes[i]['nodeName'];
											nid = nodes[i]['nodeId'];
											sstr = '<oneLineMenuItem id="xlpnode_'
													+ i
													+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpNode\',\'\', [\''
													+ nid
													+ '\',\''
													+ uid
													+ '\']);"><label><![CDATA['
													+ nname
													+ '/]]></label></oneLineMenuItem>';
											items.push(sstr);
										}
									} catch (e) {
									}
									;
									xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
											+ baseURL
											+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.xlpan"><header><simpleHeader><title>方舟</title></simpleHeader></header><menu><sections><menuSection><items>'
											+ items.join("\n")
											+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
									atv.loadAndSwapXML(atv.parseXML(xml));
								} catch (e) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '数据错误:' + e,
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								}
							});
		},
		xlpNode : function(args) {
			nid = args[0];
			uid = args[1];
			var d = new Date();
			var url = "http://svr.f.xunlei.com/file/getUserFileList?callback=jQuery17108093234219122678_"
					+ d.getTime()
					+ "&userId="
					+ uid
					+ "&node="
					+ uid
					+ "%3A"
					+ nid + "&needAudit=0&defaultIco=0&_=" + d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								if (d == null) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '代码错误，请重试，若再次失败请检查网络',
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
									return;
								}
								;
								try {
									pos = d.indexOf("(");
									res = d.substring(pos + 1, d.length - 1);
									rs = JSON.parse(res);
									rs = rs['data'];
									path = rs['path'];
									xlphist = atv.localStorage['xlphist'];
									if (!xlphist)
										xlphist = new Array();
									var nhist = new Array();
									nhist.push(new Array(nid, uid, path));
									for ( var i = 0; i < xlphist.length; i++) {
										item = xlphist[i];
										if (item[0] == nid)
											continue;
										nhist.push(item);
										if (nhist.length >= 200)
											break;
									}
									;
									atv.localStorage['xlphist'] = nhist;
									nodes = rs['nodes'];
									items = new Array();
									allnames = [];
									namebase = '';
									if (nodes.length <= 160) {
										for ( var i = 0; i < nodes.length; i++) {
											nname = nodes[i]['name'];
											allnames.push(nname);
										}
										;
										namebase = atvu.reducename(allnames, 8);
									}
									;
									xunleiimg = [];
									for ( var i = 0; i < nodes.length; i++) {
										nname = nodes[i]['name'];
										nid = nodes[i]['nodeId'];
										if (nodes[i]['type'] == 2)
											sstr = '<oneLineMenuItem id="xlpnode_'
													+ i
													+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpNode\',\'\',[\''
													+ nid
													+ '\',\''
													+ uid
													+ '\']);"><label><![CDATA['
													+ nname
													+ '/]]></label></oneLineMenuItem>';
										else {
											ext = nodes[i]['ext'].toLowerCase();
											if (ext == 'jpg') {
												sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
														+ i
														+ '" accessibilityLabel=""  onSelect="atvu.loadAction(\'xunlei.viewimage\',\'\',['
														+ xunleiimg.length
														+ '],1)"><label><![CDATA['
														+ nname
														+ ']]></label><label2>'
														+ ext
														+ '</label2><image><![CDATA['
														+ nodes[i]['thumb']
														+ '&s=sqX150]]></image><preview><mediaPreview><image><![CDATA['
														+ nodes[i]['thumb']
														+ '&s=X840]]></image></mediaPreview></preview></twoLineEnhancedMenuItem>';
												xunleiimg
														.push(nodes[i]['thumb']
																+ '&s=X840');
											} else if (ext == 'rm'
													|| ext == 'rmvb'
													|| ext == 'avi'
													|| ext == 'asf'
													|| ext == 'wmv'
													|| ext == 'mkv'
													|| ext == 'mov'
													|| ext == 'mp4'
													|| ext == 'm3u8'
													|| ext == 'm3u'
													|| ext == 'ts'
													|| ext == 'flv'
													|| ext == 'swf'
													|| ext == 'mpg'
													|| ext == 'mpeg'
													|| ext == 'vob'
													|| nodes[i]['video'] == true) {
												origname = nname;
												if (namebase.length > 0) {
													if (nname.substring(0,
															namebase.length) == namebase) {
														nname = nname
																.substring(namebase.length);
														ext = "(" + ext + ")."
																+ namebase;
													}
												}
												;
												if (ext == 'mp4')
													sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
															+ i
															+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpplay\',\'\',[\''
															+ xmlchar(origname)
															+ '\',\''
															+ nodes[i]['gcid']
															+ '\',\''
															+ nodes[i]['cid']
															+ '\',\''
															+ nodes[i]['size']
															+ '\',\'\',\''
															+ xmlchar(nodes[i]['url'])
															+ '\']);"><label><![CDATA['
															+ nname
															+ ']]></label><label2>'
															+ ext
															+ '</label2><image><![CDATA['
															+ nodes[i]['thumb']
															+ '&s=sqX150]]></image></twoLineEnhancedMenuItem>';
												else
													sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
															+ i
															+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpplay\',\'\',[\''
															+ xmlchar(origname)
															+ '\',\''
															+ nodes[i]['gcid']
															+ '\',\''
															+ nodes[i]['cid']
															+ '\',\''
															+ nodes[i]['size']
															+ '\']);"><label><![CDATA['
															+ nname
															+ ']]></label><label2>'
															+ ext
															+ '</label2><image><![CDATA['
															+ nodes[i]['thumb']
															+ '&s=sqX150]]></image></twoLineEnhancedMenuItem>';
											} else if (ext == 'mp3'
													|| ext == 'aac'
													|| ext == 'flac'
													|| ext == 'ape'
													|| ext == 'mpa'
													|| ext == 'wav') {
												sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
														+ i
														+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpplaym\',\'\',[\''
														+ nodes[i]['userId']
														+ '\',\''
														+ nodes[i]['nodeId']
														+ '\'])"><label><![CDATA['
														+ nname
														+ ']]></label><label2>'
														+ ext
														+ '</label2><image><![CDATA['
														+ nodes[i]['thumb']
														+ '&s=sqX150]]></image></twoLineEnhancedMenuItem>';
											} else {
												if (ext == 'srt') {
													srturl = nodes[i]['url'];
													realfname = nname
															.substring(
																	0,
																	nname.length - 4);
													for ( var j = 0; j < nodes.length; j++) {
														neext = nodes[j]['ext'];
														nename = nodes[j]['name'];
														if (nename == realfname
																+ "." + neext) {
															origname = nname;
															if (namebase.length > 0) {
																if (nname
																		.substring(
																				0,
																				namebase.length) == namebase) {
																	nname = nname
																			.substring(namebase.length);
																	ext = "("
																			+ ext
																			+ ")."
																			+ namebase;
																}
															}
															;
															sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
																	+ i
																	+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpplay\',\'\',[\''
																	+ xmlchar(origname)
																	+ '\',\''
																	+ nodes[j]['gcid']
																	+ '\',\''
																	+ nodes[j]['cid']
																	+ '\',\''
																	+ nodes[j]['size']
																	+ '\',\''
																	+ xmlchar(srturl)
																	+ '\']);"><label><![CDATA['
																	+ nname
																	+ ']]></label><label2>'
																	+ ext
																	+ '</label2><image><![CDATA['
																	+ nodes[j]['thumb']
																	+ '&s=sqX150]]></image></twoLineEnhancedMenuItem>';
															break;
														}
													}
												} else {
													origname = nname;
													if (namebase.length > 0) {
														if (nname
																.substring(
																		0,
																		namebase.length) == namebase) {
															nname = nname
																	.substring(namebase.length);
															ext = "(" + ext
																	+ ")."
																	+ namebase;
														}
													}
													;
													sstr = '<twoLineEnhancedMenuItem id="xlpnode_'
															+ i
															+ '" accessibilityLabel="" onSelect=""><label><![CDATA['
															+ nname
															+ ']]></label><label2>'
															+ ext
															+ '</label2><image><![CDATA['
															+ nodes[i]['thumb']
															+ '&s=sqX150]]></image></twoLineEnhancedMenuItem>';
												}
											}
										}
										;
										items.push(sstr);
									}
									;
									atv.localStorage['xunleiimg'] = xunleiimg;
									if (items.length == 0)
										items
												.push('<oneLineMenuItem id="xlpnode_0" accessibilityLabel="" onSelect=""><label>空目录</label></oneLineMenuItem>');
									xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
											+ baseURL
											+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.xunlei.xlpan.node"><header><simpleHeader><title>方舟</title></simpleHeader></header><menu><sections><menuSection><items>'
											+ items.join("\n")
											+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
									atv.loadAndSwapXML(atv.parseXML(xml));
								} catch (e) {
									atv.loadAndSwapXML(atvu.showMessage({
										title : '迅雷',
										message : '数据错误:' + e,
										buttons : [ {
											label : '确定',
											script : 'atv.unloadPage()'
										} ]
									}));
								}
							});
		},
		viewimage : function(args) {
			idx = args[0];
			xunleiimg = atv.localStorage['xunleiimg'];
			il = [];
			for ( var i = 0; i < xunleiimg.length; i++) {
				il.push({
					"type" : "photo",
					"id" : "photo_0",
					"assets" : [ {
						"width" : 1024,
						"height" : 600,
						"src" : xunleiimg[i]
					} ]
				});
			}
			;
			atv.slideShow.run(idx, il);
		},
		xlpplaym : function(args) {
			uid = args[0];
			nid = args[1];
			d = new Date();
			var url = "http://svr.f.xunlei.com/file/getAudioDetail?callback=jQuery17108196359698981729_"
					+ d.getTime()
					+ "&node="
					+ uid
					+ "%3A"
					+ nid
					+ "&_="
					+ d.getTime();
			atvu.ajax(url, "GET", null, null, function(d, c) {
				pos = d.indexOf("(");
				res = d.substring(pos + 1, d.length - 1);
				rs = JSON.parse(res);
				playlink(rs["data"]["audioUrl"]);
			});
		},
		xlpplay : function(args) {
			loadxl();
			filename = args[0];
			gcid = args[1];
			cid = args[2];
			size = args[3];
			srturl = args[4] || '';
			origurl = args[5] || '';
			var url = "xlpan:///" + filename;
			d = new Date();
			url = "http://i.vod.xunlei.com/req_get_method_vod?url="
					+ encodeURIComponent(url) + "&video_name="
					+ encodeURIComponent(filename) + "&platform=1&userid="
					+ userid + "&vip=" + isvip + "&sessionid=" + sessionid
					+ "&gcid=" + gcid + "&cid=" + cid + "&filesize=" + size
					+ "&cache=" + d.getTime()
					+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
			refer = "http://61.147.76.6/iplay.html?uvs=" + userid + "_" + isvip
					+ "_" + sessionid + "&from=vlist&url="
					+ encodeURIComponent(url) + "&filesize=" + size + "&gcid="
					+ gcid + "&cid=" + cid + "&filename="
					+ encodeURIComponent(filename) + "&list=all&p=1";
			atvu.ajax(url, "GET", {
				"Referer" : refer
			}, null, function(d, c) {
				eval(d);
			});
		},
		playurl : function(args) {
			eurl = args[0];
			srturl = args[1] || '';
			filename = args[2] || '';
			loadxl();
			if (!userid) {
				atv.loadAndSwapXML(atvu.showMessage({
					title : '登录',
					script : [ baseURL + '/main.js' ],
					message : '尚未登录',
					buttons : [ {
						label : '确定',
						script : 'atvu.loadAction(\'xunlei.login\',\'\',[],1)'
					} ]
				}));
				return;
			}
			;
			if (filename == '') {
				filename = eurl.split("|")[2];
				try {
					fname = decodeURIComponent(filename);
				} catch (e) {
					fname = unescape(filename);
				}
			}
			;
			d = new Date();
			url = "http://i.vod.xunlei.com/req_get_method_vod?url="
					+ encodeURIComponent(eurl) + "&video_name="
					+ encodeURIComponent(filename) + "&platform=1&userid="
					+ userid + "&vip=" + isvip + "&sessionid=" + sessionid
					+ "&cache=" + d.getTime()
					+ "&from=vlist&jsonp=XL_CLOUD_FX_INSTANCEqueryBack";
			refer = "http://61.147.76.6/iplay.html?uvs=" + userid + "_" + isvip
					+ "_" + sessionid + "&from=vlist&url="
					+ encodeURIComponent(eurl)
					+ "&filesize=null&gcid=null&cid=null&filename="
					+ encodeURIComponent(filename) + "&list=all&p=1";
			atvu.ajax(url, "GET", {
				"Referer" : refer
			}, null, function(d, c) {
				eval(d);
			});
		},
		xlpfollow : function(args) {
			loadxl();
			uid = args[0] || userid;
			var d = new Date();
			var url = "http://svr.f.xunlei.com/fans/getFollowingUser?callback=jQuery171003918525530025363_"
					+ d.getTime()
					+ "&pageNo=1&pagesize=20&userId="
					+ uid
					+ "&_=" + d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								pos = d.indexOf("(");
								res = d.substring(pos + 1, d.length - 1);
								rs = JSON.parse(res);
								rs = rs['data'];
								totalp = rs["pageNum"];
								curpage = rs["pageNo"];
								nodes = rs['nodes'];
								items = new Array();
								for ( var i = 0; i < nodes.length; i++) {
									nname = nodes[i]['nickname'];
									nuid = nodes[i]['userId'];
									itc = i + (curpage - 1) * 20;
									sstr = '<oneLineMenuItem id="xlpnode_'
											+ itc
											+ '" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.xlpan\',\'\',[1,\''
											+ nuid
											+ '\']);"><label>'
											+ xmlchar(nname)
											+ '</label><accessories><arrow /></accessories></oneLineMenuItem>';
									items.push(sstr);
								}
								;
								if (totalp > curpage) {
									nextp = curpage + 1;
									sstr = '<oneLineMenuItem id="xlpnode_next" accessibilityLabel="" onSelect="atvu.loadAction(\'xunlei.nextpage\',\'\',['
											+ nextp
											+ ',\''
											+ uid
											+ '\'],1);"><label>下一页</label><accessories><arrow /></accessories></oneLineMenuItem>';
									items.push(sstr);
								}
								;
								if (items.length == 0) {
									items
											.push('<oneLineMenuItem id="xlpnode_next" accessibilityLabel=""><label>没有关注</label></oneLineMenuItem>')
								}
								;
								xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="'
										+ baseURL
										+ '/main.js" /></head><body><listWithPreview id="com.atvttvv.xunlei.xlpan.followusers"><header><simpleHeader><title>方舟</title></simpleHeader></header><menu><sections><menuSection><items id="xlpflwmenu">'
										+ items.join("\n")
										+ '</items></menuSection></sections></menu></listWithPreview></body></atv>';
								atv.loadAndSwapXML(atv.parseXML(xml));
							});
		},
		nextpage : function(args) {
			loadxl();
			page = args[0];
			uid = args[1] || userid;
			var mynode = document.getElementById('xlpnode_next');
			mynode.setAttribute("onSelect", "");
			var arrow = mynode.getElementByTagName("arrow");
			var newElement = document.makeElementNamed("spinner");
			arrow.parent.appendChild(newElement);
			arrow.removeFromParent();
			var d = new Date();
			var url = "http://svr.f.xunlei.com/fans/getFollowingUser?callback=jQuery171003918525530025363_"
					+ d.getTime()
					+ "&pageNo="
					+ page
					+ "&pagesize=20&userId="
					+ uid + "&_=" + d.getTime();
			atvu
					.ajax(
							url,
							"GET",
							null,
							null,
							function(d, c) {
								pos = d.indexOf("(");
								res = d.substring(pos + 1, d.length - 1);
								rs = JSON.parse(res);
								rs = rs['data'];
								var pnode = document
										.getElementById('xlpflwmenu');
								totalp = rs["pageNum"];
								curpage = rs["pageNo"];
								nodes = rs['nodes'];
								for ( var i = 0; i < nodes.length; i++) {
									nname = nodes[i]['nickname'];
									nuid = nodes[i]['userId'];
									itc = i + (curpage - 1) * 20;
									pnode
											.appendChild(atvu
													.createNode(
															{
																"name" : "oneLineMenuItem",
																"attrs" : [
																		{
																			"name" : "id",
																			"value" : "xlpnode_"
																					+ itc
																		},
																		{
																			"name" : "onSelect",
																			"value" : "atvu.loadAction('xunlei.xlpan','',[1,'"
																					+ nuid
																					+ "']);",
																		} ],
																"children" : [
																		{
																			"name" : "label",
																			"text" : nname,
																		},
																		{
																			"name" : "accessories",
																			"children" : [ {
																				"name" : "arrow",
																			} ],
																		} ],
															}, document));
								}
								;
								var mynode = document
										.getElementById('xlpnode_next');
								mynode.removeFromParent();
								if (totalp > curpage) {
									nextp = curpage + 1;
									pnode
											.appendChild(atvu
													.createNode(
															{
																"name" : "oneLineMenuItem",
																"attrs" : [
																		{
																			"name" : "id",
																			"value" : "xlpnode_next",
																		},
																		{
																			"name" : "onSelect",
																			"value" : "atvu.loadAction('xunlei.nextpage','',["
																					+ nextp
																					+ ",'"
																					+ uid
																					+ "'],1);",
																		} ],
																"children" : [
																		{
																			"name" : "label",
																			"text" : "下一页",
																		},
																		{
																			"name" : "accessories",
																			"children" : [ {
																				"name" : "arrow",
																			} ],
																		} ],
															}, document));
								}
							});
		},
	}
};