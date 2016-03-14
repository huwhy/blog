
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
        $(form).find(".page-confirm").unbind("click").bind("click", function() {
            var no = $(form).find(".page-num").val();
            if (no) {
                $(form).find("input[name='curNo']").val(no);
                Pagination.query(form, func);
            }
        });
    },
    query: function (form, func) {
        if ($.isFunction(func)) {
            func($(form).serializeArray());
        } else {
            $(form).submit();
        }
    }
};

Pagination.init();