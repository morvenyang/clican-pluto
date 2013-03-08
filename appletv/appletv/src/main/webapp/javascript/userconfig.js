var userconfig = {
		configMap:
		{
		 "userconfig.localServerIP":"本地服务器IP"
		},
		
		loadConfigPage: function(){
			var allConfig = {deviceId:appletv.getDeivceId(),configs:{'userconfig.localServerIP':appletv.getValue['userconfig.localServerIP']}};
			var url = appletv.serverurl+'/ctl/config/loadConfigPage.xml;
			appletv.makePostRequest(url,allConfig,function(xml){
				appletv.loadAndSwapXML(xml);
			});
		},
		
		
		getLocalServerIP: function(callback) {
			var ip=appletv.getValue('userconfig.localServerIP');
			if(ip!=null&&ip.length>0){
				callback(ip);
				return;
			}
			var url = appletv.serverurl+'/ctl/config/getConfig.do?deviceId='+appletv.getDeviceId();
			url += '&key=localServerIP';
			appletv.makeRequest(url,function(result){
				ip = result;
				appletv.setValue('userconfig.localServerIP',ip);
				callback(ip);
				return;
			});
		},
		
		saveLocalServerIP: function(ip) {
			appletv.setValue('userconfig.localServerIP',ip);
			var url = appletv.serverurl+'/ctl/config/saveConfig.do?deviceId='+appletv.getDeviceId();
			url += '&key=localServerIP';
			url += '&value='+ip;
			appletv.makeRequest(url,function(result){
			});
		}
}