var Login = (function(jQuery){
	
	var $ = jQuery
	,	iframe
	,	form
	,	url
	,	loginData = {}
	,	loginAccounts = []
	,	isin = 0
	,	logining = 0
	,	config={
			action: "",
			domain:'xunlei.com',
			form:'loginform',
			box:'login_box',
			u:'login_u',
			p:'login_p',
			auto:'login_auto',
			verifycode:'login_verifycode',
			verifyimg:'login_verify_code',
			verifyref:'login_ref_verify',
			verifyp:'login_vcode_p',
			tip: "login_tip",
			submit:'login_submit',
			expire:24*7 	//7天过期
		}
    // 用户信息，默认为白金用户
    ,	vodUserInfo = {
        oriType:0,
        type: "vodVipUser",
        level: -1,
        expire: "",
        last_expire: "",
        flux: -1
    }
    ,	loginTip = {
	        sidExpired: "登录出现异常，请重新登录"
	    }
	,	_sessionid
	,	loginServer=[ 'http://login.xunlei.com/'
					, 'http://login2.xunlei.com/'
					, 'http://login3.xunlei.com/']
    ,	vodServer = "http://i.vod.xunlei.com"
	,	bridge = '/domain.html'

	,	curServerIndex=0
	,	timer
	,	hexcase = 0  /* hex output format. 0 - lowercase; 1 - uppercase        */
	,	b64pad  = "" /* base-64 pad character. "=" for strict RFC compliance   */
	,	chrsz   = 8;  /* bits per input character. 8 - ASCII; 16 - Unicode      */

	function getCookie(name){
		return (document.cookie.match( new RegExp("(^"+ name +"| "+ name +")=([^;]*)")) == null ) ? "" : decodeURIComponent(RegExp.$2);
	}
	function setCookie(name,value,hours,isBaseDomain){
		if(arguments.length>2){
			var expireDate=new Date(new Date().getTime()+hours*3600000);
			document.cookie = name + "=" + encodeURIComponent(value) + "; path=/; domain="+config.domain+"; expires=" + expireDate.toGMTString() ;
		}else
			document.cookie = name + "=" + encodeURIComponent(value) + "; path=/; domain="+config.domain; 
	}
	jQuery.extend(String.prototype, {
		trim: function(){
			return this.replace(/(^\s+)|(\s+$)/g, "");	
		}
	});
	jQuery.fn.extend({
		loginshowInputTips: function(strTips){
			this._tips = strTips;
			if (this.val().trim() == "") {
				this.val(strTips);
				this.css("color", "gray");
			}
			
			this.focus($.proxy(function(){
				if (this.val().trim() == this._tips) {
					this.css("color", "black");
					this.val("");
				}
			}, this));
			this.blur($.proxy(function(){
				if (this.val().trim() == "") {
					this.css("color", "gray");
					this.val(this._tips);
				}
			}, this));
		}
	});

	Function.prototype.delayApply = function(time, thisObj, argArray)	// 延迟调用
	{
		var f = this;
		return setTimeout( function() {
			f.apply(thisObj, argArray);
		}, time);
	};

	var EventSource = function()
	{
	};
 
	EventSource.prototype.addEventListener = function(sEvent, fpNotify, tDelay)
	{
		var oListener = this;
		if(!this[sEvent])		// !(sEvent in this)
			this[sEvent] = [ ];
		if(!(this[sEvent] instanceof Array))
			return false;
		for(var i=0; i<this[sEvent].length; i++)
			if(this[sEvent][i].o == oListener && this[sEvent][i].f == fpNotify)
				return true;
		this[sEvent].push( {o: oListener, f: fpNotify, t: tDelay} );
		return this;
		return true;
	};
	EventSource.prototype.attachEvent = EventSource.prototype.addEventListener;
 
	// 解除事件绑定
	EventSource.prototype.removeEventListener = function(sEvent, fpNotify)
	{
		var oListener = this;
		if(!this[sEvent] || !(this[sEvent] instanceof Array))
			return false;

		if(!fpNotify){
			this[sEvent] = [];
			return true;
		}
		for(var i=0; i<this[sEvent].length; i++)
			if(this[sEvent][i].o == oListener && this[sEvent][i].f == fpNotify) {
				this[sEvent].splice(i, 1);
				if(0 == this[sEvent].length)
					delete this[sEvent];
				return true;
			}
		return false;
	};
	EventSource.prototype.detachEvent = EventSource.prototype.removeEventListener;
 
	// 激发事件
	EventSource.prototype.dispatchEvent = function(sEvent)
	{	
		if(!this[sEvent] || !(this[sEvent] instanceof Array))
			return false;
		var args = [this].concat( argumentsToArray(arguments) );
		//var args = arguments;
		args.shift();
		args.shift();
		var listener = this[sEvent].slice(0);
		for(var i=0; i<listener.length; i++)
			if(typeof(listener[i].t) == "number")
				listener[i].f.delayApply( listener[i].t, listener[i].o, args );
			else
				listener[i].f.apply( listener[i].o, args );
		return true;
	};
	EventSource.prototype.fireEvent = EventSource.prototype.dispatchEvent;

	function argumentsToArray(args){
		var result = [ ];
		for(var i=0; i<args.length; i++)
			result.push(args[i]);
		return result;
	}

	var event=null;

	function setXlCookie(name,value,sec){
		if(arguments.length>2){
			var expireDate=new Date(new Date().getTime()+sec*1000);
			document.cookie = name + "=" + escape(value) + "; path=/; domain=xunlei.com; expires=" + expireDate.toGMTString() ;
		}else
		document.cookie = name + "=" + escape(value) + "; path=/; domain=xunlei.com";
	}
	function setGdCookie(name,value,sec){
		if(arguments.length>2){
			var expireDate=new Date(new Date().getTime()+sec*1000);
			document.cookie = name + "=" + escape(value) + "; path=/; domain=vod.xunlei.com; expires=" + expireDate.toGMTString() ;
		}else
		document.cookie = name + "=" + escape(value) + "; path=/; domain=vod.xunlei.com";
	}
	

//***************************************************************
	var Login = {
		init:function( _config ){
			var that = this;

			iframe = null;
			form = null;
			jQuery.extend( config, _config );
			
			loginServer = [ 'http://login.' + config.domain +'/'
						  , 'http://login2.'+ config.domain +'/'
						  , 'http://login3.'+ config.domain +'/'];
			
			event = event || new EventSource();
			jQuery.extend( that, event );

			var tmp = new String("onsuccess,onerror,onvalid,onlogining,onautoerror,onlogout,onuserinfoloaded").split(',');
			
			for(var i in _config){
				if( i != ',' && jQuery.inArray(i,tmp) != -1){
					this.removeEventListener(i);
					this[i.substr(2)]( _config[i] );
				}
			}
			
			$(window).load(function(){
				//$('#'+config.u).loginshowInputTips('迅雷号码/邮箱/手机号码');
			});

            // 启动时，检测一下是否已经添了用户名（浏览器自动保证帐号功能），如果有的话，自动发包请求下验证码信息，以免登录时的验证码信息不对
            var checkVerify = function(){
            	var userName = $('#'+config.u).val();
                userName = $.trim(userName);
                if(userName){
                    checkVerifyCode(userName);
                }
            }
            setTimeout( checkVerify, 20);

			$('#'+config.u).change(function(){
                    setTimeout( checkVerify, 50 );
			}).focus(function(){
				isin = 1;
			}).blur(function(){
				isin = 0;
                checkVerify();
			});
			
			$('#'+config.p).focus(function(){
				isin = 1;
			}).blur(function(){
				isin = 0;
			});
			
			$('#'+config.verifyref).click(function(){
				that.vcode();
			});
			
			$('#'+config.verifycode).focus(function(){
				$(this).val('');
				isin = 1;
			}).blur(function(){
				if(!$(this).val())
					$(this).val('');
					//$(this).val('验证码');
				isin = 0;
			});


            $('#'+config.auto).click(function(){
                this.focus();
            }).focus(function(){
                    isin = 1;
            }).blur(function(){
                    isin = 0;
            });

            $('#'+config.box).keydown(function(event){
				if(isin==0) return true;
				if(event.keyCode!=13) return true;
				if($('#'+config.box).css('display')=='none') return true;
				that.run();
			});
			
			
			$('#'+config.submit).click(function(){
				that.run();
			});
			$('#'+config.submit).attr('disable',false);

			var lx_login_u = $.trim(getCookie("lx_login_u"));
			var luid = $.trim(getCookie("luserid"));
			var lsessionid = $.trim(getCookie("lsessionid"));
			if(luid != '' && lsessionid != '') {
			}
			else{
				$('#'+config.u).val(lx_login_u).change();
			}

            // 处理一些登录动作
            // sessionid过期,重新登录
            if(config.action == "sidExpired"){
                this.showTip( loginTip.sidExpired );
                this.exit();
            }

			return this;
		},
		
        // 重置登录框
        resetLogBox:function(){
            var u = $('#'+config.u);
            var p = $('#'+config.p);
            var rememberMe = $('#'+config.auto);
            var vcode = $('#'+config.verifyp);
            var tip = $('#'+config.tip);

            u.val("");
            p.val("");
            rememberMe.attr("checked", true);
            vcode.hide();
            tip.text("").hide();
        },
        // 显示提示
        showTip:function(text){
            var tip = $('#'+config.tip);

            tip.text(text).show();
        },
		vcode:function(){
			var	p = $('#'+config.verifyp);
			$('#'+config.verifyimg).attr('src','http://verify.'+config.domain+'/image?cachetime='+new Date().getTime());
			var	p = $('#'+config.verifyp);
			$('#'+config.verifycode).val('');
		},
		vOneCode:function(){
			var	ver = $('#'+config.verifyimg);
			var	p = $('#'+config.verifyp);

			if(ver.attr('src')=='' || ver.attr('src')==undefined)
				ver.attr('src','http://verify.'+config.domain+'/image?cachetime='+new Date().getTime());
			else this.vcode();
			ver.show();
			p.show();
			$('#'+config.verifycode).val('');
		},
		//登陆
		run:function(){
			if(logining == 1) return;
			if(!valid()) return '';
			$('#'+config.submit).attr('disable',true);
			var ifr_name="poster_iframe_"+(new Date().getTime())+"_"+parseInt(Math.random()*100000);
			$('body').append("<iframe id='"+ifr_name+"' name='"+ifr_name+"' src='"+bridge+"' width='0' height='0'></iframe>");
			
			iframe = $('#'+ifr_name);
			iframe.load(_c);
			
			iframe.hide();
			return this;
		},
		//退出
		exit:function(){
			var ckeys1 = ["sessionid","usrname","nickname","userid","usertype","usernewno","raw","isvip","blogresult","vipstate","p_t",'lsessionid','luserid'];
			for(var i=0;i<ckeys1.length;i++){
				setCookie(ckeys1[i],"",0,1);
                // 退出登录时同时清掉vod域名下的cookie
                setGdCookie(ckeys1[i],"",0,1);
			}
			this.logout();
			return this;
		},
		//自动登录
		auto:function(id){
			var that = this;
			if(!id){
				// 列表页要重新发session校验登录
				/*if(isStrictCheck){
					_sessionid = getCookie('lsessionid') || getCookie('sessionid');
				}else{
					_sessionid = getCookie('lsessionid');
				}*/
				_sessionid = getCookie('lsessionid');
			}
			else{
				_sessionid=id;	
			}
			
			if( getCookie('sessionid') && getCookie('userid')){
				that.success(1);
				return this;
			}
			if(!_sessionid){
				that.autoerror();
				return this;
			}
			
			var ifr_name="poster_iframe_"+(new Date().getTime())+"_"+parseInt(Math.random()*100000);
			$('body').append("<iframe id='"+ifr_name+"' name='"+ifr_name+"' src='"+bridge+"' width='0' height='0'></iframe>");
			iframe = $('#'+ifr_name);
			iframe.load(session);
			iframe.hide();
			return this;
		},
		success:function(){
			var args = [this,'onsuccess'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
            // 登录成功后再去获取用户信息
            if(typeof arguments[0] != 'function')
                //not need
                //getUserInfo();
            return this;
		},
		error:function(){
			var args = [this,'onerror'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
			return this;
		},
		valid:function(){
			var args = [this,'onvalid'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
			return this;
		},
		autoerror:function(){
			var args = [this,'onautoerror'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
			return this;
		},
		logout:function(){
			var args = [this,'onlogout'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
			return this;
		},
		logining:function(){
			var args = [this,'onlogining'].concat(argumentsToArray(arguments));
			callevent.apply(this,args);
			return this;
		},
		userinfoloaded:function(){
		    var args = [this,'onuserinfoloaded'].concat(argumentsToArray(arguments));
		    callevent.apply(this,args);
		    return this;
		},
		unbind:function(name,f){
			this.removeEventListener('on'+name,f);
			return this;
		},
		getuserinfo:function(){
		    return vodUserInfo;
		}
	};

//******************************************************************

function getUserInfo(){
    // var userInfo = vodUserInfo;
    // 默认为白金用户
    var defaultUserInfo = {
        oriType: 0,
        type: "vodVipUser",
        level: -1,
        expire: "",
        flux: -1
    };

    var success = function(info){
        var userType = -1;
        if(info.type != -1){
            vodUserInfo.oriType = info.type;
            vodUserInfo.type = info.type;
            vodUserInfo.level = info.level;
            if(vodUserInfo.level != -1)
                setCookie('isvip', vodUserInfo.level);
            vodUserInfo.expire = info.expire;
            vodUserInfo.last_expire = info.last_expire;
            vodUserInfo.flux = info.flux;
            switch(info.type){
                case 0:
                    userType = "vodVipUser";
                    break;
                case 1:
                    userType = "oldVodVipUser";
                    break;
                case 2:
                    userType = "vipUser";
                    break;
                case 3:
                    userType = "expVipUser";
                    break;
                case 4:
                    userType = "norUser";
                    break;
                default:
                    // userType = -1;
                    userType = "vodVipUser";
                    break;
            }
            vodUserInfo.type = userType;
        }
        Login.userinfoloaded();
    }
    var error = function(){
        vodUserInfo = $.extend(vodUserInfo, defaultUserInfo);
        Login.userinfoloaded();
    }


    // 拼写请求url
    var url = [ vodServer,   // 服务器地址
        'get_user_info'];    // 请求列表接口

    url = url.join('/');

    var query = {
        userid: getCookie('userid'),
        from: "vodLogin",
        t: new Date().getTime()  // 增加时间戳
    };

    // ajax配置
    var config = {
        url: url,
        dataType: 'jsonp',
        jsonp: "jsonp",
        data: query,
        processData: true,
        timeout: 7000,
        error: error,
        success: success
    };

    var ret = $.ajax(config);
    return ret;
}

function callevent(obj,name,f){
	if(typeof f=='function'){
		obj.addEventListener(name,f);
	}
	else{
		var args = argumentsToArray(arguments);
		args.shift();
		setTimeout(function(){
			obj.fireEvent.apply( obj, args );
		},10);
	}
}


var checkVerifyCodeFlag = false;
function checkVerifyCode(usrname){
	if(checkVerifyCodeFlag) return;
	var checkUrl = 'http://login.xunlei.com/check?u='+usrname+'&t='+new Date();
	checkVerifyCodeFlag= true;

	var ifr_name = "union_poster_iframe_"+(new Date().getTime())+"_"+parseInt(Math.random()*100000);
	if($.browser.msie && jQuery.browser.version=='6.0'){
		$('body').append("<iframe id='"+ifr_name+"' name='"+ifr_name+"' width='0' height='0'></iframe>");
	}
	else{
		$('body').append("<iframe id='"+ifr_name+"' name='"+ifr_name+"' src='"+bridge+"' width='0' height='0'></iframe>");
	}
	var iframe = $('#'+ifr_name);
	iframe.load(function(){
		$(this).unbind('load').load(function(){
			var res = parseInt(getCookie('check_result'));
			if(res == 0){
				$('#'+config.verifyp).hide();
				var newVerifyCode = getCookie('check_result').split(':')[1];
				$('#'+config.verifycode).val(newVerifyCode);
			}else{
				$('#'+config.verifycode).val('');
				Login.vcode();
				$('#'+config.verifyp).show();
			}
			checkVerifyCodeFlag = false;
			//setTimeout(function(){
                // 注意新的jq库没有$(document).remove(iframe)这个方法了
				// $(document).remove(iframe);
                $(iframe).remove();
			//},1);
		});
		this.src = checkUrl;
		setTimeout(function(){
			checkVerifyCodeFlag=false;
		},300);
	});
	iframe.hide();
}

function valid(){
	$('#'+config.u).val($('#'+config.u).val().trim());
	$('#'+config.p).val($('#'+config.p).val().trim());
	if($('#'+config.u).val().length == 0){Login.valid("请输入帐号"); $('#'+config.u).focus(); return false;}
	if($('#'+config.p).val().length == 0){Login.valid("请输入密码"); $('#'+config.p).focus(); return false;}
	//if($('#'+config.verifycode).val().length == 0){Login.valid("请输入验证码"); $('#'+config.verifycode).focus(); return false;}
	return true;
}

function _c(){
	if(logining) return;
	logining = 1;
	iframe.unbind('load');
	form = $('<form />').attr({
						method: "post",
						target: iframe.attr('name'),
						action: loginServer[curServerIndex]+'sec2login/'
			});
	
	var data={};
	data.u=encodeURI($('#'+config.u).val());

	if($('#'+config.auto).attr('checked')){
		data.login_enable=1;
		data.login_hour=config.expire;
	}
	else {
		data.login_enable=0;
		data.login_hour=0;
	}

	var md5Pwd = hex_md5($('#'+config.p).val());
	md5Pwd = hex_md5(md5Pwd);
	md5Pwd = hex_md5(md5Pwd+$('#'+config.verifycode).val().toUpperCase());
	data.p=encodeURIComponent(md5Pwd);
	data.verifycode=encodeURIComponent($('#'+config.verifycode).val().toUpperCase());
	
	for(var i in data){
		$("<input>",{
			type:'hidden',
			val:data[i],
			name:i
		}).appendTo(form);
	}

	$('body').append(form);
	form.hide();
	iframe.load(_d);

	Login.logining();

	timer = setTimeout(function(){_d();},7000);
	form.submit();
}
function _d(){
	logining = 0;
	if(timer)clearTimeout(timer);
	var result = getCookie("blogresult");
	var login_error = {1: "验证码错误",2: "密码错误",3: "服务器忙",4: "帐号不存在",5: "数据包错误",6: "帐号被锁定",
						7: "session请求失败",8: "服务器忙",9: "验证码长度不对",10: "非法验证码",11: "验证码超时",12: "登录页面无效",13: "登录页面无效",14: "登录页面无效",15: "登录页面无效",16: "网络超时，请重新登录"};
	//$('body').remove(iframe).remove(form);
	iframe=null;
	form=null;
	if(result!="0"){
		curServerIndex++;
		if(curServerIndex<loginServer.length&&(result==''||result==3||result==7)){
			Login.run();
		}
		else{
			$('#'+config.submit).attr('disabled',false);
			//$('#'+config.verifycode).val('');
			//$('#'+config.verifyimg).attr('src','http://verify.'+config.domain+'/image?cachetime='+new Date().getTime());

			//checkVerifyCode($('#'+config.u).val());
			Login.vOneCode();

			if(result==2 || result==1 || result==9){
				$('#'+config.p).val('').focus();
			}
			curServerIndex=0;
			var msg = login_error[result];
			if(typeof login_error[result] == 'undefined'){
				var msg = '未知错误';
			}
			Login.error(result,msg);
		}
	}
	else{
		curServerIndex=0;
		Login.success(3);
	}
}

function session(){
	iframe.unbind('load');
	form = $('<form />',{
						method: "post",
						target: iframe.attr('name'),
						action: loginServer[curServerIndex]+'sessionid/'
			});
	$('<input>',{
		type:'hidden',
		name:'sessionid',
		val:_sessionid
	}).appendTo(form);
	
	$('body').append(form);
	form.hide();

	iframe.load(sessionback);

	Login.logining();
	
	timer = setTimeout(function(){sessionback();},7000);
	form.submit();
}
function sessionback(){
	if(timer)clearTimeout(timer);
	var result = getCookie("blogresult");
	
	var login_error = {1: "jumpkey失效",2: "jumpkey无效",3: "服务器内部错误",4: "帐号不存在",5: "无效帐号",6: "帐号被锁定",
						7: "服务器内部错误"};
	
	//$('body').remove(iframe).remove(form);
	iframe=null;
	form=null;

	if(result!="0"){
		curServerIndex++;
		if(curServerIndex<loginServer.length&&(result==''||result==3||result==7)){
			Login.auto(_sessionid);
		}
		else {
			curServerIndex=0;
			Login.autoerror();
		}
	}
	else{
		curServerIndex=0;
		Login.success();
	}
}
//md5

function hex_md5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}
function b64_md5(s){ return binl2b64(core_md5(str2binl(s), s.length * chrsz));}
function str_md5(s){ return binl2str(core_md5(str2binl(s), s.length * chrsz));}
function hex_hmac_md5(key, data) { return binl2hex(core_hmac_md5(key, data)); }
function b64_hmac_md5(key, data) { return binl2b64(core_hmac_md5(key, data)); }
function str_hmac_md5(key, data) { return binl2str(core_hmac_md5(key, data)); }

/*
 * Perform a simple self-test to see if the VM is working
 */
function md5_vm_test()
{
  return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}

/*
 * Calculate the MD5 of an array of little-endian words, and a bit length
 */
function core_md5(x, len)
{
  /* append padding */
  x[len >> 5] |= 0x80 << ((len) % 32);
  x[(((len + 64) >>> 9) << 4) + 14] = len;

  var a =  1732584193;
  var b = -271733879;
  var c = -1732584194;
  var d =  271733878;

  for(var i = 0; i < x.length; i += 16)
  {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;

    a = md5_ff(a, b, c, d, x[i+ 0], 7 , -680876936);
    d = md5_ff(d, a, b, c, x[i+ 1], 12, -389564586);
    c = md5_ff(c, d, a, b, x[i+ 2], 17,  606105819);
    b = md5_ff(b, c, d, a, x[i+ 3], 22, -1044525330);
    a = md5_ff(a, b, c, d, x[i+ 4], 7 , -176418897);
    d = md5_ff(d, a, b, c, x[i+ 5], 12,  1200080426);
    c = md5_ff(c, d, a, b, x[i+ 6], 17, -1473231341);
    b = md5_ff(b, c, d, a, x[i+ 7], 22, -45705983);
    a = md5_ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
    d = md5_ff(d, a, b, c, x[i+ 9], 12, -1958414417);
    c = md5_ff(c, d, a, b, x[i+10], 17, -42063);
    b = md5_ff(b, c, d, a, x[i+11], 22, -1990404162);
    a = md5_ff(a, b, c, d, x[i+12], 7 ,  1804603682);
    d = md5_ff(d, a, b, c, x[i+13], 12, -40341101);
    c = md5_ff(c, d, a, b, x[i+14], 17, -1502002290);
    b = md5_ff(b, c, d, a, x[i+15], 22,  1236535329);

    a = md5_gg(a, b, c, d, x[i+ 1], 5 , -165796510);
    d = md5_gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
    c = md5_gg(c, d, a, b, x[i+11], 14,  643717713);
    b = md5_gg(b, c, d, a, x[i+ 0], 20, -373897302);
    a = md5_gg(a, b, c, d, x[i+ 5], 5 , -701558691);
    d = md5_gg(d, a, b, c, x[i+10], 9 ,  38016083);
    c = md5_gg(c, d, a, b, x[i+15], 14, -660478335);
    b = md5_gg(b, c, d, a, x[i+ 4], 20, -405537848);
    a = md5_gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
    d = md5_gg(d, a, b, c, x[i+14], 9 , -1019803690);
    c = md5_gg(c, d, a, b, x[i+ 3], 14, -187363961);
    b = md5_gg(b, c, d, a, x[i+ 8], 20,  1163531501);
    a = md5_gg(a, b, c, d, x[i+13], 5 , -1444681467);
    d = md5_gg(d, a, b, c, x[i+ 2], 9 , -51403784);
    c = md5_gg(c, d, a, b, x[i+ 7], 14,  1735328473);
    b = md5_gg(b, c, d, a, x[i+12], 20, -1926607734);

    a = md5_hh(a, b, c, d, x[i+ 5], 4 , -378558);
    d = md5_hh(d, a, b, c, x[i+ 8], 11, -2022574463);
    c = md5_hh(c, d, a, b, x[i+11], 16,  1839030562);
    b = md5_hh(b, c, d, a, x[i+14], 23, -35309556);
    a = md5_hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
    d = md5_hh(d, a, b, c, x[i+ 4], 11,  1272893353);
    c = md5_hh(c, d, a, b, x[i+ 7], 16, -155497632);
    b = md5_hh(b, c, d, a, x[i+10], 23, -1094730640);
    a = md5_hh(a, b, c, d, x[i+13], 4 ,  681279174);
    d = md5_hh(d, a, b, c, x[i+ 0], 11, -358537222);
    c = md5_hh(c, d, a, b, x[i+ 3], 16, -722521979);
    b = md5_hh(b, c, d, a, x[i+ 6], 23,  76029189);
    a = md5_hh(a, b, c, d, x[i+ 9], 4 , -640364487);
    d = md5_hh(d, a, b, c, x[i+12], 11, -421815835);
    c = md5_hh(c, d, a, b, x[i+15], 16,  530742520);
    b = md5_hh(b, c, d, a, x[i+ 2], 23, -995338651);

    a = md5_ii(a, b, c, d, x[i+ 0], 6 , -198630844);
    d = md5_ii(d, a, b, c, x[i+ 7], 10,  1126891415);
    c = md5_ii(c, d, a, b, x[i+14], 15, -1416354905);
    b = md5_ii(b, c, d, a, x[i+ 5], 21, -57434055);
    a = md5_ii(a, b, c, d, x[i+12], 6 ,  1700485571);
    d = md5_ii(d, a, b, c, x[i+ 3], 10, -1894986606);
    c = md5_ii(c, d, a, b, x[i+10], 15, -1051523);
    b = md5_ii(b, c, d, a, x[i+ 1], 21, -2054922799);
    a = md5_ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
    d = md5_ii(d, a, b, c, x[i+15], 10, -30611744);
    c = md5_ii(c, d, a, b, x[i+ 6], 15, -1560198380);
    b = md5_ii(b, c, d, a, x[i+13], 21,  1309151649);
    a = md5_ii(a, b, c, d, x[i+ 4], 6 , -145523070);
    d = md5_ii(d, a, b, c, x[i+11], 10, -1120210379);
    c = md5_ii(c, d, a, b, x[i+ 2], 15,  718787259);
    b = md5_ii(b, c, d, a, x[i+ 9], 21, -343485551);

    a = safe_add(a, olda);
    b = safe_add(b, oldb);
    c = safe_add(c, oldc);
    d = safe_add(d, oldd);
  }
  return Array(a, b, c, d);

}

/*
 * These functions implement the four basic operations the algorithm uses.
 */
function md5_cmn(q, a, b, x, s, t)
{
  return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s),b);
}
function md5_ff(a, b, c, d, x, s, t)
{
  return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function md5_gg(a, b, c, d, x, s, t)
{
  return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function md5_hh(a, b, c, d, x, s, t)
{
  return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}
function md5_ii(a, b, c, d, x, s, t)
{
  return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}

/*
 * Calculate the HMAC-MD5, of a key and some data
 */
function core_hmac_md5(key, data)
{
  var bkey = str2binl(key);
  if(bkey.length > 16) bkey = core_md5(bkey, key.length * chrsz);

  var ipad = Array(16), opad = Array(16);
  for(var i = 0; i < 16; i++)
  {
    ipad[i] = bkey[i] ^ 0x36363636;
    opad[i] = bkey[i] ^ 0x5C5C5C5C;
  }

  var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
  return core_md5(opad.concat(hash), 512 + 128);
}

/*
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
 * to work around bugs in some JS interpreters.
 */
function safe_add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

/*
 * Bitwise rotate a 32-bit number to the left.
 */
function bit_rol(num, cnt)
{
  return (num << cnt) | (num >>> (32 - cnt));
}

/*
 * Convert a string to an array of little-endian words
 * If chrsz is ASCII, characters >255 have their hi-byte silently ignored.
 */
function str2binl(str)
{
  var bin = Array();
  var mask = (1 << chrsz) - 1;
  for(var i = 0; i < str.length * chrsz; i += chrsz)
    bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (i%32);
  return bin;
}

/*
 * Convert an array of little-endian words to a string
 */
function binl2str(bin)
{
  var str = "";
  var mask = (1 << chrsz) - 1;
  for(var i = 0; i < bin.length * 32; i += chrsz)
    str += String.fromCharCode((bin[i>>5] >>> (i % 32)) & mask);
  return str;
}

/*
 * Convert an array of little-endian words to a hex string.
 */
function binl2hex(binarray)
{
  var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i++)
  {
    str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +
           hex_tab.charAt((binarray[i>>2] >> ((i%4)*8  )) & 0xF);
  }
  return str;
}

/*
 * Convert an array of little-endian words to a base-64 string
 */
function binl2b64(binarray)
{
  var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i += 3)
  {
    var triplet = (((binarray[i   >> 2] >> 8 * ( i   %4)) & 0xFF) << 16)
                | (((binarray[i+1 >> 2] >> 8 * ((i+1)%4)) & 0xFF) << 8 )
                |  ((binarray[i+2 >> 2] >> 8 * ((i+2)%4)) & 0xFF);
    for(var j = 0; j < 4; j++)
    {
      if(i * 8 + j * 6 > binarray.length * 32) str += b64pad;
      else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
    }
  }
  return str;
}


	return Login;

})(jQuery);