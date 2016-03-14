package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TicketApi {
    private static final String API_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create";

    private static String TEMP_PARAM_FORMAT = "{\"scene\": {\"scene_id\": %2$d}}";
    private static String PARAM_FORMAT = "{\"scene\": {\"scene_id\": %1$d}}";

    public static Ticket getTempQrCode(String accessToken, Integer expireSeconds, Integer sceneId) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("expire_seconds", expireSeconds);
        jsonMap.put("action_name", "QR_SCENE");
        jsonMap.put("action_info", String.format(TEMP_PARAM_FORMAT, expireSeconds, sceneId));
        String json = JSONObject.toJSONString(jsonMap);

        String result = HttpUtils.postJson(API_URL, textMap, json);
        return JSONObject.parseObject(result, Ticket.class);
    }

    public static Ticket getQrCode(String accessToken, Integer sceneId) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
        jsonMap.put("action_name", "QR_LIMIT_SCENE");
        jsonMap.put("action_info", String.format(PARAM_FORMAT, sceneId));
        String result = HttpUtils.postJson(API_URL, textMap, JSONObject.toJSONString(jsonMap));
        return JSONObject.parseObject(result, Ticket.class);
    }

    public static void main(String[] args) {
        Ticket ticket = getTempQrCode("5xrc-i1ZCzZ7JbFEcEVgEJ8piAu92wTWbsK7zJuGMEIy-PjXtwu8uBRiC6GsDUzPYni1OueK4BtDgiSW6OhNYBohdqPIzTKMt-ezdbtIAow", 86400, 123);
        System.out.println(ticket);
    }
}
