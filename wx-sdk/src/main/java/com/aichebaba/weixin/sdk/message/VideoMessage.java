package com.aichebaba.weixin.sdk.message;

public class VideoMessage extends Message{
    private String mediaId;
    private String thumbMediaid;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaid() {
        return thumbMediaid;
    }

    public void setThumbMediaid(String thumbMediaid) {
        this.thumbMediaid = thumbMediaid;
    }
}
