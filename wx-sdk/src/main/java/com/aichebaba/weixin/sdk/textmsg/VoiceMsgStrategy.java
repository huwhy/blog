package com.aichebaba.weixin.sdk.textmsg;

import java.util.List;

public class VoiceMsgStrategy extends MsgStrategy {
    public static final String format = "<Voice><MediaId><![CDATA[%1$s]]></MediaId></Voice>";

    @Override
    public String getName() {
        return VOICE;
    }

    @Override
    public String getContent(List<String> params) {
        return String.format(format, params.get(0));
    }
}
