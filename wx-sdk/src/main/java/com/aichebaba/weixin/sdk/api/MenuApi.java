package com.aichebaba.weixin.sdk.api;

import com.aichebaba.weixin.sdk.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuApi {
    public static final String MENU_CREATE_API_URL = "https://api.weixin.qq.com/cgi-bin/menu/create";
    public static final String MENU_DELETE_API_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete";

    public static Result createMenu(String accessToken, Object btns) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        Map<String, Object> map = new HashMap<>();
        map.put("button", btns);
        String result = HttpUtils.postJson(MENU_CREATE_API_URL, textMap, JSONObject.toJSONString(map));
        return JSONObject.parseObject(result, Result.class);
    }

    public static Result deleteMenu(String accessToken) {
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("access_token", accessToken);
        String result = HttpUtils.get(MENU_DELETE_API_URL, textMap);
        return JSONObject.parseObject(result, Result.class);
    }

    public static class Btn {
        private String url;
        private String media_id;
        private String name;
        private String key;
        private String type;
        private List<Btn> sub_button;

        public Btn() {
        }

        public static Btn newClick(String name, String key) {
            Btn btn = new Btn();
            btn.name = name;
            btn.key = key;
            btn.type = "click";
            return btn;
        }

        public static Btn newView(String name, String url) {
            Btn btn = new Btn();
            btn.name = name;
            btn.url = url;
            btn.type = "view";
            return btn;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Btn> getSub_button() {
            return sub_button;
        }

        public void setSub_button(List<Btn> sub_button) {
            this.sub_button = sub_button;
        }
    }

    public static void main(String[] args) {
        AccessToken accessToken = AccessTokenApi.getAppAccessToken("wx452635db61e57bb4", "e5d85d0bc7c7746e2de5f7233c018cfe");

//        AccessToken accessToken = AccessTokenApi.getAppAccessToken("wx3e673030599d6ce7", "fd29772e797624549f63c1adc9068d8a");

//        deleteMenu(accessToken.getAccess_token());
        List<Map<String, Object>> menus = new ArrayList<>();
        List<Btn> carBtns = new ArrayList<>();
        carBtns.add(Btn.newView("爱车估价", "http://m.2788.com/sell/gujia.html?r=wxwx"));
        carBtns.add(Btn.newView("我想买车", "http://m.2788.com/car/list.html?r=wxwx"));
        carBtns.add(Btn.newView("我想卖车", "http://m.2788.com/sell.html?r=wxwx"));
        carBtns.add(Btn.newView("车主故事", "http://m.2788.com/story/list.html?r=wxwx"));
        Map<String, Object> m1 = new HashMap<>();
        m1.put("name", "爱车巴巴");
        m1.put("sub_button", carBtns);
        menus.add(m1);
        List<Btn> carBtns2 = new ArrayList<>();
        carBtns2.add(Btn.newView("准新车",
                "http://mp.weixin.qq.com/s?__biz=MzI4MjEwMjI3MA==&mid=401576338&idx=1&sn=0a9f7c4e0255da4645deba0fff8a1aac#rd"));
        carBtns2.add(Btn.newView("APP下载",
                "http://mp.weixin.qq.com/s?__biz=MzI4MjEwMjI3MA==&mid=402482142&idx=1&sn=fb4f10ddef25c717a45f4af7360352a7#rd"));
        carBtns2.add(Btn.newView("测试游戏","http://m.2788.com/activity/list.html"));
        Map<String, Object> m2 = new HashMap<>();
        m2.put("name", "福利中心");
        m2.put("sub_button", carBtns2);
        menus.add(m2);
        List<Btn> carBtns3 = new ArrayList<>();
        carBtns3.add(Btn.newView("马云的座驾", "http://mp.weixin.qq.com/s?__biz=MzI4MjEwMjI3MA==&mid=402265082&idx=1&sn=e7a61be6b0e90b87f542d2fa637ba9e2#rd"));
        carBtns3.add(Btn.newView("傻缺才买Jeep？", "http://mp.weixin.qq.com/s?__biz=MzI4MjEwMjI3MA==&mid=402337447&idx=1&sn=2eb8ac4e701a0c388c185e96bf71b2fd#rd"));
        carBtns3.add(Btn.newView("开不起的油老虎", "http://mp.weixin.qq.com/s?__biz=MzI4MjEwMjI3MA==&mid=402240583&idx=1&sn=c31a3f579e9a50ee091004b2ecfe9649#rd"));
        Map<String, Object> m3 = new HashMap<>();
        m3.put("name", "爱车精选");
        m3.put("sub_button", carBtns3);
        menus.add(m3);
        Map<String, Object> map = new HashMap<>();
        map.put("button", menus);
        System.out.println(JSONObject.toJSONString(map));
        Result result = createMenu(accessToken.getAccess_token(), menus);
        System.out.println(result.getErrcode() + ", " + result.getErrmsg());
    }
}
