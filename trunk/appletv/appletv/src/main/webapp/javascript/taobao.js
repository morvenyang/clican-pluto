var taobaoLoginApi = 'https://login.taobao.com/member/login.jhtml';
var taobaoTokenApi = 'http://i.taobao.com/my_taobao.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
var taobaoClient ={
		login:function(username,password){
			var url = taobaoLoginApi+"?TPL_username="+username+"&TPL_password="+password;
			appletv.makePostRequest(url,null,function(content){
				appletv.logToServer(content);
			});
		},
		
		getToken:function(){
			appletv.logToServer('call get token');
			appletv.makeRequest(taobaoTokenApi,function(htmlcontent){
				appletv.makePostRequest(appletv.serverurl+'/ctl/taobao/loginWithToken.do',htmlcontent,function(content){
					
				});
			});
		},
		
		addToFavorite:function(id,token){
			var url = taobaoAddToFavoriteApi+"?itemtype=1&isTmall=1&isLp=&isTaohua=&id="+id+"&_tb_token_="+token;
			appletv.makePostRequest(url,null,function(htmlcontent){
			});
		},
}