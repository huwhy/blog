package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class ImageMsgStrategy extends MsgStrategy {

    public static final String format = "<Image><MediaId><![CDATA[%1$s]]></MediaId></Image>";

    @Override
    public String getName() {
        return IMAGE;
    }

    @Override
    public String getContent(List<String> params) {
        return String.format(format, params.get(0));
    }
}