package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.AdvertStatus;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;

import java.io.Serializable;

public class Advert implements Serializable{
    private int id;
    private String title;
    private String summary;
    private String link;
    @EnumVal(EnumValType.Name)
    private AdvertStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AdvertStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertStatus status) {
        this.status = status;
    }
}
