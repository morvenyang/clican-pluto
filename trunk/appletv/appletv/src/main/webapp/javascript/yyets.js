var yyetsSearchApi = "http://www.yyets.com/php/resourcelist";
var yyetsVideoApi = "http://www.yyets.com/php/resource/";
var yyetsClient = {
		yyetsChannels:
		[
		 {label:"电影",value:"movie"},
		 {label:"电视剧",value:"tv"},
		 {label:"纪录片",value:"documentary"},
		 {label:"公开课",value:"openclass"},
		 {label:"搜索",value:"search"}
		],
			
		yyetsChannelMap:
		{
		 "tv":{label:"电视剧",value:"tv"},
		 "movie":{label:"电影",value:"movie"},
		 "documentary":{label:"纪录片",value:"documentary"},
		 "openclass":{label:"公开课",value:"openclass"},
		 "search":{label:"搜索",value:"search"}
		},
		
		loadIndexPage:function(keyword,page,channelId){
			var url = yyetsSearchApi+"?channel="+channelId+"&page="+page;
			var channel = this.yyetsChannelMap[channelId];
			appletv.makeRequest(yyetsSearchApi,function(htmlContent){
				if (htmlContent == null) {
					return;
				}
                var start = d.indexOf('<ul class="boxPadd dashed">');
                var end = d.indexOf('</ul>', start);
                var itemstr = d.substring(start, end);
                var itemlist = itemstr.split('<li class="clearfix">');
                var videos = [];
                for (var i = 1; i < itemlist.length; i++) {
                	var rs = (new RegExp('<a target="_blank" class="imglink" href="http://www.yyets.com/php/resource/(\\d+)"><img src="([^"]*)"></a>')).exec(itemlist[i]);
                	if (!rs) {
                		continue;
                	}
                	var itemid = rs[1];
                	var imgurl = rs[2];
                	var rsn = (new RegExp('<dt><a target="_blank" href="http://www.yyets.com/php/resource/\\d+">【([^<]*)】<strong>《([^<]*)》([^<]*)</strong></a>([^<]*)</dt>')).exec(itemlist[i]);
                    if (!rsn) {
                    	continue;
                    }
                    var name = rsn[2];
                    var video = {pic:imageurl,id:itemid,title:name};
                    videos.push(video);
                }
                yyetsClient.generateIndexPage(keyword,page,channel,videos);
			});
		},
		
		generateIndexPage: function(keyword,page,channel,videos){
			var begin= 1;
			var end = 1;
			if (page < 90) {
				begin = page;
				end = page + 9;
			} else {
				end = 99;
				begin = 90;
			}
			var data = {'channel':channel,'keyword':keyword,'begin':begin,'end':end,'channels':yyetsClient.yyetsChannels,'serverurl':appletv.serverurl,'videos':videos};
			var xml = new EJS({url: appletv.serverurl+'/template/yyets/index.ejs'}).render(data);
			appletv.loadAndSwapXML(xml);
		},
		
		loadVideoPage: function(id){
			atv.loadXML(appletv.makeDialog('加载中...','Loading...'));
			var url = yyetsVideoApi + itemid;
			appletv.makeRequest(url,function(htmlContent){
				if (htmlContent == null) {
					return;
				}
				rs = appletv.findall('<li[^>]*format="([^"]*)"><a class="f_out">([^<]*)<span class="f_in"></span></a>', htmlContent);
				formats = [];
                seasons = {};
                for (var i = 0; i < rs.length; i++) {
                    seasons[rs[i][0]] = [
                        [], {}];
                    if (rs[i][0] == 'all') {
                    	continue;
                    }
                    formats.push(rs[i][0]);
                };
                rs = atvu.findall('<li format="([^"]*)" season="([^"]*)"><a>([^<]*)</a></li>', htmlContent);
                for (var i = 0; i < rs.length; i++) {
                    fm = rs[i][0];
                    se = rs[i][1];
                    sn = rs[i][2];
                    seasons[fm][1][se] = [sn, []];
                    seasons[fm][0].push(se);
                };
                sdata = data.split('<ul class="resod_list"');
                for (var i = 1; i < sdata.length; i++) {
                    rs = (new RegExp('season="(.*?)"')).exec(sdata[i]);
                    season = rs[1];
                    edata = sdata[i].split('<li itemid=');
                    for (var j = 1; j < edata.length; j++) {
                        rs1 = (new RegExp('format="(.*?)"')).exec(edata[j]);
                        format = rs1[1];
                        rs2 = (new RegExp('<a title="(.*?)"')).exec(edata[j]);
                        title = rs2[1];
                        rs3 = (new RegExp('<span class="b"><font class="f5">([^<]*)</font>')).exec(edata[j]);
                        if (rs3 == null) size = '';
                        else size = rs3[1];
                        rs4 = (new RegExp('<a type="ed2k" href="([^"]*)" target="_blank">')).exec(edata[j]);
                        if (rs4 == null) rs4 = (new RegExp('<a href="(magnet:[^"]*)"')).exec(edata[j]);
                        if (rs4 == null) continue;
                        link = rs4[1];
                        seasons[format][1][season][1].push([title, size, link]);
                    }
                };
                rs = (new RegExp('<font class="f14">说明：(.*?)</font>')).exec(data);
                if (rs) status = rs[1];
                else status = '';
                rs = (new RegExp('<div class="f_l_img">\\s*<a href="([^"]*.jpg)" target="_blank">')).exec(data);
                if (rs) imgurl = rs[1];
                else imgurl = '';
                logger.debug(imgurl);
                rs = (new RegExp('<li><span>年代：</span><strong>(.*?)</strong>')).exec(data);
                if (rs) year = rs[1];
                else year = '';
                rs = (new RegExp('<font class="f5">类.*?型：</font>(.*?)</li>')).exec(data);
                if (rs) ty = rs[1];
                else ty = '';
                rs = (new RegExp('<li><span>地区：</span><strong>(.*?)</strong>')).exec(data);
                if (rs) region = rs[1];
                else region = '';
                rs = (new RegExp('<font class="f5">制作公司：</font>(.*?)</li>')).exec(data);
                if (rs) corp = rs[1];
                else corp = '';
                rs = (new RegExp('<li><span>语言：</span><strong>(.*?)</strong>')).exec(data);
                if (rs) lang = rs[1];
                else lang = '';
                rs = (new RegExp('<font class="f5">上映日期：</font><label id="pubdate">(.*?)</label></li>')).exec(data);
                if (rs) pubdate = rs[1];
                else pubdate = '';
                rs = (new RegExp('<li><span>英文：</span>(.*?)</li>')).exec(data);
                if (rs) eng = rs[1];
                else eng = '';
                rs = (new RegExp('<li><span>IMDB：</span><a href="" target="_blank"></a>(.*?)</li>')).exec(data);
                if (rs) imdb = rs[1];
                else imdb = '';
                pos = data.indexOf('<li><span>简介：</span>');
                desc = '';
                if (pos >= 0) {
                    pos1 = data.indexOf('</li>', pos);
                    desc = data.substring(pos + 20, pos1);
                    pos = desc.indexOf('<div style="display:none;">');
                    if (pos >= 0) desc = desc.substring(pos);
                    desc = sh(desc.replace(/&nbsp;/g, ' '));
                };
                
				var video = {'serverurl':appletv.serverurl,video:{'id':id,area:region,pic:imgurl,title:name,year:year,desc:desc},'items':items};
				var xml = new EJS({url: appletv.serverurl+'/template/yyets/video.ejs'}).render(video);
				atv.loadAndSwapXML(atv.parseXML(xml));
			});
		},
		

}