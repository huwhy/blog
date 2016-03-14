package cn.huwhy.katyusha.enums;

public enum ArticleStatus {
    unknown("未知道"),
    draft("未发布"),
    display("已发布"),
    hide("隐藏"),
    deleted("删除");

    private String value;

    private ArticleStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
