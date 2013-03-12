define("common/login/1.0.0/login", ["gallery/jquery/1.0.0/jquery", "common/base/1.0.0/base", "common/loc-store/1.0.0/loc-store"], function (a) {
    function y(a) {
        return null == document.cookie.match(new RegExp("(^" + a + "| " + a + ")=([^;]*)")) ? "" : decodeURIComponent(RegExp.$2)
    }
    function z(a, b, c) {
        if (arguments.length > 2) {
            var e = new Date((new Date).getTime() + 36e5 * c);
            document.cookie = a + "=" + encodeURIComponent(b) + "; path=/; domain=" + k.domain + "; expires=" + e.toGMTString()
        } else document.cookie = a + "=" + encodeURIComponent(b) + "; path=/; domain=" + k.domain
    }
    function B(a) {
        for (var b = [], c = 0; a.length > c; c++) b.push(a[c]);
        return b
    }
    function E(a, b, c) {
        if (arguments.length > 2) {
            var d = new Date((new Date).getTime() + 1e3 * c);
            document.cookie = a + "=" + escape(b) + "; path=/; domain=vod.xunlei.com; expires=" + d.toGMTString()
        } else document.cookie = a + "=" + escape(b) + "; path=/; domain=vod.xunlei.com"
    }
    function G() {
        var a = {
            oriType: 0,
            type: "vodVipUser",
            level: -1,
            expire: "",
            flux: -1
        }, c = function (a) {
            var b = -1;
            if (-1 != a.type) {
                switch (n.oriType = a.type, n.type = a.type, n.level = a.level, -1 != n.level && z("isvip", n.level), n.expire = a.expire, n.last_expire = a.last_expire, n.flux = a.flux, a.type) {
                    case 0:
                        b = "vodVipUser";
                        break;
                    case 1:
                        b = "oldVodVipUser";
                        break;
                    case 2:
                        b = "vipUser";
                        break;
                    case 3:
                        b = "expVipUser";
                        break;
                    case 4:
                        b = "norUser";
                        break;
                    default:
                        b = "vodVipUser"
                }
                n.type = b
            }
            F.userinfoloaded()
        }, e = function () {
            n = b.extend(n, a), F.userinfoloaded()
        }, f = [r, "get_user_info"];
        f = f.join("/");
        var g = {
            userid: d.getCookie("userid"),
            from: "vodLogin",
            t: (new Date).getTime()
        }, h = {
            url: f,
            dataType: "jsonp",
            jsonp: "jsonp",
            data: g,
            processData: !0,
            timeout: 7e3,
            error: e,
            success: c
        }, i = b.ajax(h);
        return i
    }
    function H(a, b, c) {
        if ("function" == typeof c) a.addEventListener(b, c);
        else {
            var d = B(arguments);
            d.shift(), setTimeout(function () {
                a.fireEvent.apply(a, d)
            }, 10)
        }
    }
    function J(a) {
        if (!I) {
            var d = "http://login.xunlei.com/check?u=" + a + "&t=" + new Date;
            I = !0;
            var e = "union_poster_iframe_" + (new Date).getTime() + "_" + parseInt(1e5 * Math.random());
            b.browser.msie && "6.0" == c.browser.version ? b("body").append("<iframe id='" + e + "' name='" + e + "' width='0' height='0'></iframe>") : b("body").append("<iframe id='" + e + "' name='" + e + "' src='" + s + "' width='0' height='0'></iframe>");
            var f = b("#" + e);
            f.load(function () {
                b(this).unbind("load").load(function () {
                    var a = parseInt(y("check_result"));
                    if (0 == a) {
                        b("#" + k.verifyp).hide();
                        var c = y("check_result").split(":")[1];
                        b("#" + k.verifycode).val(c)
                    } else b("#" + k.verifycode).val(""), F.vcode(), b("#" + k.verifyp).show();
                    I = !1, b(f).remove()
                }), this.src = d, setTimeout(function () {
                    I = !1
                }, 300)
            }), f.hide()
        }
    }
    function K() {
        return b("#" + k.u).val(b("#" + k.u).val().trim()), b("#" + k.p).val(b("#" + k.p).val().trim()), 0 == b("#" + k.u).val().length ? (F.valid("请输入帐号"), b("#" + k.u).focus(), !1) : 0 == b("#" + k.p).val().length ? (F.valid("请输入密码"), b("#" + k.p).focus(), !1) : 0 == b("#" + k.verifycode).val().length ? (F.valid("请输入验证码"), b("#" + k.verifycode).focus(), !1) : !0
    }
    function L() {
        if (!m) {
            m = 1, f.unbind("load"), g = b("<form />").attr({
                method: "post",
                target: f.attr("name"),
                action: q[t] + "sec2login/"
            });
            var a = {};
            a.u = encodeURI(b("#" + k.u).val()), b("#" + k.auto).attr("checked") ? (a.login_enable = 1, a.login_hour = k.expire) : (a.login_enable = 0, a.login_hour = 0);
            var c = P(b("#" + k.p).val());
            c = P(c), c = P(c + b("#" + k.verifycode).val().toUpperCase()), a.p = encodeURIComponent(c), a.verifycode = encodeURIComponent(b("#" + k.verifycode).val().toUpperCase());
            for (var d in a) b("<input>", {
                type: "hidden",
                val: a[d],
                name: d
            }).appendTo(g);
            b("body").append(g), g.hide(), f.load(M), F.logining(), u = setTimeout(function () {
                M()
            }, 7e3), g.submit()
        }
    }
    function M() {
        m = 0, u && clearTimeout(u);
        var a = y("blogresult"),
            c = {
                1: "验证码错误",
                2: "密码错误",
                3: "服务器忙",
                4: "帐号不存在",
                5: "数据包错误",
                6: "帐号被锁定",
                7: "session请求失败",
                8: "服务器忙",
                9: "验证码长度不对",
                10: "非法验证码",
                11: "验证码超时",
                12: "登录页面无效",
                13: "登录页面无效",
                14: "登录页面无效",
                15: "登录页面无效",
                16: "网络超时，请重新登录"
            };
        if (f = null, g = null, "0" != a) if (t++, q.length > t && ("" == a || 3 == a || 7 == a)) F.run();
        else {
            b("#" + k.submit).attr("disabled", !1), F.vOneCode(), (2 == a || 1 == a || 9 == a) && b("#" + k.p).val("").focus(), t = 0;
            var d = c[a];
            if ("undefined" == typeof c[a]) var d = "未知错误";
            F.error(a, d)
        } else t = 0, F.success(3)
    }
    function N() {
        f.unbind("load"), g = b("<form />", {
            method: "post",
            target: f.attr("name"),
            action: q[t] + "sessionid/"
        }), b("<input>", {
            type: "hidden",
            name: "sessionid",
            val: p
        }).appendTo(g), b("body").append(g), g.hide(), f.load(O), F.logining(), u = setTimeout(function () {
            O()
        }, 7e3), g.submit()
    }
    function O() {
        u && clearTimeout(u);
        var a = y("blogresult");
        f = null, g = null, "0" != a ? (t++, q.length > t && ("" == a || 3 == a || 7 == a) ? F.auto(p) : (t = 0, F.autoerror())) : (t = 0, F.success())
    }
    function P(a) {
        return fb(W(db(a), a.length * x))
    }
    function W(a, b) {
        a[b >> 5] |= 128 << b % 32, a[(b + 64 >>> 9 << 4) + 14] = b;
        for (var c = 1732584193, d = -271733879, e = -1732584194, f = 271733878, g = 0; a.length > g; g += 16) {
            var h = c,
                i = d,
                j = e,
                k = f;
            c = Y(c, d, e, f, a[g + 0], 7, -680876936), f = Y(f, c, d, e, a[g + 1], 12, -389564586), e = Y(e, f, c, d, a[g + 2], 17, 606105819), d = Y(d, e, f, c, a[g + 3], 22, -1044525330), c = Y(c, d, e, f, a[g + 4], 7, -176418897), f = Y(f, c, d, e, a[g + 5], 12, 1200080426), e = Y(e, f, c, d, a[g + 6], 17, -1473231341), d = Y(d, e, f, c, a[g + 7], 22, -45705983), c = Y(c, d, e, f, a[g + 8], 7, 1770035416), f = Y(f, c, d, e, a[g + 9], 12, -1958414417), e = Y(e, f, c, d, a[g + 10], 17, -42063), d = Y(d, e, f, c, a[g + 11], 22, -1990404162), c = Y(c, d, e, f, a[g + 12], 7, 1804603682), f = Y(f, c, d, e, a[g + 13], 12, -40341101), e = Y(e, f, c, d, a[g + 14], 17, -1502002290), d = Y(d, e, f, c, a[g + 15], 22, 1236535329), c = Z(c, d, e, f, a[g + 1], 5, -165796510), f = Z(f, c, d, e, a[g + 6], 9, -1069501632), e = Z(e, f, c, d, a[g + 11], 14, 643717713), d = Z(d, e, f, c, a[g + 0], 20, -373897302), c = Z(c, d, e, f, a[g + 5], 5, -701558691), f = Z(f, c, d, e, a[g + 10], 9, 38016083), e = Z(e, f, c, d, a[g + 15], 14, -660478335), d = Z(d, e, f, c, a[g + 4], 20, -405537848), c = Z(c, d, e, f, a[g + 9], 5, 568446438), f = Z(f, c, d, e, a[g + 14], 9, -1019803690), e = Z(e, f, c, d, a[g + 3], 14, -187363961), d = Z(d, e, f, c, a[g + 8], 20, 1163531501), c = Z(c, d, e, f, a[g + 13], 5, -1444681467), f = Z(f, c, d, e, a[g + 2], 9, -51403784), e = Z(e, f, c, d, a[g + 7], 14, 1735328473), d = Z(d, e, f, c, a[g + 12], 20, -1926607734), c = $(c, d, e, f, a[g + 5], 4, -378558), f = $(f, c, d, e, a[g + 8], 11, -2022574463), e = $(e, f, c, d, a[g + 11], 16, 1839030562), d = $(d, e, f, c, a[g + 14], 23, -35309556), c = $(c, d, e, f, a[g + 1], 4, -1530992060), f = $(f, c, d, e, a[g + 4], 11, 1272893353), e = $(e, f, c, d, a[g + 7], 16, -155497632), d = $(d, e, f, c, a[g + 10], 23, -1094730640), c = $(c, d, e, f, a[g + 13], 4, 681279174), f = $(f, c, d, e, a[g + 0], 11, -358537222), e = $(e, f, c, d, a[g + 3], 16, -722521979), d = $(d, e, f, c, a[g + 6], 23, 76029189), c = $(c, d, e, f, a[g + 9], 4, -640364487), f = $(f, c, d, e, a[g + 12], 11, -421815835), e = $(e, f, c, d, a[g + 15], 16, 530742520), d = $(d, e, f, c, a[g + 2], 23, -995338651), c = _(c, d, e, f, a[g + 0], 6, -198630844), f = _(f, c, d, e, a[g + 7], 10, 1126891415), e = _(e, f, c, d, a[g + 14], 15, -1416354905), d = _(d, e, f, c, a[g + 5], 21, -57434055), c = _(c, d, e, f, a[g + 12], 6, 1700485571), f = _(f, c, d, e, a[g + 3], 10, -1894986606), e = _(e, f, c, d, a[g + 10], 15, -1051523), d = _(d, e, f, c, a[g + 1], 21, -2054922799), c = _(c, d, e, f, a[g + 8], 6, 1873313359), f = _(f, c, d, e, a[g + 15], 10, -30611744), e = _(e, f, c, d, a[g + 6], 15, -1560198380), d = _(d, e, f, c, a[g + 13], 21, 1309151649), c = _(c, d, e, f, a[g + 4], 6, -145523070), f = _(f, c, d, e, a[g + 11], 10, -1120210379), e = _(e, f, c, d, a[g + 2], 15, 718787259), d = _(d, e, f, c, a[g + 9], 21, -343485551), c = bb(c, h), d = bb(d, i), e = bb(e, j), f = bb(f, k)
        }
        return Array(c, d, e, f)
    }
    function X(a, b, c, d, e, f) {
        return bb(cb(bb(bb(b, a), bb(d, f)), e), c)
    }
    function Y(a, b, c, d, e, f, g) {
        return X(b & c | ~b & d, a, b, e, f, g)
    }
    function Z(a, b, c, d, e, f, g) {
        return X(b & d | c & ~d, a, b, e, f, g)
    }
    function $(a, b, c, d, e, f, g) {
        return X(b ^ c ^ d, a, b, e, f, g)
    }
    function _(a, b, c, d, e, f, g) {
        return X(c ^ (b | ~d), a, b, e, f, g)
    }
    function bb(a, b) {
        var c = (65535 & a) + (65535 & b),
            d = (a >> 16) + (b >> 16) + (c >> 16);
        return d << 16 | 65535 & c
    }
    function cb(a, b) {
        return a << b | a >>> 32 - b
    }
    function db(a) {
        for (var b = Array(), c = (1 << x) - 1, d = 0; a.length * x > d; d += x) b[d >> 5] |= (a.charCodeAt(d / x) & c) << d % 32;
        return b
    }
    function fb(a) {
        for (var b = v ? "0123456789ABCDEF" : "0123456789abcdef", c = "", d = 0; 4 * a.length > d; d++) c += b.charAt(15 & a[d >> 2] >> 8 * (d % 4) + 4) + b.charAt(15 & a[d >> 2] >> 8 * (d % 4));
        return c
    }
    var f, g, p, u, b = a("gallery/jquery/1.0.0/jquery"),
        c = b,
        d = a("common/base/1.0.0/base"),
        e = a("common/loc-store/1.0.0/loc-store"),
        j = [],
        k = {
            action: "",
            domain: "xunlei.com",
            form: "loginform",
            box: "login_box",
            u: "login_u",
            p: "login_p",
            auto: "login_auto",
            verifycode: "login_verifycode",
            verifyimg: "login_verify_code",
            verifyref: "login_ref_verify",
            verifyp: "login_vcode_p",
            tip: "login_tip",
            submit: "login_submit",
            delAccount: "delAccount",
            accList: "accList",
            selectIndex: -1,
            expire: 720
        }, l = 0,
        m = 0,
        n = {
            oriType: 0,
            type: "vodVipUser",
            level: -1,
            expire: "",
            last_expire: "",
            flux: -1
        }, o = {
            logStateError: "登录出现异常，请重新登录",
            sidExpired: "登录出现异常，请重新登录"
        }, q = ["http://login.xunlei.com/", "http://login2.xunlei.com/", "http://login3.xunlei.com/"],
        r = "http://i.vod.xunlei.com",
        s = "/domain.html",
        t = 0,
        v = 0,
        x = 8;
    c.extend(String.prototype, {
        trim: function () {
            return this.replace(/(^\s+)|(\s+$)/g, "")
        }
    }), c.fn.extend({
        loginshowInputTips: function (a) {
            this._tips = a, "" == this.val().trim() && (this.val(a), this.css("color", "gray")), this.focus(b.proxy(function () {
                this.val().trim() == this._tips && (this.css("color", "black"), this.val(""))
            }, this)), this.blur(b.proxy(function () {
                "" == this.val().trim() && (this.css("color", "gray"), this.val(this._tips))
            }, this))
        }
    }), Function.prototype.delayApply = function (a, b, c) {
        var d = this;
        return setTimeout(function () {
            d.apply(b, c)
        }, a)
    };
    var A = function () {};
    A.prototype.addEventListener = function (a, b, c) {
        var d = this;
        if (this[a] || (this[a] = []), !(this[a] instanceof Array)) return !1;
        for (var e = 0; this[a].length > e; e++) if (this[a][e].o == d && this[a][e].f == b) return !0;
        return this[a].push({
            o: d,
            f: b,
            t: c
        }), this
    }, A.prototype.attachEvent = A.prototype.addEventListener, A.prototype.removeEventListener = function (a, b) {
        var c = this;
        if (!(this[a] && this[a] instanceof Array)) return !1;
        if (!b) return this[a] = [], !0;
        for (var d = 0; this[a].length > d; d++) if (this[a][d].o == c && this[a][d].f == b) return this[a].splice(d, 1), 0 == this[a].length && delete this[a], !0;
        return !1
    }, A.prototype.detachEvent = A.prototype.removeEventListener, A.prototype.dispatchEvent = function (a) {
        if (!(this[a] && this[a] instanceof Array)) return !1;
        var b = [this].concat(B(arguments));
        b.shift(), b.shift();
        for (var c = this[a].slice(0), d = 0; c.length > d; d++) "number" == typeof c[d].t ? c[d].f.delayApply(c[d].t, c[d].o, b) : c[d].f.apply(c[d].o, b);
        return !0
    }, A.prototype.fireEvent = A.prototype.dispatchEvent;
    var C = null,
        F = {
            init: function (a) {
                var d = this;
                f = null, g = null, c.extend(k, a), q = ["http://login." + k.domain + "/", "http://login2." + k.domain + "/", "http://login3." + k.domain + "/"], C = C || new A, c.extend(this, C);
                var e = new String("onsuccess,onerror,onvalid,onlogining,onautoerror,onlogout,onuserinfoloaded").split(",");
                for (var h in a) "," != h && -1 != c.inArray(h, e) && (this.removeEventListener(h), this[h.substr(2)](a[h]));
                b(window).load(function () {}), setTimeout(function () {
                    var a = b("#" + k.u).val();
                    a = b.trim(a), a && J(a)
                }, 20), b("#" + k.u).change(function () {
                    setTimeout(function () {
                        var a = b("#" + k.u).val();
                        b.trim(a) && J(a)
                    }, 50)
                }).focus(function () {
                    l = 1
                }).blur(function () {
                    l = 0, setTimeout(function () {
                        var a = b("#" + k.u).val();
                        b("#" + k.accList).hide(), b.trim(a) && J(a)
                    }, 500)
                }).keydown(function (a) {
                    if (/\b(17|27)\b/.test(a.keyCode + "")) return -1 == k.selectIndex, void 0;
                    if (/\b9\b/.test(a.keyCode + "")) {
                        -1 == k.selectIndex;
                        var c = b("#" + k.u).val() || "";
                        return "" != c && (b("#" + k.u).blur(), setTimeout(function () {
                            b("#" + k.p).focus()
                        }, 100)), a.stopPropagation(), void 0
                    }
                    if (/\b13\b/.test(a.keyCode)) {
                        var e = b("#accList ul li");
                        if (0 == e.size()) return;
                        var c = e.eq(k.selectIndex).text() || "";
                        return "" != c && (k.selectIndex = -1, setTimeout(function () {
                            b("#" + k.u).val(c).blur(), b("#" + k.p).focus()
                        }, 10), a.stopImmediatePropagation()), void 0
                    }
                    if (/\b(38|40)\b/.test(a.keyCode)) {
                        a.stopPropagation();
                        var f = b("#accList ul li");
                        return -1 == k.selectIndex ? k.selectIndex = 0 : "38" == a.keyCode && k.selectIndex > 0 ? k.selectIndex-- : "40" == a.keyCode && k.selectIndex < f.size() - 1 && k.selectIndex++, f.css({
                            "background-color": "#fff",
                            color: "#bfbfbf"
                        }).eq(k.selectIndex).css({
                            "background-color": "#4bade8",
                            color: "#333"
                        }), void 0
                    }
                    setTimeout(d.userInput, 10)
                }), b("#" + k.p).focus(function () {
                    l = 1
                }).blur(function () {
                    l = 0
                }), "" != b("#" + k.p).val(), b("#" + k.verifyref).click(function () {
                    F.vcode()
                }), b("#" + k.verifycode).focus(function () {
                    b(this).val(""), l = 1
                }).blur(function () {
                    b(this).val() || b(this).val("验证码"), l = 0
                }), b("#" + k.auto).click(function () {
                    this.focus()
                }).focus(function () {
                    l = 1
                }).blur(function () {
                    l = 0
                }), b("#" + k.box).keydown(function (a) {
                    return 0 == l ? !0 : 13 != a.keyCode ? !0 : "none" == b("#" + k.box).css("display") ? !0 : (b("#" + k.delAccount).hide(), F.run(), void 0)
                }), b("#" + k.submit).click(function () {
                    F.run()
                }), b("#" + k.submit).attr("disable", !1);
                var i = b.trim(y("lx_login_u")),
                    j = b.trim(y("luserid")),
                    m = b.trim(y("lsessionid"));
                return "" != j && "" != m || b("#" + k.u).val(i).change(), ("sidExpired" == k.action || "logStateError" == k.action) && (this.showTip(o[k.action]), this.exit()), this
            },
            userInput: function () {
                var a = b("#" + k.u).val() + "";
                if (a = a.replace(/^\s+/g, "").replace(/\s+$/g, ""), "" == a) return b("#" + k.delAccount).hide(), b("#" + k.accList).hide(), void 0;
                b("#" + k.delAccount).show();
                var c = 0,
                    d = "";
                for (var e in j) 0 == j[e].name.indexOf(a) && 6 > c && (d += '<li><a href="javascript:;">' + j[e].name + "</a></li>", c++);
                c > 0 ? (d = '<li><a href="javascript:;">' + a + "</a></li>" + d, b("#" + k.accList).find("ul").html(d), b("#" + k.accList).show()) : b("#" + k.accList).hide().find("ul").html("")
            },
            resetLogBox: function () {
                var a = b("#" + k.u),
                    c = b("#" + k.p),
                    d = b("#" + k.auto),
                    e = b("#" + k.verifyp),
                    f = b("#" + k.tip);
                a.val(""), c.val(""), d.attr("checked", !0), e.hide(), f.text("").hide(), b("#" + k.accList).hide()
            },
            showTip: function (a) {
                var c = b("#" + k.tip);
                c.text(a).show()
            },
            vcode: function () {
                b("#" + k.verifyp), b("#" + k.verifyimg).attr("src", "http://verify." + k.domain + "/image?cachetime=" + (new Date).getTime()), b("#" + k.verifyp), b("#" + k.verifycode).val("")
            },
            vOneCode: function () {
                var a = b("#" + k.verifyimg),
                    c = b("#" + k.verifyp);
                "" == a.attr("src") || void 0 == a.attr("src") ? a.attr("src", "http://verify." + k.domain + "/image?cachetime=" + (new Date).getTime()) : this.vcode(), a.show(), c.show(), b("#" + k.verifycode).val("验证码")
            },
            run: function () {
                if (1 != m) {
                    if (!K()) return "";
                    b("#" + k.submit).attr("disable", !0);
                    var a = "poster_iframe_" + (new Date).getTime() + "_" + parseInt(1e5 * Math.random());
                    return b.browser.msie && "6.0" == c.browser.version ? b("body").append("<iframe id='" + a + "' name='" + a + "' width='0' height='0'></iframe>") : b("body").append("<iframe id='" + a + "' name='" + a + "' src='" + s + "' width='0' height='0'></iframe>"), f = b("#" + a), f.load(L), f.hide(), this
                }
            },
            exit: function () {
                for (var a = ["sessionid", "usrname", "nickname", "userid", "usertype", "usernewno", "raw", "isvip", "blogresult", "vipstate", "p_t", "lsessionid", "luserid"], b = 0; a.length > b; b++) z(a[b], "", 0, 1), E(a[b], "", 0, 1);
                return F.logout(), this
            },
            auto: function (a) {
                if (p = a ? a : y("lsessionid"), y("sessionid") && y("userid")) return F.success(1), this;
                if (!p) return this.autoerror(), this;
                var c = "poster_iframe_" + (new Date).getTime() + "_" + parseInt(1e5 * Math.random());
                return b("body").append("<iframe id='" + c + "' name='" + c + "' src='" + s + "' width='0' height='0'></iframe>"), f = b("#" + c), f.load(N), f.hide(), this
            },
            success: function () {
                var a = [this, "onsuccess"].concat(B(arguments));
                return H.apply(this, a), "function" != typeof arguments[0] && G(), this
            },
            error: function () {
                var a = [this, "onerror"].concat(B(arguments));
                return H.apply(this, a), this
            },
            valid: function () {
                var a = [this, "onvalid"].concat(B(arguments));
                return H.apply(this, a), this
            },
            autoerror: function () {
                var a = [this, "onautoerror"].concat(B(arguments));
                return H.apply(this, a), this
            },
            logout: function () {
                var a = [this, "onlogout"].concat(B(arguments));
                return H.apply(this, a), this
            },
            logining: function () {
                var a = [this, "onlogining"].concat(B(arguments));
                return H.apply(this, a), this
            },
            userinfoloaded: function () {
                var a = [this, "onuserinfoloaded"].concat(B(arguments));
                return H.apply(this, a), this
            },
            unbind: function (a, b) {
                return this.removeEventListener("on" + a, b), this
            },
            getuserinfo: function () {
                return n
            },
            getLatestAccount: function () {
                var a;
                return j = e.getStoredData("accountInfo", 0), j = function (a) {
                    var b = [],
                        c = [],
                        d = [],
                        e = 0,
                        f = 0;
                    for (f in a) if (0 != c.length) {
                        for (e = 0; c.length > e; e++) if (c[e] > a[f]) {
                            c.splice(e, 0, a[f]), b.splice(e, 0, f);
                            break
                        }
                        e == c.length && (c.push(a[f]), b.push(f))
                    } else c.push(a[f]), b.push(f);
                    for (e = c.length - 1; e >= 0; e--) {
                        var g = {
                            name: b[e],
                            time: c[e]
                        };
                        d.push(g)
                    }
                    return d
                }(j), j.length > 0 && (a = j[0].name), a
            },
            setLoginAccount: function (a) {
                var b = a || "";
                if ("" != b) {
                    var c = {};
                    c[b] = (new Date).getTime(), e.storeData({
                        accountInfo: c
                    })
                }
            },
            getLastLogin: function () {
                var a = b("#" + k.delAccount),
                    c = b("#" + k.u),
                    d = b("#" + k.p);
                b("#accList ul li a").die("click").live("click", function (a) {
                    var b = a.currentTarget.innerText || a.currentTarget.innerHTML || "";
                    c.val(b).css("color", "#000"), d.focus()
                }), a.unbind("click").click(function () {
                    var b = c.val() + "";
                    return "" != b && e.removeStoredData("accountInfo > " + b), d.val(""), c.val("").focus().css("color", "#000"), a.hide(), !1
                }).css("cursor", "pointer"), (lastlog = this.getLatestAccount()) ? (a.show(), c.css("color", "#222").val(lastlog), d.focus(), setTimeout(function () {
                    var a = b.trim(b("#" + k.u).val());
                    a && J(a)
                }, 20)) : (c.focus().css("color", "#000"), a.hide()), b("#" + k.accList).hide()
            }
        };
    return F;
    var I
});