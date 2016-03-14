package com.aichebaba.weixin.sdk.textmsg;

import java.util.*;

public abstract class MsgStrategy {

    public static final String PARAM_SPLIT = "::";

    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String MUSIC = "music";
    public static final String NEWS = "news";
    public static final String VIDEO = "video";
    public static final String VOICE = "voice";

    public static final String replyMsgFormat = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><FromUserName><![CDATA[%2$s]]></FromUserName><CreateTime>%3$s</CreateTime><MsgType><![CDATA[%4$s]]></MsgType>%5$s</xml>";

    public abstract String getName();

    public abstract String getContent(List<String> params);

    public static Map<String, MsgStrategy> msgStrategyMap = new HashMap<String, MsgStrategy>();

    static {
        MsgStrategy text = new TextMsgStrategy();
        msgStrategyMap.put(text.getName(), text);
        MsgStrategy image = new ImageMsgStrategy();
        msgStrategyMap.put(image.getName(), image);
        MsgStrategy music = new MusicMsgStrategy();
        msgStrategyMap.put(music.getName(), music);
        MsgStrategy news = new NewsMsgStrategy();
        msgStrategyMap.put(news.getName(), news);
        MsgStrategy video = new VideoMsgStrategy();
        msgStrategyMap.put(video.getName(), video);
        MsgStrategy voice = new VoiceMsgStrategy();
        msgStrategyMap.put(voice.getName(), video);
    }

    public String getMsg(String from, String to, String time, List<String> params) {
        return String.format(replyMsgFormat, to, from, time, getName(), getContent(params));
    }

    public static String getReply(String type, String from, String to, List<String> params) {
        MsgStrategy ms = msgStrategyMap.get(type);
        return String.format(replyMsgFormat, to, from, String.valueOf(new Date().getTime()), ms.getName(),
                ms.getContent(params));
    }

    public static String getTextMsg(String fromUserName, String toUserName, String msg) {
        MsgStrategy msgStrategy = msgStrategyMap.get(MsgStrategy.TEXT);
        List<String> params = new ArrayList<>();
        params.add(msg);
        return msgStrategy.getMsg(fromUserName, toUserName, String.valueOf(new Date().getTime()), params);
    }

    public static String convertParams(String...values) {
        StringBuilder sb = new StringBuilder();
        for (String v : values) {
            sb.append(v).append(PARAM_SPLIT);
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }
}
