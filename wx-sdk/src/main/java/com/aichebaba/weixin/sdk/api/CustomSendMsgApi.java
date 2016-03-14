package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSendMsgApi {
    public static final String API_URI = "https://api.weixin.qq.com/cgi-bin/message/custom/send";

    public static Result sendMsg(String accessToken, PostData data) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        String result = HttpUtils.postJson(API_URI, textMap, JSONObject.toJSONString(data));
        return JSONObject.parseObject(result, Result.class);
    }

    public static class PostData {
        private String touser;
        private String msgtype;
        private Map<String, List<Article>> news;
        private Map<String, String> text;

        public String getTouser() {
            return touser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public Map<String, List<Article>> getNews() {
            return news;
        }

        public void setNews(Map<String, List<Article>> news) {
            this.news = news;
        }

        public Map<String, String> getText() {
            return text;
        }

        public void setText(Map<String, String> text) {
            this.text = text;
        }

        public void addContent(String content) {
            if (text == null) {
                text = new HashMap<String, String>();
            }
            this.text.put("content", content);
        }
    }

    public static class Article {
        private String title;
        private String description;
        private String url;
        private String picurl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}
