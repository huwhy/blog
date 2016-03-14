package cn.huwhy.katyusha.enums;

public enum MemberSource {
    WeiXin("微信"),
    QQ("QQ"),
    WeiBo("微博");

    private String value;

    private MemberSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
