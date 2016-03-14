package cn.huwhy.katyusha.enums;

public enum SeoKey {
    Unknown("未知"),
    Home("首页"),
    Cat("类目页"),
    Item("产品页");

    private String value;

    private SeoKey(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }
}
