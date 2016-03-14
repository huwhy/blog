package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenApi {

    private static final String GET_APP_TAKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String GET_USER_TAKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private static final String REFRESH_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    public static AccessToken getUserAccessToken(String appId, String appSecret, String code){
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String result = HttpUtils.get(GET_USER_TAKEN_URL, params);
        return JSONObject.parseObject(result, AccessToken.class);
    }

    public static AccessToken refreshUserAccessToken(String appId, String appSecret, String refreshToken){
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        String result = HttpUtils.get(REFRESH_USER_TOKEN_URL, params);
        return JSONObject.parseObject(result, AccessToken.class);
    }

    public static AccessToken getAppAccessToken(String appId, String appSecret){
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("grant_type", "client_credential");
        String result = HttpUtils.get(GET_APP_TAKEN_URL, params);
        return JSONObject.parseObject(result, AccessToken.class);
    }

    public static void main(String[] args) {
        AccessToken accessToken = AccessTokenApi.getAppAccessToken("wx452635db61e57bb4", "e5d85d0bc7c7746e2de5f7233c018cfe");
        System.out.println(accessToken);
    }

}
