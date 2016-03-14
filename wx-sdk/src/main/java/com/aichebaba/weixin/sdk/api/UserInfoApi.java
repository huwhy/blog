package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoApi {
    public static final String api_url = "https://api.weixin.qq.com/sns/userinfo";

    public static UserInfo getUserInfo(String accessToken, String openId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        params.put("lang", "zh_CN");
        String result = HttpUtils.get(api_url, params);
        return JSONObject.parseObject(result, UserInfo.class);
    }

    public static final String api_url2 = "https://api.weixin.qq.com/cgi-bin/user/info";

    public static UserInfo getUserInfo2(String accessToken, String openId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        params.put("lang", "zh_CN");
        String result = HttpUtils.get(api_url2, params);
        return JSONObject.parseObject(result, UserInfo.class);
    }
}
