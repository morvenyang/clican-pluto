var taobaoLoginApi = 'https://login.taobao.com/member/login.jhtml';
var taobaoTokenApi = 'http://i.taobao.com/my_taobao.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
var taobaoMyFavoriteItemApi = 'http://favorite.taobao.com/json/collect_list_chunk.htm?itemtype=1&isBigImgShow=true&orderby=time&startrow=0&chunkSize=30&chunkNum=1&deleNum=0';
var taobaoMyFavoriteShopApi = 'http://favorite.taobao.com/collect_list.htm?itemtype=0';
var taobaoLoveApi = 'http://love.taobao.com/guang/mobile_search.htm';
var taobaoAddToShoppingCartApi = 'http://cart.taobao.com/add_cart_item.htm?bankfrom=&quantity=1';
var taobaoConfirmOrderApi = 'http://buy.taobao.com/auction/order/confirm_order.htm';
var taobaoClient = {
	login : function(username, password) {
		var url = taobaoLoginApi + "?TPL_username=" + username
				+ "&TPL_password=" + password;
		appletv.makePostRequest(url, null, function(content) {
			appletv.logToServer(content);
			taobaoClient.loginWithTokenAndTid();
		});
	},

	loginWithTokenAndTid : function() {
		appletv.makeRequest(taobaoTokenApi, function(htmlcontent) {
			appletv.makePostRequest(appletv.serverurl
					+ '/ctl/taobao/loginWithTokenAndTid.do', htmlcontent, function(
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
			appletv.setValue('taobaousernameandpassword',usernameAndPassword);
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
	
	showLoginPage:function(){
		appletv
		.showInputTextPage(
				'用户名\密码',
				'请输入淘宝用户名密码,用户名和密码以\'\\\'分隔。淘宝的帐号密码直接通过HTTPS在淘宝登录获得临时令牌用于收藏相关操作,本服务器不会获取你的淘宝帐号和密码。',
				taobaoClient.loginByUserNameAndPassword,'taobaoClient.loginByUserNameAndPassword',appletv.getValue('taobaousernameandpassword'));
	},
	
	addToFavorite : function(id, itemtype, token) {
		if (token == null || token.length == 0|| token=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getTokenAndTid.do',
							function(tokenAndTid) {
								if (tokenAndTid == null
										|| tokenAndTid.length == 0) {
									taobaoClient.showLoginPage();
								} else {
									var tat = JSON.parse(tokenAndTid);
									var url = taobaoAddToFavoriteApi
											+ "?itemtype="+itemtype+"&isTmall=1&isLp=&isTaohua=&id="
											+ id + "&_tb_token_="
											+ tat['token'];
									appletv.logToServer(url);
									appletv.makePostRequest(url, null,
											function(htmlcontent) {
												appletv.showDialog('收藏成功', '');
											});
								}
							});
			return;
		} else {
			var url = taobaoAddToFavoriteApi
					+ "?itemtype="+itemtype+"&isTmall=1&isLp=&isTaohua=&id=" + id
					+ "&_tb_token_=" + token;
			appletv.logToServer(url);
			appletv.makePostRequest(url, null, function(htmlcontent) {
				appletv.showDialog('收藏成功', '');
			});
		}

	},
	
	addToShoppingCart : function(id,skuid, tid) {
		var url = taobaoAddToShoppingCartApi+'&nekot='+Date.parse(new Date());
		if(id==skuid){
			url = url+ '&outer_id_type=1';
		}else{
			url = url+ '&outer_id_type=2';
		}
		if (tid ==null || tid.length==0 ||tid=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getTokenAndTid.do',
							function(tokenAndTid) {
								if (tokenAndTid == null
										|| tokenAndTid.length == 0) {
									taobaoClient.showLoginPage();
								} else {
									var tat = JSON.parse(tokenAndTid);
									url = url +'&item_id='+id+'&outer_id='+skuid+'&ct='+tid;
									appletv.logToServer(url);
									appletv.makePostRequest(url, null,taobaoClient.showAddToShoppingCartResult);
								}
							});
			return;
		} else {
			url = url +'&item_id='+id+'&outer_id='+skuid+'&ct='+tid;
			appletv.logToServer(url);
			appletv.makePostRequest(url, null, taobaoClient.showAddToShoppingCartResult);
		}

	},
	
	showAddToShoppingCartResult: function(result){
		var start = result.indexOf("{");
		var end = result.lastIndexOf("}");
		result = result.substring(start,end+1);
		appletv.logToServer(result);
		var json = JSON.parse(result);
		var error = json['error'];
		if(error!=null&&error.length>0){
			appletv.showDialog('添加购物车失败'+error, error);
		}else{
			appletv.showDialog('添加购物车成功', '当前购物车内有'+json['cartQuantity']+'样商品,总价:'+json['cartPrice']+'元');
		}
	},
	
	loadFavoriteItemPage : function(token) {
		if (token == null || token.length == 0|| token=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getTokenAndTid.do',
							function(tokenAndTid) {
								if (tokenAndTid == null
										|| tokenAndTid.length == 0) {
									taobaoClient.showLoginPage();
								} else {
									appletv.makeRequest(taobaoMyFavoriteItemApi,
											function(htmlcontent) {
												appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favoriteItem.xml',htmlcontent,function(xmlcontent){
													appletv.loadXML(xmlcontent);
												});
											});
								}
							});
			return;
		} else {
			appletv.makeRequest(taobaoMyFavoriteItemApi,
					function(htmlcontent) {
						appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favoriteItem.xml',htmlcontent,function(xmlcontent){
							appletv.loadXML(xmlcontent);
						});
					});
		}

	},
	
	loadFavoriteShopPage : function(token) {
		if (token == null || token.length == 0|| token=='null') {
			appletv
					.makeRequest(
							appletv.serverurl + '/ctl/taobao/getTokenAndTid.do',
							function(sessiontoken) {
								if (tokenAndTid == null
										|| tokenAndTid.length == 0) {
									taobaoClient.showLoginPage();
								} else {
									appletv.makeRequest(taobaoMyFavoriteShopApi,
											function(htmlcontent) {
												appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favoriteShop.xml',htmlcontent,function(xmlcontent){
													appletv.loadXML(xmlcontent);
												});
											});
								}
							});
			return;
		} else {
			appletv.makeRequest(taobaoMyFavoriteShopApi,
					function(htmlcontent) {
						appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/favoriteShop.xml',htmlcontent,function(xmlcontent){
							appletv.loadXML(xmlcontent);
						});
					});
		}

	},
	
	loadLovePage : function(tagId,pagenum) {
		var url = taobaoLoveApi+'?pagenum='+pagenum+'&tagid='+tagId;
		appletv.logToServer(url);
		appletv.makeRequest(url,
				function(jsonContent) {
					appletv.logToServer(jsonContent);
					var json = JSON.parse(jsonContent);
					var itemList = json['itemList'];
					var photoDicts = [];
					for(var i=0;i<itemList.length;i++){
						var item = itemList[i];
						var id = item['itemId']+'';
						var photoDict = {"id":id,type:'photo',assets:[{width:1024,height:768,src:item['picUrl']}]};
						photoDict.caption = '买家 '+item['buyerNick']+' '+item['operateType'] +' '+item['operateTime'];
						photoDicts.push(photoDict);
					}
					photoDicts.push({"id":'-1',type:'photo',assets:[{width:1024,height:768,src:appletv.serverurl+'/image/taobao/button/right_arrow.png'}]});
					taobaoClient.showPhoto(photoDicts,tagId,pagenum);
				});
	},
	
	showPhoto: function(photoDicts,tagId,pagenum){
		var fullScreenMediaBrowser = new atv.FullScreenMediaBrowser();
		fullScreenMediaBrowser.onItemSelection = function(photoID) {
			if(photoID=='-1'){
				appletv.logToServer('load more data');
				taobaoClient.loadLovePage(tagId, pagenum+1)
			}else{
				appletv.logToServer(appletv.serverurl+'/ctl/taobao/item.xml?itemId='+photoID);
				atv.loadURL(appletv.serverurl+'/ctl/taobao/item.xml?itemId='+photoID);
			}
		};
		
		fullScreenMediaBrowser.show(photoDicts, 0);
	},
	
	search:function(keyword){
		atv.loadURL(appletv.serverurl+'/ctl/taobao/itemList.xml?keyword='+encodeURIComponent(keyword));
	},
	
	loadItemsByCategory:function(shopId,scid,scname){
		var url = "http://shop" + shopId + ".taobao.com/search.htm?scid="+ scid + "&scname=" + scname+ "&checkedRange=true&queryType=cat";
		appletv.makeRequest(url,
				function(htmlcontent) {
					if(htmlcontent==null||htmlcontent.length==0){
						return;
					}
					appletv.logToServer(htmlcontent);
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
	
	loadConfirmOrderPage:function(token){
		if (token == null || token.length == 0|| token=='null') {
			appletv
			.makeRequest(
					appletv.serverurl + '/ctl/taobao/getTokenAndTid.do',
					function(sessiontoken) {
						if (tokenAndTid == null
								|| tokenAndTid.length == 0) {
							taobaoClient.showLoginPage();
						} else {
							appletv.makeRequest(url, function(htmlcontent){
								appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/confirmOrderPage.xml?',htmlcontent,function(xmlcontent){
									appletv.loadXML(xmlcontent);
								});
							});
						}
					});
			return;
		}else{
			appletv.makeRequest(url, function(htmlcontent){
				appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/confirmOrderPage.xml?',htmlcontent,function(xmlcontent){
					appletv.loadXML(xmlcontent);
				});
			});
		}
		
	},
	
}