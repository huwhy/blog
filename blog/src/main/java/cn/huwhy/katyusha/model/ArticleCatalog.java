package cn.huwhy.katyusha.model;

import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ArticleCatalog implements Serializable {

    private int id;
    private int pid;
    private String name;
    private boolean parent;
    private int level;
    private Date updated;
    private Date created;

    private String parentName;
    @Transient
    private List<ArticleCatalog> children;

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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Transient
    public String getPath() {
        return "/catalog/" + id + ".html";
    }

    public List<ArticleCatalog> getChildren() {
        return children;
    }

    public void setChildren(List<ArticleCatalog> children) {
        this.children = children;
    }
}
