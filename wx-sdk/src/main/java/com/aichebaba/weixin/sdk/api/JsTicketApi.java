package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsTicketApi {
    private static String api_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    public static JsTicket getTicket(String accessToken) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", accessToken);
        params.put("type", "jsapi");
        String result = HttpUtils.get(api_url, params);
        return JSONObject.parseObject(result, JsTicket.class);
    }

    public static void main(String[] args) {

        JsTicket js = getTicket("8GJfRCLbe_01sRZviIh_KzU_WTbDZZnLE5SJEZyBJFytpwEMHogpD0jSPfxE05auH17uNMSZoVrlMcjcvaGio7AOTNZgEJeheZeuJRSHoA0");
        System.out.println(js);
    }

    public static class JsTicket {
        private String errcode;
        private String errmsg;
        private String ticket;
        private String expires_in;

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        @Override
        public String toString() {
            return "JsTicket{" +
                    "errcode='" + errcode + '\'' +
                    ", errmsg='" + errmsg + '\'' +
                    ", ticket='" + ticket + '\'' +
                    ", expires_in='" + expires_in + '\'' +
                    '}';
        }
    }
}
