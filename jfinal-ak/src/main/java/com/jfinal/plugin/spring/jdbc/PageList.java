package com.jfinal.plugin.spring.jdbc;

import java.io.Serializable;
import java.util.List;

public class PageList<T> implements Serializable {

    private static final long serialVersionUID = -5395997221963176643L;

    private List<T> data;
    private Page page;

    public PageList(List<T> data, Page page) {
        this.data = data;
        this.page = page;
    }

    public PageList(List<T> data, long totalRow, int pageSize, int curNo) {
        this.data = data;
        this.page = new Page(totalRow, pageSize, curNo);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}