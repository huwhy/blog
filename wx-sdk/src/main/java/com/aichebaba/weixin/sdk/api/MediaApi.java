package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaApi {
    public static final String UPLOAD_TEMP_URL = "https://api.weixin.qq.com/cgi-bin/media/upload";

    public static final String UPLOAD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";

    public static MediaInfo uploadTemp(String accessToken, String type, String fileName, String filePath) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        textMap.put("type", type);
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put(fileName, filePath);
        String result = HttpUtils.postForm(UPLOAD_TEMP_URL, textMap, fileMap);
        return JSONObject.parseObject(result, MediaInfo.class);
    }

    public static MediaInfo uploadNews(String accessToken, List<News> newses) {
        if (newses == null || newses.isEmpty()) return null;
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("articles", newses);
        String result = HttpUtils.postJson(UPLOAD_NEWS_URL, textMap, JSONObject.toJSONString(jsonMap));
        return JSONObject.parseObject(result, MediaInfo.class);
    }

}
