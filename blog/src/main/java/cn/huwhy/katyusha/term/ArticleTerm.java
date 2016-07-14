package cn.huwhy.katyusha.term;

import java.util.Date;

import com.jfinal.plugin.mybatis.Term;

import cn.huwhy.katyusha.enums.ArticleStatus;

public class ArticleTerm extends Term {
    private int           firstCid;
    private int           secondCid;
    private int           thirdCid;
    private String        title;
    private String        tag;
    private ArticleStatus status;
    private Date          updatedStart;
    private Date          updatedEnd;
    private Date          createdStart;
    private Date          createdEnd;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Date getUpdatedStart() {
        return updatedStart;
    }

    public void setUpdatedStart(Date updatedStart) {
        this.updatedStart = updatedStart;
    }

    public Date getUpdatedEnd() {
        return updatedEnd;
    }

    public void setUpdatedEnd(Date updatedEnd) {
        this.updatedEnd = updatedEnd;
    }

    public Date getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(Date createdStart) {
        this.createdStart = createdStart;
    }

    public Date getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(Date createdEnd) {
        this.createdEnd = createdEnd;
    }
}
