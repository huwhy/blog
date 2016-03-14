package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.SlideStatus;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;
import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

public class Slide implements Serializable {

    private int id;
    private String title;
    private String picture;
    private String content;
    @EnumVal(EnumValType.Name)
    private SlideStatus status;
    private String url;
    private Date created;

    public Slide() {
    }

    public Slide(String title, String content, String url, String picture) {
        this.title = title;
        this.url = url;
        this.content = content;
        this.picture = picture;
    }

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SlideStatus getStatus() {
        return status;
    }

    public void setStatus(SlideStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Transient
    public boolean isShow() {
        return SlideStatus.display == getStatus();
    }
}
