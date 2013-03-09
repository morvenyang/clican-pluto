function __init() {
    return {
        "version": 15,
        searchmain: function (args) {
            var textEntry = new atv.TextEntry();
            textEntry.type = 'emailAddress';
            textEntry.instructions = "搜索（输入全拼，拼音首字母或者使用Remote App直接输入中文）";
            textEntry.label = '关键字';
            textEntry.onSubmit = function (word) {
                url = "http://tip.soku.com/search_keys?query=" + encodeURIComponent(word) + "&h=11";
                atvu.ajax(url, "GET", null, null, function (data, cookie) {
                    if (data == null) return;
                    d = (new RegExp("aa.suggestUpdate\((.*)\)")).exec(data)[1];
                    u = eval(d);
                    var items = [];
                    items.push({
                        'identifier': 'list_' + i,
                        'menu-item': {
                            type: 'one-line-menu-item',
                            accessories: ['arrow'],
                            label: '直接搜索：' + word,
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'yyets.search',
                                        args: [word]
                                    }
                                }
                            }
                        }
                    });
                    r = u["r"];
                    for (var i = 0; i < r.length; i++) {
                        ri = r[i];
                        item = {
                            'identifier': 'list_' + i,
                            'menu-item': {
                                type: 'one-line-menu-item',
                                accessories: ['arrow'],
                                label: ri["c"],
                                'event-handlers': {
                                    select: {
                                        action: 'js-invoke',
                                        parameters: {
                                            function: 'atvuload',
                                            fname: 'yyets.search',
                                            args: [ri["c"]]
                                        }
                                    }
                                }
                            }
                        };
                        items.push(item);
                    };
                    o = {
                        merchant: 'ttvv',
                        'page-type': {
                            'template-name': 'generic-collection',
                            'template-parameters': {
                                'header': {
                                    'type': 'simple-header',
                                    'accessibility-label': 'TTVV',
                                    title: '人人影视'
                                }
                            }
                        },
                        items: items,
                    };
                    atv.loadAndSwapPlist(o);
                });
            };
            textEntry.show();
        },
        test: function (args) {
            url = 'ed2k://|file|%E5%A4%8D%E4%BB%87.Revenge.S01E17.Chi_Eng.WEBRip.720X400-YYeTs%E4%BA%BA%E4%BA%BA%E5%BD%B1%E8%A7%86.rmvb|180627332|75a57fd105c86b3eafc0b8c8c658bffa|h=fvlpoayxxiplyn43ssamg7hzv2muyaki|/';
            atvu.loadAction('xunlei.playurl', '', [url], 1);
        },
        select: function (args) {
            allarea = [
                ['全部', ''],
                ['美国', '美国'],
                ['大陆', '大陆'],
                ['日本', '日本'],
                ['韩国', '韩国'],
                ['英国', '英国'],
                ['香港', '香港'],
                ['台湾', '台湾'],
                ['印度', '印度'],
                ['法国', '法国'],
                ['加拿大', '加拿大'],
                ['西班牙', '西班牙'],
                ['新加坡', '新加坡'],
                ['泰国', '泰国'],
                ['意大利', '意大利'],
                ['德国', '德国'],
                ['俄罗斯', '俄罗斯'],
                ['越南', '越南'],
                ['澳大利亚', '澳大利亚'],
                ['其他', '其他']
            ];
            allcat = [
                ['全部', ''],
                ['动作', '动作'],
                ['战争', '战争'],
                ['剧情', '剧情'],
                ['喜剧', '喜剧'],
                ['生活', '生活'],
                ['偶像', '偶像'],
                ['青春', '青春'],
                ['魔幻', '魔幻'],
                ['科幻', '科幻'],
                ['历史', '历史'],
                ['纪录', '纪录'],
                ['暴力', '暴力'],
                ['血腥', '血腥'],
                ['歌舞', '歌舞'],
                ['恐怖', '恐怖'],
                ['惊悚', '惊悚'],
                ['悬疑', '悬疑'],
                ['古装', '古装'],
                ['史诗', '史诗'],
                ['丧尸', '丧尸'],
                ['爱情', '爱情'],
                ['医务', '医务'],
                ['律政', '律政'],
                ['真人秀', '真人秀'],
                ['励志', '励志'],
                ['体育', '体育'],
                ['谍战', '谍战'],
                ['罪案', '罪案'],
                ['冒险', '冒险'],
                ['动画', '动画'],
                ['科教', '科教'],
                ['西部', '西部'],
                ['枪战', '枪战'],
                ['灾难', '灾难'],
                ['传记', '传记']
            ];
            allformat = [
                ['全部', ''],
                ['RMVB', 'RMVB'],
                ['MP4', 'MP4'],
                ['HR-HDTV', 'HR-HDTV'],
                ['HDTV', 'HDTV'],
                ['DVDSCR', 'DVDSCR'],
                ['DVD', 'DVD'],
                ['720P', '720P'],
                ['1080P', '1080P'],
                ['3D', '3D'],
                ['预告片', '预告片'],
                ['OST', 'OST'],
                ['WEB-DL', 'WEB-DL'],
                ['BD-720P', 'BD-720P'],
                ['BD-1080P', 'BD-1080P']
            ];
            allsort = [
                ['更新', 'update'],
                ['发布', 'pubdate'],
                ['名称', 'name'],
                ['排名', 'rank'],
                ['评分', 'score'],
                ['点击率', 'views']
            ];
            channel = args[0];
            sel = args[1];
            area = args[2] || '';
            category = args[3] || '';
            format = args[4] || '';
            sort = args[5] || '';
            msg = {
                title: '人人影视',
                message: '',
                script: [baseURL + '/main.js'],
                buttons: []
            };
            if (sel == 'area') {
                msg.message = '请选择地区';
                for (var i = 0; i < allarea.length; i++) {
                    dspn = allarea[i][0];
                    keyn = allarea[i][1];
                    msg.buttons.push({
                        label: dspn,
                        script: "atvu.loadAction('yyets.page','',['" + channel + "','" + keyn + "','" + category + "','" + format + "','" + sort + "'],1)"
                    });
                }
            };
            if (sel == 'category') {
                msg.message = '请选择类型';
                for (var i = 0; i < allcat.length; i++) {
                    dspn = allcat[i][0];
                    keyn = allcat[i][1];
                    msg.buttons.push({
                        label: dspn,
                        script: "atvu.loadAction('yyets.page','',['" + channel + "','" + area + "','" + keyn + "','" + format + "','" + sort + "'],1)"
                    });
                }
            };
            if (sel == 'format') {
                msg.message = '请选择格式';
                for (var i = 0; i < allformat.length; i++) {
                    dspn = allformat[i][0];
                    keyn = allformat[i][1];
                    msg.buttons.push({
                        label: dspn,
                        script: "atvu.loadAction('yyets.page','',['" + channel + "','" + area + "','" + category + "','" + keyn + "','" + sort + "'],1)"
                    });
                }
            };
            if (sel == 'sort') {
                msg.message = '请选择排序方式';
                for (var i = 0; i < allsort.length; i++) {
                    dspn = allsort[i][0];
                    keyn = allsort[i][1];
                    msg.buttons.push({
                        label: dspn,
                        script: "atvu.loadAction('yyets.page','',['" + channel + "','" + area + "','" + category + "','" + format + "','" + keyn + "'],1)"
                    });
                }
            };
            atv.loadAndSwapXML(atvu.showMessage(msg));
        },
        page: function (args) {
            channel = args[0];
            area = args[1] || '';
            category = args[2] || '';
            format = args[3] || '';
            sort = args[4] || '';
            pageno = args[5] || 1;
            params = [];
            if (pageno != 1) params.push("page=" + pageno);
            params.push("channel=" + channel);
            params.push('area=' + encodeURIComponent(area));
            params.push('category=' + encodeURIComponent(category));
            params.push('format=' + encodeURIComponent(format));
            params.push('sort=' + encodeURIComponent(sort));
            url = 'http://www.yyets.com/php/resourcelist?' + params.join('&');
            atvu.ajax(url, "GET", null, null, function (d, c) {
                if (d == null) return;
                pos1 = d.indexOf('<ul class="boxPadd dashed">');
                pos2 = d.indexOf('</ul>', pos1);
                itemstr = d.substring(pos1, pos2);
                itemlist = itemstr.split('<li class="clearfix">');
                items = [];
                shelfs = [];
                act = "atvu.loadAction('yyets.select','',['" + channel + "','area','" + area + "','" + category + "','" + format + "','" + sort + "'])";
                if (area == '') st = '全部';
                else st = area;
                item = '<actionButton id="yyets_item_area" onSelect="' + act + ';" onPlay="' + act + ';"><title>地区</title><subtitle>' + st + '</subtitle></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.select','',['" + channel + "','category','" + area + "','" + category + "','" + format + "','" + sort + "'])";
                if (category == '') st = '全部';
                else st = category;
                item = '<actionButton id="yyets_item_category" onSelect="' + act + ';" onPlay="' + act + ';"><title>类型</title><subtitle>' + st + '</subtitle></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.select','',['" + channel + "','format','" + area + "','" + category + "','" + format + "','" + sort + "'])";
                if (format == '') st = '全部';
                else st = format;
                item = '<actionButton id="yyets_item_format" onSelect="' + act + ';" onPlay="' + act + ';"><title>格式</title><subtitle>' + st + '</subtitle></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.select','',['" + channel + "','sort','" + area + "','" + category + "','" + format + "','" + sort + "'])";
                if (sort == '') st = '更新';
                if (sort == 'update') st = '更新';
                if (sort == 'pubdate') st = '发布';
                if (sort == 'name') st = '名称';
                if (sort == 'rank') st = '排名';
                if (sort == 'score') st = '评分';
                if (sort == 'views') st = '点击率';
                item = '<actionButton id="yyets_item_sort" onSelect="' + act + ';" onPlay="' + act + ';"><title>排序</title><subtitle>' + st + '</subtitle></actionButton>';
                items.push(item);
                shelfs.push('<grid id="shelf_' + shelfs.length + '" columnCount="4"><items>' + items.join("\n") + '</items></grid>');
                items = [];
                for (var i = 1; i < itemlist.length; i++) {
                    rs = (new RegExp('<a target="_blank" class="imglink" href="http://www.yyets.com/php/resource/(\\d+)"><img src="([^"]*)"></a>')).exec(itemlist[i]);
                    if (!rs) continue;
                    itemid = rs[1];
                    imgurl = rs[2];
                    rsn = (new RegExp('<dt><a target="_blank" href="http://www.yyets.com/php/resource/\\d+">【([^<]*)】<strong>《([^<]*)》([^<]*)</strong></a>([^<]*)</dt>')).exec(itemlist[i]);
                    if (!rsn) continue;
                    ty = rsn[1];
                    name = rsn[2];
                    if (rsn[3] != '') info = rsn[3];
                    else info = rsn[4];
                    act = "atvu.loadAction('yyets.entry','" + xmlchar(name) + "',['" + itemid + "','" + xmlchar(name) + "'])";
                    acth = "atvu.saveAction('yyets.entry','" + xmlchar(name) + "',['" + itemid + "','" + xmlchar(name) + "'],'" + xmlchar(imgurl) + "','" + xmlchar("http://www.yyets.com/php/resource/" + itemid) + "')";
                    item = '<moviePoster id="shelf_item_' + items.length + '" accessibilityLabel="" alwaysShowTitles="true" onSelect="' + act + ';" onPlay="' + act + ';" onHoldSelect="' + acth + ';"><title>' + xmlchar(name) + '</title><subtitle>' + xmlchar(info) + '</subtitle><image>' + xmlchar(imgurl) + '</image><defaultImage>resource://Poster.png</defaultImage></moviePoster>';
                    items.push(item);
                };
                shelfs.push('<collectionDivider alignment="left" accessibilityLabel=""><title>人人影视(长按中间键加入收藏)</title></collectionDivider>');
                shelfs.push('<grid id="shelf_' + shelfs.length + '" columnCount="7"><items>' + items.join("\n") + '</items></grid>');
                items = [];
                pos1 = d.indexOf('<div class="pages">');
                pos2 = d.indexOf('</div></div>', pos1);
                if (pos1 > 0 && pos2 > 0) {
                    pagestr = d.substring(pos1, pos2);
                    pagelist = atvu.findall("a href='\\?page=(\\d+)&[^']*'>([^<]*)", pagestr);
                    for (var i = 0; i < pagelist.length; i++) {
                        act = "atvu.loadAction('yyets.page','',['" + channel + "','" + area + "','" + category + "','" + format + "','" + sort + "'," + pagelist[i][0] + "])";
                        if (pagelist[i][1] == '下一页') {
                            item = '<actionButton id="yyets_page_next" onSelect="' + act + ';" onPlay="' + act + ';"><title>' + xmlchar(pagelist[i][1]) + '</title></actionButton>';
                            items.unshift(item);
                        } else {
                            item = '<actionButton id="yyets_page_' + pagelist[i][0] + '" onSelect="' + act + ';" onPlay="' + act + ';"><title>' + xmlchar(pagelist[i][1]) + '</title></actionButton>';
                            items.push(item);
                        }
                    };
                    shelfs.push('<grid id="shelf_' + shelfs.length + '"><items>' + items.join("\n") + '</items></grid>');
                };
                xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><scroller id="com.atvttvv.yyets.list"><items>' + shelfs.join("\n") + '</items></scroller></body></atv>';
                atv.loadAndSwapXML(atv.parseXML(xml));
            });
        },
        list: function (args) {
            url = "http://www.yyets.com/php/resourcelist";
            atvu.ajax(url, "GET", null, null, function (d, c) {
                if (d == null) return;
                pos1 = d.indexOf('<ul class="likeRes top_list2 dashed clearfix">');
                pos2 = d.indexOf('</ul>', pos1);
                topstr = d.substring(pos1, pos2);
                toplist = topstr.split('<li class="clearfix">');
                items = [];
                shelfs = [];
                act = "atvu.loadAction('yyets.page','',['movie'])";
                item = '<actionButton id="yyets_item_movie" onSelect="' + act + ';" onPlay="' + act + ';"><title>电影</title></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.page','',['tv'])";
                item = '<actionButton id="yyets_item_tv" onSelect="' + act + ';" onPlay="' + act + ';"><title>电视剧</title></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.page','',['documentary'])";
                item = '<actionButton id="yyets_item_documentary" onSelect="' + act + ';" onPlay="' + act + ';"><title>纪录片</title></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.page','',['openclass'])";
                item = '<actionButton id="yyets_item_openclass" onSelect="' + act + ';" onPlay="' + act + ';"><title>公开课</title></actionButton>';
                items.push(item);
                act = "atvu.loadAction('yyets.searchmain','',[])";
                item = '<actionButton id="yyets_item_search" onSelect="' + act + ';" onPlay="' + act + ';"><title>搜索</title></actionButton>';
                items.push(item);
                shelfs.push('<grid id="shelf_' + shelfs.length + '" columnCount="5"><items>' + items.join("") + '</items></grid>');
                items = [];
                for (var i = 1; i < toplist.length; i++) {
                    rs = (new RegExp('<a href="http://www.yyets.com/php/resource/(\\d+)" class="imglink" target="_blank"><img src="([^"]*)" /></a>')).exec(toplist[i]);
                    if (!rs) continue;
                    itemid = rs[1];
                    imgurl = rs[2];
                    rsn = (new RegExp('<div><a href="http://www.yyets.com/php/resource/\\d+" target="_blank"><strong>《([^<]*)》</strong></a></div>')).exec(toplist[i]);
                    if (!rsn) continue;
                    name = rsn[1];
                    info = '';
                    act = "atvu.loadAction('yyets.entry','" + argw(name) + "',['" + itemid + "','" + argw(name) + "'])";
                    acth = "atvu.saveAction('yyets.entry','" + argw(name) + "',['" + itemid + "','" + argw(name) + "'],'" + xmlchar(imgurl) + "','" + xmlchar("http://www.yyets.com/php/resource/" + itemid) + "')";
                    item = '<moviePoster id="shelf_item_' + items.length + '" accessibilityLabel="" alwaysShowTitles="true" onSelect="' + act + ';" onPlay="' + act + ';" onHoldSelect="' + acth + ';"><title>' + xmlchar(name) + '</title><subtitle>' + xmlchar(info) + '</subtitle><image>' + xmlchar(imgurl) + '</image><defaultImage>resource://Poster.png</defaultImage></moviePoster>';
                    items.push(item);
                };
                shelfs.push('<collectionDivider alignment="left" accessibilityLabel=""><title>人人影视(长按中间键加入收藏)</title></collectionDivider>');
                shelfs.push('<grid id="shelf_' + shelfs.length + '" columnCount="7"><items>' + items.join("") + '</items></grid>');
                xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><scroller id="com.atvttvv.yyets.list"><items>' + shelfs.join("") + '</items></scroller></body></atv>';
                atv.loadAndSwapXML(atv.parseXML(xml));
            });
        },
        search: function (args) {
            word = args[0];
            url = "http://www.yyets.com/php/search/index?keyword=" + encodeURIComponent(word) + "&type=resource&order=uptime";
            atvu.ajax(url, "GET", null, null, function (data, cookie) {
                if (data == null) return;
                divs = data.split('<div class="all_search_li1 f_l">');
                if (divs.length == 1) {
                    o = {
                        merchant: 'ttvv',
                        'page-type': {
                            'template-name': 'generic-collection',
                            'template-parameters': {
                                'header': {
                                    'type': 'simple-header',
                                    'accessibility-label': 'TTVV',
                                    title: '人人影视'
                                }
                            }
                        },
                        items: []
                    };
                    atv.loadAndSwapPlist(o);
                    return;
                };
                var items = [];
                for (var i = 1; i < divs.length; i++) {
                    rs = (new RegExp('<span class="bnts_e18 e18add">(.*?)</span>')).exec(divs[i]);
                    rs1 = (new RegExp('<a href="http://www.yyets.com/php/resource/(\\d+)" target="_blank"><strong class="f14 list_title">《(.*?)》(.*?)</strong>(.*?)</a>')).exec(divs[i]);
                    if (rs) {
                        name = rs1[2];
                        itemid = rs1[1];
                        rem = rs1[4], ename = rs1[3];
                        img = '';
                        ty = rs[1];
                        act = "atvu.loadAction('yyets.entry','" + xmlchar(name) + "',['" + itemid + "','" + xmlchar(name) + "'])";
                        acth = "atvu.saveAction('yyets.entry','" + xmlchar(name) + "',['" + itemid + "','" + xmlchar(name) + "'],'','" + xmlchar("http://www.yyets.com/php/resource/" + itemid) + "')";
                        item = '<twoLineEnhancedMenuItem id="list+_' + i + '" accessibilityLabel="" onSelect="' + act + '" onHoldSelect="' + acth + '"><label>' + xmlchar(name) + '</label><label2>' + ty + '</label2><rightLabel>' + xmlchar(rem) + '</rightLabel><accessories><arrow /></accessories></twoLineEnhancedMenuItem>';
                        items.push(item);
                    }
                };
                xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><listWithPreview id="com.atvttvv.yyets.search"><header><simpleHeader><title>人人影视</title><subtitle>长按中间键加入收藏</subtitle></simpleHeader></header><menu><sections><menuSection><items>' + items.join("\n") + '</items></menuSection></sections></menu></listWithPreview></body></atv>';
                atv.loadAndSwapXML(atv.parseXML(xml));
            });
        },
        entry: function (args) {
            itemid = args[0];
            name = args[1];
            url = "http://www.yyets.com/php/resource/" + itemid;
            atvu.ajax(url, "GET", null, null, function (data, cookie) {
                if (data == null) return;
                rs = atvu.findall('<li[^>]*format="([^"]*)"><a class="f_out">([^<]*)<span class="f_in"></span></a>', data);
                formats = [];
                seasons = {};
                for (var i = 0; i < rs.length; i++) {
                    seasons[rs[i][0]] = [
                        [], {}];
                    if (rs[i][0] == 'all') continue;
                    formats.push(rs[i][0]);
                };
                rs = atvu.findall('<li format="([^"]*)" season="([^"]*)"><a>([^<]*)</a></li>', data);
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
                atv.localStorage['yyetss'] = seasons;
                atv.localStorage['yyetsd'] = {
                    status: status,
                    imgurl: imgurl,
                    year: year,
                    ty: ty,
                    region: region,
                    corp: corp,
                    lang: lang,
                    pubdate: pubdate,
                    eng: eng,
                    imdb: imdb,
                    desc: desc
                };
                atvu.loadAction('yyets.showlinks', '', [name, ''], 1);
            });
        },
        listformat: function (args) {
            name = args[0];
            seasons = atv.localStorage['yyetss'];
            msg = {
                title: '人人影视',
                message: '请选择视频格式',
                script: [baseURL + '/main.js'],
                buttons: [{
                    label: '全部',
                    script: "atvu.loadAction('yyets.showlinks','',['" + name + "','all'],1)"
                }]
            };
            for (f in seasons) {
                if (f == 'all') continue;
                msg.buttons.push({
                    label: f,
                    script: "atvu.loadAction('yyets.showlinks','',['" + name + "','" + f + "'],1)"
                })
            };
            atv.loadAndSwapXML(atvu.showMessage(msg));
        },
        listseason: function (args) {
            name = args[0];
            format = args[1];
            seasons = atv.localStorage['yyetss'];
            msg = {
                title: '人人影视',
                message: '请选择',
                script: [baseURL + '/main.js'],
                buttons: []
            };
            for (var i = 0; i < seasons[format][0].length; i++) {
                season = seasons[format][0][i];
                msg.buttons.push({
                    label: seasons[format][1][season][0],
                    script: "atvu.loadAction('yyets.showlinks','',['" + name + "','" + format + "','" + season + "'],1)"
                })
            };
            atv.loadAndSwapXML(atvu.showMessage(msg));
        },
        osearch: function (args) {
            name = args[0];
            msg = {
                title: '人人影视',
                message: '请选择搜索引擎',
                script: [baseURL + '/main.js'],
                buttons: [{
                    label: '电驴大全',
                    script: "atvu.loadAction('verycd.search','',['" + name + "'],1)"
                }, {
                    label: '搜库',
                    script: "atvu.loadAction('soku.search','',['" + name + "'],1)"
                }]
            };
            atv.loadAndSwapXML(atvu.showMessage(msg));
        },
        entrynav: function (args) {
            format = args[0];
            event = args[1];
            seasons = atv.localStorage['yyetss'];
            detail = atv.localStorage['yyetsd'];
            season = document.getElementById(event.navigationItemId).getAttribute('accessibilityLabel');
            fs = seasons[format];
            sn = fs[1][season][0];
            var allitems = document.rootElement.getElementsByTagName('twoLineEnhancedMenuItem');
            for (var i = 0; i < allitems.length; i++) allitems[i].removeFromParent();
            var menu = document.getElementById('menu_item');
            var titem;
            var ii = 2;
            if (format == 'all') {
                for (f in seasons) {
                    try {
                        li = seasons[f][1][season][1];
                    } catch (e) {
                        li = null;
                    };
                    if (li) {
                        for (var i = 0; i < li.length; i++) {
                            name = li[i][0];
                            size = li[i][1];
                            link = li[i][2];
                            sstr = '<twoLineEnhancedMenuItem id="yyeplist_' + ii + '" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.playurl\',\'\',[\'' + xmlchar(link) + '\',\'' + xmlchar(name) + '\']);"><label><![CDATA[' + name + ']]></label><label2>' + f + '(' + size + ')</label2><image></image></twoLineEnhancedMenuItem>';
                            ii++;
                            titem = atv.parseXML(sstr).rootElement;
                            titem.removeFromParent();
                            menu.appendChild(titem);
                        }
                    }
                }
            } else {
                try {
                    li = seasons[format][1][season][1];
                } catch (e) {
                    li = null;
                };
                if (li) {
                    allnames = [];
                    for (var i = 0; i < li.length; i++) allnames.push(li[i][0]);
                    namebase = atvu.reducename(allnames);
                    if (namebase.length > 3) namebase = namebase.substring(0, namebase.length - 3);
                    for (var i = 0; i < li.length; i++) {
                        name = li[i][0];
                        nname = name;
                        if (namebase.length > 0) {
                            if (nname.substring(0, namebase.length) == namebase) nname = nname.substring(namebase.length);
                        };
                        size = li[i][1];
                        link = li[i][2];
                        sstr = '<twoLineEnhancedMenuItem id="yyeplist_' + ii + '" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.playurl\',\'\',[\'' + xmlchar(xmlchar(link)) + '\',\'' + xmlchar(name) + '\']);"><label><![CDATA[' + nname + ']]></label><label2>' + format + '(' + size + ')' + namebase + '</label2><image></image></twoLineEnhancedMenuItem>';
                        ii++;
                        titem = atv.parseXML(sstr).rootElement;
                        titem.removeFromParent();
                        menu.appendChild(titem);
                    }
                }
            }
        },
        showlinks: function (args) {
            vname = args[0];
            format = args[1];
            season = args[2];
            seasons = atv.localStorage['yyetss'];
            detail = atv.localStorage['yyetsd'];
            if (format == '') {
                if (seasons['HR-HDTV']) format = 'HR-HDTV';
                else if (seasons['RMVB']) format = 'RMVB';
                else format = 'all';
            };
            fs = seasons[format];
            if (!season) {
                season = fs[0][0];
            };
            sn = fs[1][season][0];
            items = [];
            sstr = '<oneLineMenuItem id="yysearch" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.osearch\',\'\',[\'' + argw(vname) + '\']);"><label>搜索在线视频</label><preview><keyedPreview><title>' + xmlchar(vname) + '</title><summary>' + xmlchar(detail.desc) + '</summary><image>' + xmlchar(detail.imgurl) + '</image><footnote>' + xmlchar(detail.status) + '</footnote></keyedPreview></preview><accessories><arrow /></accessories></oneLineMenuItem>';
            items.push(sstr);
            fn = format;
            if (format == 'all') fn = '全部';
            sstr = '<oneLineMenuItem id="yyformat" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.listformat\',\'\',[\'' + argw(vname) + '\'],1);"><label>格式</label><rightLabel>' + fn + '</rightLabel></oneLineMenuItem>';
            items.push(sstr);
            nitems = [];
            if (fs[0].length > 1) {
                for (var i = 0; i < fs[0].length; i++) {
                    nitems.push('<navigationItem id="nav_' + i + '" accessibilityLabel="' + argw(fs[0][i]) + '"><title>' + xmlchar(fs[1][fs[0][i]][0]) + '</title></navigationItem>');
                }
            };
            if (format == 'all') {
                for (f in seasons) {
                    try {
                        li = seasons[f][1][season][1];
                    } catch (e) {
                        li = null;
                    };
                    if (li) {
                        for (var i = 0; i < li.length; i++) {
                            name = li[i][0];
                            size = li[i][1];
                            link = li[i][2];
                            sstr = '<twoLineEnhancedMenuItem id="yyeplist_' + items.length + '" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.playurl\',\'\',[\'' + xmlchar(link) + '\',\'' + xmlchar(name) + '\']);"><label><![CDATA[' + name + ']]></label><label2>' + f + '(' + size + ')</label2><image></image></twoLineEnhancedMenuItem>';
                            items.push(sstr);
                        }
                    }
                }
            } else {
                try {
                    li = seasons[format][1][season][1];
                } catch (e) {
                    li = null;
                };
                if (li) {
                    allnames = [];
                    for (var i = 0; i < li.length; i++) allnames.push(li[i][0]);
                    namebase = atvu.reducename(allnames);
                    if (namebase.length > 3) namebase = namebase.substring(0, namebase.length - 3);
                    for (var i = 0; i < li.length; i++) {
                        name = li[i][0];
                        nname = name;
                        if (namebase.length > 0) {
                            if (nname.substring(0, namebase.length) == namebase) nname = nname.substring(namebase.length);
                        };
                        size = li[i][1];
                        link = li[i][2];
                        sstr = '<twoLineEnhancedMenuItem id="yyeplist_' + items.length + '" accessibilityLabel="" onSelect="atvu.loadAction(\'yyets.playurl\',\'\',[\'' + xmlchar(xmlchar(link)) + '\',\'' + xmlchar(name) + '\']);"><label><![CDATA[' + nname + ']]></label><label2>' + format + '(' + size + ')' + xmlchar(namebase) + '</label2><image></image></twoLineEnhancedMenuItem>';
                        items.push(sstr);
                    }
                }
            };
            if (nitems.length == 0) xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><listWithPreview id="com.atvttvv.yyets.entry"><header><simpleHeader><title>' + xmlchar(vname) + '</title></simpleHeader></header><preview><keyedPreview><title>' + xmlchar(vname) + '</title><summary>' + xmlchar(detail.desc) + '</summary><image>' + xmlchar(detail.imgurl) + '</image><footnote>' + xmlchar(detail.status) + '</footnote></keyedPreview></preview><menu><sections><menuSection><items id="menu_item">' + items.join("\n") + '</items></menuSection></sections></menu></listWithPreview></body></atv>';
            else xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><listByNavigation id="com.atvttvv.yyets.entry" onNavigate="atvu.loadAction(\'yyets.entrynav\',\'\',[\'' + format + '\',event],1);"><header><tumblerWithSubtitle><subtitle>' + xmlchar(vname) + '</subtitle></tumblerWithSubtitle></header><navigation currentIndex="0">' + nitems.join('') + '</navigation><menu><sections><menuSection><items id="menu_item">' + items.join("\n") + '</items></menuSection></sections></menu></listByNavigation></body></atv>';
            atv.loadAndSwapXML(atv.parseXML(xml));
        },
        playurl: function (args) {
            url = args[0];
            filename = args[1] || '';
            atvu.loadAction('xunlei.playurl', '', [url, '', filename], 1);
            return;
            filename = args[1];
            fileparts = filename.split(".");
            fn = [];
            sub = 0;
            for (var i = 0; i < fileparts.length; i++) {
                fp = fileparts[i].toLowerCase();
                if (fp == 'chi_eng') {
                    sub = 1;
                    break;
                };
                if (fp == 'hdtv' || fp == '720p' || fp == '1080p') break;
                fn.push(fileparts[i]);
                if (/s\d\de\d\d/.exec(fp)) {
                    break;
                }
            };
            sword = fn.join(".");
            logger.debug(sword);
            if (sword.length > 0 && sub == 0) {
                surl = "http://www.yyets.com/php/search/index/?keyword=" + encodeURIComponent(sword) + "&type=subtitle";
                atvu.ajax(surl, "GET", null, null, function (d, c) {
                    rs = (new RegExp('href="http://www.yyets.com/php/subtitle/(\\d+)"')).exec(d);
                    if (rs) {
                        sid = rs[1];
                        atvu.loadAction('xunlei.playurl', '', [url], 1);
                    } else atvu.loadAction('xunlei.playurl', '', [url], 1);
                });
            } else atvu.loadAction('xunlei.playurl', '', [url], 1);
        },
    }
};