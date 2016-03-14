package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class NewsMsgStrategy extends MsgStrategy {

    public static final String format = "<item>" +
            "<Title><![CDATA[%1$s]]></Title> " +
            "<Description><![CDATA[%2$s]]></Description>" +
            "<PicUrl><![CDATA[%3$s]]></PicUrl>" +
            "<Url><![CDATA[%4$s]]></Url>" +
            "</item>";

    @Override
    public String getName() {
        return NEWS;
    }

    @Override
    public String getContent(List<String> params) {
        StringBuilder content = new StringBuilder();
        content.append("<ArticleCount>").append(params.size()).append("</ArticleCount>")
                .append("<Articles>");
        for (String param : params) {
            Object[] args = param.split(PARAM_SPLIT);
            content.append(String.format(format, args));
        }
        content.append("</Articles>");
        return content.toString();
    }
}
