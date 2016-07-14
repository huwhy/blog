package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum ItemStatus implements EnumValue<String> {
    unknown,
    display,
    hide,
    delete;

    @Override
    public String getValue() {
        return name();
    }

    @Override
    public String getCnName() {
        return "";
    }
}
