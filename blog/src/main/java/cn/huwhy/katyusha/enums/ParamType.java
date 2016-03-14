package cn.huwhy.katyusha.enums;

public enum ParamType {
    unknown("未知"),
    Bool("布尔型"),
    Int("整数型"),
    Db("精度型"),
    Str("字符形");
    private String value;
    private ParamType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
