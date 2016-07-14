package cn.huwhy.katyusha.enums;

import com.jfinal.plugin.mybatis.EnumValue;

public enum ArticleStatus implements EnumValue<String> {
    unknown("未知道"),
    draft("未发布"),
    display("已发布"),
    hide("隐藏"),
    deleted("删除");

    private String cnName;

    private ArticleStatus(String value) {
        this.cnName = value;
    }

    public String getValue() {
        return name();
    }

    public String getCnName() {
        return cnName;
    }
}
