(function(e,t){var n=e.mix({},e.EventTarget),r=e.onRequire,i=function(t,n){if(!t)return; var r=[]; return t=t.split(/\s+/),e.each(t,function(t){e.each(n,function(e){r.push(e+":"+t)})}),r.join(" ")},s={}; s.listen=function(e,t,r){n.on(i(e,r||this.groups),t)},s.broadcast=function(t,r,s){return n.fire(i(t,s||this.groups),e.mix(r,{target:this}))},s.removeListener=function(e,t,r){n.detach(i(e,r),t)}; var o=!1; e.onRequire=function(n){if(!n)return; var i=n.name,o=r&&r(n); o===t&&(o=n.value); if(i.match(/^tc\/(mods|cart)/)&&o&&!e.isFunction(o)){var u=function(t){this.config=e.merge(this.config,t),this.moduleName=i,this.groups=this.config.groups||["global"],this._init&&this._init(),i.match(/^tc\/cart/)&&KISSY.use("tc/core/monitor",function(e,t){t.init({api:"http://www.atpanel.com/tpfe_perf.gif",pageId:i.replace(/^tc\/cart\//,"")})})}; i.match(/^tc\/mods/)&&e.extend(u,e.Base),e.augment(u,e.EventTarget,s,o),n.value=u}return n.value}; var u=[],a=this,f=a.console||{},l={},c=Function.prototype.bind,h={}; e.UA.ie&&e.UA.ie<8&&(a.console={}),e.each(f,function(e,t){var n=[t,e]; u.push(n),l[n[0]]=n[1]}),e.each(["assert","count","debug","dir","dirxml","error","group","groupCollapsed","groupEnd","info","log","markTimeline","memory","profile","profileEnd","profiles","time","timeEnd","trace","warn"],function(t){if(e.UA.ie&&e.UA.ie<8){h[t]=e.isEmptyObject(f)?function(){}:f.log; return}h[t]=function(){var n=[].slice.call(arguments),r="",i=""; if(!a.console||!p.Debug||e.UA.ie&&e.UA.ie===9||!c)return n[0]; t in l||(i="["+t.toUpperCase()+"] ",t="log",n[0]=i+n[0]); if("fire"in this&&this.fire(t,{args:n})===!1)return n[0]; try{return c.call(l[t],f).apply(f,n)}catch(s){}}}),e.mix(h,e.EventTarget),e.mix(h,{decorate:function(t,n,r){if(e.UA.firefox){if(!(new RegExp("\\["+n+"\\]")).test(t[0]))return!1; var i="%c"+t.shift(); return t.unshift(i,r),!0}}}),e.each(h,function(e,t){f&&(f[t]=e)}),a.console=h,console.on("log info",function(e){return!/(loaded\.)$/.test(e.args[0])&&!/^(standard)/.test(e.args[0])}); var p={Debug:"@DEBUG",Version:"4.0",setTimeStamp:function(t,n,r){if(!t||e.Config.debug)t="4.0"; p.Version=t; var i=document.domain.indexOf("daily.")>-1; return n=n||(i?"http://assets.daily.taobao.net":"http://a.tbcdn.cn"),e.config({packages:[{name:"tc",path:n+"/apps/tradeface/"+t+"/",tag:r||t,charset:"gbk"}]}),e}}; e.TC=p,e.mix(e.TC,s)})(KISSY)