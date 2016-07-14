package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum ParamType implements EnumValue<String> {
    unknown("未知"),
    Bool("布尔型"),
    Int("整数型"),
    Db("精度型"),
    Str("字符形");
    private String cnName;
    private ParamType(String cnName) {
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
