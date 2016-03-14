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