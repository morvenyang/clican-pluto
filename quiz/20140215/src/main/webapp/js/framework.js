/**
 * 系统应用名称
 */
var iws = document.getElementById('iworkspace');

// ========================= 页面布局的JS =========================
// 工作空间宽度
var workspace = 0;
//
var workspaceListener = new Array();
// 工作空间大小变动监听
var register = function(fn) {
    workspaceListener.push(fn);
};

//全局变量
var contentHeight = 0;

// 左菜单模式
var leftMenuStyle = jQuery('#leftmenu').length = 0 ? false : true;

/**
 * 调整整体界面的布局
 */
function adjustLayout() {
    // /////////////////////////// 计算高度 ///////////////////////////
    // 窗体高度
    var windowHeight = jQuery(window).height();

    // 头部高度
    var headerHeight = 0;
    if (jQuery('#headbox').is(':visible')) {
        headerHeight = jQuery('#headbox').height();
    }

    // 顶层菜单高度（含快捷菜单栏）
    var menuHeight = jQuery('#menu').height();

    // 底层工具条高度
    var footerHeight = jQuery('#footer').height();

    // 计算中间工作区高度
    contentHeight = windowHeight - headerHeight - menuHeight - footerHeight;

    // 如果小于300，则不再进行调整
    if (contentHeight < 300) {
        return;
    }

    jQuery('#content').height(contentHeight);
    if (leftMenuStyle) {
        jQuery('#leftmenu').height(contentHeight);
        jQuery('#leftmenu-toggle').height(contentHeight);
        jQuery('#menuToolBarFormLeft:menuToolBar').height(contentHeight);
//        jQuery("#workspace").height(contentHeight + 1);     // +1 因为在IE上有1个像素的偏差
//        jQuery('#iworkspace').height(contentHeight);
    } else {
        jQuery("#workspace").height(contentHeight);
        jQuery('#iworkspace').height(contentHeight + 1);
    }

    // /////////////////////////// 计算宽度 ///////////////////////////
    // 窗体宽度
    var windowWidth = jQuery(window).width();

    // 左侧菜单宽度
    var menuWidth = 0;

    // 菜单Toggle宽度
    var menuToggleWidth = 0;

    if (leftMenuStyle) {
        if (jQuery('#leftmenu').is(':visible')) {
            menuWidth = jQuery('#leftmenu').width();
        }
        menuToggleWidth = jQuery('#leftmenu-toggle').width();
    }

//    jQuery('#content').width(windowWidth);

    // 计算工作空间宽度
//    var workspaceWidth = windowWidth - menuWidth - menuToggleWidth;
//    jQuery('#workspace').width(workspaceWidth);
//    workspace = workspaceWidth;
//
//    // CSS处使用了padding增加内边距，在此处减去
//    var innerWsWidth = workspaceWidth - 4;
//    if (jQuery('#workspace_inner').height() > jQuery('#workspace').height()) {
//        // 双DIV + 20 边距
//        // 解决IE内滚动导致部分右侧文字被滚动条遮住的问题
//        innerWsWidth = innerWsWidth - 20;
//    }
//    jQuery('#workspace_inner').width(innerWsWidth);

    //jQuery('#test').html('workspace=' + workspaceWidth + '  workspace_inner=' + innerWsWidth);
    adjustLeftMenu();

    if (workspaceListener.size() > 0) {
        var fn;
        for (var i = 0; i < workspaceListener.size(); i++) {
            fn = workspaceListener[i];
            fn(workspace);
        }
    }

    updateUI(windowWidth, windowHeight, menuWidth, 0, contentHeight);
}

/**
 *  自动调整当前菜单的高度
 */
function adjustLeftMenu() {

    var menuItemContainer = jQuery("#leftmenu > form > div > div .rich-panelbar-content-exterior");

    var titleHeight = 22 * menuItemContainer.length;

    var resizeObj = null;
    //var j = 0, w = 0;
    for (var i = 0; i < menuItemContainer.length; i++) {
        if (jQuery(menuItemContainer[i]).is(':visible')) {
            // j = i;
            //w = jQuery(menuItemContainer[i]).height();
            resizeObj = menuItemContainer[i];
            break;
        }
    }
    var container = jQuery(resizeObj);
    container.css('position', 'relative');
    container.css('overflow-x', 'hidden');
    container.height(contentHeight - titleHeight);

    // jQuery('#test').html('j=' + j + ' w=' + w + '  menuHeight : ' + contentHeight + '  titleHeight : ' + titleHeight + '  menuContentHeight : ' + ( contentHeight - titleHeight));

}

/**
 * 切换头部显示与否
 */
function toggleHeadBox() {
    var header = jQuery('#headbox');
    header.toggle();

    adjustLayout();

    var hidden = header.is(':hidden');
    updateHeadBoxShowStyle(hidden ? 'none' : '');
}

/**
 * 切换左侧菜单显示与否
 */
function toggleLeftMenu() {
    var leftmenu = jQuery('#leftmenu');

    var hidden;
    var path = jQuery('#minMenuImg').attr('src');
    if (path.indexOf('-in') > 0) {
        hidden = true;
        leftmenu.hide();
    } else {
        hidden = false;
        leftmenu.show();
    }
    if (hidden) {
        path = path.substring(0, path.length - 7);
    } else {
        path = path.substring(0, path.length - 4);
    }
    jQuery('#minMenuImg').attr('src', path + (hidden ? '' : '-in') + '.gif');

    adjustLayout();
    updateLeftMenuShowStyle(hidden ? 'none' : '');
}

/**临时显示左侧菜单*/
function showLeftMenuTemp() {
    var leftmenu = jQuery('#leftmenu');
    leftmenu.show();
}

function hiddenLeftMenuTemp() {
    var leftmenu = jQuery('#leftmenu');
    var path = jQuery('#minMenuImg').attr('src');
    if (path.indexOf("-in") > 0) {
        return;
    } else {
        leftmenu.hide();
    }
}

window.onresize = adjustLayout;
window.onload = function() {
    adjustLayout();

    setTimeout(adjustLayout, 500);
};


// ===========  消息闪动  ==================================================
var flash_title = {
    doc:document,
    timer:null,
    is_flash:false,
    o_title:'',
    msg:'有新消息',
    message:null,
    flash : function(msg) {

        if (this.is_flash) {
            this.clear();//先停止
        } else {
            this.o_title = this.doc.title;//保存原来的title
        }
        this.is_flash = true;
        if (typeof(msg) == 'undefined') {
            msg = this.msg;
        }
        this.message = [msg,this.getSpace(msg)];
        var th = this;
        this.timer = setInterval(function() {
            th._flash(msg);
        }, 1000);
    } ,
    _flash : function(msg) {
        this.index = (!this.index) ? 1 : 0;
        this.doc.title = '【' + this.message[this.index] + '】';
    },
    clear : function () {
        clearInterval(this.timer);
        if (this.is_flash)// 如果正在闪
            this.doc.title = this.o_title;//将title复原
        this.is_flash = false;
    },
    getSpace : function (msg) {
        var n = msg.length;
        var s = '';
        var num = msg.match(/\w/g);
        var n2 = (num != null) ? num.length : 0;//半角字符的个数
        n = n - n2; //全角字符个数
        var t = parseInt(n2 / 2);
        if (t > 0) {//两个半角替换为一个全角
            n = n + t;
        }
        s = (n2 % 2) ? ' ' : '';//如果半角字符个数为奇数
        while (n > 0) {
            s = s + '　';//全角空格
            n--;
        }
        return s;
    }
};


// ==================  jQuery.timers  ===============================================
/**
 * jQuery.timers - Timer abstractions for jQuery
 * Written by Blair Mitchelmore (blair DOT mitchelmore AT gmail DOT com)
 * Licensed under the WTFPL (http://sam.zoy.org/wtfpl/).
 * Date: 2009/10/16
 *
 * @author Blair Mitchelmore
 * @version 1.2
 *
 **/
jQuery.fn.extend({
    everyTime: function(interval, label, fn, times) {
        return this.each(function() {
            jQuery.timer.add(this, interval, label, fn, times);
        });
    },
    oneTime: function(interval, label, fn) {
        return this.each(function() {
            jQuery.timer.add(this, interval, label, fn, 1);
        });
    },
    stopTime: function(label, fn) {
        return this.each(function() {
            jQuery.timer.remove(this, label, fn);
        });
    }
});

jQuery.extend({
    timer: {
        global: [],
        guid: 1,
        dataKey: "jQuery.timer",
        regex: /^([0-9]+(?:\.[0-9]*)?)\s*(.*s)?$/,
        powers: {
            // Yeah this is major overkill...
            'ms': 1,
            'cs': 10,
            'ds': 100,
            's': 1000,
            'das': 10000,
            'hs': 100000,
            'ks': 1000000
        },
        timeParse: function(value) {
            if (value == undefined || value == null)
                return null;
            var result = this.regex.exec(jQuery.trim(value.toString()));
            if (result[2]) {
                var num = parseFloat(result[1]);
                var mult = this.powers[result[2]] || 1;
                return num * mult;
            } else {
                return value;
            }
        },
        add: function(element, interval, label, fn, times) {
            var counter = 0;

            if (jQuery.isFunction(label)) {
                if (!times)
                    times = fn;
                fn = label;
                label = interval;
            }

            interval = jQuery.timer.timeParse(interval);

            if (typeof interval != 'number' || isNaN(interval) || interval < 0)
                return;

            if (typeof times != 'number' || isNaN(times) || times < 0)
                times = 0;

            times = times || 0;

            var timers = jQuery.data(element, this.dataKey) || jQuery.data(element, this.dataKey, {});

            if (!timers[label])
                timers[label] = {};

            fn.timerID = fn.timerID || this.guid++;

            var handler = function() {
                if ((++counter > times && times !== 0) || fn.call(element, counter) === false)
                    jQuery.timer.remove(element, label, fn);
            };

            handler.timerID = fn.timerID;

            if (!timers[label][fn.timerID])
                timers[label][fn.timerID] = window.setInterval(handler, interval);

            this.global.push(element);

        },
        remove: function(element, label, fn) {
            var timers = jQuery.data(element, this.dataKey), ret;

            if (timers) {

                if (!label) {
                    for (label in timers)
                        this.remove(element, label, fn);
                } else if (timers[label]) {
                    if (fn) {
                        if (fn.timerID) {
                            window.clearInterval(timers[label][fn.timerID]);
                            delete timers[label][fn.timerID];
                        }
                    } else {
                        for (var fn in timers[label]) {
                            window.clearInterval(timers[label][fn]);
                            delete timers[label][fn];
                        }
                    }

                    for (ret in timers[label]) break;
                    if (!ret) {
                        ret = null;
                        delete timers[label];
                    }
                }

                for (ret in timers) break;
                if (!ret)
                    jQuery.removeData(element, this.dataKey);
            }
        }
    }
});

jQuery(window).bind("unload", function() {
    jQuery.each(jQuery.timer.global, function(index, item) {
        jQuery.timer.remove(item);
    });
});






