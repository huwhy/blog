package com.aichebaba.weixin.sdk;

public class WeiXinToMsg {
    private String to;
    private String from;
    private String createTime;
    private String msgType;
    private String content;
    private String funcFlag;

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

    public String getFuncFlag() {
        return funcFlag;
    }

    public void setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
    }
}
