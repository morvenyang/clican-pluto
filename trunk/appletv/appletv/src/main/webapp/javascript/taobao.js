var taobaoLoginApi = 'https://login.taobao.com/member/login.jhtml';
var taobaoTokenApi = 'http://i.taobao.com/my_taobao.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
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
}