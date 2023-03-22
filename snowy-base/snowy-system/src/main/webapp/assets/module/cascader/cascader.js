/** 级联选择器模块 date:2020-03-11   License By http://xiaonuo.vip */
layui.define(["jquery"],
    function(a) {
        var e = layui.jquery;
        if (e("#ew-css-cascader").length <= 0) {
            layui.link(layui.cache.base + "cascader/cascader.css")
        }
        var f = [];
        var b = {
            render: function(q) {
                var E = {
                    renderFormat: function(J, i) {
                        return J.join(" / ")
                    },
                    clearable: true,
                    clearAllActive: false,
                    disabled: false,
                    trigger: "click",
                    changeOnSelect: false,
                    filterable: false,
                    notFoundText: "没有匹配数据"
                };
                q = e.extend(E, q);
                var B = q.elem;
                var A = q.data;
                var F = q.renderFormat;
                var I = q.clearable;
                var l = q.clearAllActive;
                var p = q.disabled;
                var u = q.trigger;
                var v = q.changeOnSelect;
                var s = q.reqData;
                var n = q.filterable;
                var m = q.notFoundText;
                var D = q.reqSearch;
                var y = q.onChange;
                var H = q.onVisibleChange;
                var x = q.itemHeight;
                var t = true;
                var h = e(B);
                if (h.next().hasClass("ew-cascader-group")) {
                    h.next().remove();
                    for (var z = 0; z < f.length; z++) {
                        if (B == f[z].elem) {
                            f.splice(z, 1);
                            break
                        }
                    }
                }
                f.push({
                    elem: B,
                    onVisibleChange: H
                });
                h.addClass("ew-cascader-hide");
                var r = '<div class="ew-cascader-group">';
                r += '      <div class="ew-cascader-input-group">';
                r += '         <input class="layui-input ew-cascader-input" readonly/>';
                r += '         <input class="layui-input ew-cascader-input-search"/>';
                r += '         <i class="layui-icon layui-icon-triangle-d ew-icon-arrow"></i>';
                r += '         <i class="layui-icon layui-icon-loading-1 layui-anim layui-anim-rotate layui-anim-loop ew-icon-loading"></i>';
                r += '         <i class="layui-icon layui-icon-close-fill ew-icon-clear"></i>';
                r += "      </div>";
                r += '      <div class="ew-cascader-dropdown layui-anim layui-anim-upbit"></div>';
                r += '      <div class="ew-cascader-search-list"></div>';
                r += "   </div>";
                h.after(r);
                var G = h.next();
                var o = G.children(".ew-cascader-input-group");
                var k = o.children(".ew-cascader-input");
                var C = o.children(".ew-cascader-input-search");
                var j = G.children(".ew-cascader-dropdown");
                var w = G.children(".ew-cascader-search-list");
                k.attr("placeholder", h.attr("placeholder"));
                p && k.addClass("layui-disabled");
                var g = {
                    data: A,
                    getData: function() {
                        return A
                    },
                    open: function() {
                        if (G.hasClass("ew-cascader-open")) {
                            return
                        }
                        b.hideAll();
                        G.addClass("ew-cascader-open");
                        b.checkWidthPosition(j);
                        b.checkHeightPosition(j);
                        H && H(true);
                        if (n) {
                            o.addClass("show-search");
                            C.focus()
                        }
                    },
                    hide: function() {
                        if (!G.hasClass("ew-cascader-open")) {
                            return
                        }
                        G.removeClass("ew-cascader-open");
                        G.removeClass("dropdown-show-top");
                        G.removeClass("dropdown-show-left");
                        b.hideAllSearch();
                        H && H(false)
                    },
                    removeLoading: function() {
                        G.removeClass("show-loading");
                        j.find(".ew-cascader-dropdown-list-item").removeClass("show-loading")
                    },
                    setDisabled: function(i) {
                        p = i;
                        if (i) {
                            k.addClass("layui-disabled");
                            g.hide()
                        } else {
                            k.removeClass("layui-disabled")
                        }
                    },
                    getValue: function() {
                        return h.val()
                    },
                    getLabel: function() {
                        return k.val()
                    },
                    setValue: function(J) {
                        if (J == undefined || J == null || !J.toString()) {
                            k.val("");
                            h.val("");
                            if (l || v) {
                                j.children(".ew-cascader-dropdown-list").not(":first").remove();
                                j.find(".ew-cascader-dropdown-list-item").removeClass("active");
                                b.checkWidthPosition(j)
                            } else {
                                j.find(".ew-cascader-dropdown-list-item.is-last").removeClass("active")
                            }
                            o.removeClass("show-clear");
                            return
                        }
                        J = J.toString().split(",");
                        var K = [];
                        function i(Q, O, N, M, P) {
                            if (!Q && O) {
                                Q = [];
                                L(O)
                            } else {
                                if (Q && O && O.children) {
                                    L(O.children)
                                } else {
                                    G.addClass("show-loading");
                                    s(Q,
                                        function(R) {
                                            if (Q) {
                                                O.children = R
                                            } else {
                                                A = R;
                                                Q = []
                                            }
                                            L(R)
                                        },
                                        O)
                                }
                            }
                            function L(S) {
                                for (var R = 0; R < S.length; R++) {
                                    if (S[R].value == M[N]) {
                                        K[N] = S[R].label;
                                        Q[N] = S[R].value;
                                        if (N < M.length - 1) {
                                            i(Q, S[R], N + 1, M, P)
                                        } else {
                                            P()
                                        }
                                        break
                                    }
                                }
                            }
                        }
                        i(undefined, A, 0, J,
                            function() {
                                G.removeClass("show-loading");
                                k.val(F(K, J));
                                h.val(J.join(","))
                            })
                    }
                };
                g.setValue(h.val());
                o.off("click").on("click",
                    function(J) {
                        if (k.hasClass("layui-disabled")) {
                            return
                        }
                        if (G.hasClass("show-loading")) {
                            return
                        }
                        if (G.hasClass("ew-cascader-open")) {
                            if (!n) {
                                g.hide()
                            }
                            return
                        }
                        if (t) {
                            if (A) {
                                t = false;
                                d(j, A, undefined, x);
                                i();
                                g.open()
                            } else {
                                G.addClass("show-loading");
                                s(undefined,
                                    function(K) {
                                        t = false;
                                        A = K;
                                        d(j, K, undefined, x);
                                        G.removeClass("show-loading");
                                        g.open()
                                    },
                                    undefined)
                            }
                        } else {
                            i();
                            g.open()
                        }
                        function i() {
                            var M = h.val().toString();
                            if (M) {
                                M = M.split(",");
                                for (var L = 0; L < M.length; L++) {
                                    var K = j.children(".ew-cascader-dropdown-list").eq(L).children('.ew-cascader-dropdown-list-item[data-value="' + M[L] + '"]');
                                    if (L == M.length - 1) {
                                        K.addClass("active")
                                    } else {
                                        K.trigger("click")
                                    }
                                }
                            } else {
                                g.setValue()
                            }
                        }
                    });
                o.children(".ew-icon-arrow").off("click").on("click",
                    function(i) {
                        if (G.hasClass("ew-cascader-open")) {
                            g.hide();
                            i.stopPropagation()
                        }
                    });
                j.off("click").on("click", ".ew-cascader-dropdown-list-item",
                    function() {
                        var P = e(this);
                        if (P.hasClass("active")) {
                            if (P.hasClass("is-last")) {
                                g.hide()
                            }
                            return
                        }
                        if (P.hasClass("ew-cascader-disabled")) {
                            return
                        }
                        if (P.parent().parent().find(".ew-cascader-dropdown-list-item").hasClass("show-loading")) {
                            return
                        }
                        var O = P.data("index").toString();
                        var K = O.split("-");
                        var L = A[parseInt(K[0])],
                            Q = [L.value],
                            N = [L.label];
                        for (var M = 1; M < K.length; M++) {
                            L = L.children[parseInt(K[M])];
                            Q[M] = L.value;
                            N[M] = L.label
                        }
                        if (L.haveChildren) {
                            if (L.children) {
                                P.parent().nextAll().remove();
                                b.checkWidthPosition(j);
                                J();
                                d(j, L.children, O, x)
                            } else {
                                P.addClass("show-loading");
                                s(Q,
                                    function(i) {
                                        L.children = i;
                                        P.parent().nextAll().remove();
                                        b.checkWidthPosition(j);
                                        J();
                                        d(j, i, O, x);
                                        P.removeClass("show-loading")
                                    },
                                    L)
                            }
                            if (v) {
                                J();
                                R()
                            }
                        } else {
                            P.parent().nextAll().remove();
                            J();
                            R();
                            g.hide()
                        }
                        function J() {
                            P.parent().children(".ew-cascader-dropdown-list-item").removeClass("active");
                            P.addClass("active")
                        }
                        function R() {
                            k.val(F(N, Q));
                            h.val(Q.join(","));
                            h.removeClass("layui-form-danger");
                            y && y(Q, L)
                        }
                    });
                if (u == "hover") {
                    j.off("mouseenter").on("mouseenter", ".ew-cascader-dropdown-list-item",
                        function() {
                            if (!e(this).hasClass("is-last")) {
                                e(this).trigger("click")
                            }
                        })
                }
                if (I) {
                    o.off("mouseenter").on("mouseenter",
                        function() {
                            if (h.val().toString() && !k.hasClass("layui-disabled")) {
                                e(this).addClass("show-clear")
                            }
                        });
                    o.off("mouseleave").on("mouseleave",
                        function() {
                            e(this).removeClass("show-clear")
                        });
                    o.children(".ew-icon-clear").off("click").on("click",
                        function(i) {
                            i.stopPropagation();
                            g.setValue();
                            y && y()
                        })
                }
                if (n) {
                    C.off("input").on("input",
                        function() {
                            var N = e(this).val();
                            if (!N) {
                                G.removeClass("show-search-list");
                                o.removeClass("have-value");
                                return
                            }
                            o.addClass("have-value");
                            if (D) {
                                D(N,
                                    function(i) {
                                        c(w, i, m);
                                        G.addClass("show-search-list")
                                    },
                                    A)
                            } else {
                                var M = [],
                                    O = [];
                                function J(P, Q, U, S) {
                                    for (var R = 0; R < P.length; R++) {
                                        var T = P[R];
                                        T.__label = Q ? Q + " / " + T.label: T.label;
                                        T.__value = U ? U + "," + T.value: T.value;
                                        T.__disabled = T.disabled ? T.disabled: S;
                                        if (T.children && T.children.length) {
                                            J(T.children, T.__label, T.__value, T.__disabled);
                                            delete T.__label;
                                            delete T.__value
                                        } else {
                                            M.push({
                                                label: T.__label,
                                                value: T.__value,
                                                disabled: T.__disabled
                                            })
                                        }
                                    }
                                }
                                J(A);
                                for (var K = 0; K < M.length; K++) {
                                    var L = M[K];
                                    if (L.label.indexOf(N) > -1) {
                                        L.label = L.label.replace(new RegExp(N, "g"), '<span class="search-keyword">' + N + "</span>");
                                        O.push(L)
                                    }
                                }
                                c(w, O, m);
                                G.addClass("show-search-list")
                            }
                        });
                    w.off("click").on("click", ".ew-cascader-search-list-item",
                        function(O) {
                            O.stopPropagation();
                            if (e(this).hasClass("ew-cascader-disabled")) {
                                return
                            }
                            var N = e(this).data("value").toString();
                            g.hide();
                            g.setValue(N);
                            var J = N.split(",");
                            var M = g.getData();
                            for (var L = 0; L < J.length; L++) {
                                for (var K = 0; K < M.length; K++) {
                                    if (M[K].value == J[L]) {
                                        if (L === J.length - 1) {
                                            M = M[K]
                                        } else {
                                            M = M[K].children
                                        }
                                        break
                                    }
                                }
                            }
                            y && y(J, M)
                        })
                }
                return g
            },
            hideAll: function() {
                b.hideAllSearch();
                for (var j = 0; j < f.length; j++) {
                    var k = f[j].elem;
                    var h = f[j].onVisibleChange;
                    var g = e(k).next();
                    if (g.hasClass("ew-cascader-open")) {
                        g.removeClass("ew-cascader-open");
                        g.removeClass("dropdown-show-top");
                        g.removeClass("dropdown-show-left");
                        h && h(false)
                    }
                }
            },
            hideAllSearch: function() {
                e(".ew-cascader-input-group").removeClass("show-search");
                e(".ew-cascader-group").removeClass("show-search-list");
                e(".ew-cascader-input-group").removeClass("have-value");
                e(".ew-cascader-input-search").val("")
            },
            getPageHeight: function() {
                return document.documentElement.clientHeight || document.body.clientHeight
            },
            getPageWidth: function() {
                return document.documentElement.clientWidth || document.body.clientWidth
            },
            checkWidthPosition: function(g) {
                if (g.offset().left + g.outerWidth() > b.getPageWidth()) {
                    g.parent().addClass("dropdown-show-left")
                } else {
                    g.parent().removeClass("dropdown-show-left")
                }
            },
            checkHeightPosition: function(g) {
                if (g.offset().top + g.outerHeight() > b.getPageHeight()) {
                    g.parent().addClass("dropdown-show-top");
                    if (g.offset().top < 0) {
                        g.parent().removeClass("dropdown-show-top")
                    }
                }
            }
        };
        b.getCityData = function(h) {
            for (var g = 0; g < h.length; g++) {
                h[g].value = h[g].label;
                if (h[g].children) {
                    h[g].children = b.getCityData(h[g].children)
                }
            }
            return h
        };
        b.getCity = function(k) {
            for (var h = 0; h < k.length; h++) {
                for (var g = 0; g < k[h].children.length; g++) {
                    delete k[h].children[g].children
                }
            }
            return k
        };
        b.getProvince = function(h) {
            for (var g = 0; g < h.length; g++) {
                delete h[g].children
            }
            return h
        };
        var d = function(n, p, o, j) {
            var h = j ? ' style="height:' + j + ';"': "";
            var g = '<div class="ew-cascader-dropdown-list" ' + h + ">";
            for (var k = 0; k < p.length; k++) {
                var q = p[k];
                var l = o == undefined ? k: (o + "-" + k);
                if (q.haveChildren == undefined) {
                    q.haveChildren = q.children ? true: false
                }
                var m = q.haveChildren ? "": " is-last";
                q.disabled && (m += " ew-cascader-disabled");
                g += '   <div class="ew-cascader-dropdown-list-item' + m + '" data-index="' + l + '" data-value="' + q.value + '">' + q.label + '<i class="layui-icon layui-icon-right ew-icon-right"></i><i class="layui-icon layui-icon-loading-1 layui-anim layui-anim-rotate layui-anim-loop ew-icon-loading"></i></div>'
            }
            g += "   </div>";
            n.append(g);
            b.checkWidthPosition(n)
        };
        var c = function(m, h, l) {
            var n = "";
            if (h.length == 0) {
                n = '<div class="ew-cascader-search-list-empty">' + l + "</div>"
            } else {
                for (var g = 0; g < h.length; g++) {
                    var k = h[g];
                    var j = k.disabled ? " ew-cascader-disabled": "";
                    n += '<div class="ew-cascader-search-list-item' + j + '" data-value="' + k.value + '">' + k.label + "</div>"
                }
            }
            m.html(n)
        };
        e(document).off("click.cascader").on("click.cascader",
            function(l) {
                try {
                    var m = l.target.className.split(" ");
                    var k = ["ew-cascader-group", "ew-cascader-input", "ew-icon-arrow", "ew-cascader-dropdown", "ew-cascader-dropdown-list", "ew-cascader-dropdown-list-item", "ew-icon-right", "ew-cascader-input-search", "ew-cascader-search-list", "ew-cascader-search-list-item"];
                    for (var h in m) {
                        for (var g in k) {
                            if (m[h] == k[g]) {
                                return
                            }
                        }
                    }
                } catch(l) {}
                b.hideAll()
            });
        a("cascader", b)
    });