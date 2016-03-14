package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.api.dto.UserList;
import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserListApi {

    public static final String USER_LIST_API = "https://api.weixin.qq.com/cgi-bin/user/get";

    public static UserList getUserList(String accessToken, String openId) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        if (openId != null && !openId.equals("")) {
            params.put("next_openid", openId);
        }
        String result = HttpUtils.get(USER_LIST_API, params);
        return JSONObject.parseObject(result, UserList.class);
    }

    public static void main(String[] args) {
        AccessToken accessToken = AccessTokenApi.getAppAccessToken("wx452635db61e57bb4",
                "e5d85d0bc7c7746e2de5f7233c018cfe");
        UserList userList = getUserList(accessToken.getAccess_token(), "");
        System.out.println(userList);
        UserInfo userInfo = UserInfoApi.getUserInfo2(accessToken.getAccess_token(), userList.getNext_openid());
        System.out.println(userInfo);
    }
}
