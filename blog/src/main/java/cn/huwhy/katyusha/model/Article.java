package cn.huwhy.katyusha.model;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.enums.ArticleStatus;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;
import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

public class Article  implements Serializable {

    private int id;
    private int firstCid;
    private int secondCid;
    private int thirdCid;
    private String title;
    private String tags;
    private String summary;
    private String content;

    @EnumVal(EnumValType.Name)
    private ArticleStatus status;
    private String imgUrl;
    private String author;
    private int comments;
    private int views;
    private String url;
    private Date updated;
    private Date created;

    private String firstName;
    private String secondName;
    private String thirdName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstCid() {
        return firstCid;
    }

    public void setFirstCid(int firstCid) {
        this.firstCid = firstCid;
    }

    public int getSecondCid() {
        return secondCid;
    }

    public void setSecondCid(int secondCid) {
        this.secondCid = secondCid;
    }

    public int getThirdCid() {
        return thirdCid;
    }

    public void setThirdCid(int thirdCid) {
        this.thirdCid = thirdCid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Transient
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Transient
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Transient
    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    @Transient
    public boolean isShow() {
        return getStatus() == ArticleStatus.display;
    }

    @Transient
    public String getCatalogName() {
        if (thirdCid > 0) {
            return thirdName;
        } else if (secondCid > 0) {
            return secondName;
        } else {
            return thirdName;
        }
    }

    @Transient
    public int getCatalogId() {
        if (thirdCid > 0) {
            return thirdCid;
        } else if (secondCid > 0) {
            return secondCid;
        } else {
            return thirdCid;
        }
    }

    @Transient
    public boolean isHasImg() {
        return StringUtils.notBlank(getImgUrl());
    }

    @Transient
    public String[] getTagList() {
        if (tags != null) {
            return tags.split(" ");
        } else {
            return new String[]{};
        }
    }
}
