var serverurl = 'http://10.0.1.5/appletv';
var taobaoLoginApi = "https://login.taobao.com/member/login.jhtml";
var taobaoTokenApi = 'http://favorite.taobao.com/collect_list.htm';
var taobaoAddToFavoriteApi = 'http://favorite.taobao.com/popup/add_collection.htm';
var taobaoClient ={
		login:function(username,password){
			var payload = "TPL_username="+username+"&TPL_password="+password;
			var url = taobaoLoginApi+"?TPL_username="+username+"&TPL_password="+password;
			appletv.logToServer(payload,serverurl);
			appletv.makePostRequest(url,payload,function(content){
				appletv.logToServer(content,serverurl);
			});
		},
		
		getToken:function(){
			appletv.makeRequest(taobaoTokenApi,function(content){
				appletv.logToServer(content,serverurl);
			});
		},
		
		addToFavorite:function(id,token){
			var url = taobaoAddToFavoriteApi+"itemtype=1&isTmall=1&isLp=&isTaohua=&id="+id+"&token="+token;
			appletv.makeRequest(url,function(content){
				appletv.logToServer(content,serverurl);
			});
		},
}