package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum LinkType implements EnumValue<String> {
    unknown("未知"),
    navigation("导航"),
    friendly("友链");

    private String cnName;
    private LinkType(String cnName) {
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
