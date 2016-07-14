package cn.huwhy.katyusha.term;

import com.jfinal.plugin.mybatis.Term;

public class CatalogTerm extends Term {

    private Integer pid;
    private String  name;
    private Integer level;
    private Boolean isParent;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public void setIdSort(Sort sort) {
        addSort("a.id", sort);
    }
}
