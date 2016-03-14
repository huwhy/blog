Common.Msg = {
    success: function(Option){
        var _pTitle = Option.title || "success";
        var _pContent = Option.content || "success";
        Common.Msg.init(_pTitle, _pContent, "msg-success");
    },
    info: function(Option){
        var _pTitle = Option.title || "info";
        var _pContent = Option.content || "attention";
        Common.Msg.init(_pTitle, _pContent, "msg-info");
    },
    warn: function(Option){
        var _pTitle = Option.title || "warning";
        var _pContent = Option.content || "warning!";
        Common.Msg.init(_pTitle, _pContent, "msg-warn");
    },
    danger: function(Option){
        var _pTitle = Option.title || "danger";
        var _pContent = Option.content || "danger!";
        Common.Msg.init(_pTitle, _pContent, "msg-danger");
    },
    init: function (title, content, sClass) {
        var _title = title || "title";
        var _content =  content || "content";
        var _sClass = sClass || "";
        var $myMessage = $('#myMessage');
        if ($myMessage.length == 0) {
            $(document.body).append(
                "<div class=\"myMessage msg\" id=\"myMessage\">" +
                "<h3 class=\"msg-title\" style=\"display: block;\"><strong></strong></h3>" +
                "<button type=\"button\" class=\"close\"><span aria-hidden=\"true\">×</span></button>" +
                "<div class=\"msg-content\"></div>" +
                "</div>"
            );
        }
        $myMessage = $('#myMessage');
        $myMessage.addClass(_sClass);
        $myMessage.children("h3.msg-title").children().text(_title);
        $myMessage.find("div.msg-content").text(_content);
        var left = ($(window).width() - $myMessage.width()) / 2 + "px";
        $myMessage.css("left", left);
        $myMessage.fadeIn("slow", function(){
            $(".msg button.close").unbind("click").bind("click", function(){
                $("#myMessage").fadeOut("slow");
            });
        });
    }
};