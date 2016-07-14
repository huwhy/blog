package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum MemberSource implements EnumValue<String> {
    WeiXin("微信"),
    QQ("QQ"),
    WeiBo("微博");

    private String cnName;

    private MemberSource(String cnName) {
        this.cnName = cnName;
    }

    public String getValue() {
        return name();
    }

    @Override
    public String getCnName() {
        return cnName;
    }
}
