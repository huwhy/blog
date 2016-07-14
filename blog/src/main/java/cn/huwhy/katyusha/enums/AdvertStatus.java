package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum AdvertStatus implements EnumValue<String> {
    display,
    hide;

    @Override
    public String getValue() {
        return name();
    }

    @Override
    public String getCnName() {
        return "";
    }
}
