package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum SeoKey implements EnumValue<String> {
    Unknown("未知"),
    Home("首页"),
    Cat("类目页"),
    Item("产品页");

    private String cnName;

    private SeoKey(String val) {
        this.cnName = cnName;
    }

    public String getValue() {
        return name();
    }

    public String getCnName() {
        return cnName;
    }
}
