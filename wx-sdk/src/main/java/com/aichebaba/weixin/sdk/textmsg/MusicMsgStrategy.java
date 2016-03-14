package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class MusicMsgStrategy extends MsgStrategy {

    public static final String format = "<Music>" +
            "<Title><![CDATA[%1$s]]></Title>" +
            "<Description><![CDATA[%2$s]]></Description>" +
            "<MusicUrl><![CDATA[%3$s]]></MusicUrl>" +
            "<HQMusicUrl><![CDATA[%4$s]]></HQMusicUrl>" +
            "<ThumbMediaId><![CDATA[%5$s]]></ThumbMediaId>" +
            "</Music>";

    @Override
    public String getName() {
        return MUSIC;
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
