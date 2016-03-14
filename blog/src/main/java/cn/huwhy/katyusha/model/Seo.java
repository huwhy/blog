package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.SeoKey;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;

import java.io.Serializable;
import java.util.Date;

public class Seo implements Serializable {

    private int id;
    @EnumVal(EnumValType.Name)
    private SeoKey seoKey;
    private int targetId;
    private String title;
    private String description;
    private String keywords;
    private Date updated;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SeoKey getSeoKey() {
        return seoKey;
    }

    public void setSeoKey(SeoKey seoKey) {
        this.seoKey = seoKey;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
