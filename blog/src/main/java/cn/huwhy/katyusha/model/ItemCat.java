package cn.huwhy.katyusha.model;

import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.List;

public class ItemCat implements Serializable {
    private int id;
    private int pid;
    private int level;
    private String name;
    private boolean parent;
    private int sortOrder;

    private String parentName;

    @Transient
    private List<ItemCat> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Transient
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<ItemCat> getChildren() {
        return children;
    }

    public void setChildren(List<ItemCat> children) {
        this.children = children;
    }

    @Transient
    public String getPath() {
        return "/cat/" + getId() + ".html";
    }
}
