var taobaoLoginApi = 'https://login.taobao.com/member/login.jhtml';
var taobaoTokenApi = 'http://i.taobao.com/my_taobao.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
var taobaoMyFavoriteApi = 'http://favorite.taobao.com/json/collect_list_chunk.htm?itemtype=1&isBigImgShow=true&orderby=time&startrow=0&chunkSize=30&chunkNum=1&deleNum=0';
var taobaoMyFavoriteStoreApi = '';
var taobaoClient = {
	login : function(username, password) {
		var url = taobaoLoginApi + "?TPL_username=" + username
				+ "&TPL_password=" + password;
		appletv.makePostRequest(url, null, function(content) {
			taobaoClient.getToken();
		});
	},

	getToken : function() {
		appletv.makeRequest(taobaoTokenApi, function(htmlcontent) {
			appletv.makePostRequest(appletv.serverurl
					+ '/ctl/taobao/loginWithToken.do', htmlcontent, function(
					content) {
				if (content == 'success') {
					appletv.showDialog('登录成功', '');
				} else {
					appletv.showDialog('登录失败', '');
				}
			});
		});
	},

	loginByUserNameAndPassword:function(usernameAndPassword){
		var index = usernameAndPassword
				.indexOf('\\');
		if (index > 0) {
			var username = usernameAndPassword
					.substring(
							0,
							index);
			var password = usernameAndPassword
					.substring(index + 1);
			taobaoClient.login(
					username,
					password);
		} else {
			appletv
					.showDialog(
							'用户名密码输入格式错误',
							'请以\'\\\'分隔用户名密码');
		}
	},
	
	addToFavorite : function(id, token) {
		if (token == null || token.length == 0|| token=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getToken.do',
							function(sessiontoken) {
								if (sessiontoken == null
										|| sessiontoken.length == 0) {
									appletv
											.showSearchPage(
													'用户名\密码',
													'请输入淘宝用户名密码,用户名和密码以\'\\\'分隔',
													taobaoClient.loginByUserNameAndPassword,'taobaoClient.loginByUserNameAndPassword');
								} else {
									var url = taobaoAddToFavoriteApi
											+ "?itemtype=1&isTmall=1&isLp=&isTaohua=&id="
											+ id + "&_tb_token_="
											+ sessiontoken;
									appletv.makePostRequest(url, null,
											function(htmlcontent) {
												appletv.showDialog('收藏成功', '');
											});
								}
							});
			return;
		} else {
			var url = taobaoAddToFavoriteApi
					+ "?itemtype=1&isTmall=1&isLp=&isTaohua=&id=" + id
					+ "&_tb_token_=" + token;
			appletv.makePostRequest(url, null, function(htmlcontent) {
				appletv.showDialog('收藏成功', '');
			});
		}

	},
	
	loadFavoriteItemPage : function(token) {
		if (token == null || token.length == 0|| token=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getToken.do',
							function(sessiontoken) {
								if (sessiontoken == null
										|| sessiontoken.length == 0) {
									appletv
											.showSearchPage(
													'用户名\密码',
													'请输入淘宝用户名密码,用户名和密码以\'\\\'分隔',
													taobaoClient.loginByUserNameAndPassword,'taobaoClient.loginByUserNameAndPassword');
								} else {
									appletv.makeRequest(taobaoMyFavoriteApi,
											function(htmlcontent) {
												appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favoriteItem.xml',htmlcontent,function(xmlcontent){
													appletv.loadXML(xmlcontent);
												});
											});
								}
							});
			return;
		} else {
			appletv.makeRequest(taobaoMyFavoriteApi,
					function(htmlcontent) {
						appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favorite.xml',htmlcontent,function(xmlcontent){
							appletv.loadXML(xmlcontent);
						});
					});
		}

	},
	
	getSellerIdByShopUrl:function(shopUrl){
		appletv.makeRequest(shopUrl,
				function(htmlcontent) {
					appletv.logToServer(htmlcontent);
					var start = htmlcontent.indexOf('userid=')+'userid='.length;
					var end = htmlcontent.indexOf(';',start);
					sellerId = htmlcontent.substring(start,end);
					alert(sellerId);
				});
	},
	
	getItemsByCategory:function(shopId,scid,scname){
		var url = "http://shop" + shopId + ".taobao.com/search.htm?scid="+ scid + "&scname=" + scname+ "&checkedRange=true&queryType=cat";
		appletv.logToServer(url);
		appletv.makeRequest(url,
				function(htmlcontent) {
					var ids = '';
					var start=0;
					var end=0;
					var href='';
					var index = htmlcontent.indexOf('href="');
					var idstart=0;
					var idend=0;
					while(index!=-1){
						start = index+6;
						end = htmlcontent.indexOf('"',start);
						href = htmlcontent.substring(start,end);
						if(href.indexOf('item.htm')!=-1){
							idstart = href.indexOf('id=')+3;
							idend = href.indexOf('&',idstart);
							if(idend!=-1){
								id = href.substring(idstart,idend);
							}else{
								id = href.substring(idstart);
							}
							if(ids.indexOf(id)==-1){
								ids+=id+',';
							}
						}
						index = htmlcontent.indexOf('href="',end);
					}
					appletv.makeRequest(appletv.serverurl+'/ctl/taobao/shopCategoryItemList.xml?scid='+scid+'&itemIds='+ids,function(xmlcontent){
						appletv.loadXML(xmlcontent);
					});
				});
	
	},
}