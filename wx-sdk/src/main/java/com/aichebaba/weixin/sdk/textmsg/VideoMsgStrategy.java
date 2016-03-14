package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class VideoMsgStrategy extends MsgStrategy {

    public static final String format = "<Video>" +
            "<MediaId><![CDATA[%1$s]]></MediaId>" +
            "<Title><![CDATA[%2$s]]></Title>" +
            "<Description><![CDATA[%3$s]]></Description>" +
            "</Video>";

    @Override
    public String getName() {
        return VIDEO;
    }

    @Override
    public String getContent(List<String> params) {
        Object[] args = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            args[i] = params.get(i);
        }
        return String.format(format, args);
    }
}
