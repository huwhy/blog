package cn.huwhy.katyusha.model;

import java.io.Serializable;

public class Prop implements Serializable {

    public static final String COLOR_NAME = "color";
    public static final String SIZE_NAME = "size";
    public static final int COLOR_ID = 1;
    public static final int SIZE_ID = 2;

    private int id;
    private String name;
    private String summary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
