package cn.huwhy.katyusha.enums;

public enum LinkType {
    unknown("未知"),
    navigation("导航"),
    friendly("友链");

    private String value;
    private LinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
