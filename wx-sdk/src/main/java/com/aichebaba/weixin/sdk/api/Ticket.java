package com.aichebaba.weixin.sdk.api;

public class Ticket {

    public static final String WX_QRCODE_BASE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    private String ticket;
    private String expire_seconds;
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(String expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket='" + ticket + '\'' +
                ", expire_seconds='" + expire_seconds + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String toQrCodeImageUrl(){
        return Ticket.WX_QRCODE_BASE_URL + ticket;
    }
}
