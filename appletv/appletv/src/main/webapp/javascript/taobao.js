var serverurl = 'http://10.0.1.5/appletv';
var taobaoLoginApi = "https://login.taobao.com/member/login.jhtml";
var taobaoTokenApi = 'http://i.taobao.com/my_taobao.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
var taobaoClient ={
		login:function(username,password){
			var url = taobaoLoginApi+"?TPL_username="+username+"&TPL_password="+password;
			appletv.makePostRequest(url,null,function(content){
				appletv.logToServer(content,serverurl);
			});
		},
		
		getToken:function(){
			appletv.logToServer('call get token',serverurl);
			appletv.makeRequest(taobaoTokenApi,function(htmlcontent){
				appletv.logToServer(htmlcontent,serverurl);
				appletv.makePostRequest('http://10.0.1.5/appletv/ctl/taobao/loginWithToken.do',htmlcontent,function(content){
					
				});
			});
		},
		
}