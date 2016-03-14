package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateMsgApi {
    public static final String SET_INDUSTRY_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry";
    public static final String ADD_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template";
    public static final String SENT_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send";

    public static Result setIndustry(String accessToken, String industryId1, String industryId2){
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, String> jsonMap = new LinkedHashMap<String, String>();
        jsonMap.put("industry_id1", industryId1);
        jsonMap.put("industry_id2 ", industryId2);
        String result = HttpUtils.postJson(SET_INDUSTRY_URL, textMap, JSONObject.toJSONString(jsonMap));
        return JSONObject.parseObject(result, Result.class);
    }

    public static AddTemplateResult addTemplate(String accessToken, String templateId){
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, String> jsonMap = new LinkedHashMap<String, String>();
        jsonMap.put("template_id_short", templateId);
        String result = HttpUtils.postJson(ADD_TEMPLATE_URL, textMap, JSONObject.toJSONString(jsonMap));
        return JSONObject.parseObject(result, AddTemplateResult.class);
    }

    public static AddTemplateResult sentTemplateMsg(String accessToken, String toUser, String templateId, String color){
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, String> jsonMap = new LinkedHashMap<String, String>();
        jsonMap.put("touser", toUser);
        jsonMap.put("template_id", toUser);
        jsonMap.put("url", toUser);
        jsonMap.put("topcolor", toUser);
        String result = HttpUtils.postJson(SENT_TEMPLATE_MSG_URL, textMap, JSONObject.toJSONString(jsonMap));
        return JSONObject.parseObject(result, AddTemplateResult.class);
    }
}
