package com.aichebaba.weixin.sdk;

public class WeiXinFromMsg {
    private String to;
    private String from;
    private String createTime;
    private String msgType;
    private String content;
    private String msgId;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "WeiXinFromMsg{" +
                "msgId='" + msgId + '\'' +
                ", content='" + content + '\'' +
                ", msgType='" + msgType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
