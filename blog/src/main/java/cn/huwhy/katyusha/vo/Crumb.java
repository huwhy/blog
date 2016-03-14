package cn.huwhy.katyusha.vo;

public class Crumb {
    private String name;
    private String url;

    public Crumb(String name) {
        this.name = name;
    }

    public Crumb(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
