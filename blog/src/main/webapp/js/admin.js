(function ($) {
    $.fn.serializeJson = function () {
        var serializeObj = {};
        var array = this.serializeArray();
        $(array).each(function () {
            if (serializeObj[this.name]) {
                if ($.isArray(serializeObj[this.name])) {
                    serializeObj[this.name].push(this.value);
                } else {
                    serializeObj[this.name] = [serializeObj[this.name], this.value];
                }
            } else {
                serializeObj[this.name] = this.value;
            }
        });
        return serializeObj;
    };
    String.prototype.format = function (args) {
        var result = this;
        if (arguments.length > 0) {
            if (arguments.length == 1 && typeof (args) == "object") {
                for (var key in args) {
                    if (args[key] != undefined) {
                        var reg = new RegExp("({" + key + "})", "g");
                        result = result.replace(reg, args[key]);
                    }
                }
            }
            else {
                for (var i = 0; i < arguments.length; i++) {
                    if (arguments[i] != undefined) {
                        var reg = new RegExp("({[" + i + "]})", "g");
                        result = result.replace(reg, arguments[i]);
                    }
                }
            }
        }
        return result;
    }
})(jQuery);
var Common = {
    SUCC: "success",
    ERROR: "error",
    post: function (url, param, callBack) {
        $.post(url, param, function (json) {
            callBack(json);
        }, "json");
    },
    back: function (btn) {
        $(btn).unbind("click").bind("click", function () {
            history.go(-1);
        });
    },
    go: function (url) {
        window.location.href = url;
    },
    enter: function (target, fn) {
        $(target).unbind("keydown").keydown(function (event) {
            if (event.keyCode == 13) {
                fn();
            }
        });
    },
    refresh: function () {
        window.location.reload();
    },
    hasAttr: function (obj, field) {
        return typeof($(obj).attr(field)) != "undefined";
    },
    attrEquals: function (obj, field, val) {
        return typeof($(obj).attr(field)) != "undefined" && $(obj).attr(field) == val;
    },
    random: function (maxleng) {
        parseInt(Math.random() * maxleng);
    },
    trim: function (str) {
        if (str != undefined) {
            str = str.replace(/(^\s*)|(\s*$)/g, "");
            return str;
        }
    },
    isNumber: function (str) {
        return /^\d+$/.test(str);
    }
};

var SlideList = {
    init: function() {
        $("a.btn-show").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/slide/display", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-hide").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/slide/hide", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
    }
};

var ItemList = {
    init: function() {
        $("a.btn-show").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/item/display", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-hide").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/item/hide", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
    }
};

var ArticleList = {
    init: function() {
        $("a.btn-show").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/article/display", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-hide").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/article/hide", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-sync").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/article/sync", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-unSync").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/article/unSync", {id:$(this).attr("id")}, function(json) {
                Common.refresh();
            });
        });
        $("a.btn-syncAll").unbind("click").bind("click", function() {
            Common.post("/katyusha/admin/article/syncAll", {}, function(json) {
                Common.refresh();
            });
        });
    }
};

var SizeSel = {
    id: "#J_add_size",
    init: function () {
        $(SizeSel.id).on('shown', function () {
            $(SizeSel.id).find("button.btn-add").unbind("click").bind("click", function () {
                var size = $(SizeSel.id).find("input[name='size']").val();
                Common.post("/katyusha/admin/prop/size/save", {size: size}, function (json) {
                    if (json.code == Common.SUCC) {
                        SizeSel.okFunc(json.data);
                        $(SizeSel.id).modal('hide');
                    } else {
                        $(".total-msg-error .msg-con").text(json.msg);
                        $(".total-msg-error").removeClass("hide");
                        $(".total-msg-error").addClass("show");
                    }
                });
            });
        });
    },
    okFunc: function (propVal) {
        $("div.size-list").append("<label class=\"checkbox-pretty inline checked\" > "
        + "<input type=\"checkbox\" name='size' checked value='" + propVal.id + "'><span>" + propVal.value + "</span>"
        + "</label>");
    }
};

var ColorSel = {
    id: "#J_add_color",
    init: function () {
        $(ColorSel.id).on('shown', function () {
            $(ColorSel.id).find("button.btn-add").unbind("click").bind("click", function () {
                var color = $(ColorSel.id).find("input[name='color']").val();
                Common.post("/katyusha/admin/prop/color/save", {color: color}, function (json) {
                    if (json.code == Common.SUCC) {
                        ColorSel.okFunc(json.data);
                        $(ColorSel.id).modal('hide');
                    } else {
                        $(".total-msg-error .msg-con").text(json.msg);
                        $(".total-msg-error").removeClass("hide");
                        $(".total-msg-error").addClass("show");
                    }
                });
            });
        });
    },
    okFunc: function (propVal) {
        $("div.color-list").append("<label class=\"checkbox-pretty inline checked\" > "
        + "<input type=\"checkbox\" name='color' checked value='" + propVal.id + "'><span>" + propVal.value + "</span>"
        + "</label>");
    }
};

var CatSel = {
    init: function (url, form, inputId, inputName) {
        $("input[name='" + inputName + "']").bind("focus", function () {
            $(this).next().trigger("click");
        });
        $('#J_sel_cat').on('show', function () {
            $('.sui-modal .modal-header .modal-title').html("选择商品类目");
        });
        $('#J_sel_cat').on('shown', function () {
            $('form.sel-form .btn-clear').unbind("click").bind("click", function () {
                $(form + " input[name='" + inputId + "']").val("");
                $(form + " input[name='" + inputName + "']").val("");
                $('#J_sel_cat').modal('hide')
            });
            $('form.sel-form .btn-cancel').unbind("click").bind("click", function () {
                $('#J_sel_cat').modal('hide')
            });
            $('form.sel-form .btn-query').click(function () {
                CatSel.query(url, form, inputId, inputName, $("form.sel-form").serializeArray());
            });
            $('#J_sel_cat').find(".sel-table").find("tbody tr").unbind("dbclick").bind("dblclick", function () {
                CatSel.setValues(this, form, inputId, inputName);
            });
            $('#J_sel_cat').find(".sel-table").find("tbody tr td a.btn-sel").unbind("click").bind("click", function () {
                CatSel.setValues($(this).parent().parent(), form, inputId, inputName);
            });
            Pagination.init(function (args) {
                CatSel.query(url, form, inputId, inputName, args);
            }, "#J_sel_cat");
        });
    },
    query: function (url, form, inputId, inputName, params) {
        Common.post(url, params, function (json) {
            $('.sui-modal .modal-body').find("table").remove();
            $('.sui-modal .modal-body').find(".sui-pagination").remove();
            $('.sui-modal .modal-body').append(json.data);
            $('#J_sel_cat').find(".sel-table").find("tbody tr").unbind("dbclick").bind("dblclick", function () {
                CatSel.setValues(this, form, inputId, inputName);
            });
            $('#J_sel_cat').find(".sel-table").find("tbody tr td a.btn-sel").unbind("click").bind("click", function () {
                CatSel.setValues($(this).parent().parent(), form, inputId, inputName);
            });
            Pagination.init(function (args) {
                CatSel.query(url, form, inputId, inputName, args);
            }, "#J_sel_cat");
        });
    },
    setValues: function (tr, form, inputId, inputName) {
        var pid = $(tr).find("input[name='id']").val();
        var name = $(tr).find("input[name='name']").val();
        $(form + " input[name='" + inputId + "']").val(pid);
        $(form + " input[name='" + inputName + "']").val(name);
        $(form + " input[name='" + inputName + "']").blur();
        $('#J_sel_cat').modal('hide')
    }
};

var Pagination = {
    init: function (func, pClass) {
        var form = (pClass || "") + " form.pagination-form";
        $(form).find(".sui-pagination ul>li>a").unbind("click").bind("click", function () {
            $(form).find("input[name='curNo']").val($(this).text());
            Pagination.query(form, func);
        });
        $(form).find(".sui-pagination ul>li.prev>a").unbind("click").bind("click", function () {
            var no = parseInt($(form).find("input[name='curNo']").val());
            if (no > 1) {
                $(form).find("input[name='curNo']").val(no - 1);
            }
            Pagination.query(form, func);
        });
        $(form).find(".sui-pagination ul>li.next>a").unbind("click").bind("click", function () {
            var no = parseInt($(form).find("input[name='curNo']").val());
            var total = parseInt($(form).find("input[name='totalPage']").val());
            if (no < total) {
                $(form).find("input[name='curNo']").val(no + 1);
            }
            Pagination.query(form, func);
        });
        $(form).find(".sui-pagination ul>li.disabled>a").unbind("click");
    },
    query: function (form, func) {
        if ($.isFunction(func)) {
            func($(form).serializeArray());
        } else {
            $(form).submit();
        }
    }
};