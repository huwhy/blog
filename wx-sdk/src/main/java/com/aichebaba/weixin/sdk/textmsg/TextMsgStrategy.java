package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class TextMsgStrategy extends MsgStrategy {

    public static final String format = "<Content><![CDATA[%1$s]]></Content>";

    @Override
    public String getName() {
        return TEXT;
    }

    @Override
    public String getContent(List<String> params) {
        return String.format(format, params.get(0));
    }
}
