function __init() {
    return {
        "version": 9,
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
                                        fname: 'soku.search',
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
                                            fname: 'soku.search',
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
                                    title: '搜库搜索'
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
        searchmore: function (args) {
            word = args[0];
            tl = args[1];
            url = "http://www.soku.com/v?keyword=" + encodeURIComponent(word) + "&time_length=" + tl;
            atvu.ajax(url, "GET", null, null, function (data, cookie) {
                if (data == null) return;
                uls = data.split('<ul class="v">');
                items = [];
                for (var i = 1; i < uls.length; i++) {
                    rs = (new RegExp('<li class="v_link"><a title="([^"]*)" target="_blank" href="([^"]*)"')).exec(uls[i]);
                    if (!rs) continue;
                    name = rs[1];
                    url = rs[2];
                    img = (new RegExp('<img alt="[^"]*" src="([^"]*)"')).exec(uls[i])[1];
                    try {
                        mlength = (new RegExp('<li class="v_time"><span class="num">([^<]*)</span>')).exec(uls[i])[1];
                    } catch (e) {
                        mlength = '';
                    };
                    src = (new RegExp('<li class="v_user"><label>来源:</label><span[^>]*>([^<]*)</span></li>')).exec(uls[i])[1];
                    item = {
                        'identifier': 'list_' + i,
                        'menu-item': {
                            type: 'two-line-enhanced-menu-item',
                            accessories: ['arrow'],
                            image: {
                                image: img
                            },
                            label: name,
                            label2: mlength,
                            'right-label': src,
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'soku.playurl',
                                        args: [url, name]
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
                                title: '搜库搜索'
                            }
                        }
                    },
                    items: items
                };
                atv.loadAndSwapPlist(o);
            });
        },
        search: function (args) {
            word = args[0];
            url = "http://www.soku.com/v?keyword=" + encodeURIComponent(word);
            atvu.ajax(url, "GET", null, null, function (data, cookie) {
                if (data == null) return;
                divs = data.split('<div class="detail">');
                var items = [];
                for (var i = 1; i < divs.length; i++) {
                    rs = (new RegExp('<h1>\\s+<a href="/detail/show/([^"]*)"[^>]+>(.*?)</a>\\s+</h1>\\s+</li>\\s+<li[^>]*>(.*?)</li>\\s+<li[^>]*>(.*?)</li>')).exec(divs[i]);
                    if (rs == null) rs = (new RegExp('<h1>\\s+<a href="([^"]*)"[^>]+>(.*?)</a>\\s+</h1>\\s+</li>\\s+<li[^>]*>(.*?)</li>\\s+<li[^>]*>(.*?)</li>')).exec(divs[i]);
                    if (rs == null) {
                        uls = divs[i].split('<ul class="p psv">');
                        for (var j = 1; j < uls.length; j++) {
                            urs = (new RegExp('<li class="p_link">\\s*<a href="([^"]*)".*?title="([^"]*)"[^>]+>')).exec(uls[j]);
                            if (!url) continue;
                            name = urs[2];
                            link = urs[1];
                            lrs = (new RegExp('/detail/show/([^"]*)')).exec(link);
                            if (!lrs) itemid = link;
                            else
                                itemid = lrs[1];
                            img = (new RegExp('<li class="p_thumb">\\s*<img class="" src="([^"]*)"')).exec(uls[j])[1];
                            img = img.replace(new RegExp('^\\s*', 'g'), "");
                            desc = (new RegExp('<div class="intro">([\\s\\S]*?)</div>')).exec(uls[j])[1];
                            desc = sh(desc.replace(/[\n\t]/g, "").replace(new RegExp('^\\s*', 'g'), ""));
                            year = '';
                            ty = '';
                            srss = (new RegExp('<label>播放源:</label>([\\s\\S]*?)</div>')).exec(uls[j]);
                            if (!srss) continue;
                            srcs = atvu.findall('<span  name="([^"]*)".*?<a href="([^"]*)".*?status="([^"]*)".*?stype="([^"]*)".*?>', srss[1]);
                            rs = [];
                            for (var k = 0; k < srcs.length; k++) {
                                rs.push([srcs[k][0], srcs[k][2], srcs[k][3], srcs[k][1]]);
                            };
                            item = {
                                'identifier': 'list_' + i,
                                'menu-item': {
                                    type: 'two-line-enhanced-menu-item',
                                    accessories: ['arrow'],
                                    image: {
                                        image: img
                                    },
                                    label: name,
                                    label2: ty,
                                    'right-label': year,
                                    'event-handlers': {
                                        select: {
                                            action: 'js-invoke',
                                            parameters: {
                                                function: 'atvuload',
                                                fname: 'soku.item',
                                                args: [itemid, name, year, ty, img, desc, rs]
                                            }
                                        }
                                    }
                                }
                            };
                            items.push(item);
                        }
                    } else {
                        name = sh(rs[2]);
                        itemid = rs[1];
                        year = rs[3];
                        ty = sh(rs[4]);
                        img = (new RegExp('<li class="p_thumb"><img class="" src="([^"]*)"')).exec(divs[i])[1];
                        img = img.replace(new RegExp('^\\s*', 'g'), "");
                        desc = (new RegExp('<div class="intro">([\\s\\S]*?)</div>')).exec(divs[i])[1];
                        desc = sh(desc);
                        rs = atvu.findall('<span  name="([^"]*)".*?><a.*?status="([^"]*)".*?stype="([^"]*)".*?>', divs[i]);
                        if (itemid.substring(0, 7) == 'http://') {
                            if (divs[i].indexOf('<li class="p_ispaid">') >= 0) ty = ty + "(收费)";
                            rs = (new RegExp('<span  name="优酷"[^>]*><a href="([^"]+)"')).exec(divs[i]);
                            url = rs[1];
                            item = {
                                'identifier': 'list_' + i,
                                'menu-item': {
                                    type: 'two-line-enhanced-menu-item',
                                    accessories: ['arrow'],
                                    image: {
                                        image: img
                                    },
                                    label: name,
                                    label2: ty,
                                    'right-label': year,
                                    'event-handlers': {
                                        select: {
                                            action: 'js-invoke',
                                            parameters: {
                                                function: 'atvuload',
                                                fname: 'soku.playurl',
                                                args: [url, name]
                                            }
                                        }
                                    }
                                }
                            };
                        } else
                            item = {
                                'identifier': 'list_' + i,
                                'menu-item': {
                                    type: 'two-line-enhanced-menu-item',
                                    accessories: ['arrow'],
                                    image: {
                                        image: img
                                    },
                                    label: name,
                                    label2: ty,
                                    'right-label': year,
                                    'event-handlers': {
                                        select: {
                                            action: 'js-invoke',
                                            parameters: {
                                                function: 'atvuload',
                                                fname: 'soku.item',
                                                args: [itemid, name, year, ty, img, desc, rs]
                                            }
                                        }
                                    }
                                }
                        };
                        items.push(item);
                    }
                };
                pos = data.indexOf('<ul class="v">');
                if (pos >= 0) {
                    items.push({
                        'identifier': 'list_more',
                        'menu-item': {
                            type: 'one-line-menu-item',
                            label: '----更多内容----'
                        }
                    });
                    items.push({
                        'identifier': 'list_tl_1',
                        'menu-item': {
                            type: 'one-line-menu-item',
                            label: '60分钟以上',
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'soku.searchmore',
                                        args: [word, 4]
                                    }
                                }
                            }
                        }
                    });
                    items.push({
                        'identifier': 'list_tl_1',
                        'menu-item': {
                            type: 'one-line-menu-item',
                            label: '30-60分钟',
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'soku.searchmore',
                                        args: [word, 3]
                                    }
                                }
                            }
                        }
                    });
                    items.push({
                        'identifier': 'list_tl_1',
                        'menu-item': {
                            type: 'one-line-menu-item',
                            label: '10-30分钟',
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'soku.searchmore',
                                        args: [word, 2]
                                    }
                                }
                            }
                        }
                    });
                    items.push({
                        'identifier': 'list_tl_1',
                        'menu-item': {
                            type: 'one-line-menu-item',
                            label: '0-10分钟',
                            'event-handlers': {
                                select: {
                                    action: 'js-invoke',
                                    parameters: {
                                        function: 'atvuload',
                                        fname: 'soku.searchmore',
                                        args: [word, 1]
                                    }
                                }
                            }
                        }
                    });
                };
                o = {
                    merchant: 'ttvv',
                    'page-type': {
                        'template-name': 'generic-collection',
                        'template-parameters': {
                            'header': {
                                'type': 'simple-header',
                                'accessibility-label': 'TTVV',
                                title: '搜库搜索'
                            }
                        }
                    },
                    items: items
                };
                atv.loadAndSwapPlist(o);
            });
        },
        item: function (args) {
            itemid = args[0];
            name = args[1];
            year = args[2];
            ty = args[3];
            img = args[4];
            desc = args[5];
            sitelist = args[6];
            links = [];
            other = '';
            for (var i = 0; i < sitelist.length; i++) {
                pname = sitelist[i][0];
                status = sitelist[i][1];
                if (sitelist[i][2] == "HD" || sitelist[i][2] == "SD") hd = '<badge>HD</badge>';
                else hd = ''; if (!sitelist[i][3]) {
                    act = "atvu.loadAction('soku.detail','(" + pname + ")" + name + "',['" + itemid + "','" + pname + "'])";
                    acth = "atvu.saveAction('soku.detail','(" + pname + ")" + name + "',['" + itemid + "','" + pname + "'],'','" + xmlchar("http://www.soku.com/detail/show/" + itemid) + "')";
                } else {
                    act = "atvu.loadAction('soku.playurl','(" + pname + ")" + name + "',['" + xmlchar(sitelist[i][3]) + "','" + pname + "'])";
                    acth = "atvu.saveAction('soku.playurl','(" + pname + ")" + name + "',['" + xmlchar(sitelist[i][3]) + "','" + pname + "'],'','" + xmlchar(sitelist[i][3]) + "')";
                }; if (pname == '土豆') other = '<actionButton id="play_' + i + '" onSelect="' + act + '" onHoldSelect="' + acth + '"><title>' + pname + '</title><subtitle>' + status + '</subtitle><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage>' + hd + '</actionButton>';
                else
                    links.push('<actionButton id="play_' + i + '" onSelect="' + act + '" onHoldSelect="' + acth + '"><title>' + pname + '</title><subtitle>' + status + '</subtitle><image>resource://Play.png</image><focusedImage>resource://PlayFocused.png</focusedImage>' + hd + '</actionButton>');
            };
            if (other != '') links.push(other);
            if (links.length == 0) links.push('<actionButton id="play_0"><title>暂无</title></actionButton>');
            xml = '<atv><head><script src="' + baseURL + '/main.js" /></head><body><itemDetail id="com.atvttvv.soku.item"><title>' + xmlchar(name) + '</title><subtitle>' + year + '(长按中间键加入收藏)</subtitle><summary>' + xmlchar(desc) + '</summary><image style="moviePoster">' + img + '</image><defaultImage>resource://Poster.png</defaultImage><footnote></footnote><centerShelf><shelf id="centerShelf" columnCount="' + links.length + '" center="true"><sections><shelfSection><items>' + links.join("\n") + '</items></shelfSection></sections></shelf></centerShelf></itemDetail></body></atv>';
            atv.loadAndSwapXML(atv.parseXML(xml));
        },
        detail: function (args) {
            itemid = args[0];
            pname = args[1];
            url = "http://www.soku.com/detail/show/" + itemid;
            atvu.ajax(url, "GET", null, null, function (data, cookie) {
                var slist = atvu.findall('<li.*?name="([^"]*)">\\s+<img.*?>\\s+<label.*?title="([^"]*)".*?stype="([^"]*)" id="([^"]*)"[^>]+>(.*?)</label>', data);
                namer = (new RegExp('<ul class="base">\\s*<li class="base_name"><h1>(.*?)</h1></li>\\s*<li class="base_pub">(.*?)</li>')).exec(data);
                name = namer[1];
                year = namer[2];
                res = '<li class="p_thumb"><img class="" src="([^"]*)" alt="' + name + '" /></li>';
                img = (new RegExp(res)).exec(data)[1];
                var links = [];
                for (var i = 0; i < slist.length; i++) {
                    if ((slist[i][4]) == pname) {
                        src = slist[i][0];
                        site = slist[i][3]
                        break;
                    }
                };
                var rs;
                if (slist.length == 0) rs = atvu.findall('<a href="([^"]*)"[^>]*site="[^"]*"[^>]+>(.*?)</a>', data);
                else
                    rs = atvu.findall('<a href="([^"]*)"[^>]*site="' + src + '"[^>]+>(.*?)</a>', data); if (rs.length == 0) {
                    if (slist.length == 0) {
                        rs = atvu.findall('<div class=\'linkpanels .*?\'[^>]*>\\s*<ul[^>]*>\\s*<li><a href="([^"]*)"', data);
                        atvu.loadAction('soku.playurl', '', [rs[0][0], name], 1);
                        return;
                    } else {
                        rs = atvu.findall('<div class=\'linkpanels ' + site + '\'[^>]*>\\s*<ul[^>]*>\\s*<li><a href="([^"]*)"', data);
                        if (rs.length > 0) {
                            atvu.loadAction('soku.playurl', '', [rs[0][0], name], 1);
                            return;
                        } else {
                            rs = (new RegExp("<div class='linkpanels " + site + "'([\\s\\S]*?)</div>")).exec(data);
                            if (!rs) {
                                atv.loadAndSwapXML(atvu.showMessage({
                                    title: '搜库',
                                    message: '页面解析错误'
                                }));
                                return;
                            };
                            rs = atvu.findall('<a href="([^"]*)"[^>]+>(.*?)</a>', rs[1]);
                        }
                    }
                };
                if (pname == "爱奇艺") {
                    atvu.loadAction('qiyi.playurl', '', [rs[0][0]], 1);
                    return;
                };
                if (rs.length == 1) {
                    atvu.loadAction('soku.playurl', '', [rs[0][0], name + rs[0][1]], 0);
                    return
                };
                shelfs = [];
                items = [];
                for (var i = 0; i < rs.length; i++) {
                    l = rs[i];
                    act = "atvu.loadAction('soku.playurl','',['" + xmlchar(l[0]) + "','" + name + xmlchar(l[1]) + "'])";
                    acth = "atvu.loadAction('soku.playurl','',['" + xmlchar(l[0]) + "','" + name + xmlchar(l[1]) + "',1])";
                    item = '<actionButton id="shelf_page_' + i + '" onSelect="' + act + ';" onPlay="' + act + ';" onHoldSelect="' + acth + ';"><title>' + xmlchar(l[1]) + '</title></actionButton>';
                    items.push(item);
                };
                shelfs.push('<collectionDivider alignment="left" accessibilityLabel=""><title>' + name + '(' + pname + '):' + items.length + '</title></collectionDivider>');
                sstr = '<grid id="grid_' + shelfs.length + '" columnCount="10"><items>' + items.join('\n') + '</items></grid>';
                shelfs.push(sstr);
                xml = '<?xml version="1.0" encoding="UTF-8"?><atv><head><script src="' + baseURL + '/main.js" /></head><body><scroller id="com.atvttvv.soku.detail"><items>' + shelfs.join('\n') + '</items></scroller></body></atv>';
                atv.loadAndSwapXML(atv.parseXML(xml));
            });
        },
        playurl: function (args) {
            vurl = args[0];
            if (vurl.indexOf("#") >= 0) vurl = vurl.substring(0, vurl.indexOf("#"));
            name = args[1];
            listp = args[2];
            src = '';
            if (/sohu/.test(vurl)) src = 'sohu';
            if (/youku/.test(vurl)) src = 'youku';
            if (/letv/.test(vurl)) src = 'letv';
            if (/www.56.com/.test(vurl)) src = 'v56';
            if (/iqiyi/.test(vurl)) src = 'qiyi';
            if (/tudou/.test(vurl)) src = 'tudou';
            if (/sina/.test(vurl)) src = 'sina';
            if (/v.qq.com/.test(vurl)) src = 'qq';
            if (/pps.tv/.test(vurl)) src = 'pps';
            if (/m1905.com/.test(vurl)) src = 'm1905';
            if (/cntv.cn/.test(vurl)) src = 'cntv';
            if (/v.ku6.com/.test(vurl)) src = 'ku6';
            if (src == '') atv.loadAndSwapXML(atvu.showMessage({
                    title: '播放',
                    message: '尚未支持'
                }));
            else
                atvu.loadAction(src + '.playurl', '', [vurl, listp], 1);
        },
    }
};